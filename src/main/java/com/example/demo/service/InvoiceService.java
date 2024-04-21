package com.example.demo.service;


import com.example.demo.model.Reservation;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfWriter;
import org.springframework.stereotype.Service;
import com.itextpdf.text.Paragraph;

import java.io.ByteArrayOutputStream;

@Service
public class InvoiceService {

    public byte[] generateInvoice(Reservation reservation) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PdfWriter.getInstance(document, byteArrayOutputStream);
        document.open();

        // Add reservation details to the PDF
        addTextToPdf(document, "Reservation");
        addTextToPdf(document, "Reservation #: " + reservation.getId());
        addTextToPdf(document, "Créé: " + reservation.getCmndDate());
        addTextToPdf(document, "Montant dû: د.م." + reservation.getTotalPrice());
        addTextToPdf(document, "Facturer à: " + reservation.getUser().getFirstname() +reservation.getUser().getLastname() );
        // Add more details as needed

        document.close();
        return byteArrayOutputStream.toByteArray();
    }

    private void addTextToPdf(Document document, String text) throws DocumentException {
        document.add(new Paragraph(text));
    }
}