package com.example.loginuta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Referencia extends AppCompatActivity {

    ArrayList<Experiencia> experiencias;
    RecyclerView rv2;
    String ced_tra_ref;
    EditText edtEmpresa, edtCargo, edtTelefono;
    AdaptadorPersona ap;
    Button btnSaveRef;
    String URL_GUARDAR_REFERENCIA = "http://192.168.101.3/agiles/guardarReferencias.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_referencia);
        rv2=findViewById(R.id.rv2);

        edtEmpresa=(EditText) findViewById(R.id.edtEmpresa);
        edtCargo=(EditText) findViewById(R.id.edtCargo);
        edtTelefono=(EditText) findViewById(R.id.edtTelefono);
        btnSaveRef = (Button) findViewById(R.id.btnSaveRef);

        experiencias = new ArrayList<Experiencia>();
        LinearLayoutManager l=new LinearLayoutManager(this);
        rv2.setLayoutManager(l);
        ap=new AdaptadorPersona();
        rv2.setAdapter(ap);
        ced_tra_ref = getIntent().getStringExtra("CED_TRA");
        btnSaveRef.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Referencia.this,"Logintud"+experiencias.size(),Toast.LENGTH_LONG).show();
                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                for(Experiencia exp: experiencias) {
                    guardarReferencia(URL_GUARDAR_REFERENCIA,exp);
                }
                startActivity(i);
            }
        });

    }


    private void guardarReferencia(String URL, Experiencia exp){
        StringRequest StringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "GUARDADO REFERENCIA", Toast.LENGTH_SHORT).show();
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

                parametros.put("EMP_REF",exp.getEmpresa().toUpperCase());
                parametros.put("CAR_REF",exp.getCargo().toUpperCase());
                parametros.put("TEL_REF",exp.getTelefono().toUpperCase());
                parametros.put("CED_TRA_PER",ced_tra_ref);
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Referencia.this);
        requestQueue.add(StringRequest);
    }


    public void agregar(View v) {
        Experiencia e=new Experiencia(edtEmpresa.getText().toString(),edtCargo.getText().toString(),edtTelefono.getText().toString());
        experiencias.add(e);
        edtEmpresa.setText("");
        edtCargo.setText("");
        edtTelefono.setText("");
        ap.notifyDataSetChanged();
        rv2.scrollToPosition(experiencias.size()-1);
    }

    public void mostrar(int pos){
        edtEmpresa.setText(experiencias.get(pos).getEmpresa());
        edtCargo.setText(experiencias.get(pos).getCargo());
        edtTelefono.setText(experiencias.get(pos).getTelefono());

    }


    public void eliminar(View view){
        int pos=-1;
        for (int i = 0; i<experiencias.size();i++){
            pos = i;
        }

        if (pos != -1){
            experiencias.remove(pos);
            edtEmpresa.setText("");
            edtCargo.setText("");
            edtTelefono.setText("");
            ap.notifyDataSetChanged();
            Toast.makeText(Referencia.this, "Se elimino la experiencia",Toast.LENGTH_LONG).show();

        }
    }


    private class AdaptadorPersona extends RecyclerView.Adapter<AdaptadorPersona.AdaptadorPersonaHolder> {

        @NonNull
        @Override
        public AdaptadorPersonaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorPersonaHolder(getLayoutInflater().inflate(R.layout.referencia,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorPersonaHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return experiencias.size();
        }

        class AdaptadorPersonaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tv1,tv2,tv3;
            public AdaptadorPersonaHolder(@NonNull View itemView) {
                super(itemView);
                tv1=itemView.findViewById(R.id.tvEmpresa);
                tv2=itemView.findViewById(R.id.tvCargo);
                tv3=itemView.findViewById(R.id.tvTelefono);
                itemView.setOnClickListener(this);
            }

            public void imprimir(int position) {
                tv1.setText("EMPRESA : "+experiencias.get(position).getEmpresa());
                tv2.setText("CARGO : "+experiencias.get(position).getCargo());
                tv3.setText("TELÉFONO : "+experiencias.get(position).getTelefono());
            }

            @Override
            public void onClick(View v) {
                mostrar(getLayoutPosition());
            }
        }
    }


}