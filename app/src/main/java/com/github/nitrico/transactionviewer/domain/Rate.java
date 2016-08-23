package com.github.nitrico.transactionviewer.domain;

import android.support.annotation.NonNull;

public class Rate {

    public final double rate;
    public final String from;
    public final String to;

    public Rate(double rate, @NonNull String from, @NonNull String to) {
        this.rate = rate;
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return from + ">" + to +": " +rate;
    }

}
