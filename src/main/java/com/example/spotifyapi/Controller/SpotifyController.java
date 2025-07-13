package com.example.spotifyapi.Controller;


import com.example.spotifyapi.AuthService.SpotifyAuthService;
import com.example.spotifyapi.Service.SpotifyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.util.Map;

@RestController
    @RequiredArgsConstructor
    public class SpotifyController {

        private final SpotifyAuthService authService;
        private final SpotifyService spotifyService;

        @GetMapping("/")
        public RedirectView root() {
            return new RedirectView(authService.buildLoginURL());
        }

        @GetMapping("/callback")
        public RedirectView callback(@RequestParam String code) {
            authService.fetchToken(code);
            return new RedirectView("/spotify");
        }

        @GetMapping("/spotify")
        public Map<String, Object> getSpotifyData() {
            return Map.of(
                    "topTracks", spotifyService.getTopTracks(),
                    "currentlyPlaying", spotifyService.getNowPlaying()
            );
        }

        @PostMapping("/spotify/pause")
        public String pause() {
            spotifyService.pause();
            return "Playback paused.";
        }

        @PostMapping("/spotify/play/{id}")
        public String play(@PathVariable String id) {
            spotifyService.playTrack(id);
            return "Playing track: " + id;
        }
    }


