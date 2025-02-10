package lk.payhere.androidsdk.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

public class ListItemDecore extends RecyclerView.ItemDecoration{


    private int space;

    public ListItemDecore(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.left = 0;
        }
    }
}
