package utilities;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

/**This is the Time Helper abstract class.  It has methods to assist in date and time zone changes.*/
public abstract class TimeHelper {
    /**This is the localToUtc method.  It changes a local date-time to UTC.
     @param localDateTime is the date-time to be converted*/
    public static LocalDateTime localToUtc(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
        zonedDateTime.withZoneSameInstant(ZoneId.of("UTC"));
        localDateTime = zonedDateTime.toLocalDateTime();
        return localDateTime;
    }

    /**This is the utcToLocal method.  It changes a UTC date-time to local.
     @param localDateTime is the date-time to be converted*/
    public static LocalDateTime utcToLocal(LocalDateTime localDateTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("UTC"));
        zonedDateTime.withZoneSameInstant(ZoneId.systemDefault());
        localDateTime = zonedDateTime.toLocalDateTime();
        return localDateTime;
    }

    /**This is the localToEST method.  It changes a local date-time to EST.
     @param localTime is the date-time to be converted*/
    public static LocalDateTime localToEST(LocalDateTime localTime) {
        ZonedDateTime zonedDateTime = ZonedDateTime.of(localTime, ZoneId.systemDefault());
        zonedDateTime = zonedDateTime.withZoneSameInstant(ZoneId.of("US/Eastern"));
        localTime = zonedDateTime.toLocalDateTime();
        return localTime;
    }
}
