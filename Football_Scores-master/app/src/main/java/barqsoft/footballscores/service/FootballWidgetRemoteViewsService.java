package barqsoft.footballscores.service;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.os.Binder;
import android.os.Build;
import android.provider.ContactsContract;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.DatabaseContract;
import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;

/**
 * Created by jasonmoix on 12/9/15.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class FootballWidgetRemoteViewsService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {

            private Cursor data = null;

            @Override
            public void onCreate() {

            }

            @Override
            public void onDataSetChanged() {

                if(data != null) data.close();

                final long identityToken = Binder.clearCallingIdentity();
                data = getContentResolver().query(DatabaseContract.BASE_CONTENT_URI,
                        null,
                        null,
                        null,
                        DatabaseContract.scores_table.DATE_COL + " ASC");
                Binder.restoreCallingIdentity(identityToken);

            }

            @Override
            public void onDestroy() {

                if (data != null) {
                    data.close();
                    data = null;
                }

            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            @Override
            public RemoteViews getViewAt(int position) {

                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_list_item);

                String date = data.getString(data.getColumnIndex(DatabaseContract.scores_table.DATE_COL));
                int lge = data.getInt(data.getColumnIndex(DatabaseContract.scores_table.LEAGUE_COL));
                String league = Utilies.getLeague(lge, getBaseContext());
                String matchDay = Utilies.getMatchDay(data.getInt(data.getColumnIndex(DatabaseContract.scores_table.MATCH_DAY)),
                        lge, getBaseContext());
                String time = data.getString(data.getColumnIndex(DatabaseContract.scores_table.TIME_COL));
                String homeTeam = data.getString(data.getColumnIndex(DatabaseContract.scores_table.HOME_COL));
                String awayTeam = data.getString(data.getColumnIndex(DatabaseContract.scores_table.AWAY_COL));
                int homeScore = data.getInt(data.getColumnIndex(DatabaseContract.scores_table.HOME_GOALS_COL));
                int awayScore = data.getInt(data.getColumnIndex(DatabaseContract.scores_table.AWAY_GOALS_COL));

                SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat newFormat = new SimpleDateFormat("MM/dd/yyyy");

                try {
                    Date formattedDate = format.parse(date);
                    date = newFormat.format(formattedDate);
                }catch (ParseException e){
                    e.printStackTrace();
                }

                views.setTextViewText(R.id.header, date + " - " + time);
                views.setTextViewText(R.id.league, league + " - " + matchDay);
                views.setTextViewText(R.id.homeTeam, homeTeam);
                views.setTextViewText(R.id.awayTeam, awayTeam);
                views.setTextViewText(R.id.homeScore, (homeScore < 0) ? "0" : Integer.toString(homeScore));
                views.setTextViewText(R.id.awayScore, (awayScore < 0) ? "0" : Integer.toString(awayScore));

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {

                    String contentDescription = getBaseContext().getString(R.string.cd_league, league) +
                            getBaseContext().getString(R.string.cd_time, time) +
                            getBaseContext().getString(R.string.cd_hometeam, homeTeam) + " " + awayTeam +
                            getBaseContext().getString(R.string.cd_homescore, homeScore) + " " + awayScore;

                    setRemoteContentDescription(views, contentDescription);
                }

                return views;

            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public long getItemId(int position) {
                if (data.moveToPosition(position))
                    return data.getInt(0);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1)
            private void setRemoteContentDescription(RemoteViews views, String description) {
                views.setContentDescription(R.id.widget, description);
            }
        };
    }
}
