package com.elsewedyt.pdsapp.services;

import java.time.*;

public class ShiftManager {
    LocalDateTime dateTime = ZonedDateTime.now(ZoneId.of("Africa/Cairo"))
            .plusHours(1)
            .toLocalDateTime();
    //setSHIFT(dateTime);
    public static int SHIFT;
    public static String SHIFT_NAME;
    private static final String SHIFT_1 = "One";
    private static String SHIFT_2 = "Two";
    private static String SHIFT_3 = "Three";


    public static void setSHIFT(LocalDateTime dateTime) {
        LocalTime time = dateTime.toLocalTime();
        DayOfWeek day = dateTime.getDayOfWeek();

        if (day == DayOfWeek.FRIDAY) {
            LocalTime shift1Start = LocalTime.of(7, 0);
            LocalTime shift1End = LocalTime.of(19, 0);

            if (!time.isBefore(shift1Start) && time.isBefore(shift1End)) {
                SHIFT = 1;
                SHIFT_NAME = SHIFT_1;
            } else {
                SHIFT = 2;
                SHIFT_NAME = SHIFT_2;
            }
        } else {
            LocalTime shift1 = LocalTime.of(7, 0);
            LocalTime shift2 = LocalTime.of(15, 0);  // 3 PM
            LocalTime shift3 = LocalTime.of(23, 0);  // 11 PM

            if (!time.isBefore(shift1) && time.isBefore(shift2)) {
                SHIFT = 1;
                SHIFT_NAME = SHIFT_1;
            } else if (!time.isBefore(shift2) && time.isBefore(shift3)) {
                SHIFT = 2;
                SHIFT_NAME = SHIFT_2;
            } else {
                SHIFT = 3;
                SHIFT_NAME = SHIFT_3;
            }
        }
    }

    public static String getShiftByid(int id) {
        if (id == 1) {
            return SHIFT_1;
        } else if (id == 2) {
            return SHIFT_2;
        } else if (id == 3) {
            return SHIFT_3;
        }
        return "غير محددة";
    }

}


