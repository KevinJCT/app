package com.example.loginuta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class Profesion extends AppCompatActivity {

    TextView tvCedula;
    String ced_tra;
    ArrayList<String> nivEstudios;
    Spinner spnEstudios;
    EditText edtHabilidad;
    RecyclerView rv1;
    ArrayList<habilidad> habilidades;
    AdaptadorPersona ap;
    Button btnSave;
    String URL_GUARDAR_PROFESION = "http://192.168.101.3/agiles/guardarHabilidad.php";
    String URL_ACTUALIZAR_PROFESIONAL = "http://192.168.101.3/agiles/actualizar.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profesion);
        spnEstudios = (Spinner) findViewById(R.id.spnEstudio);
        edtHabilidad = (EditText) findViewById(R.id.edtHabilidad);
        ced_tra = getIntent().getStringExtra("CED_TRA");
        rv1 = findViewById(R.id.rv1);

        tvCedula = (TextView) findViewById(R.id.tvCedula);
        tvCedula.setText(ced_tra);

        habilidades = new ArrayList<habilidad>();
        cargarSpinner();
        LinearLayoutManager l = new LinearLayoutManager(this);
        rv1.setLayoutManager(l);
        ap = new AdaptadorPersona();
        rv1.setAdapter(ap);

        Toast.makeText(Profesion.this, ced_tra, Toast.LENGTH_LONG).show();
        btnSave = (Button) findViewById(R.id.btnSave);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Profesion.this,"Si entro"+ced_tra,Toast.LENGTH_SHORT).show();
                Intent i = new Intent(getApplicationContext(), Referencia.class);

               modificarTrabajador(URL_ACTUALIZAR_PROFESIONAL);
                for(habilidad hab: habilidades) {
                   guardarHabilidad(URL_GUARDAR_PROFESION,hab);
                }
                i.putExtra("CED_TRA",ced_tra);
                startActivity(i);
            }
        });
    }

    private void modificarTrabajador(String URL){
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
                parametros.put("NIV_TRA",spnEstudios.getSelectedItem().toString());
                parametros.put("CED_TRA_PER",ced_tra);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Profesion.this);
        requestQueue.add(StringRequest);
    }

    private void guardarHabilidad(String URL, habilidad hab){
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
                parametros.put("HAB_TRA",hab.getNombre().toUpperCase());
                parametros.put("CED_TRA_PER",ced_tra);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(Profesion.this);
        requestQueue.add(StringRequest);
    }

    public boolean cargarSpinner() {
        nivEstudios = new ArrayList<String>(Arrays.asList("EDUCACIÓN BÁSICA",
                "BACHILLERATO", "TÉCNICO SUPERIOR", "TERCER NIVEL", "POSTGRADO"));

        ArrayAdapter<String> adapterEstudios = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, nivEstudios);
        spnEstudios.setAdapter(adapterEstudios);

        return true;
    }

    public boolean agregar(View view){
        if (!edtHabilidad.getText().toString().isEmpty()) {
            habilidad h = new habilidad(edtHabilidad.getText().toString());
            habilidades.add(h);
            edtHabilidad.setText("");
            ap.notifyDataSetChanged();
            rv1.scrollToPosition(habilidades.size() - 1);
            return true;
        }
        edtHabilidad.setError("Campo vacio");
        return false;
    }

    public void mostrar(int pos){
        edtHabilidad.setText(habilidades.get(pos).getNombre());

    }

    public void eliminar(View view){
        int pos=-1;
        for (int i = 0; i<habilidades.size();i++){
            pos = i;
        }

        if (pos != -1){
            habilidades.remove(pos);
            edtHabilidad.setText("");
            ap.notifyDataSetChanged();
            Toast.makeText(Profesion.this, "Se elimino la habilidad",Toast.LENGTH_LONG).show();

        }
    }

    private class AdaptadorPersona extends RecyclerView.Adapter<AdaptadorPersona.AdaptadorPersonaHolder> {

        @NonNull
        @Override
        public AdaptadorPersonaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdaptadorPersonaHolder(getLayoutInflater().inflate(R.layout.item,parent,false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdaptadorPersonaHolder holder, int position) {
            holder.imprimir(position);
        }

        @Override
        public int getItemCount() {
            return habilidades.size();
        }

        class AdaptadorPersonaHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView tv1;
            public AdaptadorPersonaHolder(@NonNull View itemView) {
                super(itemView);
                tv1=itemView.findViewById(R.id.txtHabilidad);
                itemView.setOnClickListener(this);
            }

            public void imprimir(int position) {
                tv1.setText(habilidades.get(position).getNombre());
            }

            @Override
            public void onClick(View v) {
               mostrar(getLayoutPosition());
            }
        }
    }
}