package adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ashish.grocworld_admin.R;

import java.util.ArrayList;
import java.util.Locale;

import common.Common;
import interfaces.Accept_RejectCallback;
import model.OrderModel;

/**
 * Created by ashish.kumar on 23-07-2018.
 */

public class OrderListAdapter  extends BaseAdapter{
    ArrayList<OrderModel> OrderList;
    Activity activity;
    LayoutInflater inflater;
   Accept_RejectCallback callback;
   ArrayList<OrderModel> filteredList=new ArrayList<>();
public  OrderListAdapter(Activity activity,ArrayList<OrderModel> OrderList)
{
    this.activity=activity;
    this.OrderList=OrderList;
    inflater= activity.getLayoutInflater();
    callback=(Accept_RejectCallback)activity;
    filteredList.addAll(OrderList);
}
    @Override
    public int getCount() {
        return filteredList.size();
    }

    @Override
    public Object getItem(int i) {
        return filteredList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
    ViewHolder holder=null;
    final OrderModel model=filteredList.get(i);
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.order_row,  null, true);
            holder.name=(TextView)view.findViewById(R.id.name);
            holder.address=(TextView)view.findViewById(R.id.address);
            holder.amount=(TextView)view.findViewById(R.id.amount);
            holder.date=(TextView)view.findViewById(R.id.date);
            holder.time=(TextView)view.findViewById(R.id.time);
            holder.accept=(TextView)view.findViewById(R.id.accept);
            holder.reject=(TextView)view.findViewById(R.id.reject);
            holder.message=(TextView)view.findViewById(R.id.message);
            holder.statusvalue=(TextView)view.findViewById(R.id.statusvalue);
            holder.payment=(TextView)view.findViewById(R.id.payment);
            holder.approve_reject=(LinearLayout) view.findViewById(R.id.approve_reject);
            holder.status_column=(LinearLayout)view.findViewById(R.id.status_column);
            holder.status_icon=(ImageView)view.findViewById(R.id.status_icon);
            holder.view1=(View) view.findViewById(R.id.view1);
            holder.acceptview=(View) view.findViewById(R.id.acceptview);
            holder.rejectView=(View)view.findViewById(R.id.rejectview);
            holder.updatePaymentStatus=(View)view.findViewById(R.id.updatePaymentStatus);


        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.updatePaymentStatus.setEnabled(true);
        holder.name.setText(model.getName() +"( "+model.getMobile()+" )");
        holder.address.setText(model.getAddress()+","+model.getPincode());
        holder.amount.setText("Rs "+model.getTotalamount());
        holder.date.setText(model.getDate()[0]);
        holder.time.setText(model.getDate()[1]);
        if(model.isPaymentReceived())
        {   holder.updatePaymentStatus.setEnabled(false);
            holder.payment.setText("Payment Received");
            holder.payment.setTextColor(activity.getResources().getColor(R.color.green));
        }else {
            holder.updatePaymentStatus.setEnabled(true);
            holder.payment.setText("Payment pending");
        }
        final TextView status= holder.statusvalue;
        final ImageView icon=holder.status_icon;
        holder.message.setVisibility(View.GONE);
        if ((!model.getOrderstatus().equalsIgnoreCase("1"))) {
            if(model.getOrderstatus().equalsIgnoreCase("2"))
            {
                icon.setImageResource(R.drawable.status_ready);
                status.setText("Accepted");
                status.setTextColor(activity.getResources().getColor(R.color.yellow));
            }else if(model.getOrderstatus().equalsIgnoreCase("3"))
            {
                icon.setImageResource(R.drawable.status_deliver_icon);
                status.setText("Out For Delivey");
                status.setTextColor(activity.getResources().getColor(R.color.skyblue));
            }
            else if(model.getOrderstatus().equalsIgnoreCase("4"))
            { holder.updatePaymentStatus.setEnabled(false);
                icon.setImageResource(R.drawable.status_cancel);
                status.setText("Cancelled");
                status.setTextColor(activity.getResources().getColor(R.color.red));
                holder.message.setVisibility(View.VISIBLE);

                if(model.getStore_owner_message().length()>0)
                {
                    holder.message.setText("Cancelled by you  reason: " +model.getStore_owner_message());
                }else{
                    holder.message.setText("Cancelled by customer : "+model.getCustomermessage());
                }
                holder.message.setTextColor(activity.getResources().getColor(R.color.red));
            }
            else if(model.getOrderstatus().equalsIgnoreCase("5"))
            {
                icon.setImageResource(R.drawable.status_complete_icon);
                status.setText("Delivered");
                status.setTextColor(activity.getResources().getColor(R.color.green));
                holder.updatePaymentStatus.setEnabled(false);
            }
            holder.approve_reject.setVisibility(View.GONE);
            holder.status_column.setVisibility(View.VISIBLE);
        } else {
            holder.approve_reject.setVisibility(View.VISIBLE);
            holder.status_column.setVisibility(View.GONE);
        }
        holder.view1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               callback.OnOrderDetailsClicked(model);

            }
        });
        holder.updatePaymentStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              callback.onUpdatePaymentClicked(model);

            }
        });
        holder.acceptview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(model.getOrderstatus().equalsIgnoreCase("1")) {
                    model.setOrderstatus(Integer.toString(Common.acceptedOrderStatus));
                    status.setText("Accepted");
                    icon.setImageResource(R.drawable.status_pending_icon);
                    status.setTextColor(activity.getResources().getColor(R.color.green));
                    notifyDataSetChanged();
                    callback.onClick(model, Common.acceptedOrderStatus);
                }else{
                    callback.onUpdateStatusClicked(model);
                }
            }
        });

        holder.rejectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(model.getOrderstatus().equalsIgnoreCase("1")) {
//                    model.setOrderstatus(Integer.toString(Common.cancelledOrderStatus));
//                    status.setText("Cancelled");
//                    icon.setImageResource(R.drawable.status_cancel);
//                    status.setTextColor(activity.getResources().getColor(R.color.red));
//                    notifyDataSetChanged();
                    callback.onClick(model, Common.cancelledOrderStatus);
                }else{
                    callback.onUpdateStatusClicked(model);
                }


            }
        });
        view.setTag(holder);
        return view;
    }

    public class ViewHolder {
        TextView name, address, amount, date,time, accept, reject, statusvalue,message,payment;
        LinearLayout approve_reject, status_column;
        ImageView status_icon;
        View view1,acceptview,rejectView,updatePaymentStatus;
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        filteredList.clear();
        if (charText.length() == 0) {
            filteredList.addAll(OrderList);
        }
        else
        {
            for (OrderModel wp : OrderList)
            {
                if (wp.getName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                   filteredList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }
}
