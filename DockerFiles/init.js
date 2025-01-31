db = db.getSiblingDB('town-quiz-db');

db.createCollection('towns');
db.createCollection('statistics');

db.towns.insertMany([
    {
        "id": 1,
        "name": "Berlin",
        "country": "Germany",
        "population": 3669491,
        "area_km2": 891.8,
        "founded": "1237-01-01",
        "coordinates": {
            "latitude": 52.5200,
            "longitude": 13.4050
        },
        "major_languages": ["German"],
        "famous_landmarks": ["Brandenburger Tor", "Reichstag", "Berliner Mauer"],
        "climate": "Temperate",
        "timezone": "CET"
    },
    {
        "id": 2,
        "name": "Paris",
        "country": "France",
        "population": 2161000,
        "area_km2": 105.4,
        "founded": "52-01-01",
        "coordinates": {
            "latitude": 48.8566,
            "longitude": 2.3522
        },
        "major_languages": ["French"],
        "famous_landmarks": ["Eiffelturm", "Louvre", "Notre-Dame"],
        "climate": "Oceanic",
        "timezone": "CET"
    },
    {
        "id": 3,
        "name": "New York",
        "country": "USA",
        "population": 8419600,
        "area_km2": 783.8,
        "founded": "1624-01-01",
        "coordinates": {
            "latitude": 40.7128,
            "longitude": -74.0060
        },
        "major_languages": ["English", "Spanish"],
        "famous_landmarks": ["Statue of Liberty", "Central Park", "Empire State Building"],
        "climate": "Humid Subtropical",
        "timezone": "EST"
    },
    {
        "id": 4,
        "name": "Tokyo",
        "country": "Japan",
        "population": 13929286,
        "area_km2": 2191,
        "founded": "1603-01-01",
        "coordinates": {
            "latitude": 35.6895,
            "longitude": 139.6917
        },
        "major_languages": ["Japanese"],
        "famous_landmarks": ["Tokyo Tower", "Senso-ji", "Shibuya Crossing"],
        "climate": "Humid Subtropical",
        "timezone": "JST"
    },
    {
        "id": 5,
        "name": "Sydney",
        "country": "Australia",
        "population": 5312163,
        "area_km2": 12368,
        "founded": "1788-01-26",
        "coordinates": {
            "latitude": -33.8688,
            "longitude": 151.2093
        },
        "major_languages": ["English"],
        "famous_landmarks": ["Sydney Opera House", "Harbour Bridge", "Bondi Beach"],
        "climate": "Oceanic",
        "timezone": "AEST"
    },
    {
        "id": 6,
        "name": "London",
        "country": "United Kingdom",
        "population": 8982000,
        "area_km2": 1572,
        "founded": "0043-01-01",
        "coordinates": {
            "latitude": 51.5074,
            "longitude": -0.1278
        },
        "major_languages": ["English"],
        "famous_landmarks": ["Big Ben", "Tower of London", "Buckingham Palace"],
        "climate": "Oceanic",
        "timezone": "GMT"
    },
    {
        "id": 7,
        "name": "Rome",
        "country": "Italy",
        "population": 2873000,
        "area_km2": 1285,
        "founded": "-0753-01-01",
        "coordinates": {
            "latitude": 41.9028,
            "longitude": 12.4964
        },
        "major_languages": ["Italian"],
        "famous_landmarks": ["Colosseum", "Vatican City", "Trevi Fountain"],
        "climate": "Mediterranean",
        "timezone": "CET"
    },
    {
        "id": 8,
        "name": "Dubai",
        "country": "United Arab Emirates",
        "population": 3331000,
        "area_km2": 4114,
        "founded": "1833-01-01",
        "coordinates": {
            "latitude": 25.276987,
            "longitude": 55.296249
        },
        "major_languages": ["Arabic", "English"],
        "famous_landmarks": ["Burj Khalifa", "Palm Jumeirah", "Dubai Mall"],
        "climate": "Desert",
        "timezone": "GST"
    },
    {
        "id": 9,
        "name": "Moscow",
        "country": "Russia",
        "population": 12615279,
        "area_km2": 2561,
        "founded": "1147-01-01",
        "coordinates": {
            "latitude": 55.7558,
            "longitude": 37.6173
        },
        "major_languages": ["Russian"],
        "famous_landmarks": ["Red Square", "Kremlin", "Saint Basil's Cathedral"],
        "climate": "Humid Continental",
        "timezone": "MSK"
    },
    {
        "id": 10,
        "name": "São Paulo",
        "country": "Brazil",
        "population": 12396372,
        "area_km2": 1521,
        "founded": "1554-01-25",
        "coordinates": {
            "latitude": -23.5505,
            "longitude": -46.6333
        },
        "major_languages": ["Portuguese"],
        "famous_landmarks": ["Paulista Avenue", "Ibirapuera Park", "São Paulo Cathedral"],
        "climate": "Tropical Savanna",
        "timezone": "BRT"
    }
]);
