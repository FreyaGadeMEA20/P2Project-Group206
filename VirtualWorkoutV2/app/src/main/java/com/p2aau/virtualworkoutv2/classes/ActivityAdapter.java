package com.p2aau.virtualworkoutv2.classes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.p2aau.virtualworkoutv2.R;

public class ActivityAdapter extends RecyclerView.Adapter<ActivityAdapter.MyViewHolder> {
    String[] names;
    int[] images;
    Context context;

    public ActivityAdapter(Context _ct, String[] _names, int[] _images){
        names = _names;
        context = _ct;
        images = _images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.friend_activity_1, parent, false);
        return new ActivityAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.MyViewHolder holder, int position) {
        holder.name.setText(names[position]);
        holder.avatar.setImageResource(images[position]);
    }

    @Override
    public int getItemCount() {
        return names.length;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        ImageView avatar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
