package com.lordgasmic.recipe.repository;

import android.util.JsonReader;
import android.util.JsonToken;


import org.json.JSONTokener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * Created by bruce on 2/11/17.
 */

public class RepositoryLoader {

    public RepositoryLoader() {

    }

    protected static RepositoryItem getItem(String id, String itemDescriptor) {
        return null;
    }

    public static void readConfig(InputStream inputStream) {
        try {
            JsonReader reader = new JsonReader(new InputStreamReader(inputStream));

            readJson(reader);
        }
        catch (IOException e) {
            throw new IllegalArgumentException("Unable to read config", e);
        }
    }

    private static void readJson(JsonReader reader) throws IOException {
        JsonToken token = reader.peek();

        switch (token){
            case END_ARRAY:
                reader.endArray(); readJson(reader); break;
            case END_OBJECT:
                reader.endObject(); readJson(reader); break;
            case BEGIN_ARRAY:
                reader.beginArray(); readJson(reader); break;
            case BEGIN_OBJECT:
                reader.beginObject(); readJson(reader); break;
            default:
                break;
        }

        String name = reader.nextName();
        System.out.println(name);
        if ("repository-template".equals(name)) {
            reader.beginArray();
            reader.beginObject();
            readJson(reader);
        }
        else if ("item-descriptor".equals(name)) {

            reader.beginObject();
            readJson(reader);
        }
        /*while (reader.hasNext()) {
            System.out.println(reader.nextName());
        }*/
    }
}
