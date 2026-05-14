package com.plantsync.platform.plantprofiles.interfaces.rest;

import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/api/v1/weather")
@Tag(name = "Weather", description = "Weather Proxy Endpoint")
public class WeatherQueryController {

    private final RestTemplate restTemplate = new RestTemplate();

    @Value("${weather.api.key}")
    private String apiKey;

    @GetMapping("/city")
    public ResponseEntity<String> getWeatherByCity(@RequestParam String city) {

        String url = "https://api.openweathermap.org/data/2.5/weather?q="
                + city
                + "&appid=" + apiKey
                + "&units=metric";

        try {

            ResponseEntity<String> response =
                    restTemplate.getForEntity(url, String.class);

            return ResponseEntity.ok(response.getBody());

        } catch (Exception e) {

            return ResponseEntity
                    .status(HttpStatus.BAD_GATEWAY)
                    .body("Weather API error: " + e.getMessage());
        }
    }
}