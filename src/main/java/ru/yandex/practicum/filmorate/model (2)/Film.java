package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
     private Integer id;
    @NotNull @NotBlank
    private String name;
    private String description;
    private LocalDate releaseDate;
    @PositiveOrZero
    private Integer duration;

}
