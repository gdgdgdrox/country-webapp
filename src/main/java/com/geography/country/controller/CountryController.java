package com.geography.country.controller;

import java.util.List;
import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.geography.country.model.Countries;
import com.geography.country.model.Country;
import com.geography.country.service.CountryService;
import com.geography.country.service.UserService;

@Controller
public class CountryController {
    
    @Autowired
    private CountryService countrySvc;

    @Autowired
    private UserService userSvc;

    @PostConstruct
    public void preparation(){
        if (!countrySvc.countryListExistsInRepo()){
            countrySvc.getCountriesFromAPI();
            countrySvc.saveCountryList(Countries.listOfAllCountries);
        }
        else{
            Countries.listOfAllCountries = countrySvc.getCountryListFromRepo();
        }
    }


    @GetMapping("/random-country")
    public String randomCountryPage(Model model){
        Country randomCountry = countrySvc.getRandomCountry();
        model.addAttribute("randomCountry", randomCountry);
        return "random-country";
    }

    @PostMapping("/save")
    public String returnRandomCountryPage(@RequestBody MultiValueMap<String,String> requestBody, Model model){
        String username = requestBody.get("username").get(0);
        String countryName = requestBody.get("countryName").get(0);
        Country country = countrySvc.findCountryByName(countryName);
        userSvc.addCountryAndSave(username, country);
        return "redirect:/random-country";
    }

    @GetMapping("/login")
    public String userCountryPage(){
        return "login";
    }

    @PostMapping("/userCountries")
    public String userCountryPage2(@RequestBody String body, Model model){
        String username = body.substring(body.indexOf("=")+1);
        boolean userExists = userSvc.checkIfUserExists(username);
        if (userExists){
            model.addAttribute("userCountries", userSvc.getUserCountries(username));
            model.addAttribute("username", username);
            return "user-countries";
        }
        model.addAttribute("username", username);
        return "login";

    }

    @DeleteMapping("/delete/{username}/{countryName}")
    public String userCountryPage2(@PathVariable String username, @PathVariable String countryName, Model model){
        List<Country> userCountries = userSvc.getUserCountries(username);
        userCountries.removeIf(c -> (c.getName()).equalsIgnoreCase(countryName));
        userSvc.saveUser(username, userCountries);
        model.addAttribute("userCountries", userCountries);
        model.addAttribute("username", username);
        return "user-countries";
    }



}
