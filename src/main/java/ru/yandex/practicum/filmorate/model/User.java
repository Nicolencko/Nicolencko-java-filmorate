package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class User {
    private Integer id;
    @NotNull @NotBlank
    private String login;
    @Email @NotNull
    private String email;
    private String name;

    private LocalDate birthday;

}
