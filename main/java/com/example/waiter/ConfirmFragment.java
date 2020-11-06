package com.example.waiter;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.Order;
import model.Seat;
import model.Table;


public class ConfirmFragment extends Fragment {

    private Spinner tableSpinner;
    private Spinner seatSpinner;
    private Table selectedTable;
    private Seat selectedSeat;
    private NumberPicker seatsNumber;
    private FloatingActionButton confirmButton;


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
                ListOrderDetailActivity.getOrder().setSeat(selectedSeat);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedSeat = null;
                ListOrderDetailActivity.getOrder().setSeat(null);
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
                ListOrderDetailActivity.getOrder().setTable(selectedTable);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedTable = null;
                ListOrderDetailActivity.getOrder().setTable(null);
            }
        });
    }

    private void seatNumberPicker(View root) {
        seatsNumber = root.findViewById(R.id.seatsNumber);
        seatsNumber.setMaxValue(50);
        seatsNumber.setMinValue(1);
        seatsNumber.setWrapSelectorWheel(true);
        seatsNumber.setOnValueChangedListener((picker, oldVal, newVal) ->
                ListOrderDetailActivity.getOrder().setSeatNumber(newVal));
    }

    private void confirm(View root) {
        confirmButton = root.findViewById(R.id.confirm_button);
        confirmButton.setOnClickListener(v -> {
            boolean isAllSetted = ListOrderDetailActivity.getOrder().getSeat() != null &&
                    ListOrderDetailActivity.getOrder().getTable() != null &&
                    (Integer) ListOrderDetailActivity.getOrder().getSeatNumber() != null;
            if (isAllSetted) {
                String url ="https://www.sabersolutions.it/ristodroid/insertOrder.php";
                try {
                    insertOrderIntoDb(url);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                ArrayList<String> unselectedFields = checkSelected();
                String fields = Utility.listToStringWithDelimiter(unselectedFields, ", ");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.titleConfirmError);
                builder.setIcon(R.drawable.alert_circle);
                builder.setMessage(getResources().getQuantityString(
                        R.plurals.numberOfFields, unselectedFields.size()) + " " + fields + " " +
                        getResources().getQuantityString(R.plurals.compilated, unselectedFields.size()));

                builder.setPositiveButton(R.string.ok, (dialog, which) -> {

                });

                builder.show();
            }
        });
    }

    private void insertOrderIntoDb(String url) throws JSONException {
        String order = Order.convertToJson(ListOrderDetailActivity.getOrder());
        JSONObject jsonOrder = new JSONObject(order);

        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.POST, url, jsonOrder,
                response -> Toast.makeText(getContext(),R.string.confirm,Toast.LENGTH_LONG).show(),
                error -> Toast.makeText(getContext(),R.string.messageFailedConnectionDB,Toast.LENGTH_LONG).show());

        Volley.newRequestQueue(getContext()).add(jsonRequest);
    }


    private ArrayList<String> checkSelected() {
        ArrayList<String> fields = new ArrayList<>();

        if (ListOrderDetailActivity.getOrder().getSeat() == null) {
            fields.add(getResources().getString(R.string.seatType));
        }
        if (ListOrderDetailActivity.getOrder().getTable() == null) {
            fields.add(getResources().getString(R.string.tableNumber));
        }
        if ((Integer) ListOrderDetailActivity.getOrder().getSeatNumber() == null) {
            fields.add(getResources().getString(R.string.seatNumber));
        }
        return fields;
    }

}