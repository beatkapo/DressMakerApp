package es.beatkapo.ava_2_final;

import static es.beatkapo.ava_2_final.Utils.loadLanguageSelection;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.List;

import es.beatkapo.ava_2_final.adapter.AdaptadorListaImagenes;
import es.beatkapo.ava_2_final.db.repository.imagen.ImagenRepositorySQLite;
import es.beatkapo.ava_2_final.db.repository.prueba.PruebaRepositorySQLite;
import es.beatkapo.ava_2_final.db.repository.trabajo.TrabajoRepositorySQLite;
import es.beatkapo.ava_2_final.model.Prueba;
import es.beatkapo.ava_2_final.model.Trabajo;

public class FormularioPrueba extends AppCompatActivity {

    private boolean modoEdicion;
    private int trabajoId, pruebaId;
    private List<String> imagenes;
    private EditText detalles;
    private Button save, cancel;
    private ImageButton addPhoto;
    private ListView listaImagenes;

    public FormularioPrueba() {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguageSelection(this);

        setContentView(R.layout.activity_formulario_prueba);
        initializeComponents();
    }

    public void initializeComponents() {
        modoEdicion = getIntent().getBooleanExtra("modoEdicion", false);
        trabajoId = getIntent().getIntExtra("idTrabajo", 0);
        if (modoEdicion) {
            pruebaId = getIntent().getIntExtra("pruebaId", 0);
        }
        detalles = findViewById(R.id.editTextArea);
        save = findViewById(R.id.savePruebaButton);
        cancel = findViewById(R.id.cancelPruebaButton);
        addPhoto = findViewById(R.id.addPhoto);
        listaImagenes = findViewById(R.id.listImagenes);
        imagenes = new ImagenRepositorySQLite(this).getAll();
        listaImagenes.setAdapter(new AdaptadorListaImagenes(this, imagenes));
    }

    public void save(View view) {
        PruebaRepositorySQLite repository = new PruebaRepositorySQLite(this);
        Prueba prueba = new Prueba();
        if (detalles.getText().toString().isEmpty()) {
            Toast.makeText(this, getString(R.string.detalle_vacio), Toast.LENGTH_SHORT).show();
            return;
        }
        Date date = new Date();
        prueba.setFecha(date);
        if (modoEdicion) {
            // Actualizar
            prueba.setId(pruebaId);
            prueba.setDetalles(detalles.getText().toString());
            prueba.setTrabajoId(trabajoId);
            repository.update(prueba);
        } else {
            // Insertar
            prueba.setDetalles(detalles.getText().toString());
            prueba.setTrabajoId(trabajoId);
            TrabajoRepositorySQLite trabajoRepositorySQLite = new TrabajoRepositorySQLite(this);
            Trabajo t = trabajoRepositorySQLite.getById(trabajoId);
            t.setEstado(Trabajo.Estado.PRUEBA);
            trabajoRepositorySQLite.update(t);
            Log.i("ID Detalle", String.valueOf(repository.insert(prueba)));
        }
        finish();
    }

    public void addPhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_PICK_IMAGES);
        startActivityForResult(intent, 100);

    }

    ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        if (selectedImage != null) {
                            imagenes.add(selectedImage.getPath().toString());
                        }
                    }
                }
            });

    public void cancel(View view) {
        finish();
    }
}