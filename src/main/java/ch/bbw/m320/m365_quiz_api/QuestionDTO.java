package ch.bbw.m320.m365_quiz_api;

import lombok.*;

import java.util.List;

@Getter
@Setter
public class QuestionDTO {
    private String category;
    private String question;
    private List<String> answers;
    private String correctAnswer;

    public QuestionDTO(String category, String question, List<String> answers, String correctAnswer) {
        this.category = category;
        this.question = question;
        this.answers = answers;
        this.correctAnswer = correctAnswer;
    }


}