package com.example.mostafa.pomodoro.Presenter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;

import com.example.mostafa.pomodoro.Fragments.TimerFragment;
import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Network.Network_timer;
import com.example.mostafa.pomodoro.R;
import com.example.mostafa.pomodoro.RecyclerViewAdapter_TODOs;
import com.example.mostafa.pomodoro.RecyclerViewAdapter_TODOs_done;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class Presenter_TODOitems {
    private ArrayList<TODOitem> items = new ArrayList<TODOitem>();
    private ArrayList<TODOitem> doneItems = new ArrayList<TODOitem>();

    private TODOitem currentItem;
    private RecyclerViewAdapter_TODOs.ViewHolder currentHolder;

    private RecyclerViewAdapter_TODOs adapter;
    private RecyclerViewAdapter_TODOs_done doneAdapter;

    private TimerFragment timerFragment;
    private Network_timer network;
    private Context ctx;

    Realm realm;

    public Presenter_TODOitems(TimerFragment TimerFragment, Context ctx) {
        this.timerFragment = TimerFragment;
        this.ctx = ctx;
        realm = Realm.getDefaultInstance();
        network = new Network_timer(this, ctx);
        setAdaptersAndUpdateLists();
    }

    private void setAdaptersAndUpdateLists() {
        setAdapterForTODOitems();
        setAdapterForDoneItems();
        updateBotheItemsLists();
        getTimerFragment().getRecyclerView_todoList().setLayoutManager(new LinearLayoutManager(ctx));
    }

    private void setAdapterForDoneItems() {
        doneAdapter = new RecyclerViewAdapter_TODOs_done(this, doneItems);
        timerFragment.getRecyclerView_doneList().setAdapter(doneAdapter);
        timerFragment.getRecyclerView_doneList().setLayoutManager(new LinearLayoutManager(ctx));
        timerFragment.getRecyclerView_doneList().setNestedScrollingEnabled(false);

    }

    private void updateBotheItemsLists() {
        RealmResults<TODOitem> cache = realm.where(TODOitem.class).equalTo("done", false).findAll();
        RealmResults<TODOitem> cacheDone = realm.where(TODOitem.class).equalTo("done", true).findAll();
        Log.i("Network_timer", "loadBoards0: " + cache.size());
        for (int i = 0; i < cache.size(); i++) {
            TODOitem cachedItem = cache.get(i);
            addItem(cachedItem);
        }
        for (int i = 0; i < cacheDone.size(); i++) {
            TODOitem cachedItem = cacheDone.get(i);
            addDoneItem(cachedItem);
        }
        notifyAdapter();
        notifyDoneAdapter();

    }

    private void setAdapterForTODOitems() {
        adapter = new RecyclerViewAdapter_TODOs(this, items);
        timerFragment.getRecyclerView_todoList().setAdapter(adapter);
        timerFragment.getRecyclerView_todoList().setLayoutManager(new LinearLayoutManager(ctx));
        timerFragment.getRecyclerView_todoList().setNestedScrollingEnabled(false);

    }

    public void onAddBtnClicked() {
        String itemEntered = timerFragment.getItemNameField().getText().toString();
        timerFragment.getItemNameField().setText("");
        realm.beginTransaction();
        TODOitem newItem = realm.createObject(TODOitem.class);
        newItem.setDescription(itemEntered);
        realm.commitTransaction();
        addItemToRecyclerView(newItem);
        network.addItem(newItem);
    }

    private void addItemToRecyclerView(TODOitem itemToBeAdded) {
        if (!itemToBeAdded.isDone()) {
            addItem(itemToBeAdded);
            notifyAdapter();
        } else {
            addDoneItem(itemToBeAdded);
            notifyDoneAdapter();

        }
    }

    public void notifyAdapter() {
        adapter.notifyDataSetChanged();
    }

    public void addItem(TODOitem item) {
        items.add(item);
        notifyAdapter();
    }

    public void addDoneItem(TODOitem item) {
        doneItems.add(item);
        notifyDoneAdapter();
    }

    public void notifyDoneAdapter() {
        doneAdapter.notifyDataSetChanged();
    }

    public void removeItem(TODOitem item) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getDescription().equals(item.getDescription()))
                items.remove(i);
        }
        notifyAdapter();
    }

    public ArrayList<TODOitem> getItems() {
        return items;
    }

    public TimerFragment getTimerFragment() {
        return timerFragment;
    }

    public Network_timer getNetwork() {
        return network;
    }

    public TODOitem getCurrentItem() {
        return currentItem;
    }

    public void setCurrentItem(TODOitem currentItem) {
        this.currentItem = currentItem;
    }

    public RecyclerViewAdapter_TODOs.ViewHolder getCurrentHolder() {
        return currentHolder;
    }

    public void setCurrentHolder(RecyclerViewAdapter_TODOs.ViewHolder currentHolder) {
        this.currentHolder = currentHolder;
    }

    public ArrayList<TODOitem> getDoneItems() {
        return doneItems;
    }

    public void onItemClicked(RecyclerViewAdapter_TODOs.ViewHolder holder, TODOitem item) {
        if (currentHolder == null) {
            currentHolder = holder;
            currentItem = item;
            holder.getParent_layout().setBackgroundColor(ctx.getResources().getColor(R.color.pomodoroDarkBlue));
        } else if (currentHolder.equals(holder)) {
            currentHolder = null;
            currentItem = null;
            holder.getParent_layout().setBackgroundColor(ctx.getResources().getColor(R.color.pomodoroBlueTrans));
        } else {
            currentHolder.getParent_layout().setBackgroundColor(ctx.getResources().getColor(R.color.pomodoroBlueTrans));
            currentHolder = holder;
            currentItem = item;
            holder.getParent_layout().setBackgroundColor(ctx.getResources().getColor(R.color.pomodoroDarkBlue));
        }
    }

    public void setItems(Context ctx, ArrayList<TODOitem> items) {
        this.items = items;
    }

    public void setDoneItems(ArrayList<TODOitem> doneItems) {
        this.doneItems = doneItems;
    }
}
