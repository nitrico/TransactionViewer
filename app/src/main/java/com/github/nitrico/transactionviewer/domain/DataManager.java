package com.github.nitrico.transactionviewer.domain;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import com.github.nitrico.transactionviewer.util.JsonUtils;
import java.util.Collections;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

/**
 * Singleton class used to manage the data structures needed by the app
 */
public class DataManager {

    public interface Callback {
        void onDataReady();
    }

    public static final String DATA_DIR = "1";
    public static final String DEFAULT_CURRENCY = "GBP";
    public static final String DEFAULT_CURRENCY_SYMBOL = "£";

    private static DataManager instance = null;

    private final ConversionTreeNode conversionTree = new ConversionTreeNode(null, null);
    private final List<Product> products = new LinkedList<>();
    private final List<Rate> addedRates = new LinkedList<>();
    private List<Transaction> transactions;
    private List<Rate> rates;
    private boolean initialized = false;

    private DataManager() { }

    public static synchronized DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void initialize(@NonNull Context context, @Nullable Callback callback) {
        if (!initialized) {
            transactions = JsonUtils.loadListFromAssetsFile(context, DATA_DIR + "/transactions.json", Transaction.class);
            rates = JsonUtils.loadListFromAssetsFile(context, DATA_DIR + "/rates.json", Rate.class);
            createProducts();
            createConversionTree();
            conversionTree.print();
            initialized = true;
        }
        if (callback != null) {
            callback.onDataReady();
        }
    }

    public ConversionTreeNode getConversionTree() {
        throwExceptionIfNotInitialized();
        return conversionTree;
    }

    public List<Product> getProducts() {
        throwExceptionIfNotInitialized();
        return products;
    }

    private void throwExceptionIfNotInitialized() {
        if (!initialized) {
            throw new IllegalArgumentException("You must initialize DataManager before accessing its data.");
        }
    }

    /**
     * Creates a list of products sorted alphabetically by its 'sku'
     */
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

    /**
     * Adds the first children of the tree, those rates which convert to 'DEFAULT_CURRENCY'
     */
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

    /**
     * Adds the rest of the needed children of the tree
     */
    private void addChildren(ConversionTreeNode node) {
        for (Rate rate: rates) {
            if (!addedRates.contains(rate)
                    && rate.to.equals(node.getItem().from)
                    && !rate.from.equals(DEFAULT_CURRENCY)
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
