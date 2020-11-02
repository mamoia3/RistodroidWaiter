package com.example.waiter;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.Spinner;

import java.util.List;

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
        List<Seat> seats = Seat.getSeatsFromDb(getContext());
        SeatSpinnerAdapter seatAdapter = new SeatSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, seats);
        seatSpinner.setAdapter(seatAdapter);

        seatSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSeat = seatAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSeat = null;
            }
        });
    }

    private void tableSpinner(View root) {
        tableSpinner = root.findViewById(R.id.tables_spinner);
        List<Table> tables = Table.getTablesFromDb(getContext());
        TablesSpinnerAdapter tablesAdapter = new TablesSpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, tables);
        tableSpinner.setAdapter(tablesAdapter);

        tableSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedTable = tablesAdapter.getItem(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTable = null;
            }
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