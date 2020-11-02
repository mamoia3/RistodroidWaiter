package com.example.waiter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Currency;
import java.util.List;
import java.util.Locale;

import model.OrderDetail;
import model.Variation;

public class CheckOrderRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<OrderDetail> orderDetailsList;
    private final Context context;
    private manageClickOnButtonCard callManageMethod;

    public interface manageClickOnButtonCard{
        void onDeleteClick(int position);
        void onAddQuantityClick(int position);
        void onRemoveQuantityClick(int position);
    }


    public void setOnItemClickListener (manageClickOnButtonCard listener){
        callManageMethod = listener;
    }


    public CheckOrderRecycleViewAdapter(List<OrderDetail> orderDetailsList, Context context){
        this.orderDetailsList = orderDetailsList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context.getApplicationContext()).inflate(R.layout.layout_row_check_order, parent, false);
        return new CheckOrderRecycleViewAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        final OrderDetail detail = orderDetailsList.get(position);
        String euro = Currency.getInstance(Locale.GERMANY).getSymbol() + " ";
        String quantity = detail.getQuantity() + " " + Utility.convertResourceIdToString(R.string.piece,context);
        String total = euro + Utility.priceToString(detail.getQuantity() * (detail.getDish().getPrice() + OrderDetail.getTotalPriceVariation(detail)));

        if(detail.getVariationPlusList().size()!= 0){
            String addVariationString = Utility.convertResourceIdToString(R.string.plusSymbol,context) + " "
                    + Variation.getVariationsToString(detail.getVariationPlusList());
            ((ViewHolder) holder).textAddVariation.setText(Utility.createIndentedText(addVariationString,0,28));
        }else{
            ((ViewHolder) holder).textAddVariation.setVisibility(View.GONE);
        }

        if(detail.getVariationMinusList().size()!=0){
            String minusVariationString =  Utility.convertResourceIdToString(R.string.minusSymbol,context)+ " "
                    + Variation.getVariationsToString(detail.getVariationMinusList());
            ((ViewHolder) holder).textMinusVariation.setText(Utility.createIndentedText(minusVariationString,0,28));
        }else{
            ((ViewHolder) holder).textMinusVariation.setVisibility(View.GONE);
        }

        ((ViewHolder) holder).textDishTitle.setText(detail.getDish().getName());
        ((ViewHolder) holder).textQuantity.setText(quantity);
        ((ViewHolder) holder).textPrice.setText(total);
        ((ViewHolder) holder).dishImage.setImageBitmap(Utility.byteToBitmap(detail.getDish().getPhoto()));

        ((CheckOrderRecycleViewAdapter.ViewHolder) holder).buttonAddQuantity.setOnClickListener(v -> {
            if(position != RecyclerView.NO_POSITION){
                callManageMethod.onAddQuantityClick(position);
            }
        });


        ((ViewHolder) holder).buttonRemoveQuantity.setOnClickListener(v -> {
            if(position != RecyclerView.NO_POSITION){
                callManageMethod.onRemoveQuantityClick(position);
            }
        });

        ((CheckOrderRecycleViewAdapter.ViewHolder) holder).buttonDeleteDish.setOnClickListener(v -> {

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.deleteDishMessage);
            //builder.setMessage("Piatto aggiunto al carrello");
            builder.setIcon(R.drawable.alert_circle_outline);

            builder.setPositiveButton(R.string.ok, (dialog, which) -> {

                /*Recupero la position della riga su cui ho cliccato ed invoco tramite l'interfacci
                il metodo onDeleteClick implentato nel fragment che ospita la RecycleVicew*/
                if(position != RecyclerView.NO_POSITION){
                    callManageMethod.onDeleteClick(position);
                }

            });

            builder.setNegativeButton(R.string.cancel, (dialog, which) ->{

            });

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });

        if (WaiterActivity.getOrder().isConfirmed()){
            ((ViewHolder) holder).buttonAddQuantity.setBackground(ContextCompat.getDrawable(context, R.drawable.plus_box_grey));
            ((ViewHolder) holder).buttonRemoveQuantity.setBackground(ContextCompat.getDrawable(context, R.drawable.minus_box_outline_grey));
            ((ViewHolder) holder).buttonDeleteDish.setBackground(ContextCompat.getDrawable(context, R.drawable.delete_grey));
            ((ViewHolder) holder).buttonAddQuantity.setClickable(false);
            ((ViewHolder) holder).buttonRemoveQuantity.setClickable(false);
            ((ViewHolder) holder).buttonDeleteDish.setClickable(false);

        }else{
            ((ViewHolder) holder).buttonAddQuantity.setBackground(ContextCompat.getDrawable(context, R.drawable.plus_box));
            ((ViewHolder) holder).buttonRemoveQuantity.setBackground(ContextCompat.getDrawable(context, R.drawable.minus_box_outline));
            ((ViewHolder) holder).buttonDeleteDish.setBackground(ContextCompat.getDrawable(context, R.drawable.delete));
            ((ViewHolder) holder).buttonAddQuantity.setClickable(true);
            ((ViewHolder) holder).buttonRemoveQuantity.setClickable(true);
            ((ViewHolder) holder).buttonDeleteDish.setClickable(true);
        }
    }

    @Override
    public int getItemCount() {
        return orderDetailsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        private final TextView textDishTitle;
        private final TextView textAddVariation;
        private final TextView textMinusVariation;
        private final TextView textQuantity;
        private final TextView textPrice;
        private final ImageView dishImage;
        private final Button buttonAddQuantity;
        private final Button buttonRemoveQuantity;
        private final Button buttonDeleteDish;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textDishTitle = itemView.findViewById(R.id.text_summary_dish_title);
            textAddVariation = itemView.findViewById(R.id.summary_text_add_variations);
            textMinusVariation = itemView.findViewById(R.id.summary_text_minus_variations);
            textQuantity = itemView.findViewById(R.id.summary_text_quantity_summary);
            textPrice = itemView.findViewById(R.id.summary_text_dish_price);
            dishImage = itemView.findViewById(R.id.summary_image_dish);
            buttonAddQuantity = itemView.findViewById(R.id.summary_btn_add_quantity);
            buttonRemoveQuantity = itemView.findViewById(R.id.summary_btn_remove_quantity);
            buttonDeleteDish = itemView.findViewById(R.id.summary_btn_delete_dish);
        }
    }

    public void HideButtonsAfterConfirm() {
        notifyDataSetChanged(); //need to call it for the child views to be re-created with buttons.
    }

}
