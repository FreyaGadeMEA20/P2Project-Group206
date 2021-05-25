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

    // -- Attributes -- //
    // - Attributes that it gets when called upon - //
    String[] names;
    int[] images;
    Context context;

    // - Constructor - //
    public ActivityAdapter(Context _ct, String[] _names, int[] _images){
        names = _names;
        context = _ct;
        images = _images;
    }

    // -- Methods -- //
    // - Sets up the view, as well as what it replaces the items in the recycler view with - //
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sets it up to the activity that called upon it
        LayoutInflater inflater = LayoutInflater.from(context);
        // Tells the program what the item element is
        View view = inflater.inflate(R.layout.friend_activity_1, parent, false);
        // returns the new recycler view
        return new ActivityAdapter.MyViewHolder(view);
    }

    // - Sets the elements in the view - //
    @Override
    public void onBindViewHolder(@NonNull ActivityAdapter.MyViewHolder holder, int position) {
        holder.name.setText(names[position]);
        holder.avatar.setImageResource(images[position]);
    }

    // - Gets how many items it needs to make - //
    @Override
    public int getItemCount() {
        return names.length;
    }

    // A class that only exists within this scope
    // What each item in the recyclerview consists of
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
