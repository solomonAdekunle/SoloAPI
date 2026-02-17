package config;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class Utils {

    public Utils() {
    }

    /**
     * Adds n days from today
     *
     * @return date in dd/mm/yy format
     */
    public static String getDate(int daysToAdd) {
        DateFormat dateFormat = new SimpleDateFormat(new PropertiesReader().getProperty("simple.date.format"));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysToAdd);
        return dateFormat.format(calendar.getTime());
    }

    /**
     * Adds 7 days from today; if date is weekend adds a further 2 days
     *
     * @return date in dd/mm/yy format
     */
    public static String getNextValidDeliveryDate(int daysToAdd) {
        DateFormat dateFormat = new SimpleDateFormat(new PropertiesReader().getProperty("simple.date.format"));
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DATE, daysToAdd);

        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY
                || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {

            calendar.add(Calendar.DATE, 3);
        }
        return dateFormat.format(calendar.getTime());
    }

    public static boolean checkDayIsInWeekend() {
        Calendar cal = Calendar.getInstance();
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        return dayOfWeek == 1 || dayOfWeek == 7;
    }


}
