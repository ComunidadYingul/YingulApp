package com.valecom.yingul.service;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat.Builder;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.github.arturogutierrez.Badges;
import com.github.arturogutierrez.BadgesNotSupportedException;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Notification;
import com.valecom.yingul.model.Yng_Query;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class NotificationService extends Service{

    private Context context= this;

    final class MyThreadClass implements Runnable{
        int service_id;
        MyThreadClass(int service_id){
            this.service_id=service_id;
        }

        @Override
        public void run() {
            int i=0;
            synchronized (this){
                while(i<10){
                    try {
                        wait(60000);
                        createNotifications();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public void createNotifications(){

        final SharedPreferences settings = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);

        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {

        }else{
            final String username = settings.getString("username","");

            JsonArrayRequest postRequest = new JsonArrayRequest(Network.API_URL + "notification/getNotificationByUserAndStatus/"+username+"/pending/0/0",
                    new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try
                            {
                                JSONArray notificationList = response;
                                for (int i = 0; i < notificationList.length(); i++) {
                                    JSONObject obj = notificationList.getJSONObject(i);
                                    Yng_Notification notification = new Yng_Notification();
                                    Gson gson = new Gson();
                                    notification = gson.fromJson(String.valueOf(obj), Yng_Notification.class);

                                    Intent intent = new Intent(context, MainActivity.class);
                                    PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_ONE_SHOT);

                                    Uri soundNotification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

                                    Builder builder = new Builder(context);
                                    builder.setAutoCancel(true);
                                    builder.setContentTitle(notification.getTitle());
                                    builder.setContentText(notification.getDescription());
                                    builder.setSound(soundNotification);
                                    builder.setSmallIcon(R.drawable.ic_yingul_logo);
                                    builder.setContentIntent(pendingIntent);
                                    builder.setVibrate(new long[] { 250, 250, 250, 250, 250 });
                                    builder.setLights(Color.RED, 3000, 3000);

                                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                    Random random = new Random();
                                    notificationManager.notify(random.nextInt(),builder.build());
                                }

                                try {
                                    Badges.setBadge(context, notificationList.length());
                                } catch (BadgesNotSupportedException badgesNotSupportedException) {

                                }

                                editNotifications(Network.API_URL + "notification/updateAllNotificationsForUser/"+username+"/notified","",settings.getString("password",""));

                            }
                            catch(Exception ex)
                            {

                            }

                        }
                    }, new Response.ErrorListener()
            {

                @Override
                public void onErrorResponse(VolleyError error)
                {

                }
            })
            {

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError
                {
                    SharedPreferences settings = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Authorization", settings.getString("password",""));
                    return params;
                }
            };

            postRequest.setTag(MainActivity.TAG);
            postRequest.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS*5, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            MySingleton.getInstance(this).addToRequestQueue(postRequest);


        }





    }
    @Override
    public void onCreate(){
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){

        Toast.makeText(this, "Servicio Iniciado", Toast.LENGTH_LONG).show();
        Thread thread = new Thread(new MyThreadClass(startId));
                thread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy(){
        Toast.makeText(this, "Servicio Detenido", Toast.LENGTH_LONG).show();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void  editNotifications(String url, String json, String authorization){
        final String TAG = "bUYActivity";
        final MediaType JSON= MediaType.parse("application/json; charset=utf-8");
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(9000, TimeUnit.SECONDS)
                .writeTimeout(9000, TimeUnit.SECONDS)
                .readTimeout(9000, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(JSON, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .addHeader("Authorization",authorization)
                .post(body)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async okhttp call");
            }
            @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                if (!response.isSuccessful()) {
                    throw new IOException("Error response " + response);
                }

            }
        });
    }

}
