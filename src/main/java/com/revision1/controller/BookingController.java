package com.revision1.controller;

import com.revision1.enity.Bookings;
import com.revision1.enity.Property;
import com.revision1.enity.RoomsAvailibility;
import com.revision1.reposistory.BookingsRepository;
import com.revision1.reposistory.PropertyRepository;
import com.revision1.reposistory.RoomsAvailibilityRepository;
import com.revision1.service.PdfBookingService;
import com.revision1.service.SmsService;

import jakarta.transaction.Transactional;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/bookings")
public class BookingController {
    private BookingsRepository bookingsRepository;
    private PropertyRepository propertyRepository;
    private RoomsAvailibilityRepository roomsAvailibilityRepository;
    private PdfBookingService pdfBookingService;
    private SmsService smsService;
    //private WhatsAppService whatsAppService;

    public BookingController(BookingsRepository bookingsRepository, PropertyRepository propertyRepository, RoomsAvailibilityRepository roomsAvailibilityRepository, PdfBookingService pdfBookingService, SmsService smsService) {
        this.bookingsRepository = bookingsRepository;
        this.propertyRepository = propertyRepository;
        this.roomsAvailibilityRepository = roomsAvailibilityRepository;
        this.pdfBookingService = pdfBookingService;
        this.smsService = smsService;
        //this.whatsAppService = whatsAppService;
    }

    @Transactional
    @PostMapping("/book-hotel")
    public ResponseEntity<?> roomBookings(
            @RequestParam long id,
            @RequestParam String roomType,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
            @RequestBody Bookings booking

            ) {
        long price=0;
        Optional<Property> propertyOptional = propertyRepository.findById(id);

        if (propertyOptional.isEmpty()) {
            return new ResponseEntity<>("Property not found", HttpStatus.NOT_FOUND);
        }

        Property property = propertyOptional.get();
        LocalDate currentDate = checkInDate;
        while (currentDate.isBefore(checkOutDate) || currentDate.isEqual(checkOutDate)) {

            // Find rooms for this specific date
            List<RoomsAvailibility> availabilityList = roomsAvailibilityRepository
                    .findByPropertyAndDateAndRoomType(property, currentDate, roomType);

            // If no rooms found or rooms are full
            if (availabilityList.isEmpty() || availabilityList.get(0).getTotalRooms() <= 0) {
                return ResponseEntity.badRequest()
                        .body("No " + roomType + " rooms available on " + currentDate);
            }

            // Move to next day
            currentDate = currentDate.plusDays(1);
        }

        currentDate = checkInDate;
        while (!currentDate.isAfter(checkOutDate)) {
            RoomsAvailibility availability = roomsAvailibilityRepository
                    .findByPropertyAndDateAndRoomType(property, currentDate, roomType)
                    .get(0); // We already verified existence above

            availability.setTotalRooms(availability.getTotalRooms() - 1);
            roomsAvailibilityRepository.save(availability);
            currentDate = currentDate.plusDays(1);
            price=availability.getNightlyPrice();
        }


        long totalNights = checkInDate.until(checkOutDate).getDays();
        long amount = totalNights*price;
        booking.setPrice(amount);
        booking.setCheckIn(checkInDate);
        booking.setCheckOut(checkOutDate);
        booking.setProperty(property);
        booking.setRoomType(roomType);
        Bookings savedBookingdetails = bookingsRepository.save(booking);
        try {
            pdfBookingService.generatePdf("D:\\OneDrive\\Desktop\\Bookins_docs\\bookings"+savedBookingdetails.getId()+".pdf", savedBookingdetails);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        //smsService.sendSms( "+916299469913","Booking confirmed");
        //whatsAppService.sendWhatsAppMessage("+916299469913","Booking confirmed");
        return new ResponseEntity<>("Booking created successfully", HttpStatus.CREATED);


    }
}
