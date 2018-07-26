package com.valecom.yingul.main.myAccount;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.adapter.QueryChatAdapter;
import com.valecom.yingul.main.ActivityProductDetail;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.NewInvoiceActivity;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.main.buy.BuyItemSetShippingBranchFragment;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Query;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MyAccountSaleQuestionAnswerFragment extends Fragment
{
    private OnFragmentInteractionListener mListener;

    private MaterialDialog progressDialog;
    private JSONObject api_parameter;
    private Yng_Query query;
    private Yng_User user;
    private Yng_Ubication userUbication;

    private ImageView itemImage;
    private TextView itemCurrencyPrice,itemName,queryQuery,date;
    private LinearLayout freeShipping,itemLayout;
    private Button btnAnswer;
    private EditText editAnswer;

    String TAG="OkHttpConection";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    public MyAccountSaleQuestionAnswerFragment()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MyAccountSaleQuestionAnswerFragment newInstance(String param1, String param2)
    {
        MyAccountSaleQuestionAnswerFragment fragment = new MyAccountSaleQuestionAnswerFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        progressDialog = new MaterialDialog.Builder(getActivity())
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_my_account_sale_question_answer, container, false);

        user = new Yng_User();
        userUbication = new Yng_Ubication();
        query = new Yng_Query();
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.SESSION_USER, getActivity().MODE_PRIVATE);

        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent settingsIntent = new Intent(getActivity(), LoginActivity.class);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(settingsIntent);
        }else{
            user.setUsername(settings.getString("username",""));
            /*para obtener la ubicacion del usuario*/
            if(settings.getString("yng_Ubication","").equals("null")){
                userUbication=null;
            }else{
                Gson gson = new Gson();
                userUbication = gson.fromJson(settings.getString("yng_Ubication","") , Yng_Ubication.class);
            }
            /*fin de la ubicacion del usuario*/
            user.setPhone(settings.getString("phone",""));
            user.setDocumentType(settings.getString("documentType",""));
            user.setDocumentNumber(settings.getString("documentNumber",""));
            user.setPassword(settings.getString("password",""));
            user.setYng_Ubication(userUbication);
        }

        Bundle bundle = getArguments();
        Gson gson = new Gson();
        query = gson.fromJson(bundle.getString("query"), Yng_Query.class);
        Log.e("query que llega",bundle.getString("query"));

        itemImage = (ImageView) view.findViewById(R.id.itemImage);
        itemCurrencyPrice = (TextView) view.findViewById(R.id.itemCurrencyPrice);
        itemName = (TextView) view.findViewById(R.id.itemName);
        freeShipping = (LinearLayout) view.findViewById(R.id.freeShipping);
        queryQuery = (TextView) view.findViewById(R.id.queryQuery);
        date = (TextView) view.findViewById(R.id.date);
        itemLayout = (LinearLayout) view.findViewById(R.id.itemLayout);
        btnAnswer = (Button) view.findViewById(R.id.btnAnswer);
        editAnswer = (EditText) view.findViewById(R.id.editAnswer);
        Picasso.with(getContext()).load(Network.BUCKET_URL+query.getYng_Item().getPrincipalImage()).into(itemImage);
        itemCurrencyPrice.setText("$ "+query.getYng_Item().getPrice());
        itemName.setText(query.getYng_Item().getName());
        if(query.getYng_Item().getProductPagoEnvio()==null||!query.getYng_Item().getProductPagoEnvio().equals("gratis")){
            freeShipping.setVisibility(View.GONE);
        }else{
            freeShipping.setVisibility(View.VISIBLE);
        }
        queryQuery.setText(query.getQuery());
        date.setText(query.getDate());
        itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_detail=new Intent(getActivity(), ActivityProductDetail.class);
                intent_detail.putExtra("itemId",String.valueOf(query.getYng_Item().getItemId()));
                startActivity(intent_detail);
            }
        });
        btnAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                answer();
            }
        });
        final FloatingActionButton deleteQuestion = (FloatingActionButton) view.findViewById(R.id.deleteQuestion);
        deleteQuestion.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                new MaterialDialog.Builder(getContext())
                        .title("Eliminar pregunta")
                        .content("¿Esta seguro de eliminar esta pregunta?")
                        .positiveText("ELIMINAR")
                        .negativeText("CANCELAR")
                        .cancelable(false)
                        .negativeColorRes(R.color.colorAccent)
                        .positiveColorRes(R.color.colorAccent)
                        .callback(new MaterialDialog.ButtonCallback()
                        {
                            @Override
                            public void onPositive(MaterialDialog dialog)
                            {
                                deleteQuestionMethod();
                            }
                            @Override
                            public void onNegative(MaterialDialog dialog)
                            {
                                dialog.dismiss();
                                if (dialog != null && dialog.isShowing())
                                {
                                    dialog.dismiss();
                                }
                            }
                        })
                        .show();
            }
        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        /*if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isAdded())
        {
            ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(query.getUser().getUsername());
            NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
            navigationView.setCheckedItem(R.id.nav_settings);
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    public void answer(){
        query.setAnswer(editAnswer.getText().toString().trim());
        Gson gson = new Gson();
        String jsonBody = gson.toJson(query);
        requestArrayPost(Network.API_URL + "query/answer",jsonBody);
    }

    public void deleteQuestionMethod(){
        requestAuthorizedPost(Network.API_URL + "query/delete/"+query.getQueryId(),"");
    }

    public void requestAuthorizedPost(String url, String json){
        start("inicio");
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(9000, TimeUnit.SECONDS)
                .writeTimeout(9000, TimeUnit.SECONDS)
                .readTimeout(9000, TimeUnit.SECONDS)
                .build();
        RequestBody body = RequestBody.create(JSON, json);
        okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .addHeader("Authorization",user.getPassword())
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
                            new MaterialDialog.Builder(getContext())
                                    .title("Eliminado")
                                    .content("La pregunta se elimino correctamente")
                                    .positiveText("OK")
                                    .cancelable(false)
                                    .positiveColorRes(R.color.colorAccent)
                                    .callback(new MaterialDialog.ButtonCallback()
                                    {
                                        @Override
                                        public void onPositive(MaterialDialog dialog)
                                        {
                                            getActivity().onBackPressed();
                                        }
                                    })
                                    .show();
                        }else{
                            Toast.makeText(getContext(),responce,Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    public void requestArrayPost(String url, String json){
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
                            Toast.makeText(getContext(), "respuesta exitosa", Toast.LENGTH_SHORT).show();
                            getActivity().onBackPressed();
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
