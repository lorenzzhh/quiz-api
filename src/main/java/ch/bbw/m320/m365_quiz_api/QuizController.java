package ch.bbw.m320.m365_quiz_api;

import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/questions")
@CrossOrigin("*")
public class QuizController {

    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @GetMapping("/{category}")
    public QuestionDTO getQuestion(@PathVariable String category) {
        return quizService.getQuestion(category);
    }
}
