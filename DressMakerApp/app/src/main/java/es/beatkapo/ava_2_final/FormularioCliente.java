package es.beatkapo.ava_2_final;

import static es.beatkapo.ava_2_final.Utils.loadLanguageSelection;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.beatkapo.ava_2_final.db.repository.cliente.ClienteRepositorySQLite;
import es.beatkapo.ava_2_final.model.Cliente;

public class FormularioCliente extends AppCompatActivity {
    private EditText editNombre, editTelefono;

    private boolean modoEdicion;
    private String idCliente;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguageSelection(this);

        setContentView(R.layout.activity_formulario_cliente);
        editNombre = findViewById(R.id.editNombre);
        editTelefono = findViewById(R.id.editTelefono);
        modoEdicion = getIntent().getBooleanExtra("modoEdicion", false);
        if (modoEdicion) {
            idCliente = getIntent().getStringExtra("idCliente");
            rellenarCampos();
            title = findViewById(R.id.textViewTitle);
            title.setText(getString(R.string.editar) + " " + getString(R.string.cliente_mayus));
        }
    }

    private void rellenarCampos() {
        Cliente cliente = new ClienteRepositorySQLite(this).getById(Integer.parseInt(idCliente));
        editNombre.setText(cliente.getNombre());
        editTelefono.setText(cliente.getTelefono());
    }

    public void save(View view) {
        Cliente c = new Cliente();
        String nombre = editNombre.getText().toString();
        String telefono = editTelefono.getText().toString();

        if (nombre.isEmpty() || telefono.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos antes de aceptar", Toast.LENGTH_SHORT).show();
        } else {
            ClienteRepositorySQLite clienteRepositorySQLite = new ClienteRepositorySQLite(this);
            c.setNombre(nombre);
            c.setTelefono(telefono);
            if (modoEdicion) {
                c.setId(Integer.parseInt(idCliente));
                clienteRepositorySQLite.update(c);
                Toast.makeText(this, "Cliente actualizado con éxito", Toast.LENGTH_SHORT).show();
            } else {
                clienteRepositorySQLite.insert(c);
                Toast.makeText(this, "Cliente guardado con éxito", Toast.LENGTH_SHORT).show();
            }
            finish();
        }
    }

    public void cancel(View view) {
        finish();
    }
}