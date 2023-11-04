package com.calferinnovate.mediconnecta.Home.Fragments.HomeFragments.Rutinas;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Adaptadores.RutinasAdapter;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.Constantes;
import com.calferinnovate.mediconnecta.clases.Fechas;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PacientesAgrupadosRutinas;
import com.calferinnovate.mediconnecta.clases.Rutinas;
import com.calferinnovate.mediconnecta.clases.Unidades;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class consultasYRutinasDiariasFragment extends Fragment {

    private RecyclerView rvConsultas;
    private String url;
    private RutinasAdapter rutinasAdapter;
    private TabLayout tabLayout;
    private PacientesAgrupadosRutinas pacientesAgrupadosRutinas;
    private Fechas fechas;
    private Unidades unidades;
    private Rutinas rutinas;
    private ClaseGlobal claseGlobal;
    private Pacientes pacientes;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayList<Rutinas> listaRutinas = new ArrayList<>();
    private EditText fechaRutina;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_consultas_y_rutinas_diarias, container, false);
        referenciaVariables(vista);
        llamadaObjetosGlobales();

        //Seteamos la fecha en el EditText
        fechaRutina.setText(fechas.getFechaActual()); // FORMATEAR FECHA PARA QUE APAREZCA DE FORMA dd/MM/YYYY
        // Inicializa el adaptador una sola vez
        rutinasAdapter = new RutinasAdapter(claseGlobal.getListaProgramacion(), claseGlobal.getListaPacientes());
        rvConsultas.setAdapter(rutinasAdapter);
        seleccionDeTabs();
        return vista;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    public void referenciaVariables(View view){
        rvConsultas = view.findViewById(R.id.rvRutinas);
        tabLayout = view.findViewById(R.id.tabLAyoutRutinas);
        fechaRutina = view.findViewById(R.id.fechaActual);
    }

    public void llamadaObjetosGlobales(){
        claseGlobal = (ClaseGlobal) getActivity().getApplication();
        fechas = claseGlobal.getFechas();
        unidades = claseGlobal.getUnidades();
        rutinas = claseGlobal.getRutinas();
        pacientesAgrupadosRutinas = claseGlobal.getPacientesAgrupadosRutinas();
        pacientes = claseGlobal.getPacientes();
    }


    public void obtieneDatosRutinasDiaPacientes(String tipoRutina){
        url = Constantes.url_part+"programacionRutinas.php?fecha_rutina="+fechas.getFechaActual()+"&nombre="+unidades.getNombreUnidad()+
        "&diario="+tipoRutina;
        for(Unidades unidad: claseGlobal.getListaUnidades()){
            Log.d("PruebaNull", unidad.getNombreUnidad());
        }
        for(Pacientes paciente: claseGlobal.getListaPacientes()){
            Log.d("PruebaNull", paciente.getNombre());
        }
        requestQueue = Volley.newRequestQueue(getContext());
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("programacion");
                    for(int i =0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        pacientesAgrupadosRutinas = new PacientesAgrupadosRutinas(jsonObject.optString("fk_cip_sns"), jsonObject.optInt("fk_id_rutina"), jsonObject.optString("hora_rutina"));
                        // Agrega una nueva programacion a la lista en tu fragmento
                        claseGlobal.getListaProgramacion().add(pacientesAgrupadosRutinas);
                    }
                    // Actualiza la lista de pacientes en ClaseGlobal
                    claseGlobal.setListaProgramacion(claseGlobal.getListaProgramacion());
                    // Notifica al adaptador que los datos han cambiado
                    rutinasAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        requestQueue.add(jsonObjectRequest);
    }

   public void seleccionDeTabs(){
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                // Limpia los datos actuales en el adaptador
                claseGlobal.getListaProgramacion().clear();
                // Notifica al adaptador que los datos han cambiado
                rutinasAdapter.notifyDataSetChanged();
                obtieneDatosRutinasDiaPacientes((String) tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                // Limpia los datos actuales en el adaptador
                claseGlobal.getListaProgramacion().clear();
                // Notifica al adaptador que los datos han cambiado
                rutinasAdapter.notifyDataSetChanged();
                obtieneDatosRutinasDiaPacientes((String) tab.getText());
            }
        });
   }

}