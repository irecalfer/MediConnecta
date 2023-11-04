package com.calferinnovate.mediconnecta.clases;

import android.app.Application;

import java.util.ArrayList;

public class ClaseGlobal extends Application {

    //Variable para almacenar una única instancia de ClaseGlobal
    private static ClaseGlobal instance;
    //
    private Empleado empleado;
    private Unidades unidades;
    private Area area;
    private Avisos avisos;
    private Fechas fechas;
    private Pacientes pacientes;
    private Rutinas rutinas;
    private PacientesAgrupadosRutinas pacientesAgrupadosRutinas;
    private ArrayList<Unidades> listaUnidades;
    private ArrayList<Pacientes> listaPacientes;
    private ArrayList<PacientesAgrupadosRutinas> listaProgramacion;

    @Override public void onCreate(){
        super.onCreate();
    }

    public ClaseGlobal() {
        empleado = new Empleado();
        unidades = new Unidades();
        area = new Area();
        avisos = new Avisos();
        fechas = new Fechas();
        pacientes = new Pacientes();
        rutinas = new Rutinas();
        listaUnidades = new ArrayList<>();
        listaPacientes = new ArrayList<>(); // Inicializa la lista de pacientes vacía
        listaProgramacion = new ArrayList<>();

    }

    //  método público y estático que se utiliza para obtener la única instancia de la clase ClaseGlobal.
    //  Es estático para que pueda ser llamado sin crear una instancia previa de la clase.
    public static  ClaseGlobal getInstance(){
        // Aquí se verifica si la variable instance es nula. La primera vez que se llama a getInstance(),
        // instance será nula porque aún no se ha creado una instancia de ClaseGlobal.
        if (instance == null) {
            //Esto inicia una sección crítica sincronizada en la clase ClaseGlobal. El bloque sincronizado garantiza que solo un hilo a
            // la vez pueda ejecutar el código dentro de ese bloque. Esto es importante para evitar que
            // varios hilos creen múltiples instancias de la clase en caso de concurrencia.
            synchronized (ClaseGlobal.class) {
                //entro del bloque sincronizado, se realiza una segunda verificación de si instance es nula. Esto es necesario porque,
                // después de adquirir el bloque sincronizado, otro hilo podría haber creado la instancia de
                // ClaseGlobal, por lo que necesitas verificar nuevamente si instance es nula.
                if (instance == null) {
                    //Si, en la segunda verificación, instance aún es nula, se crea una nueva instancia de la clase ClaseGlobal y se asigna a la variable instance.
                    instance = new ClaseGlobal();
                }
            }
        }
        //Finalmente, se devuelve la instancia de ClaseGlobal. Si es la primera vez que se llama a getInstance(),
        // se crea una nueva instancia. Si no es la primera vez, se devuelve la instancia existente.
        // Esto asegura que siempre se utilice la misma instancia en toda la aplicación.
        return instance;
    }

    public Empleado getEmpleado() {
        return empleado;
    }

    public Unidades getUnidades() {
        return unidades;
    }

    public Area getArea() {
        return area;
    }

    public Avisos getAvisos() {
        return avisos;
    }

    public Fechas getFechas() {
        return fechas;
    }

    public Pacientes getPacientes() {
        return pacientes;
    }

    public Rutinas getRutinas() {
        return rutinas;
    }

    public PacientesAgrupadosRutinas getPacientesAgrupadosRutinas() {
        return pacientesAgrupadosRutinas;
    }

    public void setEmpleado(Empleado empleado) {
        this.empleado = empleado;
    }

    public void setUnidades(Unidades unidades) {
        this.unidades = unidades;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public void setAvisos(Avisos avisos) {
        this.avisos = avisos;
    }

    public void setFechas(Fechas fechas) {
        this.fechas = fechas;
    }

    public void setPacientes(Pacientes pacientes) {
        this.pacientes = pacientes;
    }

    public void setRutinas(Rutinas rutinas) {
        this.rutinas = rutinas;
    }

    public void setPacientesAgrupadosRutinas(PacientesAgrupadosRutinas pacientesAgrupadosRutinas) {
        this.pacientesAgrupadosRutinas = pacientesAgrupadosRutinas;
    }

    public ArrayList<Unidades> getListaUnidades() {
        return listaUnidades;
    }

    public void setListaUnidades(ArrayList<Unidades> listaUnidades) {
        this.listaUnidades = listaUnidades;
    }

    public ArrayList<Pacientes> getListaPacientes() {
        return listaPacientes;
    }

    public void setListaPacientes(ArrayList<Pacientes> listaPacientes) {
        this.listaPacientes = listaPacientes;
    }

    public ArrayList<PacientesAgrupadosRutinas> getListaProgramacion() {
        return listaProgramacion;
    }

    public void setListaProgramacion(ArrayList<PacientesAgrupadosRutinas> listaProgramacion) {
        this.listaProgramacion = listaProgramacion;
    }
}
