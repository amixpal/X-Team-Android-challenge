package com.xteam.sample;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.xteam.sample.R;
import com.xteam.sample.models.Warehouse;

/**
 * Created by Amit on 24-10-2015.
 */

public class WareHouseDetailActivity extends Activity {

    private String INTENT_VALUE = "warehouse";
    private Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Serializable data from parent Activity
        Warehouse warehouseDetail = (Warehouse) getIntent().getSerializableExtra(INTENT_VALUE);

        setContentView(R.layout.activity_ware_house_detail);
        TextView textView = (TextView) findViewById(R.id.warehouse_detail);
        textView.setText(warehouseDetail.getFace());
        TextView textViewPrice = (TextView) findViewById(R.id.price);
        textViewPrice.setText("$" + warehouseDetail.getPrice());

        // Button Text as a Spannable Strings
        submitButton = (Button) findViewById(R.id.button);
        submitButton.setText(Html.fromHtml("BUY NOW!"));
        if (warehouseDetail.getStock() == 1){
            submitButton.setTransformationMethod(null);
            submitButton.setText(Html.fromHtml("BUY NOW!<br/><small>(Only 1 more in stock!)</small>"));
         }

        // Mouse Hover Listner for SubmitButton
        submitButton.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                int what = event.getAction();
                switch(what){
                    case MotionEvent.ACTION_HOVER_ENTER:
                        submitButton.setBackgroundColor(Color.RED);
                        break;
                    case MotionEvent.ACTION_HOVER_MOVE:
                        submitButton.setBackgroundColor(Color.RED);
                        break;
                    case MotionEvent.ACTION_HOVER_EXIT:
                        submitButton.setBackgroundColor(Color.parseColor("#ff3c3f41"));
                        break;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_warehousedetail_activity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }


        return super.onOptionsItemSelected(item);
    }
}
