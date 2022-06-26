package com.example.loginuta;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

public class Cuenta extends AppCompatActivity {

    EditText edtNombre, edtApellido, edtDireccion, edtTelefono,edtCorreo;
    Button btnGuardar;
    TextView jtxtCedula, jtxtFecNac, jtxtESpecialidad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cuenta);

        jtxtCedula = (TextView) findViewById(R.id.jtxtCedula);
        btnGuardar = (Button) findViewById(R.id.btnGuardar);
        edtNombre = (EditText) findViewById(R.id.edtNombre);
        edtApellido = (EditText) findViewById(R.id.edtApellido);
        jtxtFecNac = (TextView) findViewById(R.id.jtxtFecNac);
        edtDireccion = (EditText) findViewById(R.id.edtDireccion);
        edtTelefono = (EditText) findViewById(R.id.edtTelefono);
        edtCorreo = (EditText) findViewById(R.id.edtCorreo);
        //jtxtESpecialidad = (TextView) findViewById(R.id.jtxtEspecialidad);

        consultarInformacionEmpleado();
        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                guardarInformacion();

            }
        });
    }

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

}