package ru.yandex.practicum.filmorate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.practicum.filmorate.model.MPA;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.MpaService;

import java.util.List;

@RestController
@RequestMapping("/mpa")
public class MpaController {

    private final MpaService MPAs;

    @Autowired
    public MpaController(MpaService MPAs) {
        this.MPAs = MPAs;
    }

    @GetMapping
    public List<MPA> getMPAs() {
        return MPAs.getMPAs();
    }

    @GetMapping("/{id}")
    public MPA getMPA(@PathVariable("id") String id) {
        return MPAs.getMPAById(Long.parseLong(id));
    }
}
