package com.valecom.yingul.main;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.main.categories.ItemsByCategoryActivity;
import com.valecom.yingul.main.index.InicioFragment;
import com.valecom.yingul.main.myAccount.MyAccountFragment;
import com.valecom.yingul.main.sell.SellActivity;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
        ItemFragment.OnFragmentInteractionListener, ClientFragment.OnFragmentInteractionListener,
        FavoriteFragment.OnFragmentInteractionListener,  EstimateFragment.OnFragmentInteractionListener, MyAccountFragment.OnFragmentInteractionListener{

    public static String api_key;
    public static final String TAG = "MainActivity";
    private JSONObject api_parameter;
    private String username;
    private TextView profile_name;
    private MaterialDialog progressDialog;
    public Toolbar toolbar;

    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();

        SharedPreferences settings = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);

        //Checks whether a user is logged in, otherwise redirects to the Login screen


        api_key = settings.getString("api_key", "");

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Yingul");
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        LinearLayout navigationHeaderView = (LinearLayout)navigationView.getHeaderView(0);

        profile_name = (TextView) navigationHeaderView.findViewById(R.id.profile_name);
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            profile_name.setText("¡Bienvenido!");
            TextView profile_email = (TextView) navigationHeaderView.findViewById(R.id.profile_email);
            profile_email.setText("Vendés más, comprás mejor!");
            ImageView profilePhoto = (ImageView) navigationHeaderView.findViewById(R.id.profile_photo);
            Picasso.with(MainActivity.this).load(Network.BUCKET_URL+"user/userProfile/profile.jpg").into(profilePhoto);
            profilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent settingsIntent = new Intent(MainActivity.this, LoginActivity.class);
                    settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(settingsIntent);
                }
            });
        }else{
            String email = settings.getString("email", "");
            username = settings.getString("username","");
            RunPersonService();

            TextView profile_email = (TextView) navigationHeaderView.findViewById(R.id.profile_email);
            profile_email.setText(email);

            ImageView profilePhoto = (ImageView) navigationHeaderView.findViewById(R.id.profile_photo);
            Picasso.with(MainActivity.this).load(Network.BUCKET_URL+"user/userProfile/"+settings.getString("profilePhoto","")).into(profilePhoto);
            profilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new MaterialDialog.Builder(MainActivity.this)
                            .title("Editar foto")
                            .content("Cambia tu foto de perfil.")
                            .positiveText("Elegir existente")
                            .negativeText("Tomar foto")
                            .cancelable(true)
                            .negativeColorRes(R.color.colorAccent)
                            .positiveColorRes(R.color.colorAccent)
                            .callback(new MaterialDialog.ButtonCallback()
                            {
                                @Override
                                public void onPositive(MaterialDialog dialog)
                                {
                                    dialog.dismiss();
                                    if (dialog != null && dialog.isShowing())
                                    {
                                        dialog.dismiss();
                                    }
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
        }

        //Setting up default tab if redirecting back from other screens
        String tab = getIntent().getStringExtra("tab");

        if (tab != null && tab.equals("items"))
        {
            ItemFragment fragment = new ItemFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (tab != null && tab.equals("clients"))
        {
            ClientFragment fragment = new ClientFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (tab != null && tab.equals("estimates"))
        {
            EstimateFragment fragment = new EstimateFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else
        {
            /*HomeFragment fragment = new HomeFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
            InicioFragment fragment = new InicioFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            /*PrincipalFragment fragment = new PrincipalFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
        }
    }

    @Override
    public void onBackPressed() {
        //makes sure the initial empty activity_main screen is not displayed on back pressed
        if (getSupportFragmentManager().getBackStackEntryCount() == 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
            if (drawer.isDrawerOpen(GravityCompat.START))
            {
                drawer.closeDrawer(GravityCompat.START);
            } else
            {
                super.onBackPressed();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchMenuItem);

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if (!hasFocus) {
                    MenuItemCompat.collapseActionView(searchMenuItem);
                    searchView.setQuery("", false);
                    InicioFragment fragment = new InicioFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                    Log.e("onclick buscador",searchView.getQuery().toString());
                    String name = searchView.getQuery().toString().replace(" ","");
                    requestArrayPost(Network.API_URL+"category/bestMatch/"+name,"");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                Log.e("buscador",searchView.getQuery().toString());
                Bundle bundle = new Bundle();
                bundle.putString("nameCategory",searchView.getQuery().toString());
                MainFindCategoryFragment fragment = new MainFindCategoryFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home)
        {
            InicioFragment fragment = new InicioFragment();
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_invoices)
        {
            FavoriteFragment fragment = new FavoriteFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_estimates)
        {
            /*PrincipalFragment fragment = new PrincipalFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
        }
        else if (id == R.id.nav_items)
        {
            ItemFragment fragment = new ItemFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();

        }
        else if (id == R.id.nav_clients) {
            ClientFragment fragment = new ClientFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_settings) {
            MyAccountFragment fragment = new MyAccountFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_sell) {
            Intent settingsIntent = new Intent(this, SellActivity.class);
            startActivity(settingsIntent);
        }
        else if (id == R.id.nav_login) {
            Intent settingsIntent = new Intent(this, LoginActivity.class);
            startActivity(settingsIntent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onFragmentInteraction(Uri uri){
        //you can leave it empty
    }


    /*para obtener el nombre del usuario*/
    public void RunPersonService()
    {
        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL + "user/person/"+username, api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        try
                        {
                            Log.e("Persona por su nombre " , response.toString());
                            String firstname = response.getString("name");

                            profile_name.setText("¡Hola "+ucFirst(firstname)+"!");

                            SharedPreferences.Editor user = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE).edit();

                            user.putString("firstname",response.getString("name"));
                            user.putString("lastname",response.getString("lastname"));

                            user.commit();

                        }
                        catch (Exception ex)
                        {
                            Toast.makeText(MainActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener()
                {

                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        // TODO Auto-generated method stub
                        if (progressDialog != null && progressDialog.isShowing()) {
                            // If the response is JSONObject instead of expected JSONArray
                            progressDialog.dismiss();
                        }

                        NetworkResponse response = error.networkResponse;
                        if (response != null && response.data != null)
                        {
                            try
                            {
                                JSONObject json = new JSONObject(new String(response.data));
                                Toast.makeText(MainActivity.this, json.has("message") ? json.getString("message")+"1" : json.getString("error")+"2", Toast.LENGTH_LONG).show();
                            }
                            catch (JSONException e)
                            {
                                Toast.makeText(MainActivity.this, R.string.error_try_again_support+"3", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            //Toast.makeText(LoginActivity.this, error != null && error.getMessage() != null ? error.getMessage()+"4" : error.toString()+"5", Toast.LENGTH_LONG).show();
                            Toast.makeText(MainActivity.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                //params.put("X-API-KEY", Network.API_KEY);
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(TAG);

        MySingleton.getInstance(this).addToRequestQueue(postRequest);
    }

    public static String ucFirst(String str) {
        str=str.toLowerCase();
        if (str == null || str.isEmpty()) {
            return str;
        } else {
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }

    public void  requestArrayPost(String url, String json){
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responce.contains("/")){
                            String categoryId = responce.replace("/","");
                            Log.e("categoryId",""+categoryId);
                            Intent intent = new Intent(MainActivity.this,ItemsByCategoryActivity.class);
                            intent.putExtra("categoryId",categoryId);
                            startActivity(intent);
                        }else{
                            Toast.makeText(MainActivity.this,"No se encontro resultados para la busqueda",Toast.LENGTH_LONG).show();
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
