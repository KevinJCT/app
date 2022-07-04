package com.example.loginuta;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class IngresoActividades extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    EditText edtSolicitante, edtHoras, edtObservacion;
    Spinner spTipo, spActividad, spEstado, spEmpleados;
    ArrayAdapter<String> estados, empleados;
    List<String> actividades = new ArrayList<>();
    String [] estadosS = new String[] {"No iniciado","En proceso","Terminado"};
    Button btnAgregar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingreso_actividades);

        //TIPO ACTIVIDAD
        spTipo = (Spinner) findViewById(R.id.spTipo);
        spActividad = (Spinner) findViewById(R.id.spActividad);

        List<String> empleados = new ArrayList<>();
        empleados.add("TANIA");
        empleados.add("MARGARITA");
        empleados.add("LEONARDO");
        empleados.add("CHRISTIAN");
        empleados.add("KEYBRISH");
        ArrayAdapter<String> employees = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,empleados);
        employees.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spEmpleados.setAdapter(employees);

        List<String> tipos = new ArrayList<>();
        tipos.add("Configuración");
        tipos.add("Soporte TI");
        tipos.add("Reunión");
        ArrayAdapter<String> types = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item,tipos);
        types.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTipo.setAdapter(types);
        spTipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if(adapterView.getItemAtPosition(i).equals("Configuración")){
                    actividades.clear();
                    actividades.add("Teléfono IP"); actividades.add("Servidor BD"); actividades.add("Servidor Correo"); actividades.add("PC"); actividades.add("Tablet");
                    fillSpinner();
                } else if(adapterView.getItemAtPosition(i).equals("Soporte TI")){
                    actividades.clear();
                    actividades.add("Ventas"); actividades.add("Compras"); actividades.add("Facturación"); actividades.add("Nómina"); actividades.add("Inventarios");
                    fillSpinner();
                } else if(adapterView.getItemAtPosition(i).equals("Reunión")){
                    actividades.clear();
                    actividades.add("Planificación"); actividades.add("Diaria");
                    fillSpinner();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spEmpleados = (Spinner) findViewById(R.id.spEmpleados);
        //edtSolicitante = (EditText) findViewById(R.id.edtSolicitante);

        //ESTADO
        spEstado = (Spinner) findViewById(R.id.spEstado);
        spEstado.setOnItemSelectedListener(this);
        estados = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item,estadosS);
        spEstado.setAdapter(estados);

        edtHoras = (EditText) findViewById(R.id.edtHoras);

        //OBSERVACIONES
        edtObservacion = (EditText) findViewById(R.id.edtObservacion);

        btnAgregar = (Button) findViewById(R.id.btnAñadir);

        btnAgregar.setOnClickListener(v -> ejecutarServicio());
    }
    private void ejecutarServicio(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, "http://192.168.1.36:8080/insertarActividad.php", onResponse -> Toast.makeText(getApplicationContext(),"OPERACION EXITOSA",Toast.LENGTH_LONG).show(), error -> Toast.makeText(getApplicationContext(),error.toString(),Toast.LENGTH_LONG).show()){

            @Override
            protected Map<String,String> getParams() {
                Map<String,String> parametros = new HashMap<>();
                parametros.put("ACT_REG",actividad());
                parametros.put("EMP_SOL_REG",solicita());
                parametros.put("EST_REG",estado());
                parametros.put("NUM_HOR_REG",edtHoras.getText().toString());
                parametros.put("OBS_REG",edtObservacion.getText().toString());
                return parametros;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    public String estado(){
        int posicion = spEstado.getSelectedItemPosition();
        String estado = "";
        if(posicion==0){
            estado = "NO INICIADO";
        } else if(posicion==1){
            estado = "EN PROGRESO";
        } else if(posicion==2){
            estado = "TERMINADO";
        }
        return estado;
    }

    public String solicita(){
        int posicion = spEmpleados.getSelectedItemPosition();
        String solicita = "";
        if(posicion==0){
            solicita = "TANIA";
        } else if(posicion==1){
            solicita = "MARGARITA";
        } else if(posicion==2){
            solicita = "LEONARDO";
        } else if(posicion==3){
            solicita = "CHRISTIAN";
        } else if(posicion==4){
            solicita = "KEYBRISH";
        }
        return solicita;
    }

    public String actividad(){
        int posicion = spTipo.getSelectedItemPosition();
        String actividad = "";
        int pos;
        if (posicion==0){
            pos = spActividad.getSelectedItemPosition();
            if(pos==0){
                actividad = "TIP";
            } else if(pos==1){
                actividad = "SBD";
            } else if(pos==2){
                actividad = "SCO";
            } else if(pos==3){
                actividad = "CPC";
            } else if(pos==4){
                actividad = "CTB";
            }
        } else if (posicion==1){
            pos = spActividad.getSelectedItemPosition();
            if(pos==0){
                actividad = "VEN";
            } else if(pos==1){
                actividad = "COM";
            } else if(pos==2){
                actividad = "FAC";
            } else if(pos==3){
                actividad = "NOM";
            } else if(pos==4){
                actividad = "INV";
            }
        } else if (posicion==2){
            pos = spActividad.getSelectedItemPosition();
            if(pos==0){
                actividad = "PLA";
            } else if(pos==1){
                actividad = "DIA";
            }
        }

        return actividad;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public void fillSpinner(){
        ArrayAdapter<String> actividadesT = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item,actividades);
        actividadesT.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spActividad.setAdapter(actividadesT);
    }
}