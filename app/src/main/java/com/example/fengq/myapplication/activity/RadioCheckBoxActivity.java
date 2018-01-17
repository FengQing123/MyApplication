package com.example.fengq.myapplication.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.fengq.myapplication.R;
import com.example.fengq.myapplication.tools.UIHelper;

/**
 * Created by fengq on 2017/6/15.
 */

public class RadioCheckBoxActivity extends Activity implements CompoundButton.OnCheckedChangeListener {

    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private CheckBox checkBox5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.radio_group);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton button = (RadioButton) group.findViewById(checkedId);
                UIHelper.ToastMessage(RadioCheckBoxActivity.this, button.getText().toString());
            }
        });

        checkBox1 = (CheckBox) findViewById(R.id.checkbox_1);
        checkBox1.setOnCheckedChangeListener(this);
        checkBox2 = (CheckBox) findViewById(R.id.checkbox_2);
        checkBox2.setOnCheckedChangeListener(this);
        checkBox3 = (CheckBox) findViewById(R.id.checkbox_3);
        checkBox3.setOnCheckedChangeListener(this);
        checkBox4 = (CheckBox) findViewById(R.id.checkbox_4);
        checkBox4.setOnCheckedChangeListener(this);
        checkBox5 = (CheckBox) findViewById(R.id.checkbox_5);
        checkBox5.setOnCheckedChangeListener(this);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if (isChecked) {
            buttonView.setChecked(true);
            if (buttonView.getId() != R.id.checkbox_5) {
                checkBox5.setChecked(false);
            } else {
                checkBox1.setChecked(false);
                checkBox2.setChecked(false);
                checkBox3.setChecked(false);
                checkBox4.setChecked(false);
            }
        } else {
            buttonView.setChecked(false);
            if (!checkBox1.isChecked() && !checkBox2.isChecked() && !checkBox3.isChecked() && !checkBox4.isChecked()) {
                checkBox5.setChecked(true);
            }
        }
    }
}
