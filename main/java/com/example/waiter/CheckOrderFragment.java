package com.example.waiter;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import model.OrderDetail;


public class CheckOrderFragment extends Fragment {

    private TextView emptySummary;
    private FloatingActionButton checkOrderButton;


    public CheckOrderFragment() {
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
        View root = inflater.inflate(R.layout.fragment_check_order, container, false);

        boolean orderNotEmpty = WaiterActivity.getOrder()!=null && WaiterActivity.getOrder().getOrderDetails().size()>0;

        emptySummary = root.findViewById(R.id.text_empty_summary);
        checkOrderButton = root.findViewById(R.id.check_order_floating_button);


        if(orderNotEmpty){
            emptySummary.setVisibility(View.GONE);


            RecyclerView checkOrderRecyclerView = root.findViewById(R.id.summary_recycler_view);

            List<OrderDetail> details = WaiterActivity.getOrder().getOrderDetails();

            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            checkOrderRecyclerView.setLayoutManager(linearLayoutManager);

            CheckOrderRecycleViewAdapter adapter = new CheckOrderRecycleViewAdapter(details,getContext());
            checkOrderRecyclerView.setAdapter(adapter);

            checkOrderRecyclerView.setHasFixedSize(true); //cardview hanno tutte le stesse dimensioni


            adapter.setOnItemClickListener(new CheckOrderRecycleViewAdapter.manageClickOnButtonCard() {
                @Override
                public void onDeleteClick(int position) {
                    details.remove(position);
                    checkOrderRecyclerView.removeViewAt(position);
                    adapter.notifyItemRemoved(position);
                    adapter.notifyItemRangeChanged(position,details.size());


                    if(details.size() == 0){
                        manageVisibilityOrderEmpty();
                    }
                }

                @Override
                public void onAddQuantityClick(int position) {
                    details.get(position).setQuantity(details.get(position).getQuantity() + 1);
                    adapter.notifyDataSetChanged();
                    Snackbar.make(root,R.string.addDishToOrder,Snackbar.LENGTH_SHORT).show();
                }

                @Override
                public void onRemoveQuantityClick(int position) {
                    int oldQuantity = details.get(position).getQuantity();

                    if((oldQuantity - 1) > 0){
                        details.get(position).setQuantity(oldQuantity - 1);
                        adapter.notifyDataSetChanged();
                    }else{
                        details.remove(position);
                        checkOrderRecyclerView.removeViewAt(position);
                        adapter.notifyItemRemoved(position);
                        adapter.notifyItemRangeChanged(position,details.size());

                        if(details.size() == 0){
                            manageVisibilityOrderEmpty();
                        }
                    }
                    Snackbar.make(root,R.string.removeDishToOrder ,Snackbar.LENGTH_SHORT).show();
                }
            });

        }else {
            emptySummary.setText(R.string.emptySummary);
        }


        checkOrderButton.setOnClickListener(v->{
            Toast.makeText(getContext(), "ciao", Toast.LENGTH_LONG).show();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            fm.beginTransaction().replace(R.id.FragmentContainer, new ConfirmFragment()).commit();
        });

        return root;
    }


    private void manageVisibilityOrderEmpty (){
        emptySummary.setText(R.string.emptySummary);
        emptySummary.setVisibility(View.VISIBLE);
    }
}