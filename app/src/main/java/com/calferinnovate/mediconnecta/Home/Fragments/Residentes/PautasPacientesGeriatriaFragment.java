package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.app.ActionBar;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.calferinnovate.mediconnecta.Adaptadores.AnatomicosAdapter;
import com.calferinnovate.mediconnecta.Adaptadores.PautasAdapter;
import com.calferinnovate.mediconnecta.Home.Fragments.PacientesFragment;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.Interfaces.IOnBackPressed;
import com.calferinnovate.mediconnecta.Model.Pacientes;
import com.calferinnovate.mediconnecta.Model.Pautas;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.ViewModel.ViewModelFactory;

import java.util.ArrayList;


public class PautasPacientesGeriatriaFragment extends Fragment implements IOnBackPressed {
    private SharedPacientesViewModel sharedPacientesViewModel;
    private RecyclerView recyclerView;
    private RecyclerView recyclerViewAnatomicos;
    private ViewModelArgs viewModelArgs;
    private PeticionesJson peticionesJson;
    private ClaseGlobal claseGlobal;
    private PautasAdapter pautasAdapter;
    private AnatomicosAdapter anatomicosAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pautas_pacientes_geriatria, container, false);

        inicializaVariables();

        // Obtener el Recycler
        recyclerView = view.findViewById(R.id.recyclerViewPautas);
        recyclerView.setHasFixedSize(true);
        recyclerViewAnatomicos = view.findViewById(R.id.recyclerViewAnatomicos);
        recyclerViewAnatomicos.setHasFixedSize(true);

        inicializaViewModel();


        return view;
    }

    public void inicializaVariables() {
        claseGlobal = ClaseGlobal.getInstance();
    }

    public void inicializaViewModel(){
        //Creas un objeto ViewModelFactory y obtienes una instancia de ConsultasYRutinasDiariasViewModel utilizando este factory.
        //Luego, observas el LiveData del ViewModel para mantener actualizada la lista de programación en el RecyclerView.
        viewModelArgs = new ViewModelArgs() {
            @Override
            public PeticionesJson getPeticionesJson() {
                return peticionesJson = new PeticionesJson(requireContext());
            }

            @Override
            public ClaseGlobal getClaseGlobal() {
                return claseGlobal;
            }
        };

        ViewModelFactory<SharedPacientesViewModel> factory = new ViewModelFactory<>(viewModelArgs);
        // Inicializa el ViewModel
        sharedPacientesViewModel = new ViewModelProvider(requireActivity(), factory).get(SharedPacientesViewModel.class);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                Pacientes pacienteActual = pacientes;
                // Limpia los datos anteriores
                obtienePautasGeneralesDelPaciente(pacienteActual);
            }
        });
    }



    public void obtienePautasGeneralesDelPaciente(Pacientes pacientes) {
        sharedPacientesViewModel.getListaMutablePautas(pacientes).observe(getViewLifecycleOwner(), new Observer<ArrayList<Pautas>>() {
            @Override
            public void onChanged(ArrayList<Pautas> pautas) {

                    rellenaPautasGenerales(pautas);
                    rellenaPautasPañal(pautas);
            }
        });

    }

    public void rellenaPautasGenerales(ArrayList<Pautas> pautas){
        if(pautas.isEmpty()){
            noTienePautas();
        }else{
            pautasAdapter = new PautasAdapter(pautas, getContext());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
            // at last set adapter to recycler view.
            recyclerView.setLayoutManager(linearLayoutManager);
            recyclerView.setAdapter(pautasAdapter);
            recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
            pautasAdapter.notifyDataSetChanged();
        }
    }

    public void rellenaPautasPañal(ArrayList<Pautas> pautas){
        anatomicosAdapter = new AnatomicosAdapter(pautas, getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerViewAnatomicos.setLayoutManager(linearLayoutManager);
        recyclerViewAnatomicos.setAdapter(anatomicosAdapter);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        anatomicosAdapter.notifyDataSetChanged();

    }

    private void noTienePautas(){
        // Agrega dinámicamente un EditText al layout indicando que no hay pautas
        EditText editText = new EditText(requireContext());
        editText.setText("Este paciente no tiene pautas.");
        editText.setEnabled(false);
        // Agrega el EditText al contenedor adecuado en tu diseño
        // Por ejemplo, si tu diseño tiene un LinearLayout llamado "contenedor", puedes hacer algo como:
        //recyclerView.addView(editText);
        //contenedor.addView(editText);
    }
    @Override
    public boolean onBackPressed() {
        requireActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new PacientesFragment()).commit();
        return true;
    }
}