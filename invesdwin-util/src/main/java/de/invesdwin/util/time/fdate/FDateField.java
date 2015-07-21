package de.invesdwin.util.time.fdate;

import java.time.temporal.ChronoField;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.concurrent.Immutable;

import org.joda.time.DateTimeFieldType;

import de.invesdwin.util.error.UnknownArgumentException;

@Immutable
public enum FDateField {

    Year {
        @Override
        public int calendarValue() {
            return Calendar.YEAR;
        }

        @Override
        public ChronoField javaTimeValue() {
            return ChronoField.YEAR;
        }

        @Override
        public DateTimeFieldType jodaTimeValue() {
            return DateTimeFieldType.year();
        }
    },
    Month {
        @Override
        public int calendarValue() {
            return Calendar.MONTH;
        }

        @Override
        public ChronoField javaTimeValue() {
            return ChronoField.MONTH_OF_YEAR;
        }

        @Override
        public DateTimeFieldType jodaTimeValue() {
            return DateTimeFieldType.monthOfYear();
        }
    },
    /**
     * Also known as DAY_OF_MONTH.
     */
    Day {
        @Override
        public int calendarValue() {
            return Calendar.DAY_OF_MONTH;
        }

        @Override
        public ChronoField javaTimeValue() {
            return ChronoField.DAY_OF_MONTH;
        }

        @Override
        public DateTimeFieldType jodaTimeValue() {
            return DateTimeFieldType.dayOfMonth();
        }
    },
    Weekday {
        @Override
        public int calendarValue() {
            return Calendar.DAY_OF_WEEK;
        }

        @Override
        public ChronoField javaTimeValue() {
            return ChronoField.DAY_OF_WEEK;
        }

        @Override
        public DateTimeFieldType jodaTimeValue() {
            return DateTimeFieldType.dayOfWeek();
        }
    },
    Hour {
        @Override
        public int calendarValue() {
            return Calendar.HOUR_OF_DAY;
        }

        @Override
        public ChronoField javaTimeValue() {
            return ChronoField.HOUR_OF_DAY;
        }

        @Override
        public DateTimeFieldType jodaTimeValue() {
            return DateTimeFieldType.hourOfDay();
        }
    },
    Minute {
        @Override
        public int calendarValue() {
            return Calendar.MINUTE;
        }

        @Override
        public ChronoField javaTimeValue() {
            return ChronoField.MINUTE_OF_HOUR;
        }

        @Override
        public DateTimeFieldType jodaTimeValue() {
            return DateTimeFieldType.minuteOfHour();
        }
    },
    Second {
        @Override
        public int calendarValue() {
            return Calendar.SECOND;
        }

        @Override
        public ChronoField javaTimeValue() {
            return ChronoField.SECOND_OF_MINUTE;
        }

        @Override
        public DateTimeFieldType jodaTimeValue() {
            return DateTimeFieldType.secondOfMinute();
        }
    },
    Millisecond {
        @Override
        public int calendarValue() {
            return Calendar.MILLISECOND;
        }

        @Override
        public ChronoField javaTimeValue() {
            return ChronoField.MILLI_OF_SECOND;
        }

        @Override
        public DateTimeFieldType jodaTimeValue() {
            return DateTimeFieldType.millisOfSecond();
        }
    };

    private static final Map<Integer, FDateField> CALENDAR_LOOKUP = new HashMap<Integer, FDateField>();
    private static final Map<ChronoField, FDateField> JAVA_TIME_LOOKUP = new HashMap<ChronoField, FDateField>();
    private static final Map<DateTimeFieldType, FDateField> JODA_TIME_LOOKUP = new HashMap<DateTimeFieldType, FDateField>();

    static {
        for (final FDateField f : FDateField.values()) {
            CALENDAR_LOOKUP.put(f.calendarValue(), f);
            JAVA_TIME_LOOKUP.put(f.javaTimeValue(), f);
            JODA_TIME_LOOKUP.put(f.jodaTimeValue(), f);
        }
    }

    public abstract int calendarValue();

    public abstract ChronoField javaTimeValue();

    public abstract DateTimeFieldType jodaTimeValue();

    public static FDateField valueOfCalendar(final int field) {
        return lookup(CALENDAR_LOOKUP, field);
    }

    public static FDateField valueOfJavaTime(final ChronoField field) {
        return lookup(JAVA_TIME_LOOKUP, field);
    }

    public static FDateField valueOfJodaTime(final DateTimeFieldType field) {
        return lookup(JODA_TIME_LOOKUP, field);
    }

    @SuppressWarnings("unchecked")
    private static <T> FDateField lookup(final Map<T, FDateField> map, final T field) {
        if (field == null) {
            throw new NullPointerException("parameter field should not be null");
        }
        final FDateField value = map.get(field);
        if (value == null) {
            throw UnknownArgumentException.newInstance((Class<T>) field.getClass(), field);
        } else {
            return value;
        }
    }

}
