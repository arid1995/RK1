package ru.mail.park.rk1.utils.news;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.Storage;

/**
 * Created by farid on 3/7/17.
 */

public class RetrieveNewsIntentService extends IntentService {

    public static final String ACTION_REQUEST_NEWS = "action.REQUEST_NEWS";
    public static final String EXTRA_TASK_ID = "extra.TASK_ID";
    public static final String EXTRA_NEWS_REFRESH = "extra.NEWS_REFRESH";

    public static final String ACTION_NEWS_RETRIEVED = "action.NEWS_RETRIEVED";
    public static final String EXTRA_NEWS_HEADER = "extra.NEWS_HEADER";
    public static final String EXTRA_NEWS_BODY = "extra.NEWS_BODY";
    public static final String EXTRA_NEWS_DATE = "extra.NEWS_DATE";

    public static final String ACTION_NEWS_REFRESH = "action.NEWS_REFRESH";

    public static final String ACTION_NEWS_ERRORED = "action.NEWS_ERRORED";


    private int taskId;

    public RetrieveNewsIntentService() {
        super("retrieve_news");
    }

    @Override
    public void onHandleIntent(Intent intent) {
        if (intent.getAction().equals(ACTION_REQUEST_NEWS)) {
            boolean refresh = intent.getBooleanExtra(EXTRA_NEWS_REFRESH, true);

            taskId = intent.getIntExtra(EXTRA_TASK_ID, -1);

            NewsProcessor processor = new NewsProcessor(this);

            News news = processor.processRequest(refresh);
            LocalBroadcastManager.getInstance(this).sendBroadcast(createIntent(news));
        }

        if (intent.getAction().equals(ACTION_NEWS_REFRESH)) {
            NewsProcessor processor = new NewsProcessor(this);
            processor.processRequest(true);
        }
    }

    private Intent createIntent(News news) {
        Intent intent;
        if (news == null) {
            intent = new Intent(ACTION_NEWS_ERRORED);
        } else {

            intent = new Intent(ACTION_NEWS_RETRIEVED);

            intent.putExtra(EXTRA_NEWS_BODY, news.getBody());
            intent.putExtra(EXTRA_NEWS_HEADER, news.getTitle());
            intent.putExtra(EXTRA_NEWS_DATE, news.getDate());
        }

        intent.putExtra(EXTRA_TASK_ID, taskId);
        return intent;
    }
}
