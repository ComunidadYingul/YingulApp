package com.valecom.yingul.Util;

import android.util.Log;
import android.util.Patterns;
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



    public boolean validarCadena(String cadena){
        if (cadena.trim().length() < 3)
        {
            return true;
        }else{
            return false;
        }
    }

    public boolean validarCadena(View view, String cadena){
        if (cadena.trim().length() < 3)
        {
            view.requestFocus();
            return true;
        }else{
            return false;
        }
    }

    public boolean valStringFiveChar(EditText view){
        if (view.getText().toString().trim().length() < 3)
        {
            view.setError("Campo requerido y mínimo 5 caracteres");
            view.requestFocus();
            return false;
        }else{
            return true;
        }
    }




    /********************************** INTRODUCCION DE EDIT TEXT *********************************/

    /************ NUMEROS *************/

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

    public boolean valNumber(EditText view){
        if (view.getText().toString().trim().length() <= 0)
        {
            view.setError("Campo requerido");
            view.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    public boolean valKilometer(EditText view){
        if (view.getText().toString().trim().length() <= 0)
        {
            view.setError("Kilómetros requerido");
            view.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    /************ CADENAS DE TEXTO ***********/

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
    public boolean valEmail(EditText email) {
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        if(!pattern.matcher(email.getText().toString()).matches()){
            email.requestFocus();
            email.setError("Correo electronico invalido");
            Log.e("message:---","error");
            return false;
        }else {
            Log.e("message:---","correcto");
            return true;
        }
    }

    public boolean valConfirmPassword(EditText pass1, EditText pass2) {
        if(!pass1.getText().toString().equals(pass2.getText().toString())){
            pass2.requestFocus();
            pass2.setError("Las contraseñas no coinciden");
            return false;
        }else {
            return true;
        }
    }
}
