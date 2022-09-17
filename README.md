Features
1) Retrieve basic information (e.g. country name, capital, population) of a country
2) View a country that is selected at random 

---API---
URL: https://restcountries.com
FILTERED: https://restcountries.com/v3.1/all?fields=name,capital,region,languages,population,flags

---PAYLOAD---
[
    {
        "flags": {
            "png": "https://flagcdn.com/w320/fi.png",
            "svg": "https://flagcdn.com/fi.svg"
        },
        "name": {
            "common": "Finland",
            "official": "Republic of Finland",
            "nativeName": {
                "fin": {
                    "official": "Suomen tasavalta",
                    "common": "Suomi"
                },
                "swe": {
                    "official": "Republiken Finland",
                    "common": "Finland"
                }
            }
        },
        "capital": [
            "Helsinki"
        ],
        "region": "Europe",
        "languages": {
            "fin": "Finnish",
            "swe": "Swedish"
        },
        "population": 5530719
    },
    {
        "flags": {
            "png": "https://flagcdn.com/w320/gt.png",
            "svg": "https://flagcdn.com/gt.svg"
        },
        "name": {
            "common": "Guatemala",
            "official": "Republic of Guatemala",
            "nativeName": {
                "spa": {
                    "official": "República de Guatemala",
                    "common": "Guatemala"
                }
            }
        },
        "capital": [
            "Guatemala City"
        ],
        "region": "Americas",
        "languages": {
            "spa": "Spanish"
        },
        "population": 16858333
    },
]
