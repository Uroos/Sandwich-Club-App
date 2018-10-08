package com.udacity.sandwichclub;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.udacity.sandwichclub.model.Sandwich;
import com.udacity.sandwichclub.utils.JsonUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_POSITION = "extra_position";
    private static final int DEFAULT_POSITION = -1;
    private static final String TAG = DetailActivity.class.getSimpleName();
    private ProgressBar progressBar;
    private TextView tvAlsoKnownAs;
    private TextView tvPlaceOfOrigin;
    private TextView tvIngredients;
    private TextView tvDescription;
    private Sandwich sandwich;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        tvAlsoKnownAs = (TextView) findViewById(R.id.also_known_tv);
        tvPlaceOfOrigin = (TextView) findViewById(R.id.origin_tv);
        tvIngredients = (TextView) findViewById(R.id.ingredients_tv);
        tvDescription = (TextView) findViewById(R.id.description_tv);
        ImageView ingredientsIv = findViewById(R.id.image_iv);

        Intent intent = getIntent();
        if (intent == null) {
            closeOnError();
        }

        int position = intent.getIntExtra(EXTRA_POSITION, DEFAULT_POSITION);
        if (position == DEFAULT_POSITION) {
            // EXTRA_POSITION not found in intent
            closeOnError();
            return;
        }

        String[] sandwiches = getResources().getStringArray(R.array.sandwich_details);
        String json = sandwiches[position];
        sandwich = JsonUtils.parseSandwichJson(json, this);
        if (sandwich == null) {
            // Sandwich data unavailable
            closeOnError();
            return;
        }

        populateUI();
        progressBar.setVisibility(View.VISIBLE);
        Picasso.with(this)
                .load(sandwich.getImage())
                .error(R.drawable.ic_error_outline)
                .into(ingredientsIv, new Callback() {
                    @Override
                    public void onSuccess() {
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError() {
                        progressBar.setVisibility(View.GONE);
                    }
                });

        setTitle(sandwich.getMainName());
    }

    private void closeOnError() {
        finish();
        Toast.makeText(this, R.string.detail_error_message, Toast.LENGTH_SHORT).show();
    }

    private void populateUI() {
        tvAlsoKnownAs.setText(sandwich.getAlsoKnownAs().get(0).toString());
        tvPlaceOfOrigin.setText(sandwich.getPlaceOfOrigin());
        List<String> ingredients = sandwich.getIngredients();
        StringBuilder ingredientlist = new StringBuilder();
        for (int i = 0; i < ingredients.size(); i++) {
            ingredientlist.append(ingredients.get(i))
                    .append("\n");
        }
        tvIngredients.setText(ingredientlist.toString());
        tvDescription.setText(sandwich.getDescription());
    }
}
