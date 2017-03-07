package ru.mail.park.rk1.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.util.SparseArray;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import ru.mail.park.rk1.utils.news.RetrieveNewsIntentService;

/**
 * Created by farid on 3/7/17.
 */

public class ServiceHelper {

    private static ServiceHelper instance;
    private int taskId = 0;
    private SparseArray<ResponseListener> listeners = new SparseArray<>();

    public static synchronized ServiceHelper getInstance(Context context) {
        if (instance == null) {
            instance = new ServiceHelper();
            instance.initializeBroadcastReceiver(context);
        }

        return instance;
    }

    private void initializeBroadcastReceiver(Context context) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(RetrieveNewsIntentService.ACTION_NEWS_ERRORED);
        filter.addAction(RetrieveNewsIntentService.ACTION_NEWS_RETRIEVED);

        LocalBroadcastManager.getInstance(context).registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(RetrieveNewsIntentService.ACTION_NEWS_RETRIEVED)) {
                    int id = intent.getIntExtra(RetrieveNewsIntentService.EXTRA_TASK_ID, -1);
                    if (id != -1) {
                        listeners.get(id).handleResult(
                                intent.getStringExtra(RetrieveNewsIntentService.EXTRA_NEWS_HEADER),
                                intent.getStringExtra(RetrieveNewsIntentService.EXTRA_NEWS_BODY),
                                new SimpleDateFormat("MM/dd/yyyy").format(new Date(
                                        intent.getLongExtra(RetrieveNewsIntentService.EXTRA_NEWS_DATE, -1)))
                        );
                        listeners.remove(id);
                    }
                    return;
                }

                Toast.makeText(context, "Something has gone waaaaaaay south!!!", Toast.LENGTH_SHORT).show();
            }
        }, filter);
    }

    public int requestInfo(Context context, ResponseListener responseListener, boolean refresh) {
        listeners.put(taskId, responseListener);

        Intent intent = new Intent(context, RetrieveNewsIntentService.class);
        intent.setAction(RetrieveNewsIntentService.ACTION_REQUEST_NEWS);
        intent.putExtra(RetrieveNewsIntentService.EXTRA_TASK_ID, taskId);
        intent.putExtra(RetrieveNewsIntentService.EXTRA_NEWS_REFRESH, refresh);

        context.startService(intent);
        return taskId++;
    }

    public void removeListener(int id) {
        listeners.remove(id);
    }

    public interface ResponseListener {
        void handleResult(String topic, String body, String date);
    }
}
