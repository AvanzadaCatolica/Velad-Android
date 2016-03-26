package com.mac.velad.general;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by ruenzuo on 26/03/16.
 */
public class DateFormat extends SimpleDateFormat {

    public DateFormat(String format) {
        super(format, new Locale("es"));
    }

}
