package es.beatkapo.ava_2_final;

import static es.beatkapo.ava_2_final.Utils.loadLanguageSelection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import es.beatkapo.ava_2_final.adapter.AdaptadorExpandibleCliente;
import es.beatkapo.ava_2_final.adapter.AdaptadorExpandibleTrabajo;
import es.beatkapo.ava_2_final.db.repository.cliente.ClienteRepositorySQLite;
import es.beatkapo.ava_2_final.db.repository.trabajo.TrabajoRepositorySQLite;
import es.beatkapo.ava_2_final.model.Cliente;
import es.beatkapo.ava_2_final.model.Trabajo;

public class VerTodo extends AppCompatActivity {
    private ExpandableListView expandableListView;
    private ImageButton imageDerecha, imageIzquierda;
    private TextView textTitulo;
    private Spinner spinner;
    private boolean listaTrabajoSeleccionada;

    private List<Trabajo> trabajos;
    private List<Cliente> clientes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguageSelection(this);
        setContentView(R.layout.activity_ver_todo);
        expandableListView = findViewById(R.id.expandableListView);
        imageDerecha = findViewById(R.id.imageDerecha);
        imageIzquierda = findViewById(R.id.imageIzquierda);
        spinner = findViewById(R.id.spinner2);
        textTitulo = findViewById(R.id.textListTitle);
        listaTrabajoSeleccionada = true;

        obtenerListas();
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        obtenerListas();
        setListeners();
    }

    private void setListeners() {
        imageDerecha.setOnClickListener(v -> {
            if (listaTrabajoSeleccionada) {
                listaTrabajoSeleccionada = false;
                actualizarListas();
            } else {
                listaTrabajoSeleccionada = true;
                actualizarListas();
            }
        });
        imageIzquierda.setOnClickListener(v -> {
            if (listaTrabajoSeleccionada) {
                listaTrabajoSeleccionada = false;
                actualizarListas();
            } else {
                listaTrabajoSeleccionada = true;
                actualizarListas();
            }
        });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                actualizarListas();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        actualizarSpinner();
    }

    private void ordenarListas() {
        if (listaTrabajoSeleccionada) {
            ordenarListaTrabajos(spinner.getSelectedItemPosition());
        } else {
            ordenarListaClientes(spinner.getSelectedItemPosition());
        }
    }

    private void actualizarSpinner() {
        String[] filtroClientes = {getString(R.string.az), getString(R.string.za)};
        String[] filtroTrabajos = {getString(R.string.date_asc), getString(R.string.date_desc), getString(R.string.estado_asc), getString(R.string.estado_desc)};
        if (listaTrabajoSeleccionada) {
            spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filtroTrabajos));
        } else {
            spinner.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, filtroClientes));
        }
    }

    private void actualizarListaTrabajos() {
        AdaptadorExpandibleTrabajo adaptadorExpandibleTrabajo = new AdaptadorExpandibleTrabajo(this, trabajos);
        expandableListView.setAdapter(adaptadorExpandibleTrabajo);
        textTitulo.setText(getString(R.string.lista_trabajos));
    }

    private void actualizarListaClientes() {
        AdaptadorExpandibleCliente adaptadorExpandibleCliente = new AdaptadorExpandibleCliente(this, clientes);
        expandableListView.setAdapter(adaptadorExpandibleCliente);
        textTitulo.setText(getString(R.string.lista_clientes));
    }

    private void actualizarListas() {
        ordenarListas();
        if (listaTrabajoSeleccionada) {
            actualizarListaTrabajos();
        } else {
            actualizarListaClientes();
        }
    }

    private void obtenerListas() {
        if (listaTrabajoSeleccionada) {
            obtenerListaTrabajosBBDD();
        } else {
            obtenerListaClientesBBDD();
        }
        actualizarListas();
    }

    private void obtenerListaClientesBBDD() {
        clientes = new ClienteRepositorySQLite(this).getAll();
    }

    private void obtenerListaTrabajosBBDD() {
        trabajos = new TrabajoRepositorySQLite(this).getAll();
    }

    private void ordenarListaClientes(int spinnerSelectionId) {
        if (clientes == null) obtenerListaClientesBBDD();

        switch (spinnerSelectionId) {
            case 0:
                //A-Z
                clientes.sort((o1, o2) -> o1.getNombre().compareTo(o2.getNombre()));
                break;
            case 1:
                // Z-A
                clientes.sort((o1, o2) -> o2.getNombre().compareTo(o1.getNombre()));
                break;
        }
    }

    private void ordenarListaTrabajos(int spinnerSelectionId) {
        if (trabajos == null) obtenerListaTrabajosBBDD();
        switch (spinnerSelectionId) {
            case 0:
                // ↑ Fecha
                trabajos.sort((o1, o2) -> o1.getFechaFin().compareTo(o2.getFechaFin()));
                break;
            case 1:
                // ↓ Fecha
                trabajos.sort((o1, o2) -> o2.getFechaFin().compareTo(o1.getFechaFin()));
                break;
            case 2:
                // ↑ Estado
                trabajos.sort((o1, o2) -> o1.getEstado().compareTo(o2.getEstado()));
                break;
            case 3:
                // ↓ Estado
                trabajos.sort((o1, o2) -> o2.getEstado().compareTo(o1.getEstado()));
                break;
        }
    }

    public void onAdd(View view) {
        if (listaTrabajoSeleccionada) {
            Intent intent = new Intent(this, FormularioTrabajo.class);
            startActivity(intent);
        } else {
            Intent intent = new Intent(this, FormularioCliente.class);
            startActivity(intent);
        }
    }
}