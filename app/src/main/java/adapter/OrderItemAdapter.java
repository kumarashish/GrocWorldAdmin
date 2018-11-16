package adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ashish.grocworld_admin.OrderDetails;
import com.ashish.grocworld_admin.R;

import java.util.ArrayList;

import common.AppController;
import interfaces.OnItemEnableDisable;
import model.OrderItemModel;

/**
 * Created by ashish.kumar on 18-07-2018.
 */

public class OrderItemAdapter extends BaseAdapter

    {
        Activity activity;
        ArrayList<OrderItemModel> list;
        LayoutInflater inflater;
        AppController controller;
        OnItemEnableDisable callBack;
    public OrderItemAdapter(Activity activity, ArrayList<OrderItemModel> list)
        {
            this.activity=activity;
            this.list=list;
            callBack=(OnItemEnableDisable)activity;
            inflater= (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        @Override
        public int getCount() {
        return list.size();
    }

        @Override
        public Object getItem(int i) {
        return list.get(i);
    }

        @Override
        public long getItemId(int i) {
        return  i;
    }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder=null;
        final OrderItemModel model=list.get(i);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.order_item_row, null);
            holder.productImage = (ImageView) view.findViewById(R.id.productImage);
            holder.productName = (TextView) view.findViewById(R.id.productName);
            holder.quantity = (TextView) view.findViewById(R.id.quantity);
            holder.mrp = (TextView) view.findViewById(R.id.mrp);
            holder.offerPrice = (TextView) view.findViewById(R.id.offerPrice);
            holder.card=(CardView)view.findViewById(R.id.card_view);
            holder.view=(View)view.findViewById(R.id.view);
            holder.discount = (TextView) view.findViewById(R.id.discount);


        } else {
            holder = (ViewHolder) view.getTag();
        }holder.discount.setVisibility(View.GONE);
        holder.productName.setText(model.getProductName().toUpperCase());
        holder.quantity.setText("Quantity: "+model.getQuantity() );
        holder.mrp.setText("Price : Rs "+model.getProductPrice());
            holder.mrp.setTextColor(activity.getResources().getColor(R.color.black_color));
//        SpannableString spannable = new SpannableString( holder.mrp.getText().toString());
//        spannable.setSpan(new StrikethroughSpan(), 4,  holder.mrp.getText().length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
//        holder.mrp.setText(spannable);
            if (model.isAvailable()== false) {
                holder.offerPrice.setText("Not available");
                holder.card.setCardBackgroundColor(activity.getResources().getColor(R.color.grey));
            } else {
                holder.offerPrice.setText("Amount : Rs " + model.getTotalAmount());
                holder.card.setCardBackgroundColor(activity.getResources().getColor(R.color.white));
            }
        final CardView card=holder.card;
           final TextView offPrice=holder.offerPrice;
            holder.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(OrderDetails.isItemseditable) {
                        if (model.isAvailable() == false) {
                            model.setisAvailable(true);
                            card.setCardBackgroundColor(activity.getResources().getColor(R.color.white));
                            offPrice.setText("Amount : Rs " + model.getTotalAmount());
                            Toast.makeText(activity, "OrderItem " + model.getProductName() + " marked as available", Toast.LENGTH_SHORT).show();
                        } else {
                            model.setisAvailable(false);
                            card.setCardBackgroundColor(activity.getResources().getColor(R.color.grey));
                            Toast.makeText(activity, "OrderItem  " + model.getProductName() + " marked as Not Available", Toast.LENGTH_SHORT).show();
                            offPrice.setText("Not Available");
                        }
                        callBack.onClick();
                    }else{
                        Toast.makeText(activity,"This order is already completed",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        view.setTag(holder);
        return view;
    }
        public class ViewHolder {
            common.Bold_TextView quantityValue;
            ImageView productImage;
            TextView productName,  mrp,  offerPrice, quantity,discount;
            CardView card;
            View view;
        }
 public int getTotal(float price, int quantity) {
        return Math.round(price * quantity);
    }
}

