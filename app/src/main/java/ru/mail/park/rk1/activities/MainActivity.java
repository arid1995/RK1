package ru.mail.park.rk1.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import ru.mail.park.rk1.R;
import ru.mail.park.rk1.utils.ServiceHelper;
import ru.mail.weather.lib.Topics;

public class MainActivity extends AppCompatActivity implements ServiceHelper.ResponseListener {
    private int taskId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TextView)findViewById(R.id.news_body)).setMovementMethod(new ScrollingMovementMethod());

        findViewById(R.id.setting_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });

        findViewById(R.id.refresh_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (taskId != 0) {
                    Toast.makeText(MainActivity.this, "Wow, wow, slow down!!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                taskId = ServiceHelper.getInstance(MainActivity.this)
                        .requestInfo(MainActivity.this, MainActivity.this, true);
            }
        });

        taskId = ServiceHelper.getInstance(MainActivity.this)
                .requestInfo(MainActivity.this, MainActivity.this, false);
    }

    @Override
    public void handleResult(String header, String body, String date) {
        ((TextView) findViewById(R.id.news_header)).setText(header);
        ((TextView) findViewById(R.id.news_body)).setText(body);
        ((TextView) findViewById(R.id.news_date)).setText(date);
        taskId = 0;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (taskId != 0) {
            ServiceHelper.getInstance(this).removeListener(taskId);
        }
    }
}
