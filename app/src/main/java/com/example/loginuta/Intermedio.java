package com.example.loginuta;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class Intermedio extends AppCompatActivity {

    Button btn_in1;
    Button btn_in2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intermedio);

        btn_in1 = findViewById(R.id.btn_registro);
        btn_in2 = findViewById(R.id.btn_cuenta);

        btn_in1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngresoA();
            }
        });

        btn_in2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                IngresoC();
            }
        });

    }
    public void IngresoA(){
        Intent ingreso = new Intent(getApplicationContext(), IngresoActividades.class);
        startActivity(ingreso);
    }


    public void IngresoC(){
        Intent ingresoc = new Intent(getApplicationContext(), Cuenta.class);
        startActivity(ingresoc);
    }
}