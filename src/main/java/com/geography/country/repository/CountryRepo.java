package com.geography.country.repository;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import com.geography.country.model.Country;

@Repository
public class CountryRepo {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    private static final Logger logger = LoggerFactory.getLogger(CountryRepo.class);


    public void saveCountryList(List<Country> countries){
        logger.info("Saving list of countries to database");
        redisTemplate.opsForValue().set("countries", countries);
    }

    public boolean countryListExists(){
        return redisTemplate.hasKey("countries");
    }

    public List<Country> getCountryList(){
        logger.info("Retrieving list of countries from database");
        return (List<Country>)redisTemplate.opsForValue().get("countries");
    }


}
