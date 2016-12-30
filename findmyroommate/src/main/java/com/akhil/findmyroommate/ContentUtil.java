package com.akhil.findmyroommate;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

/**
 * Created by akhil on 12/30/2016.
 */
public class ContentUtil {
    public static void addContentInTextView(Intent intent, String field, View view) {
        TextView textView = (TextView) view;
        String content = intent.getStringExtra(field);
        textView.setText(content);
    }
}
