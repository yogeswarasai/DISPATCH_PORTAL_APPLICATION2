package com.iocl.Dispatch_Portal_Application.ServiceLayer;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.ExceptionConverter;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class CustomFooter extends PdfPageEventHelper {
    private String generatedText;
    private String signatureText;

    public CustomFooter(String generatedText, String signatureText) {
        this.generatedText = generatedText;
        this.signatureText = signatureText;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfPTable footer = new PdfPTable(3);
        try {
            footer.setWidths(new int[]{2, 1, 2}); // Adjust column ratios
            footer.setTotalWidth(527); // Set width of the footer table
            footer.setLockedWidth(true);
            footer.getDefaultCell().setBorder(Rectangle.NO_BORDER);

            // Left-aligned text
            PdfPCell leftCell = new PdfPCell(new Phrase(generatedText, FontFactory.getFont(FontFactory.HELVETICA, 8)));
            leftCell.setBorder(Rectangle.NO_BORDER);
            leftCell.setHorizontalAlignment(Element.ALIGN_LEFT);
            footer.addCell(leftCell);

            // Center-aligned page number
            PdfPCell centerCell = new PdfPCell(new Phrase(String.format("Page %d of ", writer.getPageNumber()), FontFactory.getFont(FontFactory.HELVETICA, 8)));
            centerCell.setBorder(Rectangle.NO_BORDER);
            centerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            footer.addCell(centerCell);

            // Right-aligned text
            PdfPCell rightCell = new PdfPCell(new Phrase(signatureText, FontFactory.getFont(FontFactory.HELVETICA, 8)));
            rightCell.setBorder(Rectangle.NO_BORDER);
            rightCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            footer.addCell(rightCell);

            // Write the footer to the document
            footer.writeSelectedRows(0, -1, 34, 30, writer.getDirectContent());
        } catch (DocumentException e) {
            throw new ExceptionConverter(e);
        }
    }
}