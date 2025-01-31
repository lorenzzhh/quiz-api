package ch.bbw.m320.m365_quiz_api;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class TownDTO {
    private String name;
    private String country;
    private int population;
    private double areaKm2;
    private Date founded;

    public TownDTO(String name, String country, int population, double areaKm2, Date founded) {
        this.name = name;
        this.country = country;
        this.population = population;
        this.areaKm2 = areaKm2;
        this.founded = founded;
    }

    public TownDTO() {
    }
}
