package com.dreidev.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.dreidev.mostafa.pomodoro.Fragments.trelloCards;
import com.dreidev.mostafa.pomodoro.Model.TODOitem;
import com.dreidev.mostafa.pomodoro.Model.TrelloCard;
import com.dreidev.mostafa.pomodoro.Network.Network_Cards;
import com.dreidev.mostafa.pomodoro.RecyclerViewAdapter_Cards;


import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;

import java.util.ArrayList;

public class Presenter_Cards {
    private ArrayList<TrelloCard> items = new ArrayList<TrelloCard>();
    private RecyclerViewAdapter_Cards adapter;

    private trelloCards cardsFrag;
    private Network_Cards network;
    private Context ctx;


    public Presenter_Cards(trelloCards view, Context ctx) {
        cardsFrag = view;
        this.ctx = ctx;

        network =new Network_Cards(this, ctx);
        initAdapter(ctx, items);

    }

    private void initAdapter(Context ctx, ArrayList<TrelloCard> items) {
        adapter=new RecyclerViewAdapter_Cards(this, items);
        cardsFrag.getRecyclerView_cards().setAdapter(adapter);
        cardsFrag.getRecyclerView_cards().setLayoutManager(new LinearLayoutManager(ctx));
    }


    public Network_Cards getNetwork() {
        return network;
    }

    public void setItems(Context applicationContext, ArrayList<TrelloCard> items) {
        this.items = items;
        initAdapter(applicationContext, items);
    }

    public void onItemLongClicked(String name) {
        TODOitem newItem = new TODOitem(name);
        network.addItem(newItem).done(new DoneCallback<String>() {
            @Override
            public void onDone(String result) {
                Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            }
        }).fail(new FailCallback<String>() {
            @Override
            public void onFail(String result) {
                Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            }
        });
    }
}


