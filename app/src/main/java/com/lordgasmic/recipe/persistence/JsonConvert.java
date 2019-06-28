package com.lordgasmic.recipe.persistence;

import android.util.JsonReader;
import android.util.JsonToken;

import org.json.JSONStringer;
import org.json.JSONTokener;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JsonConvert {

    public static <T> List<T> deserialize(Class<T> clazz, JsonReader json) throws NoSuchFieldException, InstantiationException, IllegalAccessException, IOException {
        List<T> list = new ArrayList<>();

        json.beginArray();
        T obj = null;
        while(true) {
            switch (json.peek()) {
                case BEGIN_OBJECT:
                    json.beginObject();
                    obj = clazz.newInstance();
                    break;
                case END_OBJECT:
                    json.endObject();
                    list.add(obj);
                    break;
                case END_ARRAY:
                    json.endArray();
                    return list;
                case NAME:
                    String key = json.nextName();
                    Field field = clazz.getDeclaredField(key);
                    field.setAccessible(true);
                    Class<?> type = field.getType();
                    Object value = checkValue(json, obj, type);
                    field.set(obj, value);
                    break;
                default:
                    System.out.println("nope");
                    break;
            }
        }
    }

    private static <T> Object checkValue(JsonReader json, T obj, Class<?> type) throws IOException {
        if (type.getName().equals(String.class.getName())){
            return json.nextString();
        }
        else if (type.getName().equals(Double.class.getName())){
            return json.nextDouble();
        }
        else if (type.getName().equals(Integer.class.getName())){
            return json.nextInt();
        }
        else if (type.getName().equals(Long.class.getName())){
            return json.nextLong();
        }
        else if (type.getName().equals(Boolean.class.getName())){
            return json.nextBoolean();
        }

        return null;
    }
}
