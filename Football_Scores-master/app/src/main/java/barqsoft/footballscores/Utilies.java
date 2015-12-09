package barqsoft.footballscores;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by yehya khaled on 3/3/2015.
 */
public class Utilies
{
    public static final int BUNDESLIGA1 = 394;
    public static final int BUNDESLIGA2 = 395;
    public static final int BUNDESLIGA3 = 403;
    public static final int LIGUE1 = 396;
    public static final int LIGUE2 = 397;
    public static final int PREMIER_LEGAUE = 398;
    public static final int PRIMERA_DIVISION = 399;
    public static final int SEGUNDA_DIVISION = 400;
    public static final int SERIE_A = 401;
    public static final int PRIMEIRA_LIGA = 402;
    public static final int EREDIVISIE = 404;
    public static final int CHAMPIONS_LEAGUE = 405;

    public static String getLeague(int league_num, Context context) {
        switch (league_num) {
            case SERIE_A : return context.getString(R.string.serie_a_label);
            case PREMIER_LEGAUE : return context.getString(R.string.premier_label);
            case CHAMPIONS_LEAGUE : return context.getString(R.string.champions_league_label);
            case PRIMERA_DIVISION : return context.getString(R.string.primera_div_label);
            case BUNDESLIGA1 : return context.getString(R.string.bundesliga_label);
            case BUNDESLIGA2 : return context.getString(R.string.bundesliga_label);
            case BUNDESLIGA3 : return context.getString(R.string.bundesliga_label);
            case LIGUE1 : return context.getString(R.string.ligue_label);
            case LIGUE2 : return context.getString(R.string.ligue_label);
            case SEGUNDA_DIVISION : return context.getString(R.string.segunda_label);
            case PRIMEIRA_LIGA : return context.getString(R.string.primera_liga_label);
            case EREDIVISIE : return context.getString(R.string.eredivisie_label);
            default: return context.getString(R.string.league_not_known);
        }
    }

    public static String getMatchDay(int match_day,int league_num, Context context) {
        if(league_num == CHAMPIONS_LEAGUE) {
            if (match_day <= 6) {
                return context.getString(R.string.md_groupstages_label);
            } else if(match_day == 7 || match_day == 8) {
                return context.getString(R.string.md_firstknockout_label);
            } else if(match_day == 9 || match_day == 10) {
                return context.getString(R.string.md_quarterfinal_label);
            } else if(match_day == 11 || match_day == 12) {
                return context.getString(R.string.md_semifinal_label);
            } else {
                return context.getString(R.string.md_final_label);
            }
        } else {
            return context.getString(R.string.md_matchday_label, String.valueOf(match_day));
        }
    }

    public static String getScores(int home_goals,int awaygoals) {
        if(home_goals < 0 || awaygoals < 0) {
            return " - ";
        } else {
            return String.valueOf(home_goals) + " - " + String.valueOf(awaygoals);
        }
    }

    public static int getTeamCrestByTeamName (String teamname) {
        if (teamname==null){return R.drawable.soccerball;}

        switch (teamname) { //This is the set of icons that are currently in the app. Feel free to find and add more
            //as you go.
            case "Arsenal London FC" : return R.drawable.arsenal;
            case "Manchester United FC" : return R.drawable.manchester_united;
            case "Swansea City" : return R.drawable.swansea_city_afc;
            case "Leicester City" : return R.drawable.leicester_city_fc_hd_logo;
            case "Everton FC" : return R.drawable.everton_fc_logo1;
            case "West Ham United FC" : return R.drawable.west_ham;
            case "Tottenham Hotspur FC" : return R.drawable.tottenham_hotspur;
            case "West Bromwich Albion" : return R.drawable.west_bromwich_albion_hd_logo;
            case "Sunderland AFC" : return R.drawable.sunderland;
            case "Stoke City FC" : return R.drawable.stoke_city;
            default: return R.drawable.soccerball;
        }
    }

    public static Boolean checkNetworkAvailable(final Context context){

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean networkIsActive = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(!networkIsActive){
            Handler handler = new Handler(context.getMainLooper());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, context.getString(R.string.network_error_toast), Toast.LENGTH_SHORT).show();
                }
            };
            handler.post(runnable);
        }
        return  activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }

}
