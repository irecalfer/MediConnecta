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
    private ArrayList<PacientesAgrupadosRutinas> listaAgrupados;
    private Fechas fechas;
    private Unidades unidades;
    private Rutinas rutinas;
    private ArrayList<Pacientes> pacientes;
    private RequestQueue requestQueue;
    private JsonObjectRequest jsonObjectRequest;
    private ArrayList<Rutinas> listaRutinas = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_consultas_y_rutinas_diarias, container, false);
        referenciaVariables(vista);
        llamadaObjetosGlobales();


       // obtencionRutinas();
        seleccionDeTabs();
        return vista;
    }


    public void referenciaVariables(View view){
        rvConsultas = view.findViewById(R.id.rvRutinas);
        tabLayout = view.findViewById(R.id.tabLAyoutRutinas);
    }

    public void llamadaObjetosGlobales(){
        fechas = ((ClaseGlobal) getActivity().getApplicationContext()).getFechas();
        unidades = ((ClaseGlobal) getActivity().getApplicationContext()).getUnidades();
        rutinas = ((ClaseGlobal) getActivity().getApplicationContext()).getRutinas();
        pacientesAgrupadosRutinas = ((ClaseGlobal) getActivity().getApplicationContext()).getPacientesAgrupadosRutinas();
        pacientes = ((ClaseGlobal) getActivity().getApplication()).getPacientes().getListaPacientes();
    }

    public void obtencionRutinas(){
        String url = Constantes.url_part+"rutinas.php";
        jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray jsonArray = response.getJSONArray("rutina");
                    for(int i =0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        rutinas = new Rutinas(jsonObject.optInt("id_tipo_rutina"),
                                jsonObject.optString("diario"));
                        listaRutinas.add(rutinas);
                    }
                    rutinas.setListaRutinas(listaRutinas);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
    }

    public void obtieneDatosRutinasDiaPacientes(String tipoRutina){
        url = Constantes.url_part+"programacionRutinas.php?fecha_rutina="+fechas.getFechaActual()+"&nombre="+unidades.getNombreUnidad()+
        "&diario="+tipoRutina;
        requestQueue = Volley.newRequestQueue(getContext());
        jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    listaAgrupados = new ArrayList<>();
                    JSONArray jsonArray = response.getJSONArray("programacion");
                    for(int i =0; i<jsonArray.length(); i++){
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        pacientesAgrupadosRutinas = new PacientesAgrupadosRutinas(jsonObject.optString("fk_cip_sns"), jsonObject.optInt("fk_id_rutina"), jsonObject.optString("hora_rutina"));
                        listaAgrupados.add(pacientesAgrupadosRutinas);
                    }
                    pacientesAgrupadosRutinas.setListaProgramacion(listaAgrupados);
                    rutinasAdapter = new RutinasAdapter(getActivity().getApplicationContext(), listaAgrupados, pacientes);
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
                obtieneDatosRutinasDiaPacientes((String) tab.getText());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                obtieneDatosRutinasDiaPacientes((String) tab.getText());
            }
        });
   }

    //HACER DE NUEVO PHP DE RUTINAS Y MODELARLO EN JAVA

}