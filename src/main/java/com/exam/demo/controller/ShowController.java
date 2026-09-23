package com.exam.demo.controller;

import com.exam.demo.model.Show;
import com.exam.demo.model.http.CommentRequest;
import com.exam.demo.service.ShowService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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

    @GetMapping("/{show_id}")
    public ResponseEntity<?> getShowById(@PathVariable(name = "show_id") Long showId) {
        if (showId == null || showId <= 0) {
            return ResponseEntity.badRequest().body(Map.of("error", "El ID del show debe ser un número entero positivo."));
        }
        Show result = showService.getShowById(showId);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/comments")
    public ResponseEntity<?> addComment(@Valid @RequestBody CommentRequest request) {
            showService.addComment(request);
            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "status", "success",
                    "message", "Calificación y comentario guardados correctamente."
            ));

    }
}