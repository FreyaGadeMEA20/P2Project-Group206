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

public class FriendListAdapter extends RecyclerView.Adapter<FriendListAdapter.MyViewHolder> {

    // -- Attributes -- //
    // - Attributes that it gets when called upon - //
    String[] names;
    int[] images;
    Context context;

    // - Constructor - //
    public FriendListAdapter(Context _ct, String[] _names, int[] _images){
        context = _ct;
        names = _names;
        images = _images;
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sets it up to the activity that called upon it
        LayoutInflater inflater = LayoutInflater.from(context);
        // Tells the program what the item element is
        View view = inflater.inflate(R.layout.friend_layout, parent, false);
        // returns the new recycler view
        return new MyViewHolder(view);
    }

    // - Sets the elements in the view - //
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(names[position]);
        holder.avatar.setImageResource(images[position]);
    }

    // - Gets how many items it needs to make - //
    @Override
    public int getItemCount() {
        return images.length;
    }

    // A class that only exists within this scope
    // What each item in the recyclerview consists of
    public class MyViewHolder extends RecyclerView.ViewHolder{

        TextView name;
        ImageView avatar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            avatar = itemView.findViewById(R.id.avatar);
        }
    }
}
