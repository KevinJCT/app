package com.example.loginuta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btn_log;
    Button btn_reg;
    EditText et_usu;
    EditText et_pass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_usu = findViewById(R.id.TV_usu);
        et_pass = findViewById(R.id.TV_pass);
        btn_log = findViewById(R.id.btn_iniciar);
        btn_reg = findViewById(R.id.btn_registro);

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Cuenta.class);
                Toast.makeText(MainActivity.this,"Si entro",Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }
        });


        btn_log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validar("http://192.168.101.3/agiles/login.php");
            }
        });


    }

    //---------/



    private void validar(String URL){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if(!response.isEmpty()){
                    Intent intent = new Intent(getApplicationContext(), Intermedio.class);
                    startActivity(intent);
                }else{
                    Toast.makeText(MainActivity.this, "usuario o clave incorrecta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, error.toString(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String,String> parametros=new HashMap<String,String>();
                parametros.put("username",et_usu.getText().toString());
                parametros.put("password",et_pass.getText().toString());
                return parametros;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }
}