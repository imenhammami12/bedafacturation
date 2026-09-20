package pdf;

import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

public class ReadPdf {
public static void main(String[]args) throws IOException {
	File file = new File("C:\\Users\\LENOVO\\Desktop\\facture_word_12.pdf");
    PDDocument document =PDDocument.load(file);
    PDFTextStripper pdfStripper = new PDFTextStripper();
    String text= pdfStripper.getText(document);
     System.out.println(text); 
}

}
