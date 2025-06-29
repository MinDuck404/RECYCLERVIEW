package com.example.recyclerview;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private FoodAdapter foodAdapter;
    private List<Food> foodList;
    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "FoodPrefs";
    private static final String VISITED_ITEMS_KEY = "visited_items";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo SharedPreferences
        sharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        recyclerView = findViewById(R.id.recyclerView);
        foodList = new ArrayList<>();
        foodList.add(new Food("Phở", R.drawable.pho, "Phở bò truyền thống với nước dùng đậm đà.", 45000));
        foodList.add(new Food("Bún chả", R.drawable.buncha, "Bún chả Hà Nội thơm ngon, thịt nướng vàng ươm.", 40000));
        foodList.add(new Food("Bánh mì", R.drawable.banhmi, "Bánh mì kẹp thịt, rau sống, nước sốt.", 20000));
        foodList.add(new Food("Cơm tấm", R.drawable.comtam, "Cơm tấm sườn bì chả, trứng ốp la.", 50000));
        foodList.add(new Food("Gỏi cuốn", R.drawable.goicuon, "Gỏi cuốn tôm thịt, nước chấm đậm đà.", 30000));

        foodAdapter = new FoodAdapter(foodList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(foodAdapter);

        // Thêm nút gợi ý món ăn
        Button btnSuggestion = findViewById(R.id.btnSuggestion);
        btnSuggestion.setOnClickListener(v -> showSuggestions());

        // Swipe to delete
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(RecyclerView recyclerView,
                                  RecyclerView.ViewHolder viewHolder,
                                  RecyclerView.ViewHolder target) {
                return false; // Không xử lý kéo thả
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                foodAdapter.removeItem(viewHolder.getAdapterPosition());
            }
        });
        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Hiển thị thông báo khi quay lại MainActivity
        showWelcomeMessage();
    }

    private void showWelcomeMessage() {
        Set<String> visitedItems = sharedPreferences.getStringSet(VISITED_ITEMS_KEY, new HashSet<>());
        if (!visitedItems.isEmpty()) {
            StringBuilder message = new StringBuilder("Bạn vừa xem: ");
            List<String> visitedList = new ArrayList<>(visitedItems);

            // Lấy 3 món gần nhất (hoặc tất cả nếu ít hơn 3)
            int count = Math.min(3, visitedList.size());
            for (int i = 0; i < count; i++) {
                message.append(visitedList.get(i));
                if (i < count - 1) {
                    message.append(", ");
                }
            }

            Toast.makeText(this, message.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void showSuggestions() {
        Set<String> visitedItems = sharedPreferences.getStringSet(VISITED_ITEMS_KEY, new HashSet<>());

        if (visitedItems.isEmpty()) {
            Toast.makeText(this, "Bạn chưa xem món nào. Hãy khám phá danh sách!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo danh sách các món đã gọi trước đó
        StringBuilder suggestions = new StringBuilder("Các món bạn đã xem:\n\n");
        for (String foodName : visitedItems) {
            suggestions.append("• ").append(foodName).append("\n");
        }
        suggestions.append("\nBạn có muốn gọi lại một trong những món này không?");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("GỢI Ý MÓN ĂN")
                .setMessage(suggestions.toString())
                .setPositiveButton("Đóng", (dialog, which) -> dialog.dismiss())
                .setNegativeButton("Xóa lịch sử", (dialog, which) -> {
                    clearVisitedItems();
                    Toast.makeText(this, "Đã xóa lịch sử xem món", Toast.LENGTH_SHORT).show();
                })
                .show();
    }

    private void clearVisitedItems() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove(VISITED_ITEMS_KEY);
        editor.apply();
    }
}