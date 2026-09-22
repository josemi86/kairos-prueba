package com.exam.demo.controller;

import com.exam.demo.model.Show;
import com.exam.demo.service.ShowService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/shows")
public class ShowController {

    @Autowired
    private ShowService showService;

    @GetMapping("/search")
    public ResponseEntity<?> searchShows(@RequestParam(name = "search_query") String searchQuery) {
        if (searchQuery == null || searchQuery.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El parámetro 'search_query' es requerido."));
        }
        List<Show> shows = showService.searchShows(searchQuery);
        return ResponseEntity.ok(shows);
    }

}