package com.exam.demo.service;

import com.exam.demo.model.Show;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ShowService {

    private final RestTemplate restTemplate = new RestTemplate();

    public List<Show> searchShows(String searchQuery) {
        String url = "http://api.tvmaze.com/search/shows?q=" + searchQuery;
        List<Map<String, Object>> apiResponse = restTemplate.getForObject(url, List.class);
        List<Show> formattedShows = new ArrayList<>();
        if (apiResponse != null) {
            for (Map<String, Object> item : apiResponse) {
                Map<String, Object> showMap = (Map<String, Object>) item.get("show");
                if (showMap != null) {
                    Long id = ((Number) showMap.get("id")).longValue();
                    String name = (String) showMap.get("name");
                    String summary = (String) showMap.get("summary");
                    List<String> genres = (List<String>) showMap.get("genres");
                    String channel = "Unknown";
                    Map<String, Object> network = (Map<String, Object>) showMap.get("network");
                    Map<String, Object> webChannel = (Map<String, Object>) showMap.get("webChannel");

                    if (network != null && network.get("name") != null) {
                        channel = (String) network.get("name");
                    } else if (webChannel != null && webChannel.get("name") != null) {
                        channel = (String) webChannel.get("name");
                    }
                    formattedShows.add(new Show(id, name, channel, summary, genres));
                }
            }
        }
        return formattedShows;
    }
}