package com.valecom.yingul.main.sell;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.valecom.yingul.R;
import com.valecom.yingul.main.LoginActivity;
import com.valecom.yingul.main.MainActivity;
import com.valecom.yingul.main.newUserUbicationEditPersonalInfo.NewUserUbicationEditPersonalInfoActivity;
import com.valecom.yingul.model.Yng_Barrio;
import com.valecom.yingul.model.Yng_Category;
import com.valecom.yingul.model.Yng_City;
import com.valecom.yingul.model.Yng_Country;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_ItemCategory;
import com.valecom.yingul.model.Yng_ItemImage;
import com.valecom.yingul.model.Yng_Motorized;
import com.valecom.yingul.model.Yng_Product;
import com.valecom.yingul.model.Yng_Property;
import com.valecom.yingul.model.Yng_Province;
import com.valecom.yingul.model.Yng_Service;
import com.valecom.yingul.model.Yng_Ubication;
import com.valecom.yingul.model.Yng_User;
import com.valecom.yingul.network.Network;
import com.google.gson.Gson;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;

public class SellActivity extends AppCompatActivity {
    Toolbar toolbar;
    Yng_Item item;
    Yng_Product product;
    Yng_Motorized motorized;
    Yng_Property property;
    Yng_Ubication ubication, userUbication;
    Yng_Country country;
    Yng_Province province;
    Yng_City city;
    MaterialDialog progressDialog;
    Yng_Service service;
    String includes, notInclude,aditionalDescription, typePriceService;
    Yng_User user;
    Yng_Barrio barrio;
    //private Set<Yng_ItemCategory> itemCategoryList = new HashSet<>();
    Set<Yng_ItemCategory> itemCategoryList = new HashSet<>();

    static final int ITEM_PICKER_TAG = 1;
    static final int ADD_PICTURES_TAG = 3;

    String TAG="OkHttpConection";
    public static final MediaType JSON= MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Vender");
        item = new Yng_Item();
        product = new Yng_Product();
        motorized = new Yng_Motorized();
        property = new Yng_Property();
        ubication = new Yng_Ubication();
        service = new Yng_Service();
        country = new Yng_Country();
        province = new Yng_Province();
        city = new Yng_City();
        user = new Yng_User();
        barrio = new Yng_Barrio();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*usuario logeado*/
        SharedPreferences settings = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE);

        //Means user is not logged in
        if (settings == null || settings.getInt("logged_in", 0) == 0 || settings.getString("api_key", "").equals(""))
        {
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
            return;
        }else{
            user.setUsername(settings.getString("username",""));
            /*para obtener la ubicacion del usuario*/
            if(settings.getString("yng_Ubication","").equals("null")){
                userUbication=null;
            }else{
                Gson gson = new Gson();
                userUbication = gson.fromJson(settings.getString("yng_Ubication","") , Yng_Ubication.class);
                Log.e("id de ubicacion",userUbication.getUbicationId().toString());
            }
            /*fin de la ubicacion del usuario*/
            user.setPhone(settings.getString("phone",""));
            user.setDocumentType(settings.getString("documentType",""));
            user.setDocumentNumber(settings.getString("documentNumber",""));
            user.setPassword(settings.getString("password",""));
            user.setYng_Ubication(new Yng_Ubication());
            Log.e("authorization",user.getPassword());
        }

        /**/

        SellItemSetItemTypeFragment itemTypeFragment = new SellItemSetItemTypeFragment();
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, itemTypeFragment);
        fragmentTransaction.commit();

        progressDialog = new MaterialDialog.Builder(this)
                .title(R.string.progress_dialog)
                .content(R.string.please_wait)
                .cancelable(false)
                .progress(true, 0).build();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    public void setItemType(String type){
        toolbar.setTitle("");
        item.setType(type);
        SellItemAddPicturesFragment itemAddPicturesFragment = new SellItemAddPicturesFragment();
        itemAddPicturesFragment.type=type;
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, itemAddPicturesFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void setWithoutPictures(String picture){
        item.setPrincipalImage(picture);
        SellItemSetTitleFragment itemSetTitleFragment = new SellItemSetTitleFragment();
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, itemSetTitleFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setItemTitle(String text) {
        item.setName(text);
        if(item.getType()=="Service"){
            SellItemSetDescriptionFragment itemSetCategory = new SellItemSetDescriptionFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, itemSetCategory);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
            /*SellItemSetCategoryFragment itemSetCategory = new SellItemSetCategoryFragment();
            itemSetCategory.type=item.getType();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, itemSetCategory);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/
        }else if(item.getType()=="Product" || item.getType()=="Motorized" || item.getType()=="Property"){
            SellItemSetDescription2Fragment itemSetCategory = new SellItemSetDescription2Fragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, itemSetCategory);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public void setCategory(String father){
        //setear una categoria del item
        Yng_Category category = new Yng_Category();
        category.setCategoryId(Long.parseLong(father));
        Yng_ItemCategory itemCategory = new Yng_ItemCategory();
        itemCategory.setCategory(category);
        itemCategoryList.add(itemCategory);
        item.setItemCategory(itemCategoryList);
        Log.e("Eddy",item.toString());
        //setear una categoria del item
        SellItemSetSubCategoryFragment itemSetSubCategory = new SellItemSetSubCategoryFragment();
        itemSetSubCategory.father=father;
        itemSetSubCategory.type=item.getType();
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, itemSetSubCategory);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void setTypePrice(String typePrice){
        if(typePrice.equals("0")){
            typePriceService="Con precio total";
            SellItemSetPriceFragment itemSetPrice = new SellItemSetPriceFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, itemSetPrice);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else{
            if(userUbication==null||user.getPhone().equals("null")||user.getDocumentNumber().equals("null")||user.getDocumentType().equals("null")||user.getPhone().equals("")||user.getDocumentNumber().equals("")||user.getDocumentType().equals("")){
                Intent intent = new Intent(SellActivity.this, NewUserUbicationEditPersonalInfoActivity.class);
                intent.putExtra("data", user);
                startActivityForResult(intent, ITEM_PICKER_TAG);
            }else {

                typePriceService = "Con precio a convenir";
                item.setMoney("ARS");
                SellItemAddUbicationFragment itemAddUbication = new SellItemAddUbicationFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                itemAddUbication.type = item.getType();
                fragmentTransaction.replace(R.id.content_frame, itemAddUbication);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }
    public void setTypeCondition(String typePrice){
        if(typePrice.equals("0")){
            //item.setCondition("Nuevo");
            product.setProductCondition("Nuevo");
        }else{
            //item.setCondition("Used");
            product.setProductCondition("Usado");
        }
        SellItemSetQuantityFragment itemSetQuantity = new SellItemSetQuantityFragment();
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, itemSetQuantity);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    public void setItemPrice(double itemPrice){

        Log.e("Precio", itemPrice+"");
        item.setPrice(itemPrice);
        item.setPriceDiscount(0);
        item.setPriceNormal(0);

        if(userUbication==null||user.getPhone().equals("null")||user.getDocumentNumber().equals("null")||user.getDocumentType().equals("null")||user.getPhone().equals("")||user.getDocumentNumber().equals("")||user.getDocumentType().equals("")){
            Intent intent = new Intent(SellActivity.this, NewUserUbicationEditPersonalInfoActivity.class);
            intent.putExtra("data", user);
            startActivityForResult(intent, ITEM_PICKER_TAG);
        }else {

            if (item.getType() == "Product") {
                SellItemSetDeliveryFragment fragment = new SellItemSetDeliveryFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            } else if (item.getType() == "Property" || item.getType() == "Service") {
                SellItemAddUbicationFragment itemAddUbication = new SellItemAddUbicationFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                itemAddUbication.type = item.getType();
                fragmentTransaction.replace(R.id.content_frame, itemAddUbication);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }else if (item.getType() == "Motorized") {
                SellItemInfoReserveFragment fragment = new SellItemInfoReserveFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        }
    }

    public void setItemAditionalDescription() {
        //aditionalDescription=description;
        /*if(userUbication==null||user.getPhone().equals("null")||user.getDocumentNumber().equals("null")||user.getDocumentType().equals("null")||user.getPhone().equals("")||user.getDocumentNumber().equals("")||user.getDocumentType().equals("")){
            Intent intent = new Intent(SellActivity.this, NewUserUbicationEditPersonalInfoActivity.class);
            intent.putExtra("data", user);
            startActivityForResult(intent, ITEM_PICKER_TAG);
        }else{*/
            if(item.getType()=="Service"){
                SellItemSetCategoryFragment itemSetCategory = new SellItemSetCategoryFragment();
                itemSetCategory.type=item.getType();
                FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, itemSetCategory);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                /*SellItemAddUbicationFragment itemAddUbication = new SellItemAddUbicationFragment();
                FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
                itemAddUbication.type=item.getType();
                fragmentTransaction.replace(R.id.content_frame, itemAddUbication);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();*/
            }else if(item.getType()=="Product" || item.getType()=="Motorized" || item.getType()=="Property"){
                SellItemSetCategoryFragment itemSetCategory = new SellItemSetCategoryFragment();
                itemSetCategory.type=item.getType();
                FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_frame, itemSetCategory);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        //}
    }

    public void setUbication(String setUbication){
        if(setUbication.equals("null")){
            SellItemUbicationSetCountryFragment itemSetCountry = new SellItemUbicationSetCountryFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, itemSetCountry);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else{
            ubication.setUbicationId(Long.parseLong(setUbication));
            item.setYng_Ubication(ubication);
        }
    }
    public void setCountry(String countryId, String name){
        country.setCountryId(Integer.parseInt(countryId));
        country.setName(name);
        ubication.setYng_Country(country);
        SellItemUbicationSetProvinceFragment itemSetProvince = new SellItemUbicationSetProvinceFragment();
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        itemSetProvince.countryId=countryId;
        fragmentTransaction.replace(R.id.content_frame, itemSetProvince);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void setProvince(String provinceId, String name){
        province.setProvinceId(Integer.parseInt(provinceId));
        province.setName(name);
        ubication.setYng_Province(province);
        SellItemUbicationSetCityFragment itemSetCity = new SellItemUbicationSetCityFragment();
        FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
        itemSetCity.provinceId=provinceId;
        fragmentTransaction.replace(R.id.content_frame, itemSetCity);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
    public void setCity(String cityId, String name){
        city.setCityId(Integer.parseInt(cityId));
        city.setName(name);
        ubication.setYng_City(city);
        if(item.getType()=="Motorized" || item.getType()=="Property"){
            SellItemAddContactFragment itemSetUbicationDetail = new SellItemAddContactFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, itemSetUbicationDetail);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else{
            SellItemUbicationSetDetailFragment itemSetUbicationDetail = new SellItemUbicationSetDetailFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, itemSetUbicationDetail);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public void setUbicationDetail(String street, String number){
        ubication.setStreet(street);
        ubication.setNumber(number);

        if(item.getType()=="Property"){
            SellItemAddContactFragment fragment = new SellItemAddContactFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else{
            SellItemAddSummaryFragment itemSetSummary = new SellItemAddSummaryFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.content_frame, itemSetSummary);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }
    public void initSummary(){
        if(item.getType()=="Service"){
            SellItemConfirmFragment itemSetConfirm = new SellItemConfirmFragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            itemSetConfirm.ubication=ubication;
            itemSetConfirm.item=item;
            itemSetConfirm.includes=includes;
            itemSetConfirm.noInclude=notInclude;
            itemSetConfirm.typePrice=typePriceService;
            itemSetConfirm.aditionalDescription=aditionalDescription;
            fragmentTransaction.replace(R.id.content_frame, itemSetConfirm);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if(item.getType()=="Product"){
            SellItemConfirm2Fragment itemSetConfirm = new SellItemConfirm2Fragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            itemSetConfirm.ubication=ubication;
            itemSetConfirm.item=item;
            itemSetConfirm.includes=includes;
            itemSetConfirm.typePrice=typePriceService;
            fragmentTransaction.replace(R.id.content_frame, itemSetConfirm);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if(item.getType()=="Motorized"){
            SellItemConfirm3Fragment itemSetConfirm = new SellItemConfirm3Fragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            itemSetConfirm.ubication=ubication;
            itemSetConfirm.item=item;
            itemSetConfirm.includes=includes;
            itemSetConfirm.typePrice=typePriceService;
            fragmentTransaction.replace(R.id.content_frame, itemSetConfirm);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }else if(item.getType()=="Property"){
            SellItemConfirm4Fragment itemSetConfirm = new SellItemConfirm4Fragment();
            FragmentTransaction fragmentTransaction  = getSupportFragmentManager().beginTransaction();
            itemSetConfirm.ubication=ubication;
            itemSetConfirm.item=item;
            itemSetConfirm.includes=includes;
            itemSetConfirm.typePrice=typePriceService;
            fragmentTransaction.replace(R.id.content_frame, itemSetConfirm);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
    String desc;
    public void confirm(){

        desc = "";
        item.setYng_Ubication(ubication);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(item);
        Log.e("Ubication property",jsonBody);
        item.setUser(user);
        if(item.getType().equals("Service")){
            if(includes.trim().length()!=0){
                desc += "Incluye: "+includes+" - ";
            }
            if(notInclude.trim().length()!=0){
                desc += "No incluye: "+notInclude+" - ";
            }
            if(aditionalDescription.trim().length()!=0){
                desc += "Además: "+aditionalDescription+".";
            }
            item.setDescription(desc);
        }else{
            item.setDescription(aditionalDescription);
        }
        switch (item.getType()){
            case "Service":
                service.setYng_Item(item);
                RunGetInvoiceService();
                break;
            case "Product":
                product.setYng_Item(item);
                product.getYng_Item().setYng_Ubication(userUbication);
                product.setProductPaymentMethod("Aceptar pagos solo por Yingul Pay");
                product.setProductSaleConditions("Fijo");

                RunGetInvoiceProduct();
                break;
            case "Motorized":
                motorized.setYng_Item(item);
                ubication.setYng_Barrio(barrio);
                item.setYng_Ubication(ubication);
                RunGetInvoiceMotorized();
                break;
            case "Property":
                property.setYng_Item(item);
                ubication.setYng_Barrio(barrio);
                item.setYng_Ubication(ubication);
                RunGetInvoiceProperty();
                break;
            default:
                break;
        }

    }

    public void openGalery(){
        /*AlertDialog.Builder myAlertDialog = new AlertDialog.Builder(
                this);
        myAlertDialog.setTitle("Cargar imágenes");
        myAlertDialog.setMessage("¿Cómo quieres cargar tu imagen?");

        myAlertDialog.setPositiveButton("Galeria",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);
                    }
                });

        myAlertDialog.setNegativeButton("Cámara",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {

                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(f));
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), 1);

                    }
                });
        myAlertDialog.show();*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), ADD_PICTURES_TAG);

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        switch (requestCode) {
            case ITEM_PICKER_TAG:
                if (resultCode == RESULT_OK) {
                    Yng_User newUser = (Yng_User)data.getSerializableExtra("data");
                    user = newUser;
                    userUbication = newUser.getYng_Ubication();
                    Gson gson = new Gson();
                    String jsonBody = gson.toJson(newUser.getYng_Ubication());
                    Log.e("ubica:---",jsonBody);
                    SharedPreferences.Editor user = getSharedPreferences(LoginActivity.SESSION_USER, MODE_PRIVATE).edit();
                    user.putString("yng_Ubication",jsonBody);
                    user.putString("phone",newUser.getPhone());
                    user.putString("documentType",newUser.getDocumentType());
                    user.putString("documentNumber",newUser.getDocumentNumber());
                    user.commit();
                }
                break;
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
                                        item.setPrincipalImage("data:image/jpeg;base64," + encodedImage);
                                    } else {

                                        Yng_ItemImage itemImage = new Yng_ItemImage();
                                        itemImage.setImage("data:image/jpeg;base64," + encodedImage);
                                        itemImageList.add(itemImage);
                                    }
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }
                            }
                            item.setItemImage(itemImageList);
                        } else {
                            Uri uri = data.getData();
                            final InputStream imageStream;
                            try {
                                imageStream = getContentResolver().openInputStream(uri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                String encodedImage = encodeImage(selectedImage);
                                item.setPrincipalImage("data:image/jpeg;base64," + encodedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        SellItemSetTitleFragment itemSetTitleFragment = new SellItemSetTitleFragment();
                        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, itemSetTitleFragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
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

    public void setItemDelivery(String productoPagoEnvio) {
        item.setProductPagoEnvio(productoPagoEnvio);
    }

    public void RunGetInvoiceService()
    {
        progressDialog.show();

        /*RequestQueue requestQueue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(service);
        Log.e("Eddy json",jsonBody);
        final String mRequestBody = jsonBody;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Network.API_URL + "sell/service", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("LOG_RESPONSE", response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOG_RESPONSE1", error.toString());
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return mRequestBody == null ? null : mRequestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", mRequestBody, "utf-8");
                    return null;
                }
            }

            @Override
            protected Response<String> parseNetworkResponse(NetworkResponse response) {
                String responseString = "";
                if (response != null) {
                    responseString = String.valueOf(response.statusCode);
                    Log.e("Respuesta",responseString);
                }
                return Response.success(responseString, HttpHeaderParser.parseCacheHeaders(response));
            }
        };

        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue.add(stringRequest);*/

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(service);
        Log.e("Eddy json",jsonBody);
        final String mRequestBody = jsonBody;

        Toast.makeText(this,"Enviando",Toast.LENGTH_SHORT).show();

        requestArrayPost(Network.API_URL + "sell/service",mRequestBody);
    }

    private void RunGetInvoiceProduct() {
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(product);
        Log.e("Eddy json",jsonBody);
        final String mRequestBody = jsonBody;

        Toast.makeText(this,"Enviando",Toast.LENGTH_SHORT).show();

        requestArrayPost(Network.API_URL + "sell/product",mRequestBody);

    }

    private void RunGetInvoiceMotorized() {
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(motorized);
        Log.e("Eddy json",jsonBody);
        final String mRequestBody = jsonBody;

        Toast.makeText(this,"Enviando",Toast.LENGTH_SHORT).show();

        requestArrayPost(Network.API_URL + "sell/motorized",mRequestBody);

    }

    private void RunGetInvoiceProperty() {
        progressDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        Gson gson = new Gson();
        String jsonBody = gson.toJson(property);
        Log.e("Eddy json",jsonBody);
        final String mRequestBody = jsonBody;

        Toast.makeText(this,"Enviando",Toast.LENGTH_SHORT).show();

        requestArrayPost(Network.API_URL + "sell/property",mRequestBody);

    }

    /**************************** METODO DANNY *******************************/
    public void  requestArrayPost(String url, String json){
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
                Log.i("responce:------------",""+responce);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (progressDialog != null && progressDialog.isShowing()) {
                            // If the response is JSONObject instead of expected JSONArray
                            progressDialog.dismiss();
                        }
                        if(responce.equals("save")) {
                            Toast.makeText(SellActivity.this, "Publicación registrada", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(SellActivity.this, MainActivity.class);
                            startActivity(intent);
                        }else
                            Toast.makeText(SellActivity.this,"No se pudo registrar, intente nuevamente.",Toast.LENGTH_LONG).show();
                    }
                });

            }
        });
    }


}
