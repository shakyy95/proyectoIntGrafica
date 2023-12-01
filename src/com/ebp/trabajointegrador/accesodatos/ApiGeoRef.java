package com.ebp.trabajointegrador.accesodatos;

import com.ebp.trabajointegrador.modelo.Municipio;
import com.ebp.trabajointegrador.modelo.Provincia;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;

public class ApiGeoRef {
    private final String baseUrl;

    private final Gson gson;

    public ApiGeoRef() {
        Properties properties = new Properties();
        this.gson = new Gson();
        try {
            properties.load(new FileReader("src/com/ebp/trabajointegrador/resources/config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        baseUrl = properties.getProperty("api.baseurl");
    }

    public Provincia[] obtenerProvincias() {
        String json = callApi(baseUrl + "provincias?orden=nombre&campos=id,nombre");
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray provinciasJsonArray = jsonObject.getAsJsonArray("provincias");
        return gson.fromJson(provinciasJsonArray, Provincia[].class);
    }

    public Municipio[] obtenerMunicipios(String provinciaId) {
        String apiUrl = baseUrl + "municipios?campos=id,nombre&orden=nombre&max=1000&provincia=" + provinciaId;
        String json = callApi(apiUrl);
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        JsonArray municipiosJsonArray = jsonObject.getAsJsonArray("municipios");
        return gson.fromJson(municipiosJsonArray, Municipio[].class);
    }

    private String callApi(String apiUrl) {
        StringBuilder response = new StringBuilder();
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
            } else {
                System.out.println("La solicitud no fue exitosa. Código de respuesta: " + responseCode);
            }

            connection.disconnect();

        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();
    }

}
