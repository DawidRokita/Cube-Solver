package com.example.kostkav3.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kostkav3.R;

import java.util.List;

public class MovesAdapter extends RecyclerView.Adapter<MovesAdapter.VH> {

    public interface OnMoveClick {
        void onClick(int position);
    }

    private final List<Integer> drawables;
    private final OnMoveClick listener;

    public MovesAdapter(List<Integer> drawables, OnMoveClick listener) {
        this.drawables = drawables;
        this.listener = listener;
    }

    static class VH extends RecyclerView.ViewHolder {
        ImageView img;
        VH(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.imgMove);
        }
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_move, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        @DrawableRes int res = drawables.get(position);
        holder.img.setImageResource(res);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(position);
        });
    }

    @Override
    public int getItemCount() {
        return drawables.size();
    }
}
