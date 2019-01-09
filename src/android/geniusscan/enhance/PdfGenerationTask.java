package com.geniusscan.enhance;

import android.os.AsyncTask;
import android.util.Log;

import com.thegrizzlylabs.geniusscan.sdk.pdf.PDFDocument;
import com.thegrizzlylabs.geniusscan.sdk.pdf.PDFGenerator;
import com.thegrizzlylabs.geniusscan.sdk.pdf.PDFGeneratorError;
import com.thegrizzlylabs.geniusscan.sdk.pdf.PDFImageProcessor;
import com.thegrizzlylabs.geniusscan.sdk.pdf.PDFLogger;
import com.thegrizzlylabs.geniusscan.sdk.pdf.PDFPage;
import com.thegrizzlylabs.geniusscan.sdk.pdf.PDFSize;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by guillaume on 25/10/16.
 */
public class PdfGenerationTask extends AsyncTask<Void, Void, PDFGeneratorError> {
    private final static PDFSize A4_SIZE = new PDFSize(8.27f, 11.69f); // Size of A4 in inches

    public interface OnPdfGeneratedListener {
        void onPdfGenerated(PDFGeneratorError result);
    }

    private static final String TAG = PdfGenerationTask.class.getSimpleName();

    private String outputFilePath;
    private PDFDocument document;
    private OnPdfGeneratedListener listener;

    public PdfGenerationTask(String title, ArrayList<String> imageUris, String outputFilePath, HashMap pdfOptions, OnPdfGeneratedListener listener) {
        ArrayList<PDFPage> pdfPages = new ArrayList<PDFPage>();
        for (String imageUri : imageUris) {
        // Export all pages in A4
            pdfPages.add(new PDFPage(imageUri, A4_SIZE));
        }

        String password = (String)pdfOptions.get("password");;

        // Here we don't protect the PDF document with a password
        PDFDocument pdfDocument = new PDFDocument(title, password, null, pdfPages);
        this.outputFilePath = outputFilePath;
        this.document = pdfDocument;
        this.listener = listener;
    }

    @Override
    protected PDFGeneratorError doInBackground(Void... params) {
        PDFGenerator generator = PDFGenerator.createWithDocument(document, new PDFNoopImageProcessor(), new Logger());
        return generator.generatePDF(outputFilePath);
    }

    @Override
    protected void onPostExecute(PDFGeneratorError result) {
        super.onPostExecute(result);
        listener.onPdfGenerated(result);
    }

    private class Logger extends PDFLogger {
        @Override
        public void log(String debug) {
            Log.d(TAG, debug);
        }
    }

    private class PDFNoopImageProcessor extends PDFImageProcessor {
        @Override
        public String process(String inputFilePath) {
            return inputFilePath;
        }
    }
}
