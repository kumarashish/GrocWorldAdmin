package utils;

import android.content.Context;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by jaya.krishna on 14-12-2016.
 */

public class Validation {
    private static Pattern pattern;
    private static Matcher matcher;
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,3})$";
    private static final String PASSWORD_REGEX = "[_A-Za-z0-9]{8,16}";
    Context context;

    public Validation(Context context) {
        this.context = context;
    }


    /***
     * validate password with given password regular expression
     */
    public boolean isPasswordValid(EditText password) {
        boolean matches = false;
        if (isNotNull(password,"password")) {
            String pwd = password.getText().toString().trim();
            if (pwd.length() < 4) {
                Toast.makeText(context, "Password should be at least 4 characters ", Toast.LENGTH_SHORT).show();
            } else {
                return true;
            }
        }

        return matches;
    }

    public boolean isPassword_ConfirmPasswordSame(EditText password, EditText confirmPassword) {
        if (password.getText().toString().equals(confirmPassword.getText().toString())) {
            return true;
        }else{
            Toast.makeText(context, "Password and confirm password should be same. ", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /***
     * validate email string with given email regular expression
     */
    public boolean isEmailIdValid(EditText email) {
        if (isNotNull(email,"Email")) {
            pattern = Pattern.compile(EMAIL_REGEX);
            matcher = pattern.matcher(email.getText().toString());
            if (matcher.matches()) {
                return true;
            } else {
               Toast.makeText(context,"Please enter valid email id", Toast.LENGTH_SHORT).show();

            }
        }
        return false;
    }



    /**
     * check whether string contains value or not
     */
    public boolean isNameValidated(EditText txt) {
        try {

            if ((txt != null) && (txt.getText().toString().trim().length() > 0)) {
                return true;
            } else {
                Toast.makeText(context, "Please enter name", Toast.LENGTH_SHORT).show();
                return false;
            }
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return false;

        }
    }


    /**
     * check whether string contains value or not
     */
    public boolean isNotNull(EditText txt, String name) {
        try {

            if ((txt != null) && (txt.getText().toString().trim().length() > 0)) {
                return true;
            }
            Toast.makeText(context,"Please enter "+name, Toast.LENGTH_SHORT).show();
            return false;
        } catch (Exception ex) {
            ex.fillInStackTrace();
            return false;

        }
    }


    /***
     * validate phone string with given phone regular expression
     */
    public boolean isPhoneNumberValid(EditText phone) {
        boolean matches = false;
        if (isNotNull(phone,"Mobile Number")) {
            String phoneNumber = phone.getText().toString().trim();
            if(phoneNumber.length() == 10){
                matches = true;
            }else{
                Toast.makeText(context, "Phone number should be of 10 characters ", Toast.LENGTH_SHORT).show();
            }
        }
        return matches;
    }
    /***
     * validate phone string with given phone regular expression
     */
    public boolean isAddressValid(EditText address) {
        boolean matches = false;
        if (isNotNull(address,"Address")) {
            String phoneNumber = address.getText().toString().trim();
            if(phoneNumber.length() > 15){
                matches = true;
            }else{
                Toast.makeText(context, "Please enter valid address ", Toast.LENGTH_SHORT).show();
            }
        }
        return matches;
    }

}
