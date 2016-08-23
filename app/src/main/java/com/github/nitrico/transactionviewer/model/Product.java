package com.github.nitrico.transactionviewer.model;

import android.support.annotation.NonNull;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;

public class Product implements Comparable<Product>, Serializable {

    public final String sku;
    public final List<Transaction> transactions;

    public Product(@NonNull String sku, @NonNull List<Transaction> transactions) {
        this.sku = sku;
        this.transactions = transactions;
    }

    public double getTotal() {
        double total = 0;
        for (Transaction t: transactions) {
            total += t.toDefaultCurrency();
        }
        return total;
    }

    public String getTotalString() {
        return DataManager.DEFAULT_CURRENCY_SYMBOL + " " + String.format(Locale.ENGLISH, "%,.2f", getTotal());
    }

    @Override
    public int compareTo(@NonNull Product product) {
        return sku.compareTo(product.sku);
    }

}
