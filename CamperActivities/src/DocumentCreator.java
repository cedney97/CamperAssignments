import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfDocumentInfo;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

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

		createRoster(a, 1);

//		doc.close();
	}

	public static void createRoster(Activity a, int week) throws FileNotFoundException, MalformedURLException {
		File file = new File(a.getName() + ".pdf");
		PdfWriter pdfw = new PdfWriter(file);
		PdfDocument pdfDoc = new PdfDocument(pdfw);
		Document doc = new Document(pdfDoc);

		for (Period p : a.getPeriods()) {
			if (p != null && p.getEnrolled() != 0) {

//				pdfc.moveTo(25, 640);
//				pdfc.lineTo(575, 640);
//				pdfc.closePathStroke();

				PdfDocumentInfo info = pdfDoc.getDocumentInfo();
				info.setTitle(a.getName() + " Roster");

				Paragraph pLogo = new Paragraph();
				pLogo.setMultipliedLeading(.5f);
				Image logo = new Image(ImageDataFactory.create(Main.class.getResource("/photos/FrontierCampLogo.png")));
				logo.setHeight(logo.getImageHeight() * .5f);
				logo.setWidth(logo.getImageWidth() * .5f);
				pLogo.add(logo);
				doc.add(pLogo);

				Paragraph activityName = new Paragraph();
				activityName.add(a.getName());
				doc.add(activityName);

				Paragraph weekPeriod = new Paragraph();
				weekPeriod.add("JW" + week + ", Period " + p.getOrder());
				doc.add(weekPeriod);

				Camper[] roster = p.getRoster();
				UnitValue[] widths = new UnitValue[7];
				widths[0] = new UnitValue(UnitValue.POINT, 50);
				widths[1] = new UnitValue(UnitValue.POINT, 80);
				widths[2] = new UnitValue(UnitValue.POINT, 100);
				widths[3] = new UnitValue(UnitValue.POINT, 50);
				widths[4] = new UnitValue(UnitValue.POINT, 50);
				widths[5] = new UnitValue(UnitValue.POINT, 50);
				widths[6] = new UnitValue(UnitValue.POINT, 50);

				Table table = new Table(widths);
				table.addCell("#");
				table.getCell(0, 0).setBorderLeft(Border.NO_BORDER);
				table.getCell(0, 0).setBorderTop(Border.NO_BORDER);
				table.getCell(0, 0).setBorderRight(Border.NO_BORDER);
				table.addCell("Name");
				table.getCell(0, 1).setBorderLeft(Border.NO_BORDER);
				table.getCell(0, 1).setBorderTop(Border.NO_BORDER);
				table.getCell(0, 1).setBorderRight(Border.NO_BORDER);

//				Paragraph labels = new Paragraph();
//				labels.add("#");
//				labels.add(new Tab());
//				labels.add("Name");
//				labels.add(new Tab());
				if (p.getOrder() == 1 || p.getOrder() == 3) {
//					labels.add("Next Activity");
					table.addCell("Next Activity");
				} else {
//					labels.add("Previous Activity");
					table.addCell("Previous Activity");
				}
				table.getCell(0, 2).setBorderLeft(Border.NO_BORDER);
				table.getCell(0, 2).setBorderTop(Border.NO_BORDER);
				table.getCell(0, 2).setBorderRight(Border.NO_BORDER);

				table.addCell("M");
//				table.getCell(0, 3).setBorderLeft(Border.NO_BORDER);
				table.getCell(0, 3).setBorderTop(Border.NO_BORDER);
//				table.getCell(0, 3).setBorderRight(Border.NO_BORDER);
				table.getCell(0, 3).setTextAlignment(TextAlignment.CENTER);
				table.addCell("T");
				table.getCell(0, 4).setBorderLeft(Border.NO_BORDER);
				table.getCell(0, 4).setBorderTop(Border.NO_BORDER);
//				table.getCell(0, 4).setBorderRight(Border.NO_BORDER);
				table.getCell(0, 4).setTextAlignment(TextAlignment.CENTER);
				table.addCell("W");
				table.getCell(0, 5).setBorderLeft(Border.NO_BORDER);
				table.getCell(0, 5).setBorderTop(Border.NO_BORDER);
//				table.getCell(0, 5).setBorderRight(Border.NO_BORDER);
				table.getCell(0, 5).setTextAlignment(TextAlignment.CENTER);
				table.addCell("R");
				table.getCell(0, 6).setBorderLeft(Border.NO_BORDER);
				table.getCell(0, 6).setBorderTop(Border.NO_BORDER);
//				table.getCell(0, 6).setBorderRight(Border.NO_BORDER);
				table.getCell(0, 6).setTextAlignment(TextAlignment.CENTER);

				int i = 1;

				for (Camper c : roster) {
					if (c != null) {
						table.addCell(i + "");
						table.getCell(i, 0).setBorderLeft(Border.NO_BORDER);
						table.getCell(i, 0).setBorderTop(Border.NO_BORDER);
						table.getCell(i, 0).setBorderRight(Border.NO_BORDER);

						table.addCell(c.getName());
						table.getCell(i, 1).setBorderLeft(Border.NO_BORDER);
						table.getCell(i, 1).setBorderTop(Border.NO_BORDER);
						table.getCell(i, 1).setBorderRight(Border.NO_BORDER);

						if (p.getOrder() == 1 || p.getOrder() == 3) {
							table.addCell(c.getSchedule().get(p.getOrder()).getName());
						} else {
							table.addCell(c.getSchedule().get(p.getOrder() - 2).getName());
						}
						table.getCell(i, 2).setBorderLeft(Border.NO_BORDER);
						table.getCell(i, 2).setBorderTop(Border.NO_BORDER);
//						table.getCell(i, 2).setBorderRight(Border.NO_BORDER);
						table.addCell(" ");
//						table.getCell(i, 3).setBorderLeft(Border.NO_BORDER);
						table.getCell(i, 3).setBorderTop(Border.NO_BORDER);
//						table.getCell(i, 3).setBorderRight(Border.NO_BORDER);
						table.addCell(" ");
//						table.getCell(i, 4).setBorderLeft(Border.NO_BORDER);
						table.getCell(i, 4).setBorderTop(Border.NO_BORDER);
//						table.getCell(i, 4).setBorderRight(Border.NO_BORDER);
						table.addCell(" ");
//						table.getCell(i, 5).setBorderLeft(Border.NO_BORDER);
						table.getCell(i, 5).setBorderTop(Border.NO_BORDER);
//						table.getCell(i, 5).setBorderRight(Border.NO_BORDER);
						table.addCell(" ");
//						table.getCell(i, 6).setBorderLeft(Border.NO_BORDER);
						table.getCell(i, 6).setBorderTop(Border.NO_BORDER);
//						table.getCell(i, 6).setBorderRight(Border.NO_BORDER);
					}
					++i;
				}

//				doc.add(labels);
				doc.add(table);

				if (p.getOrder() != 4) {
					doc.add(new AreaBreak());
				}
			}
		}

		doc.close();
	}
}
