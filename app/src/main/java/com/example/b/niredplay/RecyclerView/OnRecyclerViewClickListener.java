package com.example.b.niredplay.RecyclerView;

import android.view.View;

import java.io.IOException;

public interface OnRecyclerViewClickListener {
    void onItemClickListener(View view) throws IOException;
    void onItemLongClickListener(View view);
}
