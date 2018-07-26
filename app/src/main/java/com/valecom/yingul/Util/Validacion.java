package com.valecom.yingul.Util;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
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
            view.setError("Campo requerido y mínimo 3 caracteres");
            view.requestFocus();
            return false;
        }else{
            return true;
        }
    }




    /********************************** INTRODUCCION DE EDIT TEXT *********************************/

    /************ NUMEROS *************/

    public boolean valNumDig(EditText view,int cantDid){
        if (view.getText().toString().trim().length() < cantDid || view.getText().toString().trim().length() > cantDid)
        {
            view.setError("Campo requerido "+cantDid+" dígitos");
            view.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    public boolean valNumMayorADig(EditText view,int cantDid){
        if (view.getText().toString().trim().length() < cantDid)
        {
            view.setError("Campo requerido, minimo "+cantDid+" dígitos");
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
        }else if(view.getText().toString().trim().length() > 6) {
            view.setError("El valor no debe ser mayor a 999999");
            view.requestFocus();
            return false;
        }else{
            return true;
        }
    }

    public boolean valMotorizedYear(EditText view){

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
        Date date = new Date();

        String fecha = dateFormat.format(date);
        int year = Integer.valueOf(fecha);

        if (view.getText().toString().trim().length() <= 0)
        {
            view.setError("Ingrese año");
            view.requestFocus();
            return false;
        }else if(view.getText().toString().trim().length() != 4) {
            view.setError("El año debe comprender entre 1900 y "+year);
            view.requestFocus();
            return false;
        }else if(Integer.valueOf(String.valueOf(view.getText().toString())) < 1900 || Integer.valueOf(String.valueOf(view.getText().toString())) > year) {
            view.setError("El año debe comprender entre 1900 y "+year);
            view.requestFocus();
            return false;
        }
        else{
            return true;
        }

        /*if(val.validarNumero(editMotorizedYear,motorizedYear)){
            editMotorizedYear.setError("Ingrese año");
        }else if(Integer.parseInt(String.valueOf(editMotorizedYear)) > year){
            editMotorizedYear.setError("El año no debe superar al actual");
        }
        else if(editMotorizedYear.length() < 4){
            editMotorizedYear.setError("Ingrese un año valido");
        }else if(Integer.parseInt(String.valueOf(editMotorizedYear)) < 1900){
            editMotorizedYear.setError("Ingrese un año entre 1900 y "+year);
        }*/

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
