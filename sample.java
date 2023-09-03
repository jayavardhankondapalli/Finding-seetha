package com.example.yojitha.game;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;


public class sample extends LinearLayout
{
    View view;
    public sample(Context context)
    {
        super(context);
        LayoutInflater mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mInflater.inflate(R.layout.fragment_all_papers, this, true);
    }




}
