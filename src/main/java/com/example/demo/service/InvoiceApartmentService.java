package com.example.demo.service;

import com.example.demo.model.AptReservation;
import com.example.demo.model.Reservation;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;

@Service
public class InvoiceApartmentService {
    public byte[] generateInvoice(AptReservation aptReservation) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        // Add reservation details to the PDF
        addTextToPdf(document, "Reservation");
        addTextToPdf(document, "Reservation #: " + aptReservation.getId());
        addTextToPdf(document, "Créé: " + aptReservation.getCmndDate());
        addTextToPdf(document, "Montant dû: د.م." + aptReservation.getTotalPrice());
        addTextToPdf(document, "Facturer à: " + aptReservation.getUser().getFirstname() +aptReservation.getUser().getLastname() );
        // Add more details as needed

        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    private void addTextToPdf(Document document, String text) throws DocumentException {
        document.add(new Paragraph(text));
    }
}
