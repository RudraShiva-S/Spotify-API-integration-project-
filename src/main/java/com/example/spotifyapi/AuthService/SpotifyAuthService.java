package com.example.spotifyapi.AuthService;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Service
public class SpotifyAuthService {
    @Value("${spotify.client-id}")
    private String clientId;

    @Value("${spotify.client-secret}")
    private String clientSecret;

    @Value("${spotify.redirect-uri}")
    private String redirectUri;

    private String accessToken = "";
    private String refreshToken = "";

    public String buildLoginURL() {
        return "https://accounts.spotify.com/authorize?client_id=" + clientId +
                "&response_type=code&redirect_uri=" + redirectUri +
                "&scope=user-top-read user-read-currently-playing user-modify-playback-state user-read-playback-state";
    }

    public void fetchToken(String code) {
        RestTemplate rest = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("code", code);
        params.add("redirect_uri", redirectUri);
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
        ResponseEntity<Map> response = rest.postForEntity("https://accounts.spotify.com/api/token", request, Map.class);

        Map body = response.getBody();
        accessToken = body.get("access_token").toString();
        refreshToken = body.get("refresh_token").toString();
    }

    public String getAccessToken() {
        return accessToken;
    }
}
