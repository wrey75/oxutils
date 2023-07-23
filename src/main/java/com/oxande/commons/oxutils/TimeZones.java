package com.oxande.commons.oxutils;

import java.util.TimeZone;

public enum TimeZones {
    FRANCE("Europe/Paris"),
    ENGLAND("Europe/London"),
    IRELAND("Europe/Dublin"),
    SPAIN("Europe/Madrid"),
    NEW_YORK("America/New-York"),
    SWITZERLAND("Europe/Zurich");

    private final TimeZone timeZone;

    TimeZones(String value){
        this.timeZone = TimeZone.getTimeZone(value);
    }

    public TimeZone tz() {
        return this.timeZone;
    }
}
