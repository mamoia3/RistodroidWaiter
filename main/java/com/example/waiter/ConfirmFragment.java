package com.example.waiter;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.Spinner;

import model.Seat;
import model.Table;


public class ConfirmFragment extends Fragment {

    Spinner tableSpinner;
    Spinner seatSpinner;

    Table selectedTable;
    Seat selectedSeat;

    public ConfirmFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_confirm, container, false);

        tableSpinner(root);
        seatSpinner(root);


        return root;
    }

    private void seatSpinner(View root) {
        seatSpinner = root.findViewById(R.id.seats_spinner);
        SeatSpinnerAdapter seatAdapter = new SeatSpinnerAdapter(getContext(), R.id.seats_spinner, Seat.getSeatsFromDb(getContext()));
        seatSpinner.setAdapter(seatAdapter);

        seatSpinner.setOnItemClickListener((parent, view, position, id) -> {
            selectedSeat = seatAdapter.getItem(position);
        });
    }

    private void tableSpinner(View root) {
        tableSpinner = root.findViewById(R.id.tables_spinner);
        TablesSpinnerAdapter tablesAdapter = new TablesSpinnerAdapter(getContext(), R.id.tables_spinner, Table.getTablesFromDb(getContext()));
        tableSpinner.setAdapter(tablesAdapter);

        tableSpinner.setOnItemClickListener((parent, view, position, id) -> {
            selectedTable = tablesAdapter.getItem(position);
        });
    }


    private void numberDickerDialog() {
        NumberPicker numberPicker = new NumberPicker(getContext());
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(1);
        numberPicker.setWrapSelectorWheel(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext()).setView(numberPicker);
        builder.setTitle(R.string.enterQuantityDish);

        builder.setPositiveButton(R.string.ok, (dialog, which) -> {
            // WaiterActivity.getOrder().setQuantity(numberPicker.getValue());

        });

        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {

        });
        builder.show();
    }



}