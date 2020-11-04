package com.example.waiter;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.TreeMap;

import model.Order;
import model.Seat;
import model.Table;
import persistence.LoadJson;


public class WaiterActivity extends AppCompatActivity {

    public static final String MIME_TEXT_PLAIN = "text/plain";

    private TextView tvIncomingMessage;
    private NfcAdapter nfcAdapter;
    private static Order order = new Order(new Table("1A", null), new Seat(1, "coperto pranzo", 2.0), 5);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receiver);
        String url = "https://www.sabersolutions.it/ristodroid/get.php";

        getJsonResponse(url);

        if (!isNfcSupported()) {
            Toast.makeText(this, R.string.nfc_not_supported, Toast.LENGTH_SHORT).show();
            finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, R.string.nfc_disabled, Toast.LENGTH_SHORT).show();
        }

        initViews();
    }

    // need to check NfcAdapter for nullability. Null means no NFC support on the device
    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }

    private void initViews() {
        FragmentManager fm = getSupportFragmentManager();
        CheckOrderFragment checkOrderFragment = new CheckOrderFragment();
        fm.beginTransaction().add(R.id.FragmentContainer, checkOrderFragment).commit();
        //this.tvIncomingMessage = findViewById(R.id.tv_in_message);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // also reading NFC message from here in case this activity is already started in order
        // not to start another instance of this activity
        super.onNewIntent(intent);
        receiveMessageFromDevice(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // foreground dispatch should be enabled here, as onResume is the guaranteed place where app
        // is in the foreground
        enableForegroundDispatch(this, this.nfcAdapter);
        receiveMessageFromDevice(getIntent());
    }

    @Override
    protected void onPause() {
        super.onPause();
        disableForegroundDispatch(this, this.nfcAdapter);
    }

    private void receiveMessageFromDevice(Intent intent) {
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)) {
            Parcelable[] parcelables = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

            NdefMessage inNdefMessage = (NdefMessage) parcelables[0];
            NdefRecord[] inNdefRecords = inNdefMessage.getRecords();
            NdefRecord ndefRecord_0 = inNdefRecords[0];

            String inMessage = new String(ndefRecord_0.getPayload());
            Gson gson = new Gson();
            order = gson.fromJson(inMessage, Order.class);

//            this.tvIncomingMessage.setText(inMessage);
        }
    }


    // Foreground dispatch holds the highest priority for capturing NFC intents
    // then go activities with these intent filters:
    // 1) ACTION_NDEF_DISCOVERED
    // 2) ACTION_TECH_DISCOVERED
    // 3) ACTION_TAG_DISCOVERED

    // always try to match the one with the highest priority, cause ACTION_TAG_DISCOVERED is the most
    // general case and might be intercepted by some other apps installed on your device as well

    // When several apps can match the same intent Android OS will bring up an app chooser dialog
    // which is undesirable, because user will most likely have to move his device from the tag or another
    // NFC device thus breaking a connection, as it's a short range

    public void enableForegroundDispatch(AppCompatActivity activity, NfcAdapter adapter) {

        // here we are setting up receiving activity for a foreground dispatch
        // thus if activity is already started it will take precedence over any other activity or app
        // with the same intent filters


        final Intent intent = new Intent(activity.getApplicationContext(), activity.getClass());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        //
        final PendingIntent pendingIntent = PendingIntent.getActivity(activity.getApplicationContext(), 0, intent, 0);

        IntentFilter[] filters = new IntentFilter[1];
        String[][] techList = new String[][]{};

        filters[0] = new IntentFilter();
        filters[0].addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
        filters[0].addCategory(Intent.CATEGORY_DEFAULT);
        try {
            filters[0].addDataType(MIME_TEXT_PLAIN);
        } catch (IntentFilter.MalformedMimeTypeException ex) {
            throw new RuntimeException("Check your MIME type");
        }

        adapter.enableForegroundDispatch(activity, pendingIntent, filters, techList);
    }

    public static Order getOrder() {
        return order;
    }

    public static void setOrder(Order order) {
        WaiterActivity.order = order;
    }

    public void disableForegroundDispatch(final AppCompatActivity activity, NfcAdapter adapter) {
        adapter.disableForegroundDispatch(activity);
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