package com.qualcomm.ftcrobotcontroller;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class AdjustValuesActivity extends Activity {

    List<TextView> labels;

    HashMap<String, EditText> inputs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adjust_values);

        if (RC.adjust == null) {


        }//if

        labels = new ArrayList<TextView>();
        inputs = new HashMap<String, EditText>();

        RelativeLayout layout = (RelativeLayout) findViewById(R.id.relLayout);

        Iterator<Map.Entry<String, Object>> iter = RC.adjust.entrySet().iterator();

        for(int i = 1; iter.hasNext(); i += 2) {
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            RelativeLayout.LayoutParams paramsEdit = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            paramsEdit.setMargins(0, 0, 20, 0);

            Map.Entry<String, Object> entry = iter.next();

            params.addRule(RelativeLayout.BELOW, (i - 1));

            labels.add(new TextView(this));
            labels.get(labels.size() - 1).setLayoutParams(params);
            labels.get(labels.size() - 1).setText(entry.getKey());
            labels.get(labels.size() - 1).setId(i);

            paramsEdit.addRule(RelativeLayout.BELOW, i);
            inputs.put(entry.getKey(), new EditText(this));
            inputs.get(entry.getKey()).setText(entry.getValue().toString());
            inputs.get(entry.getKey()).setLayoutParams(paramsEdit);
            inputs.get(entry.getKey()).setId(i + 1);
            inputs.get(entry.getKey()).setSingleLine(true);

            layout.addView(labels.get(labels.size() - 1), params);
            layout.addView(inputs.get(entry.getKey()), paramsEdit);
        }//while

    }//onCreate

    @Override
    public void onStart() {
        super.onStart();

        //updating edit texts
        if (inputs != null) {
            Iterator<Map.Entry<String, Object>> iter = RC.adjust.entrySet().iterator();

            while(iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();

                inputs.get(entry.getKey()).setText(RC.adjust.get(entry.getKey()).toString());
            }//while
        }//if

    }//onStart

    @Override
    public void onPause() {
        super.onPause();

        //updating edit texts
        if (inputs != null) {
            Iterator<Map.Entry<String, Object>> iter = RC.adjust.entrySet().iterator();

            while(iter.hasNext()) {
                Map.Entry<String, Object> entry = iter.next();

                RC.adjust.put(entry.getKey(), inputs.get(entry.getKey()).getText().toString());
            }//while
        }//if
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_adjust_values, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
