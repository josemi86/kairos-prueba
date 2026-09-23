package com.exam.demo.controller;
import com.exam.demo.model.Show;
import com.exam.demo.model.http.CommentRequest;
import com.exam.demo.service.ShowService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import tools.jackson.databind.ObjectMapper;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ShowController.class)
class ShowControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ShowService showService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void searchShows_WithValidQuery_ShouldReturnOkAndList() throws Exception {
        String query = "name";
        List<Show> expectedShows = List.of(new Show(1L, "name", "channel", "summary", List.of("genre1", "genre2")));
        when(showService.searchShows(query)).thenReturn(expectedShows);

        mockMvc.perform(get("/api/shows/search")
                        .param("search_query", query)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("name"));
    }

    @Test
    void searchShows_WithEmptyQuery_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/api/shows/search")
                        .param("search_query", "   ")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El parámetro 'search_query' es requerido."));
    }

    @Test
    void getShowById_WithValidId_ShouldReturnOkAndShow() throws Exception {
        Long showId = 1L;
        Show expectedShow = new Show(showId, "name", "channel", "summary", List.of("genre1", "genre2"));
        when(showService.getShowById(showId)).thenReturn(expectedShow);

        mockMvc.perform(get("/api/shows/{show_id}", showId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(showId))
                .andExpect(jsonPath("$.name").value("name"));
    }

    @Test
    void getShowById_WithInvalidId_ShouldReturnBadRequest() throws Exception {
        Long invalidId = -5L;

        mockMvc.perform(get("/api/shows/{show_id}", invalidId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("El ID del show debe ser un número entero positivo."));
    }

    @Test
    void addComment_WithValidRequest_ShouldReturnCreated() throws Exception {
        CommentRequest validRequest = new CommentRequest();
        validRequest.setShowId(1L);
        validRequest.setRating(5);
        validRequest.setComment("Excelente serie, muy recomendada.");

        doNothing().when(showService).addComment(validRequest);

        mockMvc.perform(post("/api/shows/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value("Calificación y comentario guardados correctamente."));

    }

    @Test
    void addComment_WithInvalidRequest_ShouldReturnBadRequest() throws Exception {
        CommentRequest invalidRequest = new CommentRequest();
        invalidRequest.setRating(12);

        mockMvc.perform(post("/api/shows/comments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
}