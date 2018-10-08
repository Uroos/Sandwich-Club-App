package com.udacity.sandwichclub.utils;

import android.content.Context;
import android.util.Log;

import com.udacity.sandwichclub.R;
import com.udacity.sandwichclub.model.Sandwich;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtils {

    private static final String TAG = JsonUtils.class.getSimpleName();

    public static Sandwich parseSandwichJson(String json, Context context) {
        String mainName = "";
        List<String> alsoKnownAs = new ArrayList<String>();
        String placeOfOrigin = "";
        String description = "";
        String image = "";
        List<String> ingredients = new ArrayList<String>();

        try {
            JSONObject jsonObject = new JSONObject(json);
            JSONObject name = jsonObject.getJSONObject(context.getString(R.string.json_key_name));
            mainName = name.getString(context.getString(R.string.json_key_main_name));
            JSONArray knownAsArrayname = name.getJSONArray(context.getString(R.string.json_key_known));
            if (knownAsArrayname.length() != 0) {
                for (int i = 0; i < knownAsArrayname.length(); i++) {
                    alsoKnownAs.add(knownAsArrayname.getString(i));
                }
            } else
                alsoKnownAs.add(context.getString(R.string.not_available));

            placeOfOrigin = jsonObject.getString(context.getString(R.string.json_key_place));
            description = jsonObject.getString(context.getString(R.string.json_key_descrip));
            image = jsonObject.getString(context.getString(R.string.json_key_image));
            JSONArray ingredientsArray = jsonObject.getJSONArray(context.getString(R.string.json_key_ingredients));
            if (ingredientsArray.length() != 0) {
                for (int i = 0; i < ingredientsArray.length(); i++) {
                    ingredients.add(ingredientsArray.getString(i));
                }
            } else
                ingredients.add(context.getString(R.string.not_available));
        } catch (JSONException e) {
            Log.e(TAG, context.getString(R.string.json_exception_msg));
        }
        return new Sandwich(
                mainName,
                alsoKnownAs,
                placeOfOrigin,
                description,
                image,
                ingredients
        );

    }
}
