package com.example.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.mostafa.pomodoro.Fragments.trelloCards;
import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Model.TrelloCard;
import com.example.mostafa.pomodoro.Network.Network_Cards;
import com.example.mostafa.pomodoro.RecyclerViewAdapter_Cards;


import org.jdeferred.Deferred;
import org.jdeferred.DoneCallback;
import org.jdeferred.FailCallback;
import org.jdeferred.Promise;
import org.jdeferred.impl.DeferredObject;
import org.json.JSONArray;

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
        adapter=new RecyclerViewAdapter_Cards(this, items, ctx);
        cardsFrag.getRecyclerView().setAdapter(adapter);
        cardsFrag.getRecyclerView().setLayoutManager(new LinearLayoutManager(ctx));
    }


    public Network_Cards getNetwork() {
        return network;
    }

    public ArrayList<TrelloCard> getItems() {
        return items;
    }

    public void setItems(Context applicationContext, ArrayList<TrelloCard> items) {
        this.items = items;
        initAdapter(applicationContext, items);
    }

    public trelloCards getCardsFrag() {
        return cardsFrag;
    }

    public void setCardsFrag(trelloCards boardsFrag) {
        this.cardsFrag = cardsFrag;
    }

    public RecyclerViewAdapter_Cards getAdapter() {
        return adapter;
    }

    public void onItemLongClicked(String name) {
        TODOitem newItem = new TODOitem(name);
        network.addItem(newItem).done(new DoneCallback<String>() {
            @Override
            public void onDone(String result) {
                Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            }
        });
        network.addItem(newItem).fail(new FailCallback<String>() {
            @Override
            public void onFail(String result) {
                Toast.makeText(ctx, result, Toast.LENGTH_SHORT).show();
            }
        });
    }
}


