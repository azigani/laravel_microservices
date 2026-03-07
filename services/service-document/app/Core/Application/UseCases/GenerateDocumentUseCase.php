namespace App\Core\Application\UseCases;

use Barryvdh\DomPDF\Facade\Pdf;
use Illuminate\Support\Facades\Storage;

class GenerateDocumentUseCase
{
    public function execute(array $data)
    {
        $id = uniqid();
        $pdf = Pdf::loadHTML('<h1>GESCO - Document</h1><p>Type: ' . $data['type'] . '</p><pre>' . json_encode($data['content'], JSON_PRETTY_PRINT) . '</pre>');
        
        $filename = 'doc_' . $id . '.pdf';
        Storage::put('public/documents/' . $filename, $pdf->output());

        return response()->json([
            'id' => $id,
            'status' => 'completed',
            'download_url' => asset('storage/documents/' . $filename),
            'filename' => $filename
        ]);
    }
}
