package com.revision1.reposistory;

import com.revision1.enity.Property;
import com.revision1.enity.PropertyImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Properties;

public interface PropertyImageRepository extends JpaRepository<PropertyImage, Long> {
    List<PropertyImage> findByProperty(
            Property property
    );
}