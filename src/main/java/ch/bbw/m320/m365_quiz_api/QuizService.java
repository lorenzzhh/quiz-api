package ch.bbw.m320.m365_quiz_api;

import com.mongodb.client.MongoClient;
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

    public List<QuestionDTO> getQuestions(String category) {
        // Fetch 5 towns from MongoDB based on the category
        List<Document> result = towns.aggregate(Arrays.asList(
                new Document("$match", new Document(category, new Document("$ne", null))), // Filter: Category must not be null
                new Document("$sort", new Document(category, -1)), // Sort by category descending
                new Document("$limit", 5) // Fetch 5 towns
        )).into(new ArrayList<>());

        System.out.println(result); // Debugging output

        List<QuestionDTO> questions = new ArrayList<>();

        for (Document town : result) {
            String name = town.getString("name");
            int population = town.getInteger("population");

            List<String> answers = Arrays.asList("Tokyo", "Berlin", "New York", name);
            String correctAnswer = answers.get(0); // Example logic: Tokyo always correct

            questions.add(
                    new QuestionDTO(
                            category, "Which city has the largest population?", answers, correctAnswer
                    )
            );
        }

        return questions;
    }
}
