package com.example.shopping_online.Layout;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.example.shopping_online.Adapter.ViewPager_Adapter;
import com.example.shopping_online.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class Menu_Layout extends AppCompatActivity {
    private TabLayout tablayout_menu;
    private ViewPager2 viewpager_menu;

    private ViewPager_Adapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_menu_layout);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        tablayout_menu = findViewById(R.id.tablayout_menu);
        viewpager_menu = findViewById(R.id.viewpager_menu);

        // Thiết lập viewPager cho giao diện từ Adapter trả về
        viewPagerAdapter = new ViewPager_Adapter(getSupportFragmentManager(), getLifecycle());
        viewpager_menu.setAdapter(viewPagerAdapter);


        // Thiết lập tablayout cho màn hình
        setTabLayout();

    }

    private void setTabLayout() {
        // Thiết lập tablayout cho màn hình
        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tablayout_menu, viewpager_menu, (tab, position) -> {
            switch (position){
                case 0:
                    tab.setText("Home");
                    tab.setIcon(R.drawable.home_icon);
                    break;
                case 1:
                    tab.setText("Like");
                    tab.setIcon(R.drawable.heart_icon);
                    break;
                case 2:
                    tab.setText("Contact");
                    tab.setIcon(R.drawable.message_icon);
                    break;
                case 3:
                    tab.setText("Setting");
                    tab.setIcon(R.drawable.setting_25);
                    break;
            }
        });
        tabLayoutMediator.attach();
    }
}