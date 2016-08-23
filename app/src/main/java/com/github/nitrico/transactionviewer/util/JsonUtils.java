package com.github.nitrico.transactionviewer.util;

import android.content.Context;
import android.support.annotation.NonNull;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

public class JsonUtils {

    private static final Moshi moshi = new Moshi.Builder().build();

    public static @NonNull <T> List<T> loadListFromAssetsFile(@NonNull Context context,
                                                              @NonNull String filename,
                                                              @NonNull Class<T> clazz) {
        String json = loadAssetsFile(context, filename);
        JsonAdapter<List<T>> adapter = moshi.adapter(Types.newParameterizedType(List.class, clazz));
        try {
            return adapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private static @NonNull String loadAssetsFile(@NonNull Context context, @NonNull String filename) {
        try {
            InputStream is = context.getAssets().open(filename);
            int size = is.available();
            byte[] buffer = new byte[size];
            //noinspection ResultOfMethodCallIgnored
            is.read(buffer);
            is.close();
            return new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return "";
        }
    }

}
