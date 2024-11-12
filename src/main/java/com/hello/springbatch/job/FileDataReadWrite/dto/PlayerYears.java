package com.hello.springbatch.job.FileDataReadWrite.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.Year;

@NoArgsConstructor
@Data
public class PlayerYears {
    private String ID;
    private String lastName;
    private String firstName;
    private int birthYear;
    private int debutYear;
    private int yearExperience;

    public PlayerYears(Player player) {
        this.ID = player.getID();
        this.lastName = player.getLastName();
        this.firstName = player.getFirstName();
        this.birthYear = player.getBirthYear();
        this.debutYear = player.getDebutYear();
        this.yearExperience = Year.now().getValue() - player.getDebutYear();
    }
}
