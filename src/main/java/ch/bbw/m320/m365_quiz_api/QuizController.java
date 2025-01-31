package ch.bbw.m320.m365_quiz_api;

import com.mongodb.client.result.InsertOneResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/questions")
@CrossOrigin("*")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/{category}")
    public List<QuestionDTO> getQuestion(@PathVariable String category) {
        return quizService.getQuestions(category);
    }

    @PostMapping
    public InsertOneResult addTown(@RequestBody TownDTO town) {
        return quizService.addTown(town);
    }

    @PostMapping("/statistics")
    public List<QuizStatisticsDto> getStatistics(@RequestBody QuizStatisticsDto quizStatisticsDto) {
        return quizService.setStatistics(quizStatisticsDto);
    }
}
