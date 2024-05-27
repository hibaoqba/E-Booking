package com.example.demo.service;

import com.example.demo.model.AptReservation;
import com.example.demo.model.Apartment;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class InvoiceApartmentService {
    public byte[] generateInvoice(AptReservation aptReservation) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        // Add title based on reservation status
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

        String titleText = "payee".equalsIgnoreCase(aptReservation.getStatus()) ? "Facture" : "Reçu";
        Paragraph title = new Paragraph(titleText, titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);

        document.add(Chunk.NEWLINE);

        // Add reservation details to the PDF
        PdfPTable reservationTable = new PdfPTable(2);
        reservationTable.setWidthPercentage(100);
        reservationTable.setSpacingBefore(10f);
        reservationTable.setSpacingAfter(10f);

        addTableCell(reservationTable, "Reservation #:", normalFont);
        addTableCell(reservationTable, aptReservation.getId().toString(), normalFont);

        addTableCell(reservationTable, "Créé:", normalFont);
        addTableCell(reservationTable, aptReservation.getCmndDate().toString(), normalFont);

        addTableCell(reservationTable, "Montant " + ("payee".equalsIgnoreCase(aptReservation.getStatus()) ? "payé:" : "dû:"), normalFont);
        addTableCell(reservationTable, "د.م." + aptReservation.getTotalPrice(), normalFont);

        addTableCell(reservationTable, "Facturer à:", normalFont);
        addTableCell(reservationTable, aptReservation.getUser().getFirstname() + " " + aptReservation.getUser().getLastname(), normalFont);

        if (!"payee".equalsIgnoreCase(aptReservation.getStatus())) {
            addTableCell(reservationTable, "Veuillez régler ce montant dans une agence.", normalFont);
            addTableCell(reservationTable, "", normalFont);  // empty cell to align text correctly
        }

        document.add(reservationTable);

        // Add apartment details to the PDF
        Apartment apartment = aptReservation.getApartment();
        Paragraph apartmentDetailsTitle = new Paragraph("Détails de l'appartement réservé:", subtitleFont);
        document.add(apartmentDetailsTitle);

        PdfPTable apartmentTable = new PdfPTable(2);
        apartmentTable.setWidthPercentage(100);
        apartmentTable.setSpacingBefore(10f);
        apartmentTable.setSpacingAfter(10f);

        addTableCell(apartmentTable, "ID de l'appartement:", normalFont);
        addTableCell(apartmentTable, apartment.getId().toString(), normalFont);

        addTableCell(apartmentTable, "Titre:", normalFont);
        addTableCell(apartmentTable, apartment.getTitre(), normalFont);

        addTableCell(apartmentTable, "Adresse:", normalFont);
        addTableCell(apartmentTable, apartment.getAddress(), normalFont);

        document.add(apartmentTable);

        // Add apartment images to the PDF
        for (String base64Image : apartment.getImages()) {
            try {
                byte[] imageBytes = Base64.getDecoder().decode(base64Image);
                Image image = Image.getInstance(imageBytes);
                image.scaleToFit(200, 200);
                document.add(image);
            } catch (IOException | DocumentException e) {
                e.printStackTrace();
            }
        }

        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    private void addTableCell(PdfPTable table, String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setBorder(Rectangle.NO_BORDER);
        table.addCell(cell);
    }
}
