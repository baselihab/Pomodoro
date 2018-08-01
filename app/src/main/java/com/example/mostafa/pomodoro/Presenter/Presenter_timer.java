package com.example.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

import com.example.mostafa.pomodoro.Fragments.timer;
import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Network.Network_timer;
import com.example.mostafa.pomodoro.RecyclerViewAdapter_TODOs;


import java.util.ArrayList;

public class Presenter_timer {
    private ArrayList<TODOitem> items = new ArrayList<TODOitem>();
    private RecyclerViewAdapter_TODOs adapter;

    private timer timer;
    private Network_timer network;
    private Context ctx;

    public Presenter_timer(timer timer, Context ctx) {
        this.timer = timer;
        this.ctx = ctx;
        network = new Network_timer(this, ctx);
        adapter=new RecyclerViewAdapter_TODOs(this, items, ctx);
        timer.getRecyclerView().setAdapter(adapter);
        timer.getRecyclerView().setLayoutManager(new LinearLayoutManager(ctx));
        timer.getRecyclerView().setNestedScrollingEnabled(false);
    }

    public void onAddBtnClicked() {
        String itemEntered = timer.getItemEditText().getText().toString();
        timer.getItemEditText().setText("");
        TODOitem newItem = new TODOitem(itemEntered);
        network.addItem(newItem);
    }

    private void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void addItem(TODOitem item) {
        items.add(item);
        notifyAdapter();
    }

    public void removeItem(TODOitem item) {
        for (int i=0;i<items.size();i++) {
            if(items.get(i).getDescription().equals(item.getDescription()))
                items.remove(i);

        }
        notifyAdapter();
    }

    public ArrayList<TODOitem> getItems() {
        return items;
    }
}