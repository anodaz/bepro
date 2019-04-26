package com.anodaz.bepro;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.anodaz.bepro.Service.DBSqlite;
import com.pusher.client.Pusher;
import com.pusher.client.PusherOptions;
import com.pusher.client.channel.Channel;
import com.pusher.client.channel.SubscriptionEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class MainActivity extends AppCompatActivity {
    Button getImage,histo,test;
    Switch power;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getImage=(Button)findViewById(R.id.getImage);
        test=(Button)findViewById(R.id.test);
        histo=(Button)findViewById(R.id.histo);

        power = findViewById(R.id.power);
        power.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // do something, the isChecked will be
                // true if the switch is in the On position
                if(power.isChecked()){
                    Long tsLong = System.currentTimeMillis()/1000;
                    String data = "{\"name\":\"server\",\"channel\":\"anodaz\",\"data\":\"on\"}";
                    String key="1c3ec83d21f8e9a21faf";
                    String secret="48f1360c9c5a8102b603";
                    String path="/apps/753307/events";
                    String md5data=md5(data);
                    String timestamp=tsLong.toString();
                    String queryString="auth_key="+key+"&auth_timestamp="+timestamp+"&auth_version=1.0&body_md5="+md5data;
                    //String authSig = MyHashHmac("POST\n" + path + "\n" + queryString, secret);
                    //String authSig = MyHashHmac("POST\n" + path + "\n" + queryString, secret);
                    String authSig = hmacSha256(""+secret,"POST\n" + path + "\n" + queryString);
                    String url="https://api-eu.pusher.com"+path+"?"+queryString+"&auth_signature="+authSig;
                    System.out.println(url);
                    new  MyAsyncTaskgetNews().execute(url,"on");
                }else{
                    Long tsLong = System.currentTimeMillis()/1000;
                    String data = "{\"name\":\"server\",\"channel\":\"anodaz\",\"data\":\"off\"}";
                    String key="1c3ec83d21f8e9a21faf";
                    String secret="48f1360c9c5a8102b603";
                    String path="/apps/753307/events";
                    String md5data=md5(data);
                    String timestamp=tsLong.toString();
                    String queryString="auth_key="+key+"&auth_timestamp="+timestamp+"&auth_version=1.0&body_md5="+md5data;
                    //String authSig = MyHashHmac("POST\n" + path + "\n" + queryString, secret);
                    //String authSig = MyHashHmac("POST\n" + path + "\n" + queryString, secret);
                    String authSig = hmacSha256(""+secret,"POST\n" + path + "\n" + queryString);
                    String url="https://api-eu.pusher.com"+path+"?"+queryString+"&auth_signature="+authSig;
                    System.out.println(url);
                    new  MyAsyncTaskgetNews().execute(url,"off");
                }
            }
        });
        histo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //showNotification(10,"20/12/2019", "1HhGn0jrox4fl6BKespJVQ52RfETXWSWM","Active");
                Class historiqueClass=Historique.class;
                Intent i = new Intent(MainActivity.this, historiqueClass);
                startActivity(i);
            }
        });
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showNotification("20/12/2019", "1HhGn0jrox4fl6BKespJVQ52RfETXWSWM","Active");
            }
        });
        getImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Long tsLong = System.currentTimeMillis()/1000;
                String data = "{\"name\":\"server\",\"channel\":\"anodaz\",\"data\":\"GetImage\"}";
                String key="1c3ec83d21f8e9a21faf";
                String secret="48f1360c9c5a8102b603";
                String path="/apps/753307/events";
                String md5data=md5(data);
                String timestamp=tsLong.toString();
                String queryString="auth_key="+key+"&auth_timestamp="+timestamp+"&auth_version=1.0&body_md5="+md5data;
                //String authSig = MyHashHmac("POST\n" + path + "\n" + queryString, secret);
                //String authSig = MyHashHmac("POST\n" + path + "\n" + queryString, secret);
                String authSig = hmacSha256(""+secret,"POST\n" + path + "\n" + queryString);
                String url="https://api-eu.pusher.com"+path+"?"+queryString+"&auth_signature="+authSig;
                System.out.println(url);
                new  MyAsyncTaskgetNews().execute(url,"GetImage");
            }
        });
        PusherOptions options = new PusherOptions();
        options.setCluster("eu");
        Pusher pusher = new Pusher("cf8973739432e04a202c", options);
        Channel channel = pusher.subscribe("anodaz");

        channel.bind("app", new SubscriptionEventListener() {
            @Override
            public void onEvent(String channelName, String eventName, String data) {
                try {
                    JSONObject objs = new JSONObject(data);
                    //db.additem(objs.getInt("id"),objs.getString("date"),objs.getString("image"),objs.getString("status"));
                    showNotification(objs.getString("date"),objs.getString("image"),objs.getString("status"));
                    System.out.println(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        pusher.connect();
    }
    private void nextPage(String progress) {

    }
    public static String hmacSha256(String KEY, String VALUE) {
        return hmacSha(KEY, VALUE, "HmacSHA256");
    }

    private static String hmacSha(String KEY, String VALUE, String SHA_TYPE) {
        try {
            SecretKeySpec signingKey = new SecretKeySpec(KEY.getBytes("UTF-8"), SHA_TYPE);
            Mac mac = Mac.getInstance(SHA_TYPE);
            mac.init(signingKey);
            byte[] rawHmac = mac.doFinal(VALUE.getBytes("UTF-8"));

            byte[] hexArray = {
                    (byte)'0', (byte)'1', (byte)'2', (byte)'3',
                    (byte)'4', (byte)'5', (byte)'6', (byte)'7',
                    (byte)'8', (byte)'9', (byte)'a', (byte)'b',
                    (byte)'c', (byte)'d', (byte)'e', (byte)'f'
            };
            byte[] hexChars = new byte[rawHmac.length * 2];
            for ( int j = 0; j < rawHmac.length; j++ ) {
                int v = rawHmac[j] & 0xFF;
                hexChars[j * 2] = hexArray[v >>> 4];
                hexChars[j * 2 + 1] = hexArray[v & 0x0F];
            }
            return new String(hexChars);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest
                    .getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuilder hexString = new StringBuilder();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
    public class MyAsyncTaskgetNews extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {

            // String url="https://api.ipify.org?format=json";
            //     stat.setText(url);

        }
        @Override
        protected String  doInBackground(String... params) {
            StringBuilder sb = new StringBuilder();
            String error;
            //String http = "http://android.schoolportal.gr/Service.svc/SaveValues";


            HttpURLConnection urlConnection=null;
            try {
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setUseCaches(false);
                urlConnection.setConnectTimeout(10000);
                urlConnection.setReadTimeout(10000);
                urlConnection.setRequestProperty("Content-Type","application/json");

                //  urlConnection.setRequestProperty("Host", "android.schoolportal.gr");
                urlConnection.connect();

                //Create JSONObject here
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("name", "server");
                jsonParam.put("channel", "anodaz");
                jsonParam.put("data", params[1]);
                OutputStreamWriter out = new OutputStreamWriter(urlConnection.getOutputStream());
                out.write(jsonParam.toString());
                out.close();
                System.out.println(url);
                int HttpResult =urlConnection.getResponseCode();
                System.out.println(""+HttpResult);
                if(HttpResult ==HttpURLConnection.HTTP_OK){
                    BufferedReader br = new BufferedReader(new InputStreamReader(
                            urlConnection.getInputStream(),"utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    publishProgress(sb.toString(),"online");
                    //System.out.println("Done "+sb.toString());
                }else{
                    publishProgress("","offline");
                    //System.out.println("offline "+urlConnection.getResponseMessage());
                }
            } catch (Exception e) {
                //System.out.println("offline "+e.getMessage());
                publishProgress(""+e.getMessage(),"offline");
                // e.printStackTrace();
            }finally{
                if(urlConnection!=null)
                    urlConnection.disconnect();
            }
            return null;
        }
        protected void onProgressUpdate(String... progress) {

            try {
                if(progress[1]=="offline") Toast.makeText(getApplicationContext(),"Exception : "+progress[0],Toast.LENGTH_LONG).show();
                else nextPage(progress[0]);

            } catch (Exception ex) {
                Toast.makeText(getApplicationContext(),"Exception",Toast.LENGTH_LONG).show();

            }

        }


        protected void onPostExecute(String  result2){

        }




    }

   

    private void showNotification(String date,String image,String status) {
        DBSqlite db = new DBSqlite(MainActivity.this);
        int id = db.additem(date,image,status);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String NOTIFICATION_CHANNEL_ID = "com.anodaz.bepro.test";
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,"Notification",
                    NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("EDMT Channel");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            notificationChannel.enableLights(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        Intent intent=new Intent(this,ImageActivity.class);
        Bundle b=new Bundle();
        b.putInt("id", id);
        b.putString("date", date);
        b.putString("image", image);
        b.putString("status", status);
        intent.putExtras(b);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,NOTIFICATION_CHANNEL_ID);
        notificationBuilder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setContentTitle("id :"+id+" => "+status)
                .setContentText("Date : "+date)
                .setContentInfo("Info")
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_notification);
        notificationManager.notify(new Random().nextInt(),notificationBuilder.build());
    }
}
