package com.revision1.reposistory;

import com.revision1.enity.Property;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PropertyRepository extends JpaRepository<Property, Long> {
    Optional<Property> findByName(String name);

    //JPQL to search for properties based on city and country
    //it will return list of properties

    @Query("select p from Property p JOIN p.city c JOIN p.country co  where c.name=:searchParam or co.name=:searchParam")
    List<Property> findProperty(
            @Param("searchParam") String searchParam
    );
}