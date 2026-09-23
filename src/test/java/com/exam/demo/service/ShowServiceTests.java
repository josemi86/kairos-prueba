package com.exam.demo.service;

import com.exam.demo.model.Show;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShowServiceTest {

    @InjectMocks
    private ShowService showService;

    @Mock
    private RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(showService, "restTemplate", restTemplate);
    }

    @Test
    void searchShows_ShouldReturnFormattedShows_WhenApiReturnsDataWithNetwork() {
        String query = "name";
        String expectedUrl = "http://api.tvmaze.com/search/shows?q=" + query;

        List<Map<String, Object>> apiResponse = new ArrayList<>();
        Map<String, Object> item = new HashMap<>();
        Map<String, Object> showMap = new HashMap<>();
        Map<String, Object> networkMap = new HashMap<>();

        networkMap.put("name", "networkName");

        showMap.put("id", 1);
        showMap.put("name", "name");
        showMap.put("summary", "summary");
        showMap.put("genres", List.of("genre1", "genre2"));
        showMap.put("network", networkMap);
        showMap.put("webChannel", null);

        item.put("show", showMap);
        apiResponse.add(item);

        when(restTemplate.getForObject(eq(expectedUrl), eq(List.class))).thenReturn(apiResponse);

        List<Show> result = showService.searchShows(query);

        assertNotNull(result);
        assertEquals(1, result.size());
        Show show = result.get(0);
        assertEquals(1L, show.getId()); // Reemplaza con tus getters reales de tu modelo Show
        assertEquals("name", show.getName());
        assertEquals("networkName", show.getChannel());
        assertEquals("summary", show.getSummary());
        assertEquals(2, show.getGenres().size());
    }

    @Test
    void getShowById_ShouldReturnShow_WhenApiReturnsValidMap() {
        Long showId = 1L;
        String expectedUrl = "https://api.tvmaze.com/shows/" + showId;

        Map<String, Object> showMap = new HashMap<>();
        Map<String, Object> networkMap = new HashMap<>();
        networkMap.put("name", "networkName");

        showMap.put("id", showId);
        showMap.put("name", "name");
        showMap.put("summary", "summary");
        showMap.put("genres", List.of("genre1", "genre2"));
        showMap.put("network", networkMap);
        showMap.put("webChannel", null);

        when(restTemplate.getForObject(eq(expectedUrl), eq(Map.class))).thenReturn(showMap);

        Show result = showService.getShowById(showId);

        assertNotNull(result);
        assertEquals(showId, result.getId());
        assertEquals("name", result.getName());
        assertEquals("networkName", result.getChannel());
        assertEquals("summary", result.getSummary());
        assertEquals(2, result.getGenres().size());
    }
}