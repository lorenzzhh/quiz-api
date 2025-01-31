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
    },
    {
        "id": 2,
        "name": "Paris",
        "country": "France",
        "population": 2161000,
        "area_km2": 105.4,
        "founded": "52-01-01",
    },
    {
        "id": 3,
        "name": "New York",
        "country": "USA",
        "population": 8419600,
        "area_km2": 783.8,
        "founded": "1624-01-01",
    },
    {
        "id": 4,
        "name": "Tokyo",
        "country": "Japan",
        "population": 13929286,
        "area_km2": 2191,
        "founded": "1603-01-01",
    },
    {
        "id": 5,
        "name": "Sydney",
        "country": "Australia",
        "population": 5312163,
        "area_km2": 12368,
        "founded": "1788-01-26",
    }
]);
