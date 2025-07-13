package com.example.spotifyapi.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
@RequiredArgsConstructor
public class SpotifyService {

    private final SpotifyAuthService authService;

    private List<Map<String, Object>> cachedTopTracks = null;
    private long lastFetchedTimeTopTracks = 0;

    private Map<String, Object> cachedNowPlaying = null;
    private long lastFetchedTimeNowPlaying = 0;

    private final long CACHE_TTL_MS = 60 * 1000; // 60 seconds

    public List<Map<String, Object>> getTopTracks() {
        if (cachedTopTracks != null && System.currentTimeMillis() - lastFetchedTimeTopTracks < CACHE_TTL_MS) {
            return cachedTopTracks;
        }

        String accessToken = authService.getAccessToken();
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = rest.exchange(
                "https://api.spotify.com/v1/me/top/tracks",
                HttpMethod.GET,
                entity,
                Map.class
        );

        Map body = response.getBody();
        cachedTopTracks = (List<Map<String, Object>>) body.get("items");
        lastFetchedTimeTopTracks = System.currentTimeMillis();

        return cachedTopTracks;
    }

    public Map<String, Object> getNowPlaying() {
        if (cachedNowPlaying != null && System.currentTimeMillis() - lastFetchedTimeNowPlaying < CACHE_TTL_MS) {
            return cachedNowPlaying;
        }

        String accessToken = authService.getAccessToken();
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);

        ResponseEntity<Map> response = rest.exchange(
                "https://api.spotify.com/v1/me/player/currently-playing",
                HttpMethod.GET,
                entity,
                Map.class
        );

        cachedNowPlaying = response.getBody();
        lastFetchedTimeNowPlaying = System.currentTimeMillis();

        return cachedNowPlaying;
    }

    public void pause() {
        String accessToken = authService.getAccessToken();
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        rest.exchange("https://api.spotify.com/v1/me/player/pause", HttpMethod.PUT, entity, Void.class);
    }

    public void playTrack(String trackId) {
        String accessToken = authService.getAccessToken();
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = Map.of("uris", List.of("spotify:track:" + trackId));
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(body, headers);

        rest.exchange("https://api.spotify.com/v1/me/player/play", HttpMethod.PUT, entity, Void.class);
    }
}
