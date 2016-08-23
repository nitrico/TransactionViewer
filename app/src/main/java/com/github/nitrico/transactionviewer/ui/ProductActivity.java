package com.github.nitrico.transactionviewer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MenuItem;
import com.github.nitrico.lastadapter.LastAdapter;
import com.github.nitrico.transactionviewer.BR;
import com.github.nitrico.transactionviewer.R;
import com.github.nitrico.transactionviewer.model.Product;
import com.github.nitrico.transactionviewer.model.Transaction;

public class ProductActivity extends ListActivity {

    public static final String EXTRA_PRODUCT = "com.github.nitrico.transactionviewer.model.Product";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Product product = (Product) intent.getSerializableExtra(EXTRA_PRODUCT);

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getString(R.string.transactions_for, product.sku));
            actionBar.setSubtitle(getString(R.string.total, product.getTotalString()));
        }

        LastAdapter.with(product.transactions, BR.transaction)
                .map(Transaction.class, R.layout.item_transaction)
                .into(list);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
