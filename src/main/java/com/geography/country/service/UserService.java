package com.geography.country.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.geography.country.model.Country;
import com.geography.country.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;

    public boolean checkIfUserExists(String username){
        return userRepo.checkIfUserExists(username);
    }

    public void saveUser(String username, List<Country> userCountries){
        userRepo.saveUser(username, userCountries);
    }

    public List<Country> getUserCountries(String username){
        return userRepo.getUserCountries(username);
    }

    public void addCountryAndSave(String username, Country country){
        boolean userExists = checkIfUserExists(username);
        if (userExists){
            List<Country> userCountries = getUserCountries(username);
            if (!(userCountries.contains(country))){
                userCountries.add(country);
                saveUser(username, userCountries);
            }
        }
        else{
            List<Country> userCountries = new ArrayList<>();
            userCountries.add(country);
            saveUser(username, userCountries);
        }
    }

}
