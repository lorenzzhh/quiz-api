package ch.bbw.m320.m365_quiz_api;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class QuestionDTO {
    private String category;
    private String question;
    private List<String> answers;
    private String correctAnswer;

    public QuestionDTO() {}

    public QuestionDTO(String category, String question, List<String> answers, String correctAnswer) {
        this.category = category;
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }

    @Override
    public String toString() {
        return "PopulationQuestionDTO{" +
                "category='" + category + '\'' +
                ", question='" + question + '\'' +
                ", answers=" + answers +
                ", correctAnswer='" + correctAnswer + '\'' +
                '}';
    }
}
