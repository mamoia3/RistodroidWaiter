package com.example.waiter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import java.util.List;

import model.Seat;
import model.Table;

public class SeatSpinnerAdapter extends ArrayAdapter<Seat> {
    private Context context;
    private List<Seat> seats;

    public SeatSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Seat> seats) {
        super(context, resource, seats);
        this.context = context;
        this.seats = seats;
    }

    @Override
    public int getCount(){
        return seats.size();
    }

    @Override
    public Seat getItem(int position){
        return seats.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(ContextCompat.getColor(context, R.color.secondary_text));
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(seats.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(seats.get(position).getName());

        return label;
    }
}
