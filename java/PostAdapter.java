package com.example.demoapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {
    private ArrayList<Post> arrayList;
    private Context context;

    private static ClickListener clickListener;

    public PostAdapter(ArrayList<Post> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = arrayList.get(position);
        holder.textView_userId.setText("userId: " + post.getUserId());
        holder.textView_id.setText("id: " + post.getId());
        holder.textView_title.setText("title: " + post.getTitle());
        holder.textView_body.setText("body: " + post.getBody());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView_userId, textView_id, textView_title, textView_body;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_userId = itemView.findViewById(R.id.textView_userId);
            textView_id = itemView.findViewById(R.id.textView_id);
            textView_title = itemView.findViewById(R.id.textView_title);
            textView_body = itemView.findViewById(R.id.textView_body);

            itemView.setOnClickListener(view -> {
                clickListener.onItemClick(getAdapterPosition(), view);
            });
        }
    }

    public interface ClickListener {
        void onItemClick(int position, View view);
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        PostAdapter.clickListener = clickListener;
    }

}