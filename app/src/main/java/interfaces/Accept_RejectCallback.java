package interfaces;

import model.OrderModel;

/**
 * Created by ashish.kumar on 25-07-2018.
 */

public interface Accept_RejectCallback {
 public void    onClick(OrderModel orderId, final int status);
 public void OnOrderDetailsClicked(OrderModel orderModel);
 public void onUpdateStatusClicked(final OrderModel orderModel);
 public void onUpdatePaymentClicked(final OrderModel orderModel);
}
