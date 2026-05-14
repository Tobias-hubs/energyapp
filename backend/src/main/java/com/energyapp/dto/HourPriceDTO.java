package com.energyapp.dto;


public class HourPriceDTO {
    private final String timeStart;
    private final String timeEnd;
    private final double sek;

    public HourPriceDTO(String timeStart, String timeEnd, double sek) {
        this.timeStart = timeStart;
        this.timeEnd = timeEnd;
        this.sek = sek;
    }

    public String getTimeStart() { return timeStart; }
    public String getTimeEnd() { return timeEnd; }
    public double getSek() { return sek; }
}
