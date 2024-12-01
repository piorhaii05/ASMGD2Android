package com.example.shopping_online.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.shopping_online.Fragment.MyReviewHaveItem_Fragment;
import com.example.shopping_online.Fragment.MyReviewNone_Fragment;

public class ViewPagerMyReivewAdapter extends FragmentStateAdapter {

    private boolean hasItems;

    public ViewPagerMyReivewAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle, boolean hasItems) {
        super(fragmentManager, lifecycle);
        this.hasItems = hasItems;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        // Trả về fragment dựa trên trạng thái dữ liệu
        if (hasItems) {
            return new MyReviewHaveItem_Fragment();
        } else {
            return new MyReviewNone_Fragment();
        }
    }

    @Override
    public int getItemCount() {
        return 1; // Chỉ hiển thị một fragment tại một thời điểm
    }
}
