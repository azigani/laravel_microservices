<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use App\Core\Application\UseCases\GenerateDocumentUseCase;

class DocumentController extends Controller
{
    public function generate(Request $request, GenerateDocumentUseCase $useCase)
    {
        $data = $request->validate([
            'type' => 'required|string',
            'content' => 'required|array',
        ]);

        return $useCase->execute($data);
    }

    public function show($id)
    {
        $filename = 'doc_' . $id . '.pdf';
        return response()->json([
            'id' => $id,
            'status' => 'ready',
            'download_url' => asset('storage/documents/' . $filename)
        ]);
    }
}
