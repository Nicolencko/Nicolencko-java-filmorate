package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    private Long id;
    @NotNull @NotEmpty
    private String login;
    @Email @NotNull
    private String email;
    private String name;

    private LocalDate birthday;

    public User(){}


}
