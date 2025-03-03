package ch.bbw.m320.m365_quiz_api;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.InsertOneResult;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QuizService {

    private final MongoDatabase db;
    private final MongoCollection<Document> townCollection;
    private final MongoCollection<Document> statisticsCollection;

    @Autowired
    public QuizService(MongoClient mongoClient) {
        this.db = mongoClient.getDatabase("town-quiz-db");
        this.townCollection = db.getCollection("towns");
        this.statisticsCollection = db.getCollection("statistics");
    }

    public List<QuestionDTO> getQuestions(String category) {
        List<QuestionDTO> questions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            questions.add(generateQuestion(category));
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


    private final Random random = new Random();

    public QuestionDTO generateQuestion(String category) {
        List<TownDTO> answers = getRandomTowns(category);

        boolean findLargest = random.nextBoolean();

        Comparator<TownDTO> comparator;
        String questionText;

        switch (category.toLowerCase()) {
            case "population":
                comparator = Comparator.comparingInt(TownDTO::getPopulation);
                questionText = findLargest
                        ? "Which city has the largest population?"
                        : "Which city has the smallest population?";
                break;
            case "areakm2":
                comparator = Comparator.comparingDouble(TownDTO::getAreaKm2);
                questionText = findLargest
                        ? "Which city has the largest area?"
                        : "Which city has the smallest area?";
                break;
            case "founded":
                comparator = Comparator.comparing(TownDTO::getFounded);
                questionText = findLargest
                        ? "Which city was founded most recently?"
                        : "Which city was founded the earliest?";
                break;
            default:
                throw new IllegalArgumentException("Unknown category: " + category);
        }

        if (!findLargest) {
            comparator = comparator.reversed();
        }

        TownDTO correctAnswer = answers.stream()
                .max(comparator)
                .orElse(answers.get(0));

        return new QuestionDTO(category, questionText, answers, correctAnswer);
    }


    private List<TownDTO> getRandomTowns(String category) {
        List<Document> result = townCollection.aggregate(Arrays.asList(
                new Document("$match", new Document(category, new Document("$ne", null))),
                new Document("$sample", new Document("size", 3))
        )).into(new ArrayList<>());

        return mapToTownDTO(result);
    }

    public InsertOneResult addTown(TownDTO town) {
        Document townDocument = new Document("name", town.getName())
                .append("population", town.getPopulation())
                .append("area_km2", town.getAreaKm2())
                .append("founded", town.getFounded())
                .append("country", town.getCountry());

        return townCollection.insertOne(townDocument);
    }


    public List<QuizStatisticsDto> setStatistics(QuizStatisticsDto quizStatisticsDto) {

        Document document = new Document()
                .append("name", quizStatisticsDto.getName())
                .append("points", quizStatisticsDto.getPoints())
                .append("timeInSeconds", quizStatisticsDto.getTimeInSeconds());

        statisticsCollection.insertOne(document);

        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$sort", new Document("points", -1).append("timeInSeconds", 1)));
        pipeline.add(new Document("$limit", 3));

        List<Document> topThreeDocuments = statisticsCollection.aggregate(pipeline).into(new ArrayList<>());

        List<QuizStatisticsDto> topThreeStatistics = new ArrayList<>();
        for (Document doc : topThreeDocuments) {
            String name = doc.getString("name");
            int points = doc.getInteger("points");
            int timeInSeconds = doc.getInteger("timeInSeconds");

            QuizStatisticsDto quizStatisticsDto2 = new QuizStatisticsDto(name, points, timeInSeconds);
            topThreeStatistics.add(quizStatisticsDto2);
        }

        return topThreeStatistics;
    }
}
