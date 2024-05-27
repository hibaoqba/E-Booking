package com.example.demo.service;

import com.example.demo.model.Reservation;
import com.example.demo.model.Car;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

@Service
public class InvoiceService {

    public byte[] generateInvoice(Reservation reservation) throws DocumentException, IOException {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        // Add title based on reservation status
        Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
        Font subtitleFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
        Font normalFont = new Font(Font.FontFamily.HELVETICA, 12);

        String titleText = "payé".equalsIgnoreCase(reservation.getStatus()) ? "Facture" : "Reçu";
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
        addTableCell(reservationTable, reservation.getId().toString(), normalFont);

        addTableCell(reservationTable, "Créé:", normalFont);
        addTableCell(reservationTable, reservation.getCmndDate().toString(), normalFont);

        addTableCell(reservationTable, "Montant " + ("payé".equalsIgnoreCase(reservation.getStatus()) ? "payé:" : "dû:"), normalFont);
        addTableCell(reservationTable, "د.م." + reservation.getTotalPrice(), normalFont);

        addTableCell(reservationTable, "Facturer à:", normalFont);
        addTableCell(reservationTable, reservation.getUser().getFirstname() + " " + reservation.getUser().getLastname(), normalFont);

        if (!"payé".equalsIgnoreCase(reservation.getStatus())) {
            addTableCell(reservationTable, "Veuillez régler ce montant dans une agence.", normalFont);
            addTableCell(reservationTable, "", normalFont);  // empty cell to align text correctly
        }

        document.add(reservationTable);

        // Add car details to the PDF
        Car car = reservation.getCar();
        Paragraph carDetailsTitle = new Paragraph("Détails de la voiture réservée:", subtitleFont);
        document.add(carDetailsTitle);

        PdfPTable carTable = new PdfPTable(2);
        carTable.setWidthPercentage(100);
        carTable.setSpacingBefore(10f);
        carTable.setSpacingAfter(10f);

        addTableCell(carTable, "ID de la voiture:", normalFont);
        addTableCell(carTable, car.getId().toString(), normalFont);

        addTableCell(carTable, "Modèle:", normalFont);
        addTableCell(carTable, car.getModel(), normalFont);

        addTableCell(carTable, "Marque:", normalFont);
        addTableCell(carTable, car.getBrand(), normalFont);

        document.add(carTable);

        // Add car images to the PDF
        for (String base64Image : car.getImages()) {
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
