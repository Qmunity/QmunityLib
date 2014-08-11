package com.qmunity.lib.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class ApiHTTP {

    public static String readFromURL(URL url, String lineSeparator) throws IOException {

        InputStreamReader isr = new InputStreamReader(url.openStream());
        BufferedReader br = new BufferedReader(isr);

        String content = "";

        String line;
        while ((line = br.readLine()) != null) {
            content += line + lineSeparator;
        }

        br.close();
        isr.close();

        return content;
    }

    public static String readFromURL(URL url) throws IOException {

        return readFromURL(url, "\r\n");
    }

    public static JsonObject readJSONObjectFromURL(URL url) throws JsonSyntaxException, IOException {

        return (JsonObject) readJSONFromURL(url);
    }

    public static JsonArray readJSONArrayFromURL(URL url) throws JsonSyntaxException, IOException {

        return (JsonArray) readJSONFromURL(url);
    }

    public static JsonElement readJSONFromURL(URL url) throws JsonSyntaxException, IOException {

        return new JsonParser().parse(readFromURL(url, " "));
    }

}
