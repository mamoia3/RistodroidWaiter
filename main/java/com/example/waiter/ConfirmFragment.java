package com.example.waiter;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import model.Seat;
import model.Table;


public class ConfirmFragment extends Fragment {

    private Spinner tableSpinner;
    private Spinner seatSpinner;
    private Table selectedTable;
    private Seat selectedSeat;
    private NumberPicker seatsNumber;
    private FloatingActionButton confirmButton;
    private TextView labelTable;
    private TextView labelSeatType;
    private TextView labelSeatNumber;




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
        labelSeatNumber = root.findViewById(R.id.titleSeatsNumber);
        labelSeatType = root.findViewById(R.id.titleSeatsType);
        labelTable = root.findViewById(R.id.titleTable);
        labelTable = root.findViewById(R.id.titleTable);

        tableSpinner(root);
        seatSpinner(root);
        seatNumberPicker(root);
        confirm(root);

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
                WaiterActivity.getOrder().setSeat(selectedSeat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSeat = null;
                WaiterActivity.getOrder().setSeat(null);
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
                WaiterActivity.getOrder().setTable(selectedTable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTable = null;
                WaiterActivity.getOrder().setTable(null);
            }
        });
    }

    private void seatNumberPicker(View root) {
        seatsNumber = root.findViewById(R.id.seatsNumber);
        seatsNumber.setMaxValue(50);
        seatsNumber.setMinValue(1);
        seatsNumber.setWrapSelectorWheel(true);
        seatsNumber.setOnValueChangedListener((picker, oldVal, newVal) -> {
            WaiterActivity.getOrder().setSeatNumber(newVal);
        });
    }

    private void confirm(View root) {
        confirmButton = root.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            boolean isAllSetted = WaiterActivity.getOrder().getSeat() != null &&
                    WaiterActivity.getOrder().getTable() != null &&
                    (Integer) WaiterActivity.getOrder().getSeatNumber() != null;
            if (isAllSetted) {

            } else {
                ArrayList<String> unselectedFields = checkSelected();
                String fields = Utility.listToStringWithDelimiter(unselectedFields, ", ");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.titleConfirmError);
                builder.setMessage(getResources().getQuantityString(
                        R.plurals.numberOfFields, unselectedFields.size()) + " " + fields + " " +
                        getResources().getQuantityString(R.plurals.compilated, unselectedFields.size()));

                builder.setPositiveButton(R.string.ok, (dialog, which) -> {

                });

                builder.show();
            }
        });
    }

    private ArrayList<String> checkSelected() {
        ArrayList<String> fields = new ArrayList<>();

        if (WaiterActivity.getOrder().getSeat() == null) {
            fields.add(getResources().getString(R.string.seatType));
        }
        if (WaiterActivity.getOrder().getTable() == null) {
            fields.add(getResources().getString(R.string.tableNumber));
        }
        if ((Integer) WaiterActivity.getOrder().getSeatNumber() == null) {
            fields.add(getResources().getString(R.string.seatNumber));
        }
        return fields;
    }

}