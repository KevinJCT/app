package com.example.loginuta;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONArray;

import cz.msebera.android.httpclient.Header;

public class Cuenta extends AppCompatActivity {

    EditText edtCedula, edtNombre, edtApellido, edtFecNac, edtDireccion, edtTelefono1, edtTelefono2, edtPassword;
    Button btnGuardar;
    Spinner spnciudades;
    AsyncHttpClient cliente;
    String URL_LISTAR_CIUDAD="http://192.168.101.3/agiles/listar.php";
    String URL_GUARDAR = "http://192.168.101.3/agiles/guardar.php";
    Calendar calendar = Calendar.getInstance();
    String CED_TRA,NOM_TRA,APE_TRA,FEC_NAC_TRA,DIR_TRA,TEL1_TRA, TEL2_TRA, NAC_TRA, CON_TRA;
    ArrayList<Ciudad> lista = new ArrayList<Ciudad>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        edtCedula = (EditText) findViewById(R.id.edtCedula);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtApellido = (EditText) findViewById(R.id.edtApellido);
        edtFecNac = (EditText) findViewById(R.id.edtFecNac);
        edtDireccion = (EditText) findViewById(R.id.edtDireccion);
        edtTelefono1 = (EditText) findViewById(R.id.edtTelefono1);
        edtTelefono2 = (EditText) findViewById(R.id.edtTelefono2);
        edtPassword = (EditText) findViewById(R.id.edtPassword);

        btnGuardar = (Button) findViewById(R.id.btnGuardar);

        cliente = new AsyncHttpClient();
        spnciudades = (Spinner) findViewById(R.id.spnciudades);

        llenarSpinner();

        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        edtFecNac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(Cuenta.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month+1;
                        String date = year+"/"+month+"/"+day;
                        edtFecNac.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });



        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validar()) {
                    NAC_TRA = ideNacionalidad();
                    Toast.makeText(Cuenta.this,NAC_TRA,Toast.LENGTH_LONG).show();
                    ejecutarServicio(URL_GUARDAR);
                    Intent i = new Intent(getApplicationContext(), Profesion.class);
                    i.putExtra("CED_TRA",CED_TRA);
                    startActivity(i);
                }else{
                    Toast.makeText(Cuenta.this,"Campos no validos",Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    public String ideNacionalidad(){
        String ide_ciu = "";
        for(Ciudad ciudad : lista){
            if (spnciudades.getSelectedItem().toString().equals(ciudad.getNombre())){
                ide_ciu = ciudad.getId();
            }
        }
        return ide_ciu;
    }
    public boolean  validar(){
        boolean logico = true;

        CED_TRA = edtCedula.getText().toString();
        NOM_TRA = edtNombre.getText().toString();
        APE_TRA=edtApellido.getText().toString();
        FEC_NAC_TRA=edtFecNac.getText().toString();
        DIR_TRA = edtDireccion.getText().toString();
        TEL1_TRA = edtTelefono1.getText().toString();
        TEL2_TRA = edtTelefono2.getText().toString();
        CON_TRA = edtPassword.getText().toString();

        if (CED_TRA.isEmpty() & CED_TRA.length()!=10){
            edtCedula.setError("Campo vacio o error de caracteres ");
            logico = false;
        }
        if (NOM_TRA.isEmpty() & NOM_TRA.length() < 20){
            edtNombre.setError("Campo vacio o error de caracteres");
            logico = false;
        }
        if (APE_TRA.isEmpty() & APE_TRA.length() < 20){
            edtApellido.setError("Campo vacio o error de caracteres");
            logico = false;
        }
        if (FEC_NAC_TRA.isEmpty()){
            edtFecNac.setError("Campo vacio");
            logico = false;
        }

        if  (DIR_TRA.isEmpty() & DIR_TRA.length()<20){
            edtDireccion.setError("Campo vacio o error de caracteres");
            logico = false;
        }

        if (TEL1_TRA.isEmpty() & TEL1_TRA.length() != 10){
            edtTelefono1.setError("Campo vacio o error de caracteres");
            logico = false;
        }
        if (TEL2_TRA.isEmpty() & TEL1_TRA.length() != 10){
            edtTelefono2.setError("Campo vacio o error de caracteres");
            logico = false;
        }
        if  (!validatepass(CON_TRA)){
            logico = false;
        }

        return logico;

    }


    public boolean validatepass(String password) {

        boolean retorno =true;
        // check for pattern
        Pattern uppercase = Pattern.compile("[A-Z]");
        Pattern lowercase = Pattern.compile("[a-z]");
        Pattern digit = Pattern.compile("[0-9]");

        // if lowercase character is not present
        if (!lowercase.matcher(password).find()) {
            edtPassword.setError("Error");
            retorno =false;
        }

        // if uppercase character is not present
        if (!uppercase.matcher(password).find()) {
            edtPassword.setError("Error");
            retorno =false;
        }

        // if digit is not present
        if (!digit.matcher(password).find()) {
            edtPassword.setError("Error");
            retorno =false;
        }
        // if password length is less than 8
        if (password.length() < 8 || password.length()>15) {
            edtPassword.setError("Error");
            retorno =false;
        }

        return  retorno;

    }

    public void llenarSpinner(){
        cliente.post(URL_LISTAR_CIUDAD, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if (statusCode==200){
                    cargarSpinner(new String(responseBody));
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
    Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void cargarSpinner(String respuesta){

        try{
            JSONArray jsonArray = new JSONArray(respuesta);
            for (int i=0; i< jsonArray.length(); i++){
                Ciudad c = new Ciudad();
                c.setId(jsonArray.getJSONObject(i).getString("IDE_CIU"));
                c.setNombre(jsonArray.getJSONObject(i).getString("NOM_CIU"));
                lista.add(c);

            }

            ArrayAdapter<Ciudad> adapterCiudad = new ArrayAdapter<Ciudad>(this, android.R.layout.simple_dropdown_item_1line,lista);
            spnciudades.setAdapter(adapterCiudad);
        }catch (Exception e){
            Toast.makeText(Cuenta.this,e.getMessage(),Toast.LENGTH_SHORT).show();
        }


    }

    private void ejecutarServicio(String URL){
        StringRequest StringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                parametros.put("CED_TRA",CED_TRA);
                parametros.put("NOM_TRA",NOM_TRA);
                parametros.put("APE_TRA",APE_TRA );
                parametros.put("FEC_NAC_TRA",FEC_NAC_TRA);
                parametros.put("DIR_TRA",DIR_TRA);
                parametros.put("TEL1_TRA",TEL1_TRA);
                parametros.put("TEL2_TRA",TEL2_TRA );
                parametros.put("NAC_TRA", NAC_TRA);
                parametros.put("CON_TRA",CON_TRA);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Cuenta.this);
        requestQueue.add(StringRequest);
    }

/*
    public Connection connectionBD(){
        Connection cnn = null;
        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            Class.forName("net.sourceforge.jtds.jdbc.Driver").newInstance();
            cnn= DriverManager.getConnection("jdbc:jtds:sqlserver://IDKTechnologies.mssql.somee.com;databaseName=IDKTechnologies;user=Christian13TL_SQLLogin_1;password=p3q2pj7nsb;");
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return cnn;
    }

    public void consultarInformacionEmpleado(){
        try {
            Statement statement = connectionBD().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * FROM Empleados WHERE CED_EMP='1804373767'");
            //ResultSet resultSet = statement.executeQuery("SELECT * FROM Empleados WHERE CED_EMP='"+edtCedula.getText().toString()+"'");
            if(resultSet.next()){
                jtxtCedula.setText(resultSet.getString(1));
                edtNombre.setText(resultSet.getString(2));
                edtApellido.setText(resultSet.getString(3));
                jtxtFecNac.setText(resultSet.getString(4));
                edtDireccion.setText(resultSet.getString(5));
                edtTelefono.setText(resultSet.getString(6));
                edtCorreo.setText(resultSet.getString(7));
                //jtxtESpecialidad.setText(resultSet.getString(8));
            }
        }catch (Exception e){
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    private void guardarInformacion(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.100.26:80/PMA/modificarEmpleado.php", onResponse -> Toast.makeText(getApplicationContext(),"OPERACION EXITOSA",Toast.LENGTH_LONG).show(), error -> Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show()){

            @Override
            protected Map<String,String> getParams() {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("CED_EMP",jtxtCedula.getText().toString());
                parametros.put("NOM_EMP",edtNombre.getText().toString());
                parametros.put("APE_EMP",edtApellido.getText().toString());
                parametros.put("DIR_EMP",edtDireccion.getText().toString());
                parametros.put("TEL_EMP",edtTelefono.getText().toString());
                parametros.put("COR_EMP",edtCorreo.getText().toString());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
*/
}