package com.gesco.payment.core.application.port.output;

import com.gesco.payment.core.domain.model.Invoice;
import com.gesco.payment.core.domain.model.Payment;

import java.util.Map;

public interface PaymentProvider {
    /**
     * Identifiant unique du fournisseur (ex: STRIPE, FASO_ARZEKA)
     */
    String getProviderName();

    /**
     * Traite un paiement pour une facture donnée
     * @param invoice La facture à payer
     * @param params Paramètres spécifiques au fournisseur (ex: source token, phone number)
     * @return L'objet Payment avec les détails de la transaction
     */
    Payment process(Invoice invoice, Map<String, Object> params);
}
