package ch.bbw.m320.m365_quiz_api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizStatisticsDto {
    private String name;
    private int points;
    private int timeInSeconds;

    public QuizStatisticsDto(String name, int points, int timeInSeconds) {
        this.name = name;
        this.points = points;
        this.timeInSeconds = timeInSeconds;
    }
}
