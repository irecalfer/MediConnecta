package com.calferinnovate.mediconnecta.Home.Fragments.Residentes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.calferinnovate.mediconnecta.Adaptadores.FamiliaresAdapter;
import com.calferinnovate.mediconnecta.Adaptadores.PacientesAdapter;
import com.calferinnovate.mediconnecta.R;
import com.calferinnovate.mediconnecta.clases.ClaseGlobal;
import com.calferinnovate.mediconnecta.clases.ContactoFamiliares;
import com.calferinnovate.mediconnecta.clases.Pacientes;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.PeticionesJson;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.SharedPacientesViewModel;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelArgs;
import com.calferinnovate.mediconnecta.clases.PeticionesHTTP.ViewModel.ViewModelFactory;

import java.util.ArrayList;

public class ContactoFamiliaresPacienteFragment extends Fragment {
    private ClaseGlobal claseGlobal;
    private SharedPacientesViewModel sharedPacientesViewModel;
    private RecyclerView recyclerView;
    private ViewModelArgs viewModelArgs;
    private PeticionesJson peticionesJson;
    private ArrayList<ContactoFamiliares> listaContactoFamiliares;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacto_familiares_paciente, container, false);
        llamaAClaseGlobal();
        // Obtener el Recycler
        recyclerView = view.findViewById(R.id.recyclerViewFamiliares);
        recyclerView.setHasFixedSize(true);

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

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Observación de LiveData: Has configurado la observación de un LiveData en el ViewModel utilizando el método observe(). Cuando los datos cambian en el LiveData,
        // el adaptador se actualiza automáticamente para reflejar los cambios en el RecyclerView.
        sharedPacientesViewModel.getPaciente().observe(getViewLifecycleOwner(), new Observer<Pacientes>() {
            @Override
            public void onChanged(Pacientes pacientes) {
                Pacientes nuevoPaciente = pacientes;
                obtieneListaFamiliares(nuevoPaciente);
            }
        });

    }

    public void llamaAClaseGlobal(){
        claseGlobal = ClaseGlobal.getInstance();
    }

    public void obtieneListaFamiliares(Pacientes paciente){
        sharedPacientesViewModel.getListaMutableFamiliares(paciente).observe(getViewLifecycleOwner(), new Observer<ArrayList<ContactoFamiliares>>() {
            @Override
            public void onChanged(ArrayList<ContactoFamiliares> contactoFamiliares) {
                // added data from arraylist to adapter class.
                FamiliaresAdapter adapter = new FamiliaresAdapter(contactoFamiliares, getContext());
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                // at last set adapter to recycler view.
                recyclerView.setLayoutManager(linearLayoutManager);
                recyclerView.setAdapter(adapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
            }
        });


    }


}