package it.jaschke.alexandria;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.widget.Toast;

/**
 * Created by jasonmoix on 11/30/15.
 */
public class Utilities {

    public static Boolean checkNetworkAvailable(final Context context){

        ConnectivityManager cm = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        Boolean networkIsActive = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        if(!networkIsActive){
            Handler handler = new Handler(context.getMainLooper());
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context, context.getString(R.string.network_error_toast), Toast.LENGTH_LONG).show();
                }
            };
            handler.post(runnable);
        }
        return  activeNetwork != null && activeNetwork.isConnectedOrConnecting();

    }
}
