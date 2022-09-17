package com.geography.country.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

import com.geography.country.model.Country;

@Repository
public class UserRepo {

    @Autowired
    private RedisTemplate<String,Object> redisTemplate;
    
    public boolean checkIfUserExists(String username){
        return redisTemplate.hasKey(username) ? true : false;
    }
    
    public void saveUser(String username, List<Country> userCountries){
        redisTemplate.opsForValue().set(username, userCountries);
    }

    public List<Country> getUserCountries(String username){
        List<Country> userCountries = (List<Country>)redisTemplate.opsForValue().get(username);
        return userCountries;
    }
}
