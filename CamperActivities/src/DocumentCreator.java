import java.io.File;
import java.io.FileNotFoundException;

import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;

public class DocumentCreator {
	public static void main(String[] args) throws FileNotFoundException{
		File file = new File("ActivityRoster.pdf");
		PdfWriter pdfw = new PdfWriter(file);
		PdfDocument pdfDoc = new PdfDocument(pdfw);
		pdfDoc.addNewPage();
		Document doc = new Document(pdfDoc);
		PdfDocumentInfo info = pdfDoc.getDocumentInfo();
		info.setTitle("Testing Document Creation");
		Paragraph p = new Paragraph();
		p.add(new Text("Hello there"));
		doc.add(p);
		doc.close();
	}
}
