import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.properties.AreaBreakType;

public class DocumentCreator {
	public static void main(String[] args) throws FileNotFoundException, MalformedURLException {
//		File file = new File("ActivityRoster.pdf");
//		PdfWriter pdfw = new PdfWriter(file);
//		PdfDocument pdfDoc = new PdfDocument(pdfw);
//		pdfDoc.addNewPage();
//		Document doc = new Document(pdfDoc);
//		PdfDocumentInfo info = pdfDoc.getDocumentInfo();
//		info.setTitle("Testing Document Creation");
//		Image logo = new Image(ImageDataFactory.create("src/photos/FrontierCampLogo.png"));
//		logo.setHeight(logo.getImageHeight() * .5f);
//		logo.setWidth(logo.getImageWidth() * .5f);
//		Paragraph p = new Paragraph();
//		p.add(logo);
//		doc.add(p);
//
//		Paragraph activityName = new Paragraph();
//		activityName.add("Adventure Challenge 1");
//		doc.add(activityName);
//
//		Paragraph weekPeriod = new Paragraph();
//		weekPeriod.add("JW1, Period 1");
//		doc.add(weekPeriod);

		Activity a = new Activity("AC1", null, 12);
		
		createRoster(a);
		
		
//		doc.close();
	}

	public static void createRoster(Activity a) throws FileNotFoundException, MalformedURLException {
		File file = new File(a.getName() + ".pdf");
		PdfWriter pdfw = new PdfWriter(file);
		PdfDocument pdfDoc = new PdfDocument(pdfw);
		Document doc = new Document(pdfDoc);
		
		for (Period p : a.getPeriods()) {
			if (p != null) {
				PdfPage pdfp = pdfDoc.addNewPage();
				PdfCanvas pdfc = new PdfCanvas(pdfp);
				
				pdfc.moveTo(25, 640);
				pdfc.lineTo(575, 640);
				pdfc.closePathStroke();
				
				PdfDocumentInfo info = pdfDoc.getDocumentInfo();
				info.setTitle(a.getName() + " Roster");

				Paragraph pLogo = new Paragraph();
				pLogo.setMultipliedLeading(.5f);
				Image logo = new Image(ImageDataFactory.create("src/photos/FrontierCampLogo.png"));
				logo.setHeight(logo.getImageHeight() * .5f);
				logo.setWidth(logo.getImageWidth() * .5f);
				pLogo.add(logo);
				doc.add(pLogo);

				Paragraph activityName = new Paragraph();
				activityName.add(a.getName());
				doc.add(activityName);

				Paragraph weekPeriod = new Paragraph();
				weekPeriod.add("JW1, Period " + p.getOrder());
				doc.add(weekPeriod);
				
				Paragraph labels = new Paragraph();
				labels.add("#\t\t\tName\t\t\t\t\t\t\t\t");
				if (p.getOrder() == 1 || p.getOrder() == 3) {
					labels.add("Next Activity");
				} else {
					labels.add("Previous Activity");
				}
				doc.add(labels);

				doc.add(new AreaBreak());
			}
		}

		doc.close();
	}
}
