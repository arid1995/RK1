package ru.mail.park.rk1.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;

import ru.mail.park.rk1.R;
import ru.mail.weather.lib.Storage;

/**
 * Created by farid on 3/7/17.
 */

public class SettingsActivity extends AppCompatActivity{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        findViewById(R.id.choose_category_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentTopic = ((Spinner) findViewById(R.id.category_picker)).getSelectedItem().toString();
                Storage.getInstance(SettingsActivity.this).saveCurrentTopic(currentTopic);
                startActivity(new Intent(SettingsActivity.this, MainActivity.class));
            }
        });
    }

}
