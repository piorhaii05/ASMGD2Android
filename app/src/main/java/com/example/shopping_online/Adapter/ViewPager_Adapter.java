package com.example.shopping_online.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.shopping_online.Fragment.Setting_Fragment;
import com.example.shopping_online.Fragment.Contact_Fragment;
import com.example.shopping_online.Fragment.Home_Fragment;
import com.example.shopping_online.Fragment.ListLike_Fragment;

// Thiết lập Adapter extends FragmentStateAdapter
public class ViewPager_Adapter extends FragmentStateAdapter {
    public Home_Fragment homeFragment;
    public ListLike_Fragment listLikeFragment;
    public Setting_Fragment settingFragment;
    public Contact_Fragment contactFragment;

    public ViewPager_Adapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
        homeFragment = new Home_Fragment();
        listLikeFragment = new ListLike_Fragment();
        settingFragment = new Setting_Fragment();
        contactFragment = new Contact_Fragment();
    }

    // Trả về số thứ tự của các Fragment
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return homeFragment;
            case 1:
                return listLikeFragment;
            case 2:
                return contactFragment;
            case 3:
                return settingFragment;
            default:
                return homeFragment;
        }
    }

    // Trả về số fragment có trên tablayout
    @Override
    public int getItemCount() {
        return 4;
    }

}
