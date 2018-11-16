package model;

import java.util.Map;

/**
 * Created by ashish.kumar on 13-07-2018.
 */

public class OrderItemModel {
    int quantity;
    String productName;
    String productImage;
    String productPrice;
    String totalAmount;
   boolean isAvailable;
    String orderItemId;

    public  OrderItemModel (Map map)
    {
        quantity=  (Integer) map.get("quantity");
        productName= (String) map.get("productname");
        productPrice= (String) map.get("productprice");
        totalAmount=(String) map.get("totalamount");
        isAvailable= (Boolean) map.get("isavailable");
        orderItemId=(String)map.get("objectId");



    }



    public void setisAvailable(boolean disabled) {
        isAvailable = disabled;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public String getOrderItemId() {
        return orderItemId;
    }

    public String getProductName() {
        return productName;
    }

    public String getProductImage() {
        return productImage;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getTotalAmount() {
        return totalAmount;
    }
}
