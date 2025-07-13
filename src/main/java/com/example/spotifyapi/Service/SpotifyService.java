package com.example.spotifyapi.Service;


import com.example.spotifyapi.AuthService.SpotifyAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
    @RequiredArgsConstructor
    public class SpotifyService {
        private final SpotifyAuthService authService;
        private final RestTemplate restTemplate = new RestTemplate();

        public List<Map<String, String>> getTopTracks() {
            String url = "https://api.spotify.com/v1/me/top/tracks?limit=10";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authService.getAccessToken());
            HttpEntity<?> request = new HttpEntity<>(headers);

            ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
            List<Map<String, Object>> items = (List<Map<String, Object>>) response.getBody().get("items");

            return items.stream().map(item -> Map.of(
                    "id", item.get("id").toString(),
                    "name", item.get("name").toString(),
                    "artist", ((List<Map<String, Object>>) item.get("artists")).get(0).get("name").toString()
            )).toList();
        }

        public Map<String, String> getNowPlaying() {
            String url = "https://api.spotify.com/v1/me/player/currently-playing";
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authService.getAccessToken());
            HttpEntity<?> request = new HttpEntity<>(headers);

            try {
                ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.GET, request, Map.class);
                Map<String, Object> item = (Map<String, Object>) response.getBody().get("item");
                return Map.of(
                        "name", item.get("name").toString(),
                        "artist", ((List<Map<String, Object>>) item.get("artists")).get(0).get("name").toString()
                );
            } catch (Exception e) {
                return Map.of("nowPlaying", "Nothing is playing");
            }
        }

        public void pause() {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authService.getAccessToken());
            restTemplate.exchange("https://api.spotify.com/v1/me/player/pause", HttpMethod.PUT, new HttpEntity<>(headers), Void.class);
        }

        public void playTrack(String id) {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(authService.getAccessToken());
            headers.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> body = Map.of("uris", List.of("spotify:track:" + id));
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);
            restTemplate.exchange("https://api.spotify.com/v1/me/player/play", HttpMethod.PUT, request, Void.class);
        }
    }


