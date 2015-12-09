package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.widget.RemoteViews;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.MainScreenFragment;
import barqsoft.footballscores.R;
import barqsoft.footballscores.widget.FootballWidgetProvider;

/**
 * Created by jasonmoix on 12/9/15.
 */
public class FootballWidgetIntentService extends IntentService {

    public static final String TAG = "FootballWidgetIntentService";

    public FootballWidgetIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this,
                FootballWidgetProvider.class));

        int[] games = new int[3];
        int j = 0;

        Cursor gamesCheck = null;

        for (int i = 1;i < 4;i++) {
            Date fragmentdate = new Date(System.currentTimeMillis()+((i-2)*86400000));
            SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");
            String date = mformat.format(fragmentdate);

            gamesCheck = getContentResolver().query(
                    DatabaseContract.scores_table.buildScoreWithDate(),
                    null,
                    null,
                    new String[]{date},
                    null
            );

            if(gamesCheck == null || gamesCheck.getCount() == 0){
                games[j++] = 0;
            }else{
                games[j++] = gamesCheck.getCount();
            }

            if(gamesCheck != null) gamesCheck.close();

        }


        for(int appWidgetId : appWidgetIds){

            RemoteViews views = new RemoteViews(
                    this.getPackageName(),
                    R.layout.widget_football);


            views.setTextViewText(R.id.yesterdayGames, getString(R.string.msg_widget_games, Integer.toString(games[0])));
            views.setTextViewText(R.id.todayGames, getString(R.string.msg_widget_games, Integer.toString(games[1])));
            views.setTextViewText(R.id.tomorrowGames, getString(R.string.msg_widget_games, Integer.toString(games[2])));

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                setRemoteContentDescription(views, this.getString(R.string.cd_widget));
            }

            Intent launchIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, launchIntent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);

        }
    }

    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
    private void setRemoteContentDescription(RemoteViews views, String description) {
        views.setContentDescription(R.id.widget, description);
    }

}
