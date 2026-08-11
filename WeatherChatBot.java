
import java.io.*;
import java.net.*;
import java.util.*;

public class WeatherChatBot {
private static final String API_KEY = loadApiKey();

private static String loadApiKey() {
    Properties properties = new Properties();
y
    try (FileInputStream file = new FileInputStream("config.properties")) {
        properties.load(file);
        return properties.getProperty("API_KEY");
    } catch (IOException e) {
        System.out.println("API key could not be loaded.");
        return "";
    }
}

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        do {
            System.out.println("\n=========== WEATHER CHATBOT MENU ===========");
            System.out.println("1. Start Chatbot");
            System.out.println("2. Compare Weather of Two Cities");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");

            choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    startChatbot(sc);
                    break;

                case 2:
                    compareTwoCities(sc);
                    break;

                case 3:
                    System.out.println("Exiting program. Goodbye!");
                    break;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }

        } while (choice != 3);

        sc.close();
    }

    // ================= START CHATBOT =================

    static void startChatbot(Scanner sc) {

        System.out.println("=======================================");
        System.out.println("       WELCOME TO WEATHER CHATBOT");
        System.out.println("=======================================");
        System.out.println("Ask me anything about weather!");
        System.out.println("Type 'exit' to quit");
        System.out.println("Use 'in' before city name");
        System.out.println("=======================================");

        while (true) {

            System.out.print("\nYou: ");
            String input = sc.nextLine().toLowerCase().trim();

            if (input.equals("exit")) {
                System.out.println("\nChatbot: Goodbye! Have a nice day!");
                break;
            }

            String city = extractCity(input);

            if (city == null) {
                System.out.println("\nChatbot: Please mention a city.");
                continue;
            }

            try {
                String json = getWeather(city);
                respond(input, city, json);

            } catch (Exception e) {
                System.out.println(
                    "\nChatbot: I'm having trouble getting weather information."
                );
                System.out.println("Please check the city name or try again later.");
            }
        }
    }

    // ================= COMPARE TWO CITIES =================

    public static void compareTwoCities(Scanner sc) {

        try {
            System.out.print("Enter first city: ");
            String city1 = sc.nextLine().trim();

            System.out.print("Enter second city: ");
            String city2 = sc.nextLine().trim();

            String json1 = getWeather(city1);
            String json2 = getWeather(city2);

            String temp1 = extract(json1, "\"temp\":", ",");
            String cond1 = extract(json1, "\"main\":\"", "\"");
            String humid1 = extract(json1, "\"humidity\":", ",");
            String wind1 = extract(json1, "\"speed\":", ",");

            String temp2 = extract(json2, "\"temp\":", ",");
            String cond2 = extract(json2, "\"main\":\"", "\"");
            String humid2 = extract(json2, "\"humidity\":", ",");
            String wind2 = extract(json2, "\"speed\":", ",");

            System.out.println("\n================ WEATHER COMPARISON ================");

            System.out.printf(
                "%-20s %-20s %-20s%n",
                "Parameter",
                city1,
                city2
            );

            System.out.printf(
                "%-20s %-20s %-20s%n",
                "Temperature (°C)",
                temp1,
                temp2
            );

            System.out.printf(
                "%-20s %-20s %-20s%n",
                "Condition",
                cond1,
                cond2
            );

            System.out.printf(
                "%-20s %-20s %-20s%n",
                "Humidity (%)",
                humid1,
                humid2
            );

            System.out.printf(
                "%-20s %-20s %-20s%n",
                "Wind Speed (m/s)",
                wind1,
                wind2
            );

            System.out.println("====================================================");

        } catch (Exception e) {

            System.out.println(
                "Error fetching weather data for one or both cities."
            );
            System.out.println("Please check the city names and try again.");
        }
    }

    // ================= EXTRACT CITY =================

    static String extractCity(String input) {

        String[] words = input.split("\\s+");

        for (int i = 0; i < words.length - 1; i++) {

            if (words[i].equals("in") || words[i].equals("for")) {
                return words[i + 1];
            }
        }

        return null;
    }

    // ================= GET WEATHER FROM API =================

    static String getWeather(String city) throws Exception {

        String encodedCity = URLEncoder.encode(
            city,
            java.nio.charset.StandardCharsets.UTF_8
        );

        String link =
            "https://api.openweathermap.org/data/2.5/weather?q="
            + encodedCity
            + "&appid="
            + API_KEY
            + "&units=metric";

        HttpURLConnection con =
            (HttpURLConnection) new URL(link).openConnection();

        con.setRequestMethod("GET");
        con.setConnectTimeout(10000);
        con.setReadTimeout(10000);

        int responseCode = con.getResponseCode();

        if (responseCode != 200) {
            throw new IOException(
                "API request failed with response code: " + responseCode
            );
        }

        BufferedReader br = new BufferedReader(
            new InputStreamReader(con.getInputStream())
        );

        StringBuilder sb = new StringBuilder();
        String line;

        while ((line = br.readLine()) != null) {
            sb.append(line);
        }

        br.close();
        con.disconnect();

        return sb.toString();
    }

    // ================= CHATBOT RESPONSE =================

    static void respond(String input, String city, String json) {

        try {

            String temp = extract(json, "\"temp\":", ",");
            String condition = extract(json, "\"main\":\"", "\"");
            String humidity = extract(json, "\"humidity\":", ",");
            String wind = extract(json, "\"speed\":", ",");

            String capCity =
                city.substring(0, 1).toUpperCase()
                + city.substring(1);

            String res;

            input = input.toLowerCase();

            // Temperature
            if (
                input.contains("temp")
                || input.contains("hot")
                || input.contains("cold")
            ) {

                res =
                    "The current temperature in "
                    + capCity
                    + " is "
                    + temp
                    + "°C.";
            }

            // Rain
            else if (
                input.contains("rain")
                || input.contains("umbrella")
            ) {

                boolean rain =
                    condition.toLowerCase().contains("rain")
                    || condition.toLowerCase().contains("drizzle");

                res =
                    "It's currently "
                    + condition
                    + " in "
                    + capCity
                    + ". "
                    + (
                        rain
                            ? "You might want to bring an umbrella!"
                            : "No need for an umbrella right now."
                    );
            }

            // Cloudy
            else if (
                input.contains("cloudy")
                || input.contains("cloud")
            ) {

                boolean cloudy =
                    condition.toLowerCase().contains("cloud")
                    || condition.toLowerCase().contains("overcast");

                res =
                    capCity
                    + " is "
                    + (cloudy ? "cloudy" : "not cloudy")
                    + " right now. The current condition is "
                    + condition
                    + ".";
            }

            // Humidity
            else if (input.contains("humid")) {

                res =
                    "The humidity in "
                    + capCity
                    + " is "
                    + humidity
                    + "%.";
            }

            // Wind
            else if (input.contains("wind")) {

                res =
                    "The wind speed in "
                    + capCity
                    + " is "
                    + wind
                    + " m/s.";
            }

            // Recommendation
            else if (
                input.contains("should i")
                || input.contains("trip")
                || input.contains("recommend")
            ) {

                double t = Double.parseDouble(temp);

                boolean good =
                    !condition.toLowerCase()
                        .matches(".*(rain|storm|snow).*")
                    && t > 15
                    && t < 30;

                res =
                    "Based on the current weather in "
                    + capCity
                    + " ("
                    + condition
                    + ", "
                    + temp
                    + "°C), "
                    + (
                        good
                            ? "it's a good time for outdoor activities!"
                            : "you might want to reconsider or prepare accordingly."
                    );
            }

            // General weather information
            else {

                res =
                    "Currently in "
                    + capCity
                    + ", it's "
                    + condition
                    + " with a temperature of "
                    + temp
                    + "°C. The humidity is "
                    + humidity
                    + "% and wind speed is "
                    + wind
                    + " m/s.";
            }

            System.out.println("\nChatbot: " + res);

        } catch (Exception e) {

            System.out.println(
                "\nChatbot: Error processing weather data."
            );
        }
    }

    // ================= EXTRACT JSON VALUE =================

    static String extract(
        String json,
        String key,
        String endChar
    ) {

        int startIndex = json.indexOf(key);

        if (startIndex == -1) {
            return "N/A";
        }

        startIndex += key.length();

        int endIndex = json.indexOf(
            endChar,
            startIndex
        );

        if (endIndex == -1) {
            return "N/A";
        }

        return json.substring(
            startIndex,
            endIndex
        );
    }
}