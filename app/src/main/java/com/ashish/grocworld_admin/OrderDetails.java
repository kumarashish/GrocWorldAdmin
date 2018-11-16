package com.ashish.grocworld_admin;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.OrderItemAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import interfaces.OnItemEnableDisable;
import model.OrderItemModel;
import utils.Utils;
import utils.WebApiCall;

/**
 * Created by ashish.kumar on 25-07-2018.
 */

public class OrderDetails  extends Activity implements View.OnClickListener, OnItemEnableDisable {
    AppController controller;
    @BindView(R.id.back)
    ImageView back;
    @BindView(R.id.productList)
    ListView orderList;
    @BindView(R.id.heading)
    TextView heading;
    @BindView(R.id.progressbar)
    ProgressBar progress;
    @BindView(R.id.noItem)
    TextView noItem;
    @BindView(R.id.toalView)
    LinearLayout totalView;
    @BindView(R.id.totalCost)
    TextView total;
    ArrayList<OrderItemModel> list = new ArrayList<>();
    String orderId = null;
    String totalValue;
    @BindView(R.id.status_update)
    Button updateStaus;
    int totalItemCount;
    String message="" ;
    public static boolean isItemseditable;
    String customerMobile="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orders);
        controller = (AppController) getApplicationContext();
        ButterKnife.bind(this);
        totalView.setVisibility(View.VISIBLE);
        heading.setText("Order Details");
        orderId = getIntent().getStringExtra("orderId");
        totalValue = getIntent().getStringExtra("total");
        totalItemCount=getIntent().getIntExtra("totalItemCount",0);
        totalItemCount=getIntent().getIntExtra("totalItemCount",0);
        customerMobile=getIntent().getStringExtra("customerMobile");
        total.setText("Rs " + totalValue);
        back.setOnClickListener(this);
        updateStaus.setOnClickListener(this);
        if (Utils.isNetworkAvailable(OrderDetails.this)) {
            getData();
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back:
                onBackPressed();
                break;
            case R.id.status_update:
                updateOrderOnServer();
                break;
        }
    }

    public void getData() {
        progress.setVisibility(View.VISIBLE);
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                String query = "" + Common.orderId + " like '" + orderId + "'";
                IDataStore<Map> contactStorage = Backendless.Data.of(Common.orderDetails);
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(query);
                queryBuilder.setPageSize(100);
                contactStorage.find(queryBuilder, new AsyncCallback<List<Map>>() {
                    @Override
                    public void handleResponse(List<Map> categoryList) {
                        list.clear();
                        for (int i = 0; i < categoryList.size(); i++) {
                            Map category = categoryList.get(i);
                            list.add(new OrderItemModel(category));

                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                progress.setVisibility(View.GONE);
                                if (list.size() > 0) {
                                    orderList.setVisibility(View.VISIBLE);
                                    noItem.setVisibility(View.GONE);
                                    orderList.setAdapter(new OrderItemAdapter(OrderDetails.this, list));
                                } else {
                                    orderList.setVisibility(View.GONE);
                                    noItem.setVisibility(View.VISIBLE);
                                }
                            }
                        });
                        Log.i("MYAPP", "Retrieved " + categoryList.size() + " objects");
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        progress.setVisibility(View.GONE);
                        Log.e("MYAPP", "Server reported an error " + fault);
                    }
                });
            }
        });
        T.start();
    }

    @Override
    public void onClick() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (isItemDisabled()) {
                    updateStaus.setVisibility(View.VISIBLE);
                } else {
                    updateStaus.setVisibility(View.GONE);
                }
            }
        });

    }

    public boolean isItemDisabled() {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isAvailable()==false) {
                return true;
            }
        }
        return false;
    }

    public void updateOrderOnServer() {
        int totalPayable = 0;
        int totalItemCount = 0;
        progress.setVisibility(View.VISIBLE);
        orderList.setVisibility(View.GONE);
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).isAvailable() == false) {
                updateOrderStatus(orderId, list.get(i).getOrderItemId());
                if (message.length() == 0) {
                    message = list.get(i).getProductName();
                } else {
                    message += ", " + list.get(i).getProductName();
                }
            } else {
                totalPayable += Integer.parseInt(list.get(i).getTotalAmount());
                totalItemCount += list.get(i).getQuantity();
            }
        }
        updateOrder(orderId, totalItemCount, totalPayable);
    }
    public void updateOrder(final String orderId,final int Quantity,final int price) {
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                map.put("TotalItemsCount", Quantity);
                map.put("totalamount", Integer.toString( price));
                String query = "" + Common.objectId + "= '" + orderId + "'";
                Backendless.Data.of(Common.orderTable).update(query, map, new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                    progress.setVisibility(View.GONE);
                        updateStaus.setVisibility(View.GONE);
                        orderList.setVisibility(View.VISIBLE);
                        total.setText("Rs " + price);
                        Toast.makeText(OrderDetails.this,"Item Status updated on server sucessfully,same has been notified to customer.",Toast.LENGTH_SHORT).show();
                        callSmsApi(customerMobile);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e("MYAPP", "Server reported an error " + fault);
                    }
                });
            }
        });
        T.start();
    }
    public void updateOrderStatus(final String orderId, final String ordertemId) {
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                map.put("isavailable", false);
                String query = "" + Common.orderId + "= '" + orderId + "' and " + Common.objectId + "= '" + ordertemId + "'";
                Backendless.Data.of(Common.orderDetails).update(query, map, new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        Log.e("MYAPP", "Server reported an error " + response);
                    }

                    @Override
                    public void handleFault(BackendlessFault fault) {
                        Log.e("MYAPP", "Server reported an error " + fault);
                    }
                });
            }
        });
        T.start();
    }

    public void callSmsApi(final String mobileNumber) {
        Thread T1 = new Thread(new Runnable() {
            @Override
            public void run() {
                new WebApiCall(OrderDetails.this).getData(Common.getSendSMSUrl(mobileNumber, message +" is not available in grocworld store, rest items you will get in 3 hours."));

            }
        });
        T1.start();


    }

}