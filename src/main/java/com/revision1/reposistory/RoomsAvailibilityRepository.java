package com.revision1.reposistory;

import com.revision1.enity.Property;
import com.revision1.enity.RoomsAvailibility;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface RoomsAvailibilityRepository extends JpaRepository<RoomsAvailibility, Long> {


    @Query("SELECT r FROM RoomsAvailibility r WHERE r.property = :property AND r.date BETWEEN :checkIn AND :checkOut")
    List<RoomsAvailibility> findByPropertyAndDate(@Param("property") Property property,
                                                  @Param("checkIn") LocalDate checkIn,
                                                  @Param("checkOut") LocalDate checkOut);

    //    @Query("SELECT r FROM RoomsAvailibility r WHERE r.property = :property AND r.date BETWEEN :startDate AND :endDate AND r.roomType = :roomType")
//    List<RoomsAvailibility> findByPropertyAndDateBetweenAndRoomType(
//            @Param("property") Property property,
//            @Param("startDate") LocalDate startDate,
//            @Param("endDate") LocalDate endDate,
//            @Param("roomType") String roomType
//    );
    @Query("SELECT r FROM RoomsAvailibility r WHERE " +
            "r.property = :property AND " +
            "r.date = :date AND " +
            "r.roomType = :roomType")
    List<RoomsAvailibility> findByPropertyAndDateAndRoomType(
            @Param("property") Property property,
            @Param("date") LocalDate date,
            @Param("roomType") String roomType
    );

}