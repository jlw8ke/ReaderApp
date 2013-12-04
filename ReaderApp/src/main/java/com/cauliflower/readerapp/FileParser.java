package com.cauliflower.readerapp;

import com.itextpdf.text.Document;
import com.itextpdf.text.io.RandomAccessSourceFactory;
import com.itextpdf.text.pdf.PRTokeniser;
import com.itextpdf.text.pdf.PdfImportedPage;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.RandomAccessFileOrArray;

import java.io.IOException;

/**
 * Created by jlw8k_000 on 12/1/13.
 */
public class FileParser {
    public static String parse(String fileLocation) {
        if(fileLocation.endsWith(".pdf"))
            return parsePDF(fileLocation);
        else
            return "";
    }

    private static String parsePDF(String fileLocation) {
        String content = "";

        try {
            Document doc = new Document();
            PdfReader reader = new PdfReader(fileLocation);
            int numPages = reader.getNumberOfPages();

            PdfImportedPage page;

            for (int i = 1; i <= numPages; i++) {
                byte[] stream = reader.getPageContent(i);
                PRTokeniser tokenizer = new PRTokeniser(new RandomAccessFileOrArray(
                        new RandomAccessSourceFactory().createSource(stream)));
                while (tokenizer.nextToken()) {
                    if (tokenizer.getTokenType() == PRTokeniser.TokenType.STRING)
                        content += tokenizer.getStringValue();
                }
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
