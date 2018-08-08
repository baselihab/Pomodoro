package com.example.mostafa.pomodoro;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.mostafa.pomodoro.Model.TODOitem;
import com.example.mostafa.pomodoro.Presenter.Presenter_TODOitems;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.mostafa.pomodoro.Model.TODOitem.decreasePomododro;
import static com.example.mostafa.pomodoro.Model.TODOitem.increasePomododro;
import static com.example.mostafa.pomodoro.Model.TODOitem.markDone;

public class RecyclerViewAdapter_TODOs extends RecyclerView.Adapter<RecyclerViewAdapter_TODOs.ViewHolder>{
    private static final String TAG = "RecyclerViewAdapter_TODOs";
    private ArrayList<TODOitem> mItems= new ArrayList<TODOitem>();
    private Presenter_TODOitems presenter;

    public RecyclerViewAdapter_TODOs(Presenter_TODOitems presenter, ArrayList<TODOitem> mItems){
        this.presenter=presenter;
        this.mItems=mItems;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_listitem, parent, false);
        ViewHolder holder= new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        if(!mItems.isEmpty() && holder != null && holder.itemName!= null) {
            holder.itemName.setText(mItems.get(position).getDescription());
            presenter.getTimerFragment().updateBtnText(holder.add_pomodoro_btn, mItems.get(position));
            presenter.getNetwork().getItemsRef().child(mItems.get(position).getDescription()).child("pomodoros");

            holder.parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    presenter.onItemClicked(holder,mItems.get(position));
                }
            });
        }
        else{
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_name)
        TextView itemName;
        @BindView(R.id.parent_layout)
        ConstraintLayout parent_layout;
        @BindView(R.id.addPomodoro)
        Button add_pomodoro_btn;
        @BindView(R.id.removePomodoro)
        Button removePomodoroBtn;
        @BindView(R.id.markDone)
        Button markDone;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            setOnClickListeners();
        }

        public ConstraintLayout getParent_layout() {
            return parent_layout;
        }

        public Button getAdd_pomodoro_btn() {
            return add_pomodoro_btn;
        }

        private void setOnClickListeners() {
            add_pomodoro_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TODOitem itemSelected= presenter.getItems().get(getAdapterPosition());
                    increasePomododro(itemSelected);
                    presenter.getTimerFragment().updateBtnText(add_pomodoro_btn, itemSelected);
                    presenter.getNetwork().updatePomodoros(itemSelected);
                }
            });

            removePomodoroBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TODOitem itemSelected= presenter.getItems().get(getAdapterPosition());
                    decreasePomododro(itemSelected);
                    presenter.getNetwork().updatePomodoros(itemSelected);
                    if(itemSelected.getPomodoros()==0){
                        presenter.getNetwork().removeNode(itemSelected.getDescription());
                    }
                    else{
                        presenter.getTimerFragment().updateBtnText(add_pomodoro_btn, itemSelected);
                        presenter.getNetwork().updatePomodoros(itemSelected);
                    }
                }
            });

            markDone.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    TODOitem itemSelected= presenter.getItems().get(getAdapterPosition());
                    presenter.getNetwork().removeNode(itemSelected.getDescription());
                    markDone(itemSelected);
                    presenter.getNetwork().addNode(itemSelected.getDescription(), itemSelected.isDone(), itemSelected.getPomodoros());
//                    presenter.getNetwork().markDone(itemSelected);

                }
            });
        }
    }
}
