package com.valecom.yingul.network;

import android.content.Context;
import android.net.ConnectivityManager;

public class Network
{
    //public static final String API_URL = "http://192.168.100.16:8080/";
    //public static final String API_URL = "http://192.168.0.14:8080/";
    //public static final String API_URL = "http://192.168.100.51:8080/";
    //public static final String API_URL = "http://192.168.100.51:8080/";
    public static final String API_URL = "http://backendyingul-env.cqx28e6j2j.us-west-2.elasticbeanstalk.com/";
    public static final String API_KEY = "RUREWVFVRU5BTExBVEE6ZWRkeQ==";
    //public static final String BUCKET_URL ="https://s3-us-west-2.amazonaws.com/jsa-s3-bucketimage/dev/image/";
    public static final String BUCKET_URL ="https://s3-us-west-2.amazonaws.com/jsa-s3-bucketimage/image/";

    public Network()
    {
        // TODO Auto-generated constructor stub
        super();
    }

    public static boolean checkStatus(Context context)
    {
        // TODO Auto-generated method stub
        final ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connMgr.getActiveNetworkInfo() != null
                && connMgr.getActiveNetworkInfo().isAvailable()
                && connMgr.getActiveNetworkInfo().isConnected())
        {
            return true;
        }
        else
        {
            return false;
        }
    }
}
