package com.example.ks.bookstore.RecylerAdapters;

import android.view.View;


public interface RecyclerItemEventListener {
    void onClick(View view, int position);
    void onLongClick(View view, int position);
}
