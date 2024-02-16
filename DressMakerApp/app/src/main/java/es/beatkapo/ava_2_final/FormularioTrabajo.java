package es.beatkapo.ava_2_final;

import static es.beatkapo.ava_2_final.Utils.loadLanguageSelection;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import es.beatkapo.ava_2_final.db.repository.cliente.ClienteRepositorySQLite;
import es.beatkapo.ava_2_final.db.repository.trabajo.TrabajoRepositorySQLite;
import es.beatkapo.ava_2_final.model.Cliente;
import es.beatkapo.ava_2_final.model.Trabajo;

public class FormularioTrabajo extends AppCompatActivity {

    private EditText editPrecio, editPagado, editFecha;
    private Spinner spinnerCliente, spinnerTipo;
    private boolean modoEdicion;
    private String idTrabajo;
    private TextView title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        modoEdicion = getIntent().getBooleanExtra("modoEdicion", false);

        super.onCreate(savedInstanceState);
        loadLanguageSelection(this);

        setContentView(R.layout.activity_formulario_trabajo);
        inicializarComponentes();
        if (modoEdicion) {
            idTrabajo = getIntent().getStringExtra("idTrabajo");
            rellenarCampos();
            title = findViewById(R.id.tvTitle);
            title.setText(getString(R.string.editar) + " " + getString(R.string.trabajo_mayus));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializarSpinners();
    }

    private void rellenarCampos() {
        Trabajo trabajo = new TrabajoRepositorySQLite(this).getById(Integer.parseInt(idTrabajo));
        editPrecio.setText(String.valueOf(trabajo.getPrecioTotal()));
        editPagado.setText(String.valueOf(trabajo.getPagado()));
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        editFecha.setText(sdf.format(trabajo.getFechaFin()));
        int posicionCliente = getSelectedClientePosition(trabajo.getCliente());
        spinnerCliente.setSelection(posicionCliente);
        spinnerTipo.setSelection(trabajo.getTipo().equals(Trabajo.Tipo.TRAJE) ? 0 : 1);
    }

    private int getSelectedClientePosition(Cliente cliente) {
        for (int i = 1; i < spinnerCliente.getCount(); i++) {
            if (((Cliente) spinnerCliente.getItemAtPosition(i)).equals(cliente.getId())) {
                return i;
            }
        }
        return 0;
    }

    private void inicializarComponentes() {
        editPrecio = findViewById(R.id.editPrecio);
        editPagado = findViewById(R.id.editPagado);
        editFecha = findViewById(R.id.editFecha);
        spinnerCliente = findViewById(R.id.spinnerCliente);
        spinnerTipo = findViewById(R.id.spinnerTipo);
        inicializarSpinners();

    }

    private void inicializarSpinners() {
        List<Cliente> clientes = new ArrayList<>();
        clientes.add(0, new Cliente("", "")); // Para que el primer item sea un mensaje (no seleccionado
        clientes.add(1, new Cliente(getString(R.string.nuevo_minus) + "...", ""));
        clientes.addAll(new ClienteRepositorySQLite(this).getAll());
        ArrayAdapter<Cliente> adapterCliente = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, clientes);
        adapterCliente.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCliente.setAdapter(adapterCliente);
        spinnerCliente.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 1) {
                    abrirFormularioCliente();
                } else if (position > 1) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        ArrayAdapter<Trabajo.Tipo> adapterTipo = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Trabajo.Tipo.values());
        adapterTipo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTipo.setAdapter(adapterTipo);
    }

    private void abrirFormularioCliente() {
        Intent intent = new Intent(this, FormularioCliente.class);
        intent.putExtra("modoEdicion", false);
        startActivity(intent);
    }

    public void pickDate(View view) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                editFecha.setText(day + "/" + (month + 1) + "/" + year);
            }
        }, year, month, day);
        datePickerDialog.show();
    }

    public void cancel(View view) {
        finish();
    }

    public void save(View view) {
        String precio = editPrecio.getText().toString();
        String pagado = editPagado.getText().toString();
        String fecha = editFecha.getText().toString();
        Trabajo.Tipo tipo = (Trabajo.Tipo) spinnerTipo.getSelectedItem();
        if (precio.isEmpty() || fecha.isEmpty() || tipo == null || spinnerCliente.getSelectedItemPosition() <= 1) {
            Toast.makeText(this, getString(R.string.rellena_todo), Toast.LENGTH_SHORT).show();
            return;
        } else {
            Cliente cliente = (Cliente) spinnerCliente.getSelectedItem();
            Trabajo trabajo = new Trabajo();
            trabajo.setPrecioTotal(Float.parseFloat(precio));
            if (!pagado.isEmpty()) {
                trabajo.setPagado(Float.parseFloat(pagado));
            } else {
                trabajo.setPagado(0);

            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            Date fechaPedido = new Date();
            trabajo.setFechaPedido(fechaPedido);
            try {
                trabajo.setFechaFin(sdf.parse(fecha));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            trabajo.setTipo(tipo);
            trabajo.setCliente(cliente);
            trabajo.setEstado(Trabajo.Estado.TOMAR_MEDIDAS);
            TrabajoRepositorySQLite trabajoRepositorySQLite = new TrabajoRepositorySQLite(this);

            if (modoEdicion) {
                trabajo.setId(Integer.parseInt(idTrabajo));
                trabajoRepositorySQLite.update(trabajo);
                Toast.makeText(this, (tipo == Trabajo.Tipo.TRAJE ? getString(R.string.traje) : getString(R.string.arreglo)) + getString(R.string.actualizado), Toast.LENGTH_SHORT).show();
            } else {
                trabajoRepositorySQLite.insert(trabajo);
                Toast.makeText(this, (tipo == Trabajo.Tipo.TRAJE ? getString(R.string.traje) : getString(R.string.arreglo)) + getString(R.string.guardado), Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }
}