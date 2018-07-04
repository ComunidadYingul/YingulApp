package com.valecom.yingul.Util;

import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.util.regex.Pattern;

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

    public boolean valNumFourDig(EditText view){
        if (view.getText().toString().trim().length() < 4)
        {
            view.setError("Campo requerido...");
            view.requestFocus();
            return true;
        }else{
            return false;
        }
    }

    public boolean valNumDig(EditText view,int cantDid){
        if (view.getText().toString().trim().length() < cantDid)
        {
            view.setError("Campo requerido y mínimo "+cantDid+" dígitos");
            view.requestFocus();
            return false;
        }else{
            return true;
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

    public boolean validarCadena(View view, String cadena){
        if (cadena.trim().length() < 5)
        {
            view.requestFocus();
            return true;
        }else{
            return false;
        }
    }

    public boolean valStringFiveChar(EditText view){
        if (view.getText().toString().trim().length() < 5)
        {
            view.setError("Campo requerido y mínimo 5 caracteres");
            view.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    public boolean valCantString(EditText view,int cantDig){
        if (view.getText().toString().trim().length() < cantDig)
        {
            view.setError("Campo requerido y mínimo "+cantDig+" caracteres");
            view.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    public boolean valExpireDate(EditText view){
        String REGEX_NUMEROS = "^(0[0-9]||1[0-2])/([2-9][0-9]||1[8-9])$";
        Pattern patron = Pattern.compile(REGEX_NUMEROS);
        if (!patron.matcher(view.getText().toString().trim()).matches())
        {
            view.setError("Campo requerido y debe tener formato mes/año");
            view.requestFocus();
            return false;
        }else{
            return true;
        }
    }
}
