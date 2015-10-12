package com.wiredave.imoto;

import android.app.Activity;
import android.os.Bundle;

import common.BaseActivity;

/**
 * Created by Sp on 12-Oct-15.
 */
public class ConfirmOrderActivity extends BaseActivity {
    public ConfirmOrderActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmorder);
    }
}
