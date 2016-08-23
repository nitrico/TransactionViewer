package com.github.nitrico.transactionviewer;

import android.app.Application;
import com.github.nitrico.transactionviewer.model.Product;
import com.github.nitrico.transactionviewer.model.Rate;
import com.github.nitrico.transactionviewer.model.Transaction;
import com.github.nitrico.transactionviewer.model.ConversionTreeNode;
import com.github.nitrico.transactionviewer.util.JsonUtils;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

public class App extends Application {

    public static final String DATA_DIR = "1";
    public static final String DEFAULT_CURRENCY = "GBP";
    public static final String DEFAULT_CURRENCY_SYMBOL = "£";

    public static final List<Product> products = new LinkedList<>();
    public static final ConversionTreeNode conversionTree = new ConversionTreeNode(null, null);

    private static final List<Rate> addedRates = new LinkedList<>();
    private static List<Transaction> transactions;
    private static List<Rate> rates;

    @Override
    public void onCreate() {
        super.onCreate();
        transactions = JsonUtils.loadListFromAssetsFile(this, DATA_DIR + "/transactions.json", Transaction.class);
        rates = JsonUtils.loadListFromAssetsFile(this, DATA_DIR + "/rates.json", Rate.class);

        createProducts();
        createConversionTree();
        conversionTree.print();
    }

    private void createProducts() {
        Hashtable<String, List<Transaction>> table = new Hashtable<>();
        for (Transaction t: transactions) {
            if (!table.containsKey(t.sku)) {
                table.put(t.sku, new LinkedList<Transaction>());
            }
            table.get(t.sku).add(t);
        }
        for (String key: table.keySet()) {
            products.add(new Product(key, table.get(key)));
        }
        Collections.sort(products);
    }

    private void createConversionTree() {
        for (Rate rate: rates) {
            if (rate.to.equals(DEFAULT_CURRENCY) && !addedRates.contains(rate)) {
                conversionTree.addChild(rate);
                addedRates.add(rate);
            }
        }
        for (ConversionTreeNode n: conversionTree.getChildren()) {
            addChildren(n);
        }
    }

    private void addChildren(ConversionTreeNode node) {
        for (Rate rate: rates) {
            if (!addedRates.contains(rate)
                    && rate.to.equals(node.getItem().from)
                    && !rate.from.equals(App.DEFAULT_CURRENCY)
                    && conversionTree.find(rate.from) == null) {
                node.addChild(rate);
                addedRates.add(rate);
            }
        }
        for (ConversionTreeNode n: node.getChildren()) {
            addChildren(n);
        }
    }

}
