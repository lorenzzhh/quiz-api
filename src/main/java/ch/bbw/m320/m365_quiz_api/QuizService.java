package ch.bbw.m320.m365_quiz_api;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class QuizService {

    private final MongoDatabase db;
    private final MongoCollection<Document> towns;
    private final MongoCollection<Document> statistics;

    @Autowired
    public QuizService(MongoClient mongoClient) {
        this.db = mongoClient.getDatabase("town-quiz-db");
        this.towns = db.getCollection("towns");
        this.statistics = db.getCollection("statistics");
    }

    //todo aggregations - evtl get new tows for every questions

    public List<QuestionDTO> getQuestions(String category) {
        // Fetch 5 towns from MongoDB based on the category
        List<Document> result = towns.aggregate(Arrays.asList(
                new Document("$match", new Document(category, new Document("$ne", null))), // Filter: Category must not be null
                new Document("$sort", new Document(category, -1)), // Sort by category descending
                new Document("$limit", 5) // Fetch 5 towns
        )).into(new ArrayList<>());

        List<TownDTO> townDTOS = mapToTownDTO(result);

        List<QuestionDTO> questions = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
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


    private final Random random = new Random();

    public QuestionDTO generateQuestion(List<TownDTO> towns, String category) {
        List<TownDTO> answers = getRandomTowns(towns);

        boolean findLargest = random.nextBoolean(); // 50:50 Chance für größte oder kleinste Werte

        Comparator<TownDTO> comparator;
        String questionText;

        switch (category.toLowerCase()) {
            case "population":
                comparator = Comparator.comparingInt(TownDTO::getPopulation);
                questionText = findLargest
                        ? "Which city has the largest population?"
                        : "Which city has the smallest population?";
                break;
            case "areaKm2":
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


    private List<TownDTO> getRandomTowns(List<TownDTO> towns) {
        int numAnswers = Math.min(3, towns.size());
        List<TownDTO> shuffledTowns = new ArrayList<>(towns);
        Collections.shuffle(shuffledTowns);
        return shuffledTowns.subList(0, numAnswers);
    }


    public List<QuizStatisticsDto> setStatistics(QuizStatisticsDto quizStatisticsDto) {
        // Create a Document object from the QuizStatisticsDto
        Document document = new Document()
                .append("name", quizStatisticsDto.getName())
                .append("points", quizStatisticsDto.getPoints())
                .append("timeInSeconds", quizStatisticsDto.getTimeInSeconds());

        // Insert the document into the MongoDB collection
        statistics.insertOne(document);

        List<Document> pipeline = new ArrayList<>();
        pipeline.add(new Document("$sort", new Document("points", -1).append("timeInSeconds", 1))); // Sort by points (desc), timeInSeconds (asc)
        pipeline.add(new Document("$limit", 3)); // Limit the result to 3 documents

        // Run the aggregation query and get the result as a list of documents
        List<Document> topThreeDocuments = statistics.aggregate(pipeline).into(new ArrayList<>());

        // Convert the list of documents into a list of QuizStatisticsDto
        List<QuizStatisticsDto> topThreeStatistics = new ArrayList<>();
        for (Document doc : topThreeDocuments) {
            String name = doc.getString("name");
            int points = doc.getInteger("points");
            int timeInSeconds = doc.getInteger("timeInSeconds");

            // Create a QuizStatisticsDto and add it to the list
            QuizStatisticsDto quizStatisticsDto2 = new QuizStatisticsDto(name, points, timeInSeconds);
            topThreeStatistics.add(quizStatisticsDto2);
        }

        return topThreeStatistics;
    }
}
