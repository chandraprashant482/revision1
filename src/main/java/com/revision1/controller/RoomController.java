package com.revision1.controller;

import com.revision1.enity.Property;
import com.revision1.enity.RoomsAvailibility;
import com.revision1.payload.ShowAvailability;
import com.revision1.reposistory.PropertyRepository;
import com.revision1.reposistory.RoomsAvailibilityRepository;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/roomcontroller")
public class RoomController {
    private RoomsAvailibilityRepository roomsAvailibilityRepository;
    private PropertyRepository propertyRepository;


    public RoomController(RoomsAvailibilityRepository roomsAvailibilityRepository, PropertyRepository propertyRepository) {
        this.roomsAvailibilityRepository = roomsAvailibilityRepository;
        this.propertyRepository = propertyRepository;

    }

    @PostMapping("/set-room-availibility")
    public String setRoomsAvailibility(
            @RequestBody RoomsAvailibility setavailibility,
            @RequestParam long id
    ){
        Optional<Property> byId = propertyRepository.findById(id);
        if(byId.isPresent()){

            Property property = byId.get();
            setavailibility.setProperty(property);
            roomsAvailibilityRepository.save(setavailibility);
            return "Room availability set successfully";
        }
        else {
            return "Property not found";
        }

    }
    @GetMapping("/rooms/available")
    public ResponseEntity<?> getRoomsAvailability(
            @RequestParam long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate
    )
    {
        Optional<Property> byId = propertyRepository.findById(id);
        if(byId.isPresent()){
            Property property = byId.get();

            List<RoomsAvailibility> availableRooms = roomsAvailibilityRepository.findByPropertyAndDate(property, checkInDate, checkOutDate);
            List<ShowAvailability> availabilityList = new ArrayList<>();

            for (RoomsAvailibility room : availableRooms) {
                if (room.getTotalRooms()==0){
                    return  new ResponseEntity<>("No room available", HttpStatus.INTERNAL_SERVER_ERROR);

                }
                ShowAvailability showAvailability = new ShowAvailability();
                showAvailability.setHotelname(property.getName());
                showAvailability.setHotelCity(property.getCity().getName());
                showAvailability.setHotelCountry(property.getCountry().getName());
                showAvailability.setRoomAvalible(room.getTotalRooms().intValue());
                showAvailability.setRoomType(room.getRoomType());
                showAvailability.setNightlyPrice(room.getNightlyPrice().intValue());
                showAvailability.setCheckIn(checkInDate);
                showAvailability.setCheckOut(checkOutDate);

                availabilityList.add(showAvailability);
            }
            return new ResponseEntity<>(availabilityList,HttpStatus.OK);
        }
        else {
            return null;
        }

    }
}
