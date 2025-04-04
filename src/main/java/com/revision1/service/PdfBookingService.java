package com.revision1.service;

import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.TextAlignment;
import com.revision1.enity.Bookings;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

@Service
public class PdfBookingService {

    public void generatePdf(String filePath, Bookings booking) throws FileNotFoundException {
        File file = new File(filePath);
        file.getParentFile().mkdirs(); // Create directories if they don't exist

        try (PdfWriter writer = new PdfWriter(file);
             PdfDocument pdf = new PdfDocument(writer);
             Document document = new Document(pdf)) {

            // Header
            document.add(createHeader(booking.getProperty().getName()));

            // Booking Details
            document.add(createBookingDetailsSection(booking));

            // Stay Duration
            document.add(createStayDurationSection(booking));

            // Amount Summary
            document.add(createAmountSection(booking));

            // Footer
            document.add(createFooter());

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate PDF", e);
        }
    }

    private Paragraph createHeader(String propertyName) {
        return new Paragraph(propertyName + " - BOOKING CONFIRMATION")
                .setTextAlignment(TextAlignment.CENTER)
                .setFontSize(18)
                .setBold()
                .setMarginBottom(15f);
    }

    private Table createBookingDetailsSection(Bookings booking) {
        float[] columnWidths = {150, 300};
        Table detailsTable = new Table(columnWidths);

        detailsTable.addCell(createStyledCell("Booking ID:", true));
        detailsTable.addCell(createStyledCell(booking.getId().toString(), false));

        detailsTable.addCell(createStyledCell("Guest Name:", true));
        detailsTable.addCell(createStyledCell(booking.getGuestName(), false));

        detailsTable.addCell(createStyledCell("Contact:", true));
        detailsTable.addCell(createStyledCell(booking.getMobile() + " / " + booking.getEmail(), false));

        detailsTable.addCell(createStyledCell("Guests:", true));
        detailsTable.addCell(createStyledCell(booking.getNoOfGuest().toString(), false));

        return detailsTable.setMarginBottom(15f);
    }

    private Paragraph createStayDurationSection(Bookings booking) {
        return new Paragraph()
                .add("Stay Duration: ")
                .add(booking.getCheckIn().toString())
                .add(" to ")
                .add(booking.getCheckOut().toString())
                .setMarginBottom(15f);
    }

    private Table createAmountSection(Bookings booking) {
        float[] columnWidths = {200, 100};
        Table amountTable = new Table(columnWidths);

        // Calculate values
        long subtotal = booking.getPrice();
        long taxes = (long)(subtotal * 0.12); // 12% tax
        long total = subtotal + taxes;

        // Add rows
        amountTable.addCell(createStyledCell("Room Charges:", true));
        amountTable.addCell(createStyledCell("₹" + subtotal, false));

        amountTable.addCell(createStyledCell("Taxes (12%):", true));
        amountTable.addCell(createStyledCell("₹" + taxes, false));

        amountTable.addCell(createStyledCell("Total Amount:", true));
        amountTable.addCell(createStyledCell("₹" + total, false).setBold());

        return amountTable.setMarginBottom(20f);
    }

    private Paragraph createFooter() {
        return new Paragraph("Thank you for your booking!\nFor any queries, please contact our support team.")
                .setTextAlignment(TextAlignment.CENTER)
                .setItalic()
                .setFontSize(10);
    }

    private Cell createStyledCell(String text, boolean isHeader) {
        Cell cell = new Cell().add(new Paragraph(text));
        if (isHeader) {
            cell.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            cell.setBold();
        }
        return cell;
    }
}