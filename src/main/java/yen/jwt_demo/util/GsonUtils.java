package yen.jwt_demo.util;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class GsonUtils {

    private static Gson gson = new Gson();

    public static <T> T fromJson(String json) {
        Type type = getType();
        return gson.fromJson(json, type);
    }

    private static <T> Type getType() {
        return new TypeToken<T>() {
        }.getType();
    }
}
