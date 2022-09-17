package com.geography.country.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.geography.country.model.Countries;
import com.geography.country.model.Country;
import com.geography.country.repository.CountryRepo;

@Service
public class CountryService {
    private static final String COUNTRY_API_URL = "https://restcountries.com/v3.1/all?fields=name,capital,region,languages,population,flags";
    private static final String BASE_WIKI_URL = "https://en.wikipedia.org/wiki/";
    private static final String GENERAL_AUDIO_URL = "https://ssl.gstatic.com/dictionary/static/pronunciation/2022-03-02/audio/twoLetterCode/countryName_en_gb_1.mp3";
    private static final Logger logger = LoggerFactory.getLogger(CountryService.class);
    private RestTemplate restTemplate = new RestTemplate();
    private Random random = new Random();

    @Autowired
    private CountryRepo countryRepo;
    
    public List<Country> getCountriesFromAPI(){
        logger.info("Calling RestCountries API");
        ResponseEntity<String> respEntity = restTemplate.getForEntity(COUNTRY_API_URL, String.class);
        String payload = respEntity.getBody();
        ObjectMapper mapper = new ObjectMapper()
                        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        try {
            JsonNode treeNode = mapper.readTree(payload);
            ArrayNode arrayNode = (ArrayNode) treeNode;
            for (JsonNode node : arrayNode){
                Country c = new Country();
                c.setName(node.get("name").get("common").asText());
                c.setRegion(node.get("region").asText());
                c.setPopulation(node.get("population").asInt());
                ArrayNode capitalNode = (ArrayNode) node.get("capital");
                if (capitalNode.isEmpty()){
                    c.setCapital("nil");
                }
                else{
                    c.setCapital(capitalNode.get(0).asText());
                }
                c.setFlagURL(node.get("flags").get("png").asText());
                Map<String, String> languageMap = mapper.convertValue(node.get("languages"), new TypeReference<Map<String, String>>(){});
                c.setLanguages(languageMap.values().stream().toList());
                setWikiURL(c);
                setAudioURL(c);
                Countries.listOfAllCountries.add(c);
            }
            
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        logger.info("Retrieved a list of " + Countries.listOfAllCountries.size() + " countries from API.");
        return Countries.listOfAllCountries;
    }

    public void setWikiURL(Country country){
        String fullWikiURL = BASE_WIKI_URL + country.getName();
        country.setWikiURL(fullWikiURL);
    }

    public void setAudioURL(Country country){
        String twoLetterCode = country.getName().substring(0, 2).toLowerCase();
        String fullAudioURL = GENERAL_AUDIO_URL.replace("twoLetterCode", twoLetterCode)
                                                .replace("countryName", country.getName().toLowerCase());
        try {
            ResponseEntity<String> respEntity = restTemplate.getForEntity(fullAudioURL, String.class);
            if (respEntity.getStatusCodeValue()==200){
                country.setAudioURL(fullAudioURL);
            }
        }
        catch (HttpClientErrorException ex){
            logger.info("Unable to locate audio for " + country.getName());
            country.setAudioURL(null);
        }
    }

    public void saveCountryList(List<Country> countries){
        countryRepo.saveCountryList(countries);
    }

    public boolean countryListExistsInRepo(){
        return countryRepo.countryListExists();
    }

    public List<Country> getCountryListFromRepo(){
        return countryRepo.getCountryList();
    }

    public Optional<Country> findCountry(String name){
        for (Country country : Countries.listOfAllCountries){
            if (country.getName().equalsIgnoreCase(name)){
                return Optional.of(country);
            }
        }
        return Optional.empty();
    }

    public Country getRandomCountry(){
        int randomNum = random.nextInt(0, Countries.listOfAllCountries.size());
        Country randomCountry = Countries.listOfAllCountries.get(randomNum);
        return randomCountry;
    }

    public Country findCountryByName(String countryName){
        for (Country c : Countries.listOfAllCountries){
            if (c.getName().equals(countryName)){
                return c;
            }
        }
        return null;
    }




}
