package com.calferinnovate.mediconnecta.ViewModel;

import com.calferinnovate.mediconnecta.Model.ClaseGlobal;
import com.calferinnovate.mediconnecta.PeticionesHTTP.PeticionesJson;

import java.io.Serializable;

public interface ViewModelArgs extends Serializable {
    PeticionesJson getPeticionesJson();

    ClaseGlobal getClaseGlobal();
}