package com.wiredave.imoto;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import common.BaseActivity;
import common.PlaceOrder_POJO;

/**
 * Created by Sp on 12-Oct-15.
 */
public class PlaceOrderActivity extends BaseActivity {

    Gallery myHorizontalListView;
    ArrayList<PlaceOrder_POJO> placeOrder_pojos=new ArrayList<PlaceOrder_POJO>();

    public PlaceOrderActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_placeorder);

        myHorizontalListView = (Gallery)findViewById(R.id.horizontallistview);


    }

    private class ListCustomAdapter extends BaseAdapter {
        // private Resources mResources;

        private int fixedWidth = 0;
        private int fixedHeight = 0;

        public ListCustomAdapter() {

            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            float sX = (float) metrics.widthPixels / 480;
            float sY = (float) metrics.heightPixels / 800;
            float sMin = Math.min(sX, sY);

            fixedWidth = (int) (100 * sMin);
            fixedHeight = (int) (100 * sMin);
        }

        public int getCount() {

            return placeOrder_pojos.size();
        }

        public PlaceOrder_POJO getItem(int position) {

            return placeOrder_pojos.get(position);
        }

        public long getItemId(int position) {

            return position;
        }


        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {

            View v;
            if (convertView == null) {
                LayoutInflater li = getLayoutInflater();
                v = li.inflate(
                        R.layout.custom_placeorder, null);
            } else {
                v = convertView;
            }

            final PlaceOrder_POJO Detail = getItem(position);


            return v;
        }

        @SuppressWarnings("unused")
        private Context mContext;
    }
}
