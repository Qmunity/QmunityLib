package uk.co.qmunity.lib.helper;

/**
 * Created by Quetzi on 26/02/15.
 */
public class StringHelper {

    public static String millisToString(long ms) {
        int days = (int) (ms / (1000 * 60 * 60 * 24)) % 7;
        int hours = (int) (ms / (1000 * 60 * 60)) % 24;
        int mins = (int) (ms / (1000 * 60)) % 60;
        int secs = (int) (ms / 1000) % 60;
        if (days > 0) {
            return String.format("Current uptime: %s days, %sh %sm %ss", days, hours, mins, secs);
        } else if (hours > 0) {
            return String.format("Current uptime: %sh %sm %ss", hours, mins, secs);
        } else {
            return String.format("Current uptime: %sm %ss", mins, secs);
        }
    }

    public static String bytesToString(long bytes) {

        int unit = 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = ("KMGTPE").charAt(exp-1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }
}
