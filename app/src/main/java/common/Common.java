package common;

/**
 * Created by ashish.kumar on 03-07-2018.
 */

public class Common {
    public static double storeLat=27.382020;
    public static double storeLon=79.592254;

    public static String name="GrocWorldAdminName";
    public static String email="GrocWorldAdminemail";
    public static String mobile="GrocWorldAdminphoneNumber";
    public static String address="GrocWorldAdminAddress";
    public static String userId="GrocWorldAdminownerId";
    public static String isAdmin="GrocWorldAdminisAdmin";
    public static String sendSMSBaseUrl="https://www.ontimesms.in/Rest/AIwebservice/Bulk?";

    /*--------------------------------------------------------------*/
    public static String orderTable="Orders";
    public static String orderstatus="orderstatus";
    public static String objectId="objectId";
    public static String updatedOn="updated";
    public static String createdOn="created";
    /*--------------------------------------------------------------*/
    public static String orderDetails="OrderItems";
    public static String orderId="orderId";
    /*--------------------------------------------------------------*/

    public static int pendingOrderStatus=1;
    public static int acceptedOrderStatus=2;
    public static int outForDelivery=3;
    public static int deliveredOrderStatus=5;
    public static int cancelledOrderStatus=4;

    public static String getSendSMSUrl(String mobileNumber, String message) {
        return sendSMSBaseUrl + "user=Ashish&password=ashish@123&mobilenumber=" + mobileNumber + "&message=" + message +"&mtype=n&sid=GROCWO";
    }

}
