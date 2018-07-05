package com.valecom.yingul.main.rememberPassword;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.Util.Validacion;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.RegisterActivity;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.main.newUserUbicationEditPersonalInfo.NewUbicationSetCountryFragment;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.Network;

import org.json.JSONException;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 */
public class RememberPasswordFragment extends Fragment {

    private TextView textLogo, textTitle;
    private Button buttonSendEmail;
    private EditText editEmail;
    private MaterialDialog progressDialog;
    String TAG="OkHttpConection";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    public RememberPasswordFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_remember_password, container, false);
        Typeface typeface1 = Typeface.createFromAsset(getContext().getAssets(), "fonts/font-yingul.ttf");
        textLogo = (TextView)v.findViewById(R.id.text_logo);
        textTitle = (TextView)v.findViewById(R.id.text_title);
        textLogo.setTypeface(typeface1);
        textTitle.setTypeface(typeface1);
        buttonSendEmail = (Button) v.findViewById(R.id.buttonSendEmail);
        editEmail = (EditText) v.findViewById(R.id.editEmail);
        buttonSendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Validacion val = new Validacion();
                if (val.valEmail(editEmail)){
                    sendRecoveryEmail();
                }
            }
        });
        progressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();

    }
    public void sendRecoveryEmail(){
        ((RememberPasswordActivity)getActivity()).user.setEmail(editEmail.getText().toString().trim());
        Gson gson = new Gson();
        String jsonBody = gson.toJson(((RememberPasswordActivity)getActivity()).user);
        Log.e("user final", jsonBody);
        requestStringPost(Network.API_URL + "login/sendRecoveryEmail",jsonBody);
    }
    public void  requestStringPost(String url, String json){
        start("inicio");
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(9000, TimeUnit.SECONDS)
                .writeTimeout(9000, TimeUnit.SECONDS)
                .readTimeout(9000, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(JSON, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Content-Type","application/json")
                .post(body)
                .build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                Log.e(TAG, "error in getting response using async okhttp call");
            }
            @Override public void onResponse(Call call, okhttp3.Response response) throws IOException {
                ResponseBody responseBody = response.body();
                if (!response.isSuccessful()) {
                    throw new IOException("Error response " + response);
                }
                //

                final String responce=""+(responseBody.string());
                try {
                    end(""+responce);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.i("responce:------------",""+responce);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responce.equals("save")) {
                            RememberPasswordSetCodeFragment fragment = new RememberPasswordSetCodeFragment();
                            FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }else{
                            Toast.makeText(getContext(),responce,Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }
    public void end(String end) throws JSONException {
        Log.i("end",""+end);
        progressDialog.dismiss();
    }
    public void start(String start){
        Log.i("start",""+start);
        progressDialog.show();
    }
}
