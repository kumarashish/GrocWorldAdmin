package com.ashish.grocworld_admin;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.DragAndDropPermissions;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.backendless.Backendless;
import com.backendless.IDataStore;
import com.backendless.async.callback.AsyncCallback;
import com.backendless.exceptions.BackendlessFault;
import com.backendless.persistence.DataQueryBuilder;
import com.google.android.gms.maps.model.Dash;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.SimpleTimeZone;

import adapter.OrderListAdapter;
import butterknife.BindView;
import butterknife.ButterKnife;
import common.AppController;
import common.Common;
import common.SlideButton;
import interfaces.Accept_RejectCallback;
import model.OrderModel;
import okhttp3.internal.Util;
import utils.Utils;
import utils.WebApiCall;

public class DashBoard extends Activity implements Accept_RejectCallback,View.OnClickListener {
    @BindView(R.id.orderList)
    ListView orderList;
    @BindView(R.id.progressbar)
    ProgressBar progressBar;
    ArrayList<OrderModel> OrderList=new ArrayList<>();
    String message="";
    OrderModel model=null;

    @BindView(R.id.logout)
    ImageView logout;
    @BindView(R.id.date)
    TextView dateTv;
    AppController controller;
    String date="";
    Calendar calendar;
    private int year, month, day;
    private DatePicker datePicker;
    @BindView(R.id.search)
    EditText search;
    @BindView(R.id.noItem)
    TextView noItems;
    OrderListAdapter adapter=null;
    BottomSheetDialog mBottomSheetDialog;
    common.DetailsCustomTextView heading ;
    LinearLayout multiView;
    LinearLayout  singleView;
    ImageView icon ;
    common.Bold_TextView text;
    ImageView icon2 ;
    common.Bold_TextView  text2;
    ImageView icon3 ;
    common.Bold_TextView text3 ;
    common.Bold_TextView  messageTv ;
    RelativeLayout layout ;
    SlideButton button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        controller=(AppController)getApplicationContext();
        date = getCurrentDate();
        dateTv.setText(date);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        logout.setOnClickListener(this);
        dateTv.setOnClickListener(this);
        getLatestData();
        search.setTypeface(controller.getNormal());

        search.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = search.getText().toString().toLowerCase(Locale.getDefault());
                if(adapter!=null) {
                    adapter.filter(text);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void getLatestData() {
        if (Utils.isNetworkAvailable(DashBoard.this)) {

            getData();
        }
    }
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);

    }
    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            DatePickerDialog dialog = new DatePickerDialog(this, myDateListener, year, month, day);
            dialog.getDatePicker().setMaxDate(new Date().getTime());
            return dialog;
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                }
            };

    private void showDate(int year, int month, int day) {
        this.year=year;
        this.month=month;
        this.day=day;
        date=month+"/"+day+"/"+year;
        dateTv.setText(date);
        getLatestData();
    }
    public void updateOrderStatus(final String orderId, final int status) {
        progressBar.setVisibility(View.VISIBLE);
        orderList.setVisibility(View.GONE);
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                map.put("orderstatus", Integer.toString(status));
                String query = "" + Common.objectId + "= '" + orderId + "'";
                Backendless.Data.of(Common.orderTable).update(query, map, new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        progressBar.setVisibility(View.GONE);
                        if(mBottomSheetDialog!=null)
                        {
                            mBottomSheetDialog.cancel();
                            updateStatus(orderId,status);
                        }
                        orderList.setVisibility(View.VISIBLE);
                        Toast.makeText(DashBoard.this, message, Toast.LENGTH_SHORT).show();
                        callSmsApi();
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
    public void cancelOrderStatus(final String orderId, final int status,final String reason) {
        progressBar.setVisibility(View.VISIBLE);
        orderList.setVisibility(View.GONE);
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                map.put("orderstatus", Integer.toString(status));
                map.put("store_owner_message", reason);
                String query = "" + Common.objectId + "= '" + orderId + "'";
                Backendless.Data.of(Common.orderTable).update(query, map, new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        progressBar.setVisibility(View.GONE);
                        if(mBottomSheetDialog!=null)
                        {
                            mBottomSheetDialog.cancel();

                        }
                        getData();
                        Toast.makeText(DashBoard.this,"You have cancelled the order.", Toast.LENGTH_SHORT).show();
                        callSmsApi();
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


    public void updateStatus(String orderId, int status) {
        for (int i = 0; i < OrderList.size(); i++) {
            OrderModel model = OrderList.get(i);
            if (model.getOrderId().equalsIgnoreCase(orderId)) {
                OrderList.get(i).setOrderstatus(Integer.toString(status));
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }

public String getCurrentDate()
{
    Date c = Calendar.getInstance().getTime();
    System.out.println("Current time => " + c);
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    String formattedDate = df.format(c);
   return formattedDate ;
}
    public void getData() {
        search.setVisibility(View.GONE);
        orderList.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                String query =  ""+ Common.createdOn+">= '"+date+"' ";
                IDataStore<Map> contactStorage = Backendless.Data.of(Common.orderTable);
                DataQueryBuilder queryBuilder = DataQueryBuilder.create();
                queryBuilder.setWhereClause(query);
                queryBuilder.setPageSize(100);
               // queryBuilder.setSortBy( Common.orderstatus+" ASC");
                queryBuilder.setSortBy("created DESC");
                contactStorage.find( queryBuilder, new AsyncCallback<List<Map>>()
                {
                    @Override
                    public void handleResponse( List<Map>categoryList )
                    {

                        OrderList.clear();
                        for(int i=0;i<categoryList.size();i++)
                        {
                            Map orders=categoryList.get(i);
                            OrderList.add(new OrderModel(orders));
                        }
if(OrderList.size()>0) {
    runOnUiThread(new Runnable() {
        @Override
        public void run() {
            progressBar.setVisibility(View.GONE);
            search.setVisibility(View.VISIBLE);
            adapter = new OrderListAdapter(DashBoard.this, OrderList);
            orderList.setAdapter(adapter);
            orderList.setVisibility(View.VISIBLE);
            noItems.setVisibility(View.GONE);
        }
    });
}else{
    noItems.setVisibility(View.VISIBLE);
    progressBar.setVisibility(View.GONE);
    search.setVisibility(View.GONE);
    orderList.setVisibility(View.GONE);

}

                    }

                    @Override
                    public void handleFault( BackendlessFault fault )
                    {
                        Log.e( "MYAPP", "Server reported an error " + fault );
                    }
                } );
            }
        });
        T.start();
    }

    @Override
    public void onClick(final OrderModel ordermodel, final int status) {
        this.model=ordermodel;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(status==Common.acceptedOrderStatus)
                {
                    message="You have Accepted the order";
                    updateOrderStatus(ordermodel.getOrderId(),status);
                }else{
                    message="You have Rejected the order";
                    cancelOrderPopUp(model.getOrderId());
                }


            }
        });

    }

    @Override
    public void OnOrderDetailsClicked(final OrderModel orderModel) {
        if ((orderModel.getOrderstatus().equalsIgnoreCase("5")) || (orderModel.getOrderId().equalsIgnoreCase("4"))) {
            OrderDetails.isItemseditable = false;
        } else {
            OrderDetails.isItemseditable = true;
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Intent in=new Intent(DashBoard.this,OrderDetails.class);
                in.putExtra("orderId",orderModel.getOrderId());
                in.putExtra("total",orderModel.getTotalamount());
                in.putExtra("totalItemCount",orderModel.getTotalItemsCount());
                in.putExtra("customerMobile",orderModel.getMobile());
                startActivityForResult(in,2);
            }
        });

    }

    @Override
    public void onUpdateStatusClicked(final OrderModel orderModel) {
        this.model=orderModel;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                showStatusAlert(model);
            }
        });
    }

    @Override
    public void onUpdatePaymentClicked(final OrderModel orderModel) {
        this.model=orderModel;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                updatePaymentStaus(orderModel.getOrderId(),orderModel.getTotalamount());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if((requestCode==2))
        {search.setText("");
            getLatestData();
        }
    }

    public void callSmsApi() {
        Thread T1 = new Thread(new Runnable() {
            @Override
            public void run() {
                if(message.contains("Accepted"))
                {
                    message="Your order has been accepted by Grocworld, you will get your items delivered to your door step in 3 hours";
                }else if(message.contains("out"))
                {
                    message="Your order is out for delivery.";
                }else  if(message.contains("delivered"))
                {
                    message="Your order has been delivered sucessfully..";
                }

                new WebApiCall(DashBoard.this).getData(Common.getSendSMSUrl(model.getMobile(),  message));

            }
        });
        T1.start();


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.logout:
                showAlert();
                break;
            case R.id.date:
                setDate(view);
                break;

        }
    }

    public void showAlert() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.logout);
        final Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Button no = (Button) dialog.findViewById(R.id.no);
        Button yes = (Button) dialog.findViewById(R.id.yes);
        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });
        yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                controller.setLogout();
                dialog.cancel();
                Intent in = new Intent(DashBoard.this, Login.class);
                startActivity(in);
                finish();
            }
        });
        dialog.show();
    }


    public void showdonePopUp(String message) {
//        layout.setVisibility(View.GONE);
//        multiView.setVisibility(View.GONE);
//        singleView.setVisibility(View.VISIBLE);
//        text3.setText("Done");
//        messageTv.setText(message);
//        icon3.setImageResource(R.drawable.status_complete_icon);
//        heading.setText("");
//        button.setVisibility(View.GONE);
//        text3.setTextColor(getResources().getColor(R.color.green));
    }

    public void showStatusAlert(final OrderModel model) {
        mBottomSheetDialog = new BottomSheetDialog(DashBoard.this);
        View sheetView = getLayoutInflater().inflate(R.layout.set_status_alert, null);
        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomSheetDialog.setContentView(sheetView);
        heading = (common.DetailsCustomTextView) sheetView.findViewById(R.id.heading);
        multiView = (LinearLayout) sheetView.findViewById(R.id.multiView);
        singleView = (LinearLayout) sheetView.findViewById(R.id.singleView);
        icon = (ImageView) sheetView.findViewById(R.id.icon);
        text = (common.Bold_TextView) sheetView.findViewById(R.id.text);
        icon2 = (ImageView) sheetView.findViewById(R.id.icon2);
        text2 = (common.Bold_TextView) sheetView.findViewById(R.id.text2);
        icon3 = (ImageView) sheetView.findViewById(R.id.icon3);
        text3 = (common.Bold_TextView) sheetView.findViewById(R.id.text3);
        messageTv = (common.Bold_TextView) sheetView.findViewById(R.id.message);
        layout = (RelativeLayout) sheetView.findViewById(R.id.lSlideButton);
        button = (SlideButton) sheetView.findViewById(R.id.swipe);
        if ((Integer.parseInt(model.getOrderstatus()) == 5) ||( Integer.parseInt(model.getOrderstatus())) == 4 ) {
            layout.setVisibility(View.GONE);
            multiView.setVisibility(View.GONE);
            singleView.setVisibility(View.VISIBLE);
            text3.setText(getOrderStatus(model.getOrderstatus()));
            messageTv.setText(getMessage(model.getOrderstatus()));
            icon3.setImageResource(getIcon(model.getOrderstatus()));
        } else {
            multiView.setVisibility(View.VISIBLE);
            singleView.setVisibility(View.GONE);
            setIcon(model.getOrderstatus(),icon,icon2,text,text2);
            layout.setVisibility(View.VISIBLE);
        }
        mBottomSheetDialog.setCancelable(false);
        Button close = (Button) sheetView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.cancel();
            }
        });
        button.setSlideButtonListener(new SlideButton.SlideButtonListener() {
            @Override
            public void handleSlide() {
                button.setSaveEnabled(true);
                String nextStatus=getNextOrderStatus(model.getOrderstatus());

                updateOrderStatus(model.getOrderId(),Integer.parseInt(nextStatus));



            }
        });
        FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.rectangle);
        mBottomSheetDialog.show();
    }

    public void setIcon(String orderStatus, ImageView icon, ImageView icon2, TextView text, TextView text2) {
        switch (Integer.parseInt(orderStatus)) {
            case 2:
                icon.setImageResource(R.drawable.status_ready);
                icon2.setImageResource(R.drawable.status_deliver_icon);
                text.setText("Accepted");
                text2.setText("Out For Delivery");
                break;
            case 3:
                icon.setImageResource(R.drawable.status_deliver_icon);
                icon2.setImageResource(R.drawable.status_complete_icon);
                text.setText("Out For Delivery");
                text2.setText("Delivered");
                break;


        }
    }

    public String getOrderStatus(String orderStatus) {
        String status = "";
        switch (Integer.parseInt(orderStatus)) {
            case 2:
                status = "Accepted";
                break;
            case 3:
                status = "Out For Delivery";
                break;
            case 4:
                status = "Cancelled";
                break;
            case 5:
                status = "Delivered";
                break;

        }
        return status;
    }

    public String getMessage(String orderStatus) {
        String status = "";
        switch (Integer.parseInt(orderStatus)) {
            case 2:
                status = "Order accepted";
                break;
            case 3:
                status = "Order is out for delivery";
                break;
            case 4:
                status = "order is cancelled";
                break;
            case 5:
                status = "Order has been delivered";
                break;

        }
        return status;
    }
    public String getNextOrderStatus(String orderStatus) {
        String status = "";
        switch (Integer.parseInt(orderStatus)) {
            case 2:
                message="out for delivery";
                status = "3";
                break;
            case 3:
                message="delivered";
                status = "5";
                break;


        }
        return status;
    }
    public int getIcon(String orderStatus) {
        int icon = -1;
        switch (Integer.parseInt(orderStatus)) {
            case 2:
                icon = R.drawable.status_ready;
                break;
            case 3:
                icon = R.drawable.status_deliver_icon;
                break;
            case 4:
                icon = R.drawable.status_cancel;
                break;
            case 5:
                icon = R.drawable.status_complete_icon;
                break;

        }
        return icon;
    }

    public void cancelOrderPopUp(final String orderId) {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(DashBoard.this);
        View sheetView = getLayoutInflater().inflate(R.layout.cancel_order_popup, null);
        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
        final EditText messag = (EditText) sheetView.findViewById(R.id.address);
        Button submit = (Button) sheetView.findViewById(R.id.submit);
        submit.setTypeface(controller.getNormal());

        messag.setTypeface(controller.getNormal());
        Button close = (Button) sheetView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (messag.getText().length() > 10) {

//                    progress.setVisibility(View.VISIBLE);
//                    cancelOrder(model.getOrderId(),message.getText().toString(),4);
                    mBottomSheetDialog.cancel();
                    orderList.setVisibility(View.GONE);
                    message="Your order has been rejected by  Grocworld Reason : "+messag.getText().toString()+" , Please contact on 8005300408 for more details.";
                    cancelOrderStatus(orderId,4, messag.getText().toString());

                } else {
                    if (messag.getText().length() == 0) {
                        Toast.makeText(DashBoard.this, "Please enter reson for cancellation", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(DashBoard.this, "Please enter minimum 10 characters", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.rectangle);
        mBottomSheetDialog.show();
    }

    public void updatePaymentStaus(final  String orderId,final String amount)
    {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(DashBoard.this);
        View sheetView = getLayoutInflater().inflate(R.layout.update_payment_status, null);
        mBottomSheetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.setCancelable(false);
        final RadioButton paymentpending=(RadioButton) sheetView.findViewById(R.id.paymentpending);
        final RadioButton paymentreceived=(RadioButton) sheetView.findViewById(R.id.paymentreceived);
        final RadioGroup radiogp=(RadioGroup) sheetView.findViewById(R.id.radiogp);
        Button submit = (Button) sheetView.findViewById(R.id.submit);
        submit.setTypeface(controller.getNormal());
        radiogp.check(paymentpending.getId());
        Button close = (Button) sheetView.findViewById(R.id.close);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.cancel();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean paymentReceived = false;
                if (radiogp.getCheckedRadioButtonId() == paymentreceived.getId()) {
                    paymentReceived = true;
                }
                updatePayment(orderId,paymentReceived,amount );
                mBottomSheetDialog.cancel();

            }
        });
        FrameLayout bottomSheet = (FrameLayout) mBottomSheetDialog.getWindow().findViewById(android.support.design.R.id.design_bottom_sheet);
        bottomSheet.setBackgroundResource(R.drawable.rectangle);
        mBottomSheetDialog.show();
    }

    public void updatePayment(final String orderId, final boolean paymentStatus,final String amount)
    {
        progressBar.setVisibility(View.VISIBLE);
        orderList.setVisibility(View.GONE);
        Thread T = new Thread(new Runnable() {
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                map.put("isPaymentDone",paymentStatus);
                String query = "" + Common.objectId + "= '" + orderId + "'";
                Backendless.Data.of(Common.orderTable).update(query, map, new AsyncCallback<Integer>() {
                    @Override
                    public void handleResponse(Integer response) {
                        progressBar.setVisibility(View.GONE);
                        if(mBottomSheetDialog!=null)
                        {
                            mBottomSheetDialog.cancel();

                        }
                        getData();
                        Toast.makeText(DashBoard.this,"You have Updated Payment Status.", Toast.LENGTH_SHORT).show();
                        message="Thank you for making payment ,Grocworld have received payment of Rs ."+amount+" against your order";
                        callSmsApi();
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
}
