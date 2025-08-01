package com.example.recyclerview;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.HashSet;
import java.util.Set;

public class DetailActivity extends AppCompatActivity {

    ImageView detailImageView;
    TextView detailTextView;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FoodPrefs";
    private static final String VISITED_ITEMS_KEY = "visited_items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        detailImageView = findViewById(R.id.detailImageView);
        detailTextView = findViewById(R.id.detailTextView);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        // Nhận dữ liệu từ Intent
        Food food = getIntent().getParcelableExtra("foodItem");

        assert food != null;
        detailImageView.setImageResource(food.getImageResId());
        String detailText = "Tên món ăn: " + food.getName() +
                "\nMô tả: " + food.getDescription() +
                "\nGiá: " + food.getPrice() + " VND";
        detailTextView.setText(detailText);

        // Lưu món ăn đã xem vào SharedPreferences
        saveVisitedItem(food.getName());

        Button orderButton = findViewById(R.id.orderButton);
        orderButton.setOnClickListener(v -> {
            Toast.makeText(this, "Bạn đã gọi món: " + food.getName(), Toast.LENGTH_SHORT).show();
        });

        Button btnCall = findViewById(R.id.btnCall);
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:0123456789"));
                startActivity(callIntent);
            }
        });

        Button btnMap = findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String geoUri = "geo:0,0?q=quán+ăn+ngon";
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                startActivity(mapIntent);
            }
        });

        Button btnWeb = findViewById(R.id.btnWeb);
        btnWeb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://www.google.com";
                Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(webIntent);
            }
        });
    }

    private void saveVisitedItem(String foodName) {
        Set<String> visitedItems = sharedPreferences.getStringSet(VISITED_ITEMS_KEY, new HashSet<>());
        Set<String> newVisitedItems = new HashSet<>(visitedItems);
        newVisitedItems.add(foodName);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(VISITED_ITEMS_KEY, newVisitedItems);
        editor.apply();
    }
}