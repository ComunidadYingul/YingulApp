package com.valecom.yingul.main.rememberPassword;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.gson.Gson;
import com.valecom.yingul.R;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.NewClientActivity;
import com.valecom.yingul.main.newUserUbicationEditPersonalInfo.NewUbicationSetDetail1Fragment;
import com.valecom.yingul.main.newUserUbicationEditPersonalInfo.NewUserUbicationEditPersonalInfoActivity;
import com.valecom.yingul.model.Yng_ResetPassword;
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
public class RememberPasswordSetCodeFragment extends Fragment {

    Button buttonSetCode;
    EditText editNumber;
    private TextView textTitle;
    private MaterialDialog progressDialog;
    private int cont;
    String TAG="OkHttpConection";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    public RememberPasswordSetCodeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_remember_password_set_code, container, false);
        cont=3;
        editNumber = (EditText) v.findViewById(R.id.editNumber);
        buttonSetCode = (Button) v.findViewById(R.id.buttonSetCode);
        textTitle = (TextView) v.findViewById(R.id.textTitle);
        textTitle.setText("Te enviamos un código a "+((RememberPasswordActivity)getActivity()).user.getEmail()+" para que puedas ingresar a tu cuenta.");
        buttonSetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cont>0){
                    checkCode();
                }else{
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
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

        //Checks to make sure fragment is still attached to activity
        /*if (isAdded())
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Envio");
        }*/
    }
    public void checkCode(){
        ((RememberPasswordActivity)getActivity()).resetPassword.setCodeResetPassword(Integer.parseInt(editNumber.getText().toString().trim()));
        ((RememberPasswordActivity)getActivity()).resetPassword.setUser(((RememberPasswordActivity)getActivity()).user);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(((RememberPasswordActivity)getActivity()).resetPassword);
        Log.e("resetpassword final", jsonBody);
        requestStringPost(Network.API_URL + "login/checkAuthorizationAndroid",jsonBody);
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
                Log.e("responce:------------",""+responce);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(!responce.equals("prohibited")) {
                            Gson gson = new Gson();
                            ((RememberPasswordActivity)getActivity()).resetPassword= gson.fromJson(String.valueOf(responce), Yng_ResetPassword.class);
                            RememberPasswordSetNewPasswordFragment fragment = new RememberPasswordSetNewPasswordFragment();
                            FragmentTransaction fragmentTransaction  = getFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.content_frame, fragment);
                            fragmentTransaction.addToBackStack(null);
                            fragmentTransaction.commit();
                        }else{
                            cont--;
                            Toast.makeText(getContext(),"Verifica el email nuevamente te quedan "+cont+" opotunidades.",Toast.LENGTH_LONG).show();
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
