package com.example.waiter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Locale;
import java.util.TreeMap;

import model.Order;
import model.Seat;
import model.Table;
import persistence.LoadJson;

public class ListOrderDetailActivity extends AppCompatActivity {

    private static Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_order_detail);

        String url = "https://www.sabersolutions.it/ristodroid/get.php?lang=" + Locale.getDefault().getLanguage();
        getJsonResponse(url);

        Intent intent = getIntent();
        String jsonOrder = intent.getStringExtra("JSON_ORDER");

        Gson gson = new Gson();
        order = gson.fromJson(jsonOrder, Order.class);
        order.setConfirmed(false);

        FragmentManager fm = getSupportFragmentManager();
        CheckOrderFragment checkOrderFragment = new CheckOrderFragment();
        fm.beginTransaction().add(R.id.FragmentContainer, checkOrderFragment).commit();

    }


    public static Order getOrder(){
        return order;
    }

    public static void setOrder(Order newOrder){
        order = newOrder;
    }

    /**
     * Procedura per il caricamento del json nel db
     * @param url indirizzo per la richiesta GET
     */
    private void getJsonResponse(String url) {
        JsonObjectRequest jsonRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                JSONObject jsonDb = response.getJSONObject("db");
                TreeMap<String, JSONArray> tables = getDbTablesFromJson(jsonDb);
                LoadJson.insertJsonIntoDb(tables, getApplicationContext());

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {
            Toast toast= Toast.makeText(getApplicationContext(),R.string.SyncFailed,Toast.LENGTH_LONG);
            toast.show();
        });

        Volley.newRequestQueue(this).add(jsonRequest);
    }

    /**
     * Ritorna una mappa chiave (nome tabella) valore (row della rispettiva tabella) del db
     * @param db database
     * @return tables
     * @throws JSONException json exception
     */
    private TreeMap<String, JSONArray> getDbTablesFromJson(JSONObject db) throws JSONException {
        TreeMap<String, JSONArray> tables = new TreeMap<>();
        JSONArray keys = db.names();
        for(int i=0; i< db.length(); i++) {
            if (keys != null) {
                tables.put(keys.getString(i) ,db.getJSONArray(keys.getString(i)));
            }
        }
        return tables;
    }


}