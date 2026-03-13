package com.gesco.document.core.application.usecase;

import com.gesco.document.core.application.dto.DocumentResponseDto;
import com.gesco.document.core.domain.model.Document;
import com.gesco.document.core.domain.model.DocumentVersion;
import com.gesco.document.core.domain.port.DocumentRepository;
import com.gesco.document.core.domain.port.DocumentVersionRepository;
import com.gesco.document.core.domain.port.FileStoragePort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Generates a PDF invoice using JasperReports when a sale is completed.
 * Can be triggered asynchronously by a RabbitMQ event or synchronously via
 * REST.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class GenerateInvoiceUseCase {

    private final DocumentRepository documentRepository;
    private final DocumentVersionRepository versionRepository;
    private final FileStoragePort fileStoragePort;

    public DocumentResponseDto execute(UUID saleId, String customerName, double totalAmount) {
        try {
            log.info("Generating invoice for sale: {} - customer: {}", saleId, customerName);

            // 1. Load the JasperReports template (.jrxml) from classpath
            InputStream templateStream = new ClassPathResource("reports/invoice.jrxml").getInputStream();
            JasperReport jasperReport = JasperCompileManager.compileReport(templateStream);

            // 2. Fill parameters
            Map<String, Object> params = new HashMap<>();
            params.put("INVOICE_NUMBER", "INV-" + saleId.toString().substring(0, 8).toUpperCase());
            params.put("CUSTOMER_NAME", customerName);
            params.put("TOTAL_AMOUNT", totalAmount);
            params.put("INVOICE_DATE", LocalDateTime.now().toString());
            params.put("COMPANY_NAME", "GESCO ERP");

            // 3. Generate PDF bytes
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, params, new JREmptyDataSource());
            byte[] pdfBytes = JasperExportManager.exportReportToPdf(jasperPrint);

            // 4. Upload to MinIO
            UUID docId = UUID.randomUUID();
            String filename = "INV-" + saleId.toString().substring(0, 8).toUpperCase() + ".pdf";
            String objectKey = "invoices/" + docId + "/v1/" + filename;

            fileStoragePort.upload(objectKey, new ByteArrayInputStream(pdfBytes), "application/pdf", pdfBytes.length);

            // 5. Save Document metadata
            Document document = Document.builder()
                    .id(docId)
                    .title("Facture - " + customerName)
                    .type(Document.DocumentType.INVOICE)
                    .currentVersion("v1")
                    .linkedEntityId(saleId)
                    .linkedEntityType("SALE")
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            Document saved = documentRepository.save(document);

            // 6. Save version
            DocumentVersion version = DocumentVersion.builder()
                    .id(UUID.randomUUID())
                    .documentId(saved.getId())
                    .versionNumber(1)
                    .objectStorageKey(objectKey)
                    .originalFileName(filename)
                    .fileSize(pdfBytes.length)
                    .mimeType("application/pdf")
                    .createdAt(LocalDateTime.now())
                    .build();
            versionRepository.save(version);

            String downloadUrl = fileStoragePort.getPresignedUrl(objectKey, 3600);

            log.info("Invoice generated and stored successfully: {}", objectKey);

            return DocumentResponseDto.builder()
                    .id(saved.getId())
                    .title(saved.getTitle())
                    .type(saved.getType())
                    .currentVersion(saved.getCurrentVersion())
                    .linkedEntityId(saved.getLinkedEntityId())
                    .linkedEntityType(saved.getLinkedEntityType())
                    .downloadUrl(downloadUrl)
                    .createdAt(saved.getCreatedAt())
                    .build();

        } catch (Exception e) {
            log.error("Erreur lors de la génération de la facture: {}", e.getMessage(), e);
            throw new RuntimeException("Erreur lors de la génération de la facture: " + e.getMessage(), e);
        }
    }
}
