package fi.tuni.prog3.sisu;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * The class that we can use to interact with the API.
 */
class SisuAPI implements iAPI {

    /**
     * Constructor for the SisuAPI class.
     */
    public SisuAPI(){
    }

    /**
     * Returns a JsonObject that is recieved from Sisu using the given URL.
     * @param urlString URL with which we can recieve information from Sisu.
     * @return JsonObject.
     */
    @Override
    public JsonObject getJsonObjectFromApi(String urlString) {
        // JsonObject for storing the data.
        JsonObject jsonObject = null;
        try {
            // Opening the URL connection and defining the requests.
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("charset", "utf-8");

            int responseCode = connection.getResponseCode();

            // If response code is valid continues
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuffer response = new StringBuffer();

                // Read the api response and append it to the variable response.
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                connection.disconnect();

                try {
                    jsonObject = JsonParser.parseString(response.toString()).getAsJsonObject();
                } catch (Exception e) {
                    JsonArray moduleArray = JsonParser.parseString(response.toString()).getAsJsonArray();
                    jsonObject = moduleArray.get(0).getAsJsonObject();
                }

            } else {
                System.out.println("HTTP response code is not correct");
            }
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
        return jsonObject;
    }
}