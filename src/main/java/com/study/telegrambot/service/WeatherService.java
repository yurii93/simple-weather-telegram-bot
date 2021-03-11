package com.study.telegrambot.service;

import com.study.telegrambot.model.WeatherModel;
import com.study.telegrambot.util.Config;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;

public class WeatherService {
    private static final String WEATHER_URL_PROP_TITLE = "weatherapi.url";
    private static final String WEATHER_TOKEN_PROP_TITLE = "weatherapi.token";

    public static String getWeather(String message, WeatherModel weatherModel) throws IOException {
        URL url = new URL(String.format(
                Config.get(WEATHER_URL_PROP_TITLE),
                message,
                Config.get(WEATHER_TOKEN_PROP_TITLE)
        ));

        Scanner in = new Scanner((InputStream) url.getContent());
        StringBuilder responseString = new StringBuilder();
        while (in.hasNext()) {
            responseString.append(in.nextLine());
        }

        JSONObject object = new JSONObject(responseString.toString());
        weatherModel.setName(object.getString("name"));

        JSONObject main = object.getJSONObject("main");
        weatherModel.setTemp(main.getDouble("temp"));
        weatherModel.setHumidity(main.getInt("humidity"));

        JSONArray getArray = object.getJSONArray("weather");
        for (int i = 0; i < getArray.length(); i++) {
            JSONObject obj = getArray.getJSONObject(i);
            weatherModel.setIcon((String) obj.get("icon"));
            weatherModel.setMain((String) obj.get("main"));
        }

        return "City: " + weatherModel.getName() + "\n" +
                "Temperature: " + weatherModel.getTemp() + " C" + "\n" +
                "Humidity:" + weatherModel.getHumidity() + " %" + "\n" +
                "Main: " + weatherModel.getMain() + "\n" +
                weatherModel.getIcon();
    }
}
