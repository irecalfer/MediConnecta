package com.calferinnovate.mediconnecta.View.Home.Fragments.Pacientes;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.calferinnovate.mediconnecta.Adaptadores.PartePacienteAdapter;
import com.calferinnovate.mediconnecta.View.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.View.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Model.Constantes;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Hashtable;
import java.util.Map;

public class PartePacientesFragment extends Fragment implements IOnBackPressed {


    private TextInputEditText descripcion;
    private Button registrar;
    private ClaseGlobal claseGlobal;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private PartePacienteAdapter partePacienteAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_parte_pacientes, container, false);
        claseGlobal = ClaseGlobal.getInstance();
        inicializaVariables(view);
        getActivity().setTitle("Parte");
        sharedPacientesViewModel = new ViewModelProvider(requireActivity()).get(SharedPacientesViewModel.class);
        return view;
    }

    public void inicializaVariables(View view) {
        descripcion = view.findViewById(R.id.descripcionParte);
        registrar = view.findViewById(R.id.registraParte);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                Pacientes pacienteActual = pacientes;
                rellenaUI(pacienteActual, view);
                clickListenerButtonRegistrar(pacienteActual);
            }
        });


    }

    public void rellenaUI(Pacientes pacienteActual, View view) {
        partePacienteAdapter = new PartePacienteAdapter(pacienteActual, claseGlobal, requireContext());
        partePacienteAdapter.rellenaUI(view);
    }

    public void clickListenerButtonRegistrar(Pacientes pacienteActual) {
        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registraElParte(pacienteActual);
                descripcion.setText("");
            }
        });
    }


    public void registraElParte(Pacientes paciente) {
        final String fecha = fechaDateTimeSql();
        final String cipSns = paciente.getCipSns();
        final String descripcionParte = descripcion.getText().toString();
        final int codEmpleado = claseGlobal.getEmpleado().getCod_empleado();

        //Realizamos validaciones
        if(TextUtils.isEmpty(descripcionParte)){
            descripcion.setError("La descripción no puede estar vacía");
            return;
        }

        String url = Constantes.url_part + "crea_parte.php";
        // creating a new variable for our request queue
        StringRequest stringRequest;
        stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    String message = jsonResponse.getString("message");

                    if ("Registro exitoso".equals(message)) {
                        Toast.makeText(getContext(), "Parte registrado correctamente", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error al registrar el parte", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error en el formato de respuesta", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Ha habido un error al intentar registrar el parte", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> parametros = new Hashtable<String, String>();
                parametros.put("fk_cip_sns", cipSns.trim());
                parametros.put("descripcion", descripcionParte.trim());
                parametros.put("fk_cod_empleado", String.valueOf(codEmpleado));
                parametros.put("fecha", fecha.trim());
                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(stringRequest);
    }

    public String fechaDateTimeSql() {
        DateTimeFormatter fechaHora = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        // Formatea la fecha en el formato de salida
        String fechaFormateada = fechaHora.format(LocalDateTime.now());
        return fechaFormateada;
    }

    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}