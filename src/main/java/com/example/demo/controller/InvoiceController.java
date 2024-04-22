package com.example.demo.controller;

import com.example.demo.model.Reservation;
import com.example.demo.service.InvoiceService;
import com.example.demo.service.ReservationService;
import com.itextpdf.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/invoice")

public class InvoiceController {
    @Autowired
    private ReservationService reservationService;
    @Autowired
    InvoiceService invoiceService;

    @GetMapping("/{reservationId}")
    public void generateInvoice(@PathVariable Long reservationId, HttpServletResponse response) throws IOException, DocumentException {
        Reservation reservation = reservationService.getReservationById(reservationId);
        if (reservation != null) {
            byte[] invoiceBytes = invoiceService.generateInvoice(reservation);
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition", "attachment; filename=invoice.pdf");
            response.getOutputStream().write(invoiceBytes);
        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
