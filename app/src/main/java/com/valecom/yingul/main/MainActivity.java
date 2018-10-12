package com.valecom.yingul.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.gson.Gson;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.valecom.yingul.R;
import com.valecom.yingul.main.buy.BuyActivity;
import com.valecom.yingul.main.categories.ItemsByCategoryActivity;
import com.valecom.yingul.main.createStore.CreateStoreActivity;
import com.valecom.yingul.main.edit.EditImageActivity;
import com.valecom.yingul.main.filter.SearchActivity;
import com.valecom.yingul.main.index.InicioFragment;
import com.valecom.yingul.main.myAccount.MyAccountFragment;
import com.valecom.yingul.main.myAccount.MyAccountPurchasesListFragment;
import com.valecom.yingul.main.myAccount.MyAccountSaleDetailFragment;
import com.valecom.yingul.main.myAccount.MyAccountSalesListFragment;
import com.valecom.yingul.main.myAccount.MyAccountShoppingQuestionsListFragment;
import com.valecom.yingul.main.myAccount.confirmDelivery.ConfirmDeliveryActivity;
import com.valecom.yingul.main.myAccount.yingulPay.YingulPayActivity;
import com.valecom.yingul.main.rememberPassword.RememberPasswordActivity;
import com.valecom.yingul.main.sell.SellActivity;
import com.valecom.yingul.main.store.ActivityStore;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_ItemImage;
import com.valecom.yingul.model.Yng_Store;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.MySingleton;
import com.valecom.yingul.network.Network;
import com.valecom.yingul.service.NotificationService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, HomeFragment.OnFragmentInteractionListener,
        ItemFragment.OnFragmentInteractionListener, ClientFragment.OnFragmentInteractionListener,
        FavoriteFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener, MyAccountFragment.OnFragmentInteractionListener{

    public static String api_key;
    public static final String TAG = "MainActivity";
    private JSONObject api_parameter;
    private String username,itemName;
    private TextView profile_name, textCartItemCount;
    private MaterialDialog progressDialog;
    public Toolbar toolbar;
    private Yng_User user;
    static final int ADD_PICTURES_TAG = 1;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    private MaterialDialog setting_address_edit_dialog;
    private ListView list;
    private ArrayAdapter adapter1;
    private ArrayList array_list;
    private static final int MY_CAMERA_REQUEST_CODE = 100;

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
        //String urlsec="";
        String dataurl="";
        //String itemId="";
        Intent intent=getIntent();
        String dataString = getIntent().getDataString();
        if(intent!=null&&intent.getData()!=null)
        {
            Log.e("daniel:-------","recupero main:"+dataString);
            String string = dataString;
            String[] parts = string.split("/");
            //http://www.yingul.com/frontYingulPay
            String aux="";

            String urlAux="";
            Log.e("da"+"  length: ","");

           // if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
          //  {
             //   Intent intentL = new Intent(this, LoginActivity.class);
              //  intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
               // startActivity(intent);
                //getActivity().finish();
               // return;
          //  }else{
                switch (dataString){
                    case "http://www.yingul.com":

                        break;
                    case "http://www.yingul.com/frontYingulPay":
                        Intent intentP = new Intent(MainActivity.this, YingulPayActivity.class);
                        startActivity(intentP);
                        break;
                    case "http://www.yingul.com/userFront/claims":
                        dataurl=parts[4];
                        Log.e("da",""+dataurl);
                        break;
                    case "http://www.yingul.com/userFront/purchases":
                        dataurl=parts[4];
                        Log.e("da",""+dataurl);
                        break;
                    case "http://www.yingul.com/favorites":
                        dataurl=parts[3];
                        Log.e("da",""+dataurl);
                        break;
                    case "http://www.yingul.com/userFront/sales/query":
                        dataurl=parts[5];
                        break;
                    case "http://www.yingul.com/userFront/purchases/query":
                        dataurl=parts[5]+"P";
                        break;
                    case "http://www.yingul.com/about/contactUs":
                        //para despues
                        break;
                    case "http://www.yingul.com/userFront/sales":
                        dataurl=parts[4]+"P";
                        Log.e("da",""+dataurl);
                        break;
                    /*case "http://www.yingul.com/confirmwos/":
                        break;
                    case "http://www.yingul.com/confirmws/":
                        break;
                    case "http://www.yingul.com/agreement/":
                        break;
                    case "http://www.yingul.com/tiendaOficial/":
                        break;
                    case "http://www.yingul.com/resetPassword/":
                        break;*/
                    default:
                        if(parts.length==5){
                            String dd=parts[3];
                            switch (dd) {
                                case "itemDetail":
                                    Intent detail =new Intent(this,ActivityProductDetail.class);
                                    detail.putExtra("itemId",parts[4]);
                                    startActivity(detail);
                                    break;
                                case "confirmwos":
                                    Intent intentC = new Intent(this, ConfirmDeliveryActivity.class);
                                    intentC.putExtra("confirmId",parts[4]);
                                    startActivity(intentC);
                                    break;
                                case "agreement":
//para despues
                                    break;
                                case "tiendaOficial":
                                    Intent intentS = new Intent(MainActivity.this, ActivityStore.class);
                                    intentS.putExtra("store",parts[4]);
                                    startActivity(intentS);
                                    break;
                                case "resetPassword":
                                    Intent intentR = new Intent(MainActivity.this, RememberPasswordActivity.class);
                                    //intentR.putExtra("param",parts[4]);
                                    intentR.putExtra("rest","rest");
                                    startActivity(intentR);
                                    break;
                                default:
                                    Intent atras=new Intent( this,MainActivity.class);
                                    startActivity(atras);
                                    break;
                            }
                        }
                        break;
                }
           //}




        }

        array_list = new ArrayList();
        array_list.add("Tomar foto");
        array_list.add("Elegir existente");
        adapter1 = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1, array_list);

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

        user = new Yng_User();
        Menu nav_Menu = navigationView.getMenu();
        final MenuItem menuItem = nav_Menu.findItem(R.id.nav_settings);
        View actionView = MenuItemCompat.getActionView(menuItem);
        textCartItemCount = (TextView) actionView.findViewById(R.id.cart_badge);
        setupBadge();

        profile_name = (TextView) navigationHeaderView.findViewById(R.id.profile_name);
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            profile_name.setText("¡Bienvenido!");
            TextView profile_email = (TextView) navigationHeaderView.findViewById(R.id.profile_email);
            profile_email.setText("Vendés más, comprás mejor!");
            CircleImageView profilePhoto = (CircleImageView) navigationHeaderView.findViewById(R.id.profile_photo);
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

            nav_Menu.findItem(R.id.nav_login).setVisible(true);
        }else{
            nav_Menu.findItem(R.id.nav_login).setVisible(false);
            String email = settings.getString("email", "");
            username = settings.getString("username","");
            user.setEmail(email);
            user.setUsername(username);
            user.setPassword(settings.getString("password",""));
            RunPersonService();

            TextView profile_email = (TextView) navigationHeaderView.findViewById(R.id.profile_email);
            profile_email.setText(email);

            CircleImageView profilePhoto = (CircleImageView) navigationHeaderView.findViewById(R.id.profile_photo);
            Picasso.with(MainActivity.this).load(Network.BUCKET_URL+"user/userProfile/"+settings.getString("profilePhoto","")).into(profilePhoto);
            profilePhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setting_address_edit_dialog = new MaterialDialog.Builder(MainActivity.this)
                            .customView(R.layout.type_upload_image_layout, true)
                            .cancelable(true)
                            .showListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    View view = setting_address_edit_dialog.getCustomView();
                                    list = (ListView) view.findViewById(R.id.list);
                                    // Assigning the adapter to ListView
                                    list.setAdapter(adapter1);

                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener()
                                    {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
                                        {
                                            switch (position){
                                                case 0:
                                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                        if (checkSelfPermission(Manifest.permission.CAMERA)
                                                                != PackageManager.PERMISSION_GRANTED) {
                                                            requestPermissions(new String[]{Manifest.permission.CAMERA},
                                                                    MY_CAMERA_REQUEST_CODE);
                                                        }else{
                                                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                                                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                                            }
                                                        }
                                                    }else{
                                                        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                                                        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                                            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                                                        }
                                                    }
                                                    if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                                                        setting_address_edit_dialog.dismiss();
                                                    }
                                                    break;
                                                case 1:
                                                    Intent intent = new Intent();
                                                    intent.setType("image/*");
                                                    intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                                    intent.setAction(Intent.ACTION_GET_CONTENT);
                                                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), ADD_PICTURES_TAG);
                                                    if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing())
                                                    {
                                                        setting_address_edit_dialog.dismiss();
                                                    }
                                                    break;
                                            }
                                        }
                                    });

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
            NotificationFragment fragment = new NotificationFragment();
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
            switch (dataurl){
                case "query":
                    MyAccountShoppingQuestionsListFragment fragmentQ = new MyAccountShoppingQuestionsListFragment();
                    FragmentTransaction fragmentTransactionQ = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionQ.replace(R.id.content_frame, fragmentQ);
                    fragmentTransactionQ.addToBackStack(null);
                    fragmentTransactionQ.commit();
                    break;
                case "favorites":
                    FavoriteFragment fragmentF = new FavoriteFragment();
                    FragmentTransaction fragmentTransactionF  = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionF.replace(R.id.content_frame, fragmentF);
                    fragmentTransactionF.addToBackStack(null);
                    fragmentTransactionF.commit();
                    break;
                case "purchases":
                    MyAccountPurchasesListFragment fragmentP = new MyAccountPurchasesListFragment();
                    FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragmentP);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    break;
                case "claims":
                    MyAccountPurchasesListFragment fragmentC = new MyAccountPurchasesListFragment();
                    FragmentTransaction fragmentTransactionC  = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionC.replace(R.id.content_frame, fragmentC);
                    fragmentTransactionC.addToBackStack(null);
                    fragmentTransactionC.commit();
                    break;
                case "queryP":
                    MyAccountShoppingQuestionsListFragment fragmentQP = new MyAccountShoppingQuestionsListFragment();
                    FragmentTransaction fragmentTransactionQP  = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionQP.replace(R.id.content_frame, fragmentQP);
                    fragmentTransactionQP.addToBackStack(null);
                    fragmentTransactionQP.commit();
                    break;
                case "salesP":
                    MyAccountSalesListFragment fragmentS = new MyAccountSalesListFragment();
                    FragmentTransaction fragmentTransactionS  = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionS.replace(R.id.content_frame, fragmentS);
                    fragmentTransactionS.addToBackStack(null);
                    fragmentTransactionS.commit();
                    break;
                default:
                    HomeFragment fragment = new HomeFragment();
                    FragmentTransaction fragmentTransactionI = getSupportFragmentManager().beginTransaction();
                    fragmentTransactionI.replace(R.id.content_frame, fragment);
                    fragmentTransactionI.addToBackStack(null);
                    fragmentTransactionI.commit();
                break;
            }
            /*InicioFragment fragmentI = new InicioFragment();
            FragmentTransaction fragmentTransactionI = getSupportFragmentManager().beginTransaction();
            fragmentTransactionI.replace(R.id.content_frame, fragmentI);
            fragmentTransactionI.addToBackStack(null);
            fragmentTransactionI.commit();*/
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
                    /*InicioFragment fragment = new InicioFragment();
                    FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();*/
                }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String arg0) {
                // TODO Auto-generated method stub
                    Log.e("onclick buscador",searchView.getQuery().toString());
                    //itemName = searchView.getQuery().toString().replace(" ","");
                    itemName = searchView.getQuery().toString();
                    Log.e("onclick buscador",itemName);
                    Intent intent = new Intent(MainActivity.this,SearchActivity.class);
                    intent.putExtra("itemName",itemName);
                    startActivity(intent);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String arg0) {
                // TODO Auto-generated method stub
                Log.e("buscador",searchView.getQuery().toString());
                Bundle bundle = new Bundle();
                bundle.putString("nameCategory",searchView.getQuery().toString());
                /*MainFindCategoryFragment fragment = new MainFindCategoryFragment();
                fragment.setArguments(bundle);
                FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.commit();*/
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
            HomeFragment fragment = new HomeFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
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
        else if (id == R.id.nav_notifications)
        {
            NotificationFragment fragment = new NotificationFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
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
        else if (id == R.id.nav_create_store){
            createOrVisiteStore();
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
                (Request.Method.GET, Network.API_URL + "user/getPersonWithAuthorization/"+username, api_parameter, new Response.Listener<JSONObject>()
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

                        SharedPreferences.Editor settings = MainActivity.this.getSharedPreferences(LoginActivity.SESSION_USER, MainActivity.this.MODE_PRIVATE).edit();
                        settings.clear();
                        settings.commit();
                        Intent settingsIntent = new Intent(MainActivity.this, MainActivity.class);
                        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        settingsIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(settingsIntent);

                        /*NetworkResponse response = error.networkResponse;
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
                        }*/
                    }
                })
        {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", user.getPassword());
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

    public void end(String end) throws JSONException {
        Log.i("end",""+end);
        progressDialog.dismiss();
    }
    public void start(String start){
        Log.i("start",""+start);
        progressDialog.show();
    }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        switch (requestCode) {
            case ADD_PICTURES_TAG:
                super.onActivityResult(requestCode, resultCode, data);
                if (resultCode == RESULT_OK) {
                    Log.e("entro", "donde devia");
                    if (null != data) { // checking empty selection
                        if (null != data.getClipData()) { // checking multiple selection or not
                            Set<Yng_ItemImage> itemImageList = new HashSet<>();
                            for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                Uri uri = data.getClipData().getItemAt(i).getUri();
                                final InputStream imageStream;
                                try {
                                    imageStream = getContentResolver().openInputStream(uri);
                                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                    String encodedImage = encodeImage(selectedImage);
                                    if (i == 0) {
                                        user.setProfilePhoto("data:image/jpeg;base64," + encodedImage);
                                    } else {

                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                        } else {
                            Uri uri = data.getData();
                            final InputStream imageStream;
                            try {
                                imageStream = getContentResolver().openInputStream(uri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                String encodedImage = encodeImage(selectedImage);
                                user.setProfilePhoto("data:image/jpeg;base64," + encodedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        /*que hacer despues de mandar la nueva foto*/
                        Gson gson = new Gson();
                        String jsonBody = gson.toJson(user);
                        Log.e("usuario final", jsonBody);
                        requestUpdatePhotoProfile(Network.API_URL + "user/updateProfilePhoto",jsonBody);
                    }
                }
                break;
                case REQUEST_IMAGE_CAPTURE:
                    if (resultCode == RESULT_OK) {
                        Bundle extras = data.getExtras();
                        Bitmap imageBitmap = (Bitmap) extras.get("data");
                        String encodedImage = encodeImage(imageBitmap);
                        user.setProfilePhoto("data:image/jpeg;base64," + encodedImage);
                        Gson gson = new Gson();
                        String jsonBody = gson.toJson(user);
                        Log.e("usuario final", jsonBody);
                        requestUpdatePhotoProfile(Network.API_URL + "user/updateProfilePhoto",jsonBody);
                    }
                break;
        }
    }
    private String encodeImage(Bitmap bm)
    {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int bitmapByteCount=android.support.v4.graphics.BitmapCompat.getAllocationByteCount(bm);
        int byteCount=bitmapByteCount/1024;
        Log.e("tamaño byte",""+bitmapByteCount/1024);
        if(byteCount>0 && byteCount<600) {
            bm.compress(Bitmap.CompressFormat.JPEG,100,baos);
        }
        if(byteCount>=600 && byteCount<1800) {
            bm.compress(Bitmap.CompressFormat.JPEG,90,baos);
        }
        if(byteCount>=1800 && byteCount<3600) {
            bm.compress(Bitmap.CompressFormat.JPEG,80,baos);
        }
        if(byteCount>=3600 && byteCount<10800) {
            bm.compress(Bitmap.CompressFormat.JPEG,60,baos);
        }
        if(byteCount>=10800 && byteCount<32400) {
            bm.compress(Bitmap.CompressFormat.JPEG,40,baos);
        }
        if(byteCount>=32400 && byteCount<40000) {
            bm.compress(Bitmap.CompressFormat.JPEG,20,baos);
        }
        if(byteCount>=40000 && byteCount<50000) {
            bm.compress(Bitmap.CompressFormat.JPEG,10,baos);
        }
        if(byteCount>=50000) {
            bm.compress(Bitmap.CompressFormat.JPEG,1,baos);
        }

        byte[] b = baos.toByteArray();
        String encImage;
        encImage = Base64.encodeToString(b, Base64.DEFAULT);
        return encImage;
    }
    public void requestUpdatePhotoProfile(String url, String json){
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
                .addHeader("Authorization",user.getPassword())
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(responce.equals("save")) {
                            RunLoginService();
                        }else{
                            Toast.makeText(MainActivity.this,"Algo salio mal vuelva a intentarlo mas tarde",Toast.LENGTH_LONG).show();
                        }
                    }
                });

            }
        });
    }

    public void RunLoginService(){

        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.POST, Network.API_URL + "user/"+user.getUsername(), api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        try
                        {
                            Log.e("Response: " , response.toString());
                            SharedPreferences.Editor user = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE).edit();
                            user.putString("profilePhoto",response.getString("profilePhoto"));
                            user.commit();

                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
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
                params.put("X-API-KEY", Network.API_KEY);
                /*params.put("Authorization",
                        "Basic " + Base64.encodeToString(
                                (editEmail.getText().toString().trim() + ":" + editPassword.getText().toString().trim()).getBytes(), Base64.NO_WRAP)
                );*/
                return params;
            }
        };

        // Get a RequestQueue
        RequestQueue queue = MySingleton.getInstance(this.getApplicationContext()).getRequestQueue();

        //Used to mark the request, so we can cancel it on our onStop method
        postRequest.setTag(TAG);

        MySingleton.getInstance(this).addToRequestQueue(postRequest);
    }
    public void createOrVisiteStore(){
        progressDialog.show();

        JsonObjectRequest postRequest = new JsonObjectRequest
                (Request.Method.GET, Network.API_URL + "store/findByUsername/"+username, api_parameter, new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }

                        try
                        {
                            Log.e("Tienda por su nombre " , response.toString());
                            if(response!=null){
                                Yng_Store store = new Yng_Store();
                                Gson gson = new Gson();
                                store = gson.fromJson(String.valueOf(response), Yng_Store.class);
                                Log.e("nombre tienda",store.getName());
                                Intent intent = new Intent(MainActivity.this, ActivityStore.class);
                                intent.putExtra("store",store.getName());
                                startActivity(intent);
                            }else{
                                Intent settingsIntent = new Intent(MainActivity.this, CreateStoreActivity.class);
                                startActivity(settingsIntent);
                            }
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
                            Intent settingsIntent = new Intent(MainActivity.this, CreateStoreActivity.class);
                            startActivity(settingsIntent);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == MY_CAMERA_REQUEST_CODE) {

            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
                if (setting_address_edit_dialog != null && setting_address_edit_dialog.isShowing()) {
                    setting_address_edit_dialog.dismiss();
                }
            } else {

                Toast.makeText(this, "camera permission denied", Toast.LENGTH_LONG).show();

            }

        }
    }
    private void setupBadge() {
        SharedPreferences settings = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);
        if (textCartItemCount.getVisibility() != View.GONE) {
            textCartItemCount.setVisibility(View.GONE);
        }
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals("")) {

        }else{
            String jsonBody = "";
            getNumberQueries(Network.API_URL + "query/Number/"+settings.getString("username",""),jsonBody);
        }
    }

    public void  getNumberQueries(String url, String json){
        Log.e("empezar preguntas",url);
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
                .get()
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
                        Log.e("cantidad de preguntas",responce+"");
                        if(responce.equals("0")){
                            if (textCartItemCount.getVisibility() != View.GONE) {
                                textCartItemCount.setVisibility(View.GONE);
                            }
                        }else{
                            if (textCartItemCount.getVisibility() != View.VISIBLE) {
                                textCartItemCount.setVisibility(View.VISIBLE);
                            }
                            textCartItemCount.setText(responce+"");
                        }
                    }
                });

            }
        });
    }
}
