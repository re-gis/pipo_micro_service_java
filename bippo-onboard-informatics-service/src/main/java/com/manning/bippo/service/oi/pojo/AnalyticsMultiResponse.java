package com.manning.bippo.service.oi.pojo;

import java.util.ArrayList;
import java.util.List;

public class AnalyticsMultiResponse {

    public final List<Quarter> dataPointsByQuarter;
    public final List<Year> dataPointsByYear;

    public AnalyticsMultiResponse() {
        this.dataPointsByQuarter = new ArrayList<>();
        this.dataPointsByYear = new ArrayList<>();
    }

    public void withQuarter(int year, int quarter, int price, int count) {
        this.dataPointsByQuarter.add(new Quarter(year, quarter, price, count));
    }

    public void withYear(int year, int price, int count) {
        this.dataPointsByYear.add(new Year(year, price, count));
    }


    public static class Quarter {

        public int year, quarter, price, count;

        public Quarter(int year, int quarter, int price, int count) {
            this.year = year;
            this.quarter = quarter;
            this.price = price;
            this.count = count;
        }
    }

    public static class Year {

        public int year, price, count;

        public Year(int year, int price, int count) {
            this.year = year;
            this.price = price;
            this.count = count;
        }
    }
}
