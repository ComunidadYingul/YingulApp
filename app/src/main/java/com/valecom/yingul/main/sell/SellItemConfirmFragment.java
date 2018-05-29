package com.valecom.yingul.main.sell;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.valecom.yingul.R;
import com.valecom.yingul.model.Yng_Country;
import com.valecom.yingul.model.Yng_Item;
import com.valecom.yingul.model.Yng_Ubication;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellItemConfirmFragment extends Fragment {

    Button buttonSetUbicationDetail;
    EditText editTypePrice,editIncluded,editNoIncluded,editAditionalDescription,editUbication;
    Yng_Item item = new Yng_Item();
    Yng_Ubication ubication = new Yng_Ubication();
    String typePrice, includes, noInclude,aditionalDescription;

    public SellItemConfirmFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sell_item_confirm, container, false);
        buttonSetUbicationDetail = (Button) v.findViewById(R.id.buttonSetUbicationDetail);
        editTypePrice = (EditText) v.findViewById(R.id.editTypePrice);
        editIncluded = (EditText) v.findViewById(R.id.editIncluded);
        editNoIncluded = (EditText) v.findViewById(R.id.editNoIncluded);
        editAditionalDescription = (EditText) v.findViewById(R.id.editAditionalDescription);
        editUbication = (EditText) v.findViewById(R.id.editUbication);
        editTypePrice.setText(typePrice);
        editIncluded.setText(includes);
        editNoIncluded.setText(noInclude);
        editUbication.setText(ubication.getYng_Country().getName()+" . "+ubication.getYng_Province().getName()+", "+ubication.getYng_City().getName()+" - "+ubication.getStreet()+" ° "+ubication.getNumber());
        //editUbication.setText(item.getYng_Ubication().getYng_Country().getName()+" . "+item.getYng_Ubication().getYng_Province().getName()+", "+item.getYng_Ubication().getYng_City().getName()+" - "+item.getYng_Ubication().getStreet()+" ° "+item.getYng_Ubication().getNumber());
        editAditionalDescription.setText(aditionalDescription);
        buttonSetUbicationDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SellActivity)getActivity()).confirm();
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
}
