package ru.mail.park.rk1.utils.news;

import android.content.Context;

import java.io.IOException;

import ru.mail.weather.lib.News;
import ru.mail.weather.lib.NewsLoader;
import ru.mail.weather.lib.Storage;
import ru.mail.weather.lib.Topics;

/**
 * Created by farid on 3/7/17.
 */

public class NewsProcessor {
    private Context context;

    public NewsProcessor(Context context) {
        this.context = context;
    }

    public News processRequest(boolean refresh) {
        Storage storage = Storage.getInstance(context);
        News news;

        if (storage.loadCurrentTopic() == null) {
            storage.saveCurrentTopic(Topics.IT);
        }

        String category = storage.loadCurrentTopic();

        if (!refresh) {
            news = storage.getLastSavedNews();

            if (news != null) {
                return news;
            }
        }

        NewsLoader newsLoader = new NewsLoader();

        try {
            news = newsLoader.loadNews(category);
        } catch (IOException ex) {
            return null;
        }

        storage.saveNews(news);
        storage.saveCurrentTopic(category);

        return news;
    }
}
