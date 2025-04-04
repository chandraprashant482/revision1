package com.revision1.controller;

import com.revision1.enity.City;
import com.revision1.enity.Country;
import com.revision1.enity.Property;
import com.revision1.enity.PropertyImage;
import com.revision1.payload.DeleteProperty;
import com.revision1.reposistory.CityRepository;
import com.revision1.reposistory.CountryRepository;
import com.revision1.reposistory.PropertyImageRepository;
import com.revision1.reposistory.PropertyRepository;
import com.revision1.service.BucketService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/property")
public class PropertyController {
    private  PropertyRepository propertyRepository;
    private  CountryRepository countryRepository;
    private  CityRepository cityRepository;
    private BucketService bucketService;
    private PropertyImageRepository imageRepository;
    public PropertyController(PropertyRepository propertyRepository, CountryRepository countryRepository, CityRepository cityRepository, BucketService bucketService, PropertyImageRepository imageRepository) {
        this.propertyRepository = propertyRepository;
        this.countryRepository = countryRepository;
        this.cityRepository = cityRepository;
        this.bucketService = bucketService;
        this.imageRepository = imageRepository;
    }

    @PostMapping("/addproperty")
    public String addproperty(
            @RequestBody Property property
    )
    {
        // Find or create Country
        Optional<Country> countryOptional = countryRepository.findByName(property.getCountry().getName());
        Country country;
        if (countryOptional.isPresent()) {
            country = countryOptional.get();
        } else {
            country = new Country();
            country.setName(property.getCountry().getName());
            country = countryRepository.save(country);
        }

        // Find or create City
        Optional<City> cityOptional = cityRepository.findByName(property.getCity().getName());
        City city;
        if (cityOptional.isPresent()) {
            city = cityOptional.get();
        } else {
            city = new City();
            city.setName(property.getCity().getName());
            city = cityRepository.save(city);
        }
        property.setCity(city);
        property.setCountry(country);

        propertyRepository.save(property);
        return "Property added successfully!";
    }

    @DeleteMapping("/deleteproperty")
    public String deleteProperty(@RequestBody DeleteProperty delete){

        Optional<Property> byName = propertyRepository.findByName(delete.getName());
        if(byName.isPresent()){
            Property p = byName.get();
            propertyRepository.delete(p);
            return "Property deleted successfully!";
        }

        else {
            return "Invalid Name";
        }

    }
    @GetMapping("/{searchParam}")
    public List<Property> searchHotel(
            @PathVariable String searchParam
    ){
        List<Property> property = propertyRepository.findProperty(searchParam);
        return property;

    }
    @PostMapping("/upload/file/{bucketName}/property/{propertyId}")
    public String uploadPropertyPhotos(
            @RequestParam MultipartFile file,
            @PathVariable String bucketName,
            @PathVariable Long propertyId
    ){
        String imageUrl=bucketService.uploadFile(file, bucketName);
        PropertyImage propertyImage=new PropertyImage();
        propertyImage.setUrl(imageUrl);
        //set fk
        Property property = propertyRepository.findById(propertyId).get();
        propertyImage.setProperty(property);
        imageRepository.save(propertyImage);
        return "image is uploaded";
    }

    @GetMapping("get/property/images")
    public List<PropertyImage> getPropertyImages(
            @RequestParam long id
    )
    {
        Property property = propertyRepository.findById(id).get();
        return imageRepository.findByProperty(property);

    }
}
