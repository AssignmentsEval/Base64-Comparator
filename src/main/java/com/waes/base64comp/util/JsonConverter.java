package com.waes.base64comp.util;

import com.google.gson.Gson;
import com.waes.base64comp.model.Result;

/**
 * The JsonConverter class provides the function to convert object json.
 */
public class JsonConverter {

    /**
     * Convert Result object to json.
     *
     * @param result the Result object would like to convert.
     * @return json string.
     */
    public static String convertCompResultToJson(Result result) {
        Gson gson = new Gson();
        String json = gson.toJson(result);
        return json;
    }
}
