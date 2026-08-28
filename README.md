# Weather ChatBot

A console-based Java chatbot that answers natural-language questions about the weather using real-time data from the OpenWeatherMap API.

## Overview

Weather ChatBot lets users type everyday questions like *"is it cold in Lahore"* or *"should I go for a trip in Karachi"* and get a natural, conversational response — no rigid commands required. It also includes a side-by-side weather comparison tool for two cities.

## Features

- 💬 **Natural Language Understanding** — Detects the city from phrases like "in \<city>" or "for \<city>", and understands intent (temperature, rain, cloudiness, humidity, wind, recommendations)
- 🌦️ **Real-Time Weather Data** — Fetches live conditions via the OpenWeatherMap API
- ⚖️ **City Comparison Mode** — Compares temperature, condition, humidity, and wind speed between two cities in a clean table
- 🧭 **Smart Recommendations** — Suggests whether it's a good time for outdoor activities based on current temperature and conditions
- 🔑 **Secure API Key Handling** — Key is loaded from a `config.properties` file at runtime, never hardcoded
- 📟 **Simple Menu-Driven Interface** — Easy console navigation between chatbot mode and comparison mode

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java (core Java, no external libraries) |
| Weather Data | OpenWeatherMap API |
| Networking | `HttpURLConnection` |
| Config | `config.properties` (API key storage) |

## How It Works

1. User selects a mode from the menu: **Start Chatbot** or **Compare Weather of Two Cities**
2. In chatbot mode, the user types a question mentioning a city (e.g. "is it going to rain in Multan")
3. The app extracts the city name, calls the OpenWeatherMap API, and parses the JSON response
4. Based on keywords in the question (temp, rain, cloudy, humid, wind, or trip/recommendation), it crafts a relevant, human-readable reply

## Project Structure

```
WeatherChatBot/
├── WeatherChatBot.java      # Main chatbot logic, API calls, response generation
├── config.properties        # Stores API key (gitignored)
└── .gitignore
```

## Getting Started

### Prerequisites
- Java JDK installed
- A free API key from [OpenWeatherMap](https://openweathermap.org/api)

### Setup

1. Clone the repo:
   ```bash
   git clone https://github.com/noor-ul-subha/weather-chatbot.git
   ```
2. Create a `config.properties` file in the project root with:
   ```
   API_KEY=your_openweathermap_api_key_here
   ```
3. Compile and run:
   ```bash
   javac WeatherChatBot.java
   java WeatherChatBot
   ```

## Example Interaction

```
You: is it cold in Islamabad
Chatbot: The current temperature in Islamabad is 18.5°C.

You: should i go for a trip in Karachi
Chatbot: Based on the current weather in Karachi (Clear, 27.0°C), 
it's a good time for outdoor activities!
```

## Author

**Noor Ul Subha**
BS Software Engineering, COMSATS University Islamabad — Sahiwal Campus
