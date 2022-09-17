package com.geography.country.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.geography.country.model.Country;
import com.geography.country.service.CountryService;

import jakarta.json.Json;
import jakarta.json.JsonObject;

@RestController
public class CountryRestController {

    @Autowired
    private CountryService countrySvc;

    
    
    @GetMapping(path="/search", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> findCountry(@RequestParam String country){
        ObjectMapper mapper = new ObjectMapper();
        Optional<Country> optCountry = countrySvc.findCountry(country);
        if (optCountry.isPresent()){
            Country c = optCountry.get();
            try {
                String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(c);
                return ResponseEntity.ok(json);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        JsonObject errorObj = Json.createObjectBuilder().add("Error", "Cannot find country " + country).build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorObj.toString());
    }


}
