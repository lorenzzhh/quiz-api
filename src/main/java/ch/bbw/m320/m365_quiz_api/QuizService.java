package ch.bbw.m320.m365_quiz_api;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QuizService {

    private final MongoDatabase db;
    private final MongoCollection<Document> towns;

    @Autowired
    public QuizService(MongoClient mongoClient) {
        this.db = mongoClient.getDatabase("town-quiz-db");
        this.towns = db.getCollection("towns");
    }


    public QuestionDTO getQuestion(String category) {

        List<Document> result = towns.aggregate(Arrays.asList(
                new Document("$match", new Document(category, new Document("$ne", null))), // Filter: Kategorie darf nicht null sein
                new Document("$sort", new Document(category, -1)), // Sortieren nach der Kategorie in absteigender Reihenfolge
                new Document("$limit", 3) // Nur die ersten drei Städte auswählen
        )).into(new ArrayList<>());

        System.out.println(result);

        List<String> answers = Arrays.asList("Tokyo", "Berlin", "New York");
        return new QuestionDTO(category, "Which has the biggest population?", answers, "Tokyo");
    }

}