package model;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

/**
 * Created by ashish.kumar on 13-07-2018.
 */

public class OrderModel {
    String orderId;
    String userId;
    String totalamount;
 String pincode;
    String orderstatus;
    String name;
    String mobile;
    String address;
    String customermessage="";
    String store_owner_message="";
    java.util.Date date;
   boolean isAccepted=false;
   boolean isRejected=false;
   boolean isPaymentReceived=false;
   int totalItemsCount;

    public OrderModel(String userId, String name, String mobile, String address, String pincode, String totalamount, String customermessage, String orderstatus, String store_owner_message)
    {

        this.userId=userId;
        this.totalamount=totalamount;
        this.pincode=pincode;
        this.orderstatus=orderstatus;
        this.name=name;
        this.mobile=mobile;
        this.address=address;
        this.customermessage=customermessage;
        this.store_owner_message=store_owner_message;

    }
    public OrderModel()
    {}
    public OrderModel(Map map){
        this.orderId=(String)map.get("objectId");
        this.userId=(String)map.get("userId");
        this.totalamount=(String)map.get("totalamount");
        this.pincode=(String)map.get("pincode");
        this.orderstatus=(String)map.get("orderstatus");
        this.name=(String)map.get("name");
        this.mobile=(String)map.get("mobile");
        this.address=(String)map.get("address");
        this.customermessage=(String)map.get("customermessage");
        this.store_owner_message=(String)map.get("store_owner_message");
        this.date=  (java.util.Date)map.get("created");
        this.totalItemsCount=(Integer) map.get("TotalItemsCount");
        this.isPaymentReceived=(Boolean)map.get("isPaymentDone");



    }

    public boolean isPaymentReceived() {
        return isPaymentReceived;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCustomermessage(String customermessage) {
        this.customermessage = customermessage;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }


    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setOrderstatus(String orderstatus) {
        this.orderstatus = orderstatus;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public void setStore_owner_message(String store_owner_message) {
        this.store_owner_message = store_owner_message;
    }

    public void setTotalamount(String totalamount) {
        this.totalamount = totalamount;
    }

    public String getName() {
        return name;
    }

    public String getUserId() {
        return userId;
    }

    public String getAddress() {
        return address;
    }

    public String getMobile() {
        return mobile;
    }

    public String getOrderstatus() {
        return orderstatus;
    }

    public String getCustomermessage() {
        return customermessage;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getPincode() {
        return pincode;
    }

    public String getStore_owner_message() {
        return store_owner_message;
    }

    public String getTotalamount() {
        return totalamount;
    }

    public String[] getDate() {
        DateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm");

        //to convert Date to String, use format method of SimpleDateFormat class.
        String strDate = dateFormat.format(date);
        return strDate.split(" ");
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public int getTotalItemsCount() {
        return totalItemsCount;
    }
}
