package com.valecom.yingul.Util;

import android.view.View;

/**
 * Created by gonzalo on 14-06-18.
 */

public class Validacion {

    public Validacion() {
    }

    public boolean validarNumber(String number){
        if (number.trim().length() == 0)
        {
            return true;
        }else{
            return false;
        }
    }

    public boolean validarNumero(View view, String number){
        if (number.trim().length() == 0 || number.trim().equals("0"))
        {
            view.requestFocus();
            return true;
        }else{
            return false;
        }
    }

    public boolean validarCadena(String cadena){
        if (cadena.trim().length() < 5)
        {
            return true;
        }else{
            return false;
        }
    }
}
