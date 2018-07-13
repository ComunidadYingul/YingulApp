package com.valecom.yingul.main.createStore;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_ItemImage;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreateStoreSetBannerFragment extends Fragment {

    private Button buttonSetLogo;
    private LinearLayout layoutSpecification;
    private TextView txtTitle,textSpecifications;
    static final int ADD_PICTURES_TAG = 3;

    public CreateStoreSetBannerFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_create_store_set_logo, container, false);
        buttonSetLogo = (Button) v.findViewById(R.id.buttonSetLogo);
        layoutSpecification = (LinearLayout) v.findViewById(R.id.layoutSpecification);
        txtTitle = (TextView) v.findViewById(R.id.txtTitle);
        textSpecifications = (TextView) v.findViewById(R.id.textSpecifications);
        textSpecifications.setText("El banner debe medir 1170 px X 300 px descargar ejemplo");
        txtTitle.setText("Sube una portada (banner) para tu tienda");
        buttonSetLogo.setText("Subir Banner y Continuar");
        layoutSpecification.setVisibility(View.VISIBLE);

        layoutSpecification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewIntent =
                        new Intent("android.intent.action.VIEW",
                                Uri.parse("https://s3-us-west-2.amazonaws.com/jsa-s3-bucketimage/image/store/sampleBanner.jpg"));
                startActivity(viewIntent);
            }
        });

        buttonSetLogo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), ADD_PICTURES_TAG);
            }
        });
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
                                    imageStream = getActivity().getContentResolver().openInputStream(uri);
                                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                    String encodedImage = encodeImage(selectedImage);
                                    if (i == 0) {
                                        ((CreateStoreActivity)getActivity()).store.setBannerImage("data:image/jpeg;base64," + encodedImage);
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
                                imageStream = getActivity().getContentResolver().openInputStream(uri);
                                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                                String encodedImage = encodeImage(selectedImage);
                                ((CreateStoreActivity)getActivity()).store.setBannerImage("data:image/jpeg;base64," + encodedImage);
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        CreateStoreSetVideoFragment fragment = new CreateStoreSetVideoFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.content_frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }
                break;
        }
    }
    private String encodeImage(Bitmap bm) {
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

}
