# 🎵 Spotify API Integration - Spring Boot

This is a Spring Boot application that integrates with the **Spotify Web API** to display your **Top 10 Tracks** and the **Currently Playing** song. It also includes basic playback controls like pause and play a specific track.

## 🚀 Features

- 🔐 Spotify OAuth 2.0 Authentication
- 🎶 View Top 10 Tracks
- 🎧 See Currently Playing Song
- ⏸️ Pause Playback
- ▶️ Play a Track by ID

## 📦 Tech Stack

- Java 17
- Spring Boot
- RestTemplate (for HTTP requests)
- Spotify Web API
- Maven

## 🔗 Endpoints

| Endpoint              | Method | Description                         |
|-----------------------|--------|-------------------------------------|
| `/`                   | GET    | Redirect to Spotify Login           |
| `/callback?code=xxx`  | GET    | Handles Spotify OAuth redirect      |
| `/spotify`            | GET    | Shows Top Tracks + Now Playing song |
| `/spotify/pause`      | POST   | Pause current playback              |
| `/spotify/play/{id}`  | POST   | Play a track using its ID           |

## 🛠️ Setup Instructions

1. **Clone the repo**
   ```bash
   git clone https://github.com/your-username/spotify-api-springboot.git
   cd spotify-api-springboot

2. Configure Spotify Credentials
#Spotify SetUp application.properties:

spotify.client-id=YOUR_CLIENT_ID
spotify.client-secret=YOUR_CLIENT_SECRET
spotify.redirect-uri=http://127.0.0.1:8080/callback

3.Run the application
mvn spring-boot:run

4.Access
Visit:http://127.0.0.1:8080/callback
Example Output (JSON)
{
  "topTracks": [
    { "id": "abc123", "name": "Blinding Lights", "artist": "The Weeknd" },
    { "id": "def456", "name": "Levitating", "artist": "Dua Lipa" }
  ],
  "currentlyPlaying": {
    "name": "Heat Waves",
    "artist": "Glass Animals"
  }
}
