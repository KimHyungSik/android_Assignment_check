package com.example.assignment_check.interface_;

import android.view.View;

import com.example.assignment_check.Note_Adapter;

public interface OnPersonItemClickListener {
    public void onItemClick(Note_Adapter.Note_Holder holder, View view, int position);

}
