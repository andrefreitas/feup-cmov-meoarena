package org.feup.meoarenacustomer.app;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.*;


public class Helpers {

    static public boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    static public boolean isValidPassword(String pass) {
        if (pass != null && pass.length() > 6) {
            return true;
        }
        return false;
    }

    static public boolean isValidName(String name){
        if (name != null && name.length() > 2) {
            return true;
        }
        return false;
    }


    static public boolean isValidNif(String nif){
        if (nif != null && nif.length() == 9) {
            return true;
        }
        return false;
    }



    static public String getCreditCardType(String creditCardNumber){
        String patternVisa = "^4[0-9]{12}(?:[0-9]{3})?$";
        String patternMaster = "^5[1-5][0-9]{14}$";
        String patternExpress = "^3[47][0-9]{13}$";
        String patternDiners = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$";
        String patternDiscover = "^6(?:011|5[0-9]{2})[0-9]{12}$";
        String patternJCB = "^(?:2131|1800|35\\d{3})\\d{11}$";

        if(Pattern.matches(patternVisa, creditCardNumber)){
            return "visa";
        }

        if(Pattern.matches(patternMaster, creditCardNumber)){
            return "mastercard";
        }

        if(Pattern.matches(patternDiners, creditCardNumber)){
            return "miners";
        }

        if(Pattern.matches(patternDiscover, creditCardNumber)){
            return "discover";
        }

        if(Pattern.matches(patternJCB, creditCardNumber)){
            return "jcb";
        }

        return "invalid";
    }

}
