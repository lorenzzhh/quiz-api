package ch.bbw.m320.m365_quiz_api;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QuizService {

    private final MongoDatabase db;
    private final MongoCollection<Document> towns;

    @Autowired
    public QuizService(MongoClient mongoClient) {
        this.db = mongoClient.getDatabase("town-quiz-db");
        this.towns = db.getCollection("towns");
    }

    //todo aggregations - evtl get new tows for every questions
    //todo höchstes und niedrigstes fragen stellen

    public List<QuestionDTO> getQuestions(String category) {
        // Fetch 5 towns from MongoDB based on the category
        List<Document> result = towns.aggregate(Arrays.asList(
                new Document("$match", new Document(category, new Document("$ne", null))), // Filter: Category must not be null
                new Document("$sort", new Document(category, -1)), // Sort by category descending
                new Document("$limit", 5) // Fetch 5 towns
        )).into(new ArrayList<>());

        List<TownDTO> townDTOS = mapToTownDTO(result);

        List<QuestionDTO> questions = new ArrayList<>();

        for (int i = 0; i < 3; i++) {
            questions.add(generateQuestion(townDTOS, category));
        }

        return questions;
    }



    private List<TownDTO> mapToTownDTO(List<Document> result) {
        List<TownDTO> townDTOs = new ArrayList<>();

        for (Document town : result) {
            TownDTO townDTO = new TownDTO();
            townDTO.setName(town.getString("name"));
            townDTO.setCountry(town.getString("country"));
            townDTO.setPopulation(town.getInteger("population"));

            Object areaValue = town.get("area_km2");
            if (areaValue instanceof Number) {
                townDTO.setAreaKm2(((Number) areaValue).doubleValue());
            } else if (areaValue instanceof String) {
                townDTO.setAreaKm2(Double.parseDouble((String) areaValue));
            }


            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            try {
                townDTO.setFounded(dateFormat.parse(town.getString("founded")));
            } catch (ParseException e) {
                e.printStackTrace(); // oder eine sinnvolle Fehlerbehandlung
            }

            townDTOs.add(townDTO);
        }

        return townDTOs;
    }

    private QuestionDTO generateQuestion(List<TownDTO> towns, String category){

        List<TownDTO> answers = getRandomTowns(towns);

        TownDTO correctAnswer = answers.stream()
                .max(Comparator.comparingInt(TownDTO::getPopulation))
                .orElse(answers.get(0));

        return new QuestionDTO(category, "Which city has the largest population?", answers, correctAnswer);
    }

    private List<TownDTO> getRandomTowns(List<TownDTO> towns) {
        int numAnswers = Math.min(3, towns.size());
        List<TownDTO> shuffledTowns = new ArrayList<>(towns);
        Collections.shuffle(shuffledTowns);
        return shuffledTowns.subList(0, numAnswers);
    }


}
