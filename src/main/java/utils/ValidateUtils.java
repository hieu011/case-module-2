package utils;



import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class ValidateUtils {
    public static final String NAME_REGEX = "^[a-zA-Z]+(([',. -][a-zA-Z ])?[a-zA-Z]*)*$";
    public static final String PASSWORD_REGEX = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]{8,}$";
    public static final String PHONE_REGEX = "^[0][1-9][0-9]{8,9}$";
    public static final String EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,3}$";
    public static final String USERNAME_REGEX = "^[A-Za-z][A-Za-z0-9_]{7,19}$";
    public static final String ADDRESS_REGEX = "^[0-9]{1,5}[ ][a-zA-Z]+(([',. -][a-zA-Z])?[a-zA-Z]*)*$";

    public static boolean isPasswordValid(String password) {
        return Pattern.matches(PASSWORD_REGEX, password);
    }

    public static boolean isNameValid(String name) {
        return Pattern.matches(NAME_REGEX, name);
    }

    public static boolean isPhoneValid(String phone) {
        return Pattern.matches(PHONE_REGEX, phone);
    }

    public static boolean isEmailValid(String email) {
        return Pattern.matches(EMAIL_REGEX, email);
    }

    public static boolean isUsernameValid(String username) {
        return Pattern.matches(USERNAME_REGEX, username);
    }

    public static boolean isAddressValid(String address) {
        return Pattern.matches(ADDRESS_REGEX, address);
    }

    public static boolean isDateValid(String dateStr) {
        DateTimeFormatter dateFormatter = DateTimeFormatter.BASIC_ISO_DATE;
        try {
            LocalDate.parse(convertDate(dateStr), dateFormatter);
        } catch (DateTimeParseException e) {
            return false;
        }
        return true;
    }

    public static String convertDate(String date) {
        //  23/04/2021 -> 20210423
        String[] array = date.split("/");
        String result = "";
        for (int i = array.length - 1; i >= 0; i--) {
            result += array[i];
        }
        return result;
    }

    public static long convertDateToMilli(String date) throws ParseException {
        // 23/04/2021 -> 1619110800000
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        return date1.getTime();
    }

    public static String convertMilliToDate(long millisecond) {
        // 1619110800000 -> 23/04/2021
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return dateFormat.format(millisecond);
    }

    public static String priceWithDecimal (Double price) {
        DecimalFormat formatter = new DecimalFormat("###,###,###");
        return formatter.format(price);
    }

}

