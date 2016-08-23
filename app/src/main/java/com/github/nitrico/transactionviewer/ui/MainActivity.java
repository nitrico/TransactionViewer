package com.github.nitrico.transactionviewer.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.github.nitrico.lastadapter.LastAdapter;
import com.github.nitrico.transactionviewer.App;
import com.github.nitrico.transactionviewer.BR;
import com.github.nitrico.transactionviewer.R;
import com.github.nitrico.transactionviewer.model.Product;

public class MainActivity extends ListActivity implements LastAdapter.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (actionBar != null) {
            actionBar.setTitle(R.string.products);
        }

        LastAdapter.with(App.products, BR.product)
                .map(Product.class, R.layout.item_product)
                .onClickListener(this)
                .into(list);
    }

    @Override
    public void onClick(Object item, View view, int type, int position) {
        Intent intent = new Intent(this, ProductActivity.class);
        intent.putExtra(ProductActivity.EXTRA_PRODUCT, ((Product) item));
        startActivity(intent);
    }

}
