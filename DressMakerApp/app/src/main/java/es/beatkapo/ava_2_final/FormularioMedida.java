package es.beatkapo.ava_2_final;

import static es.beatkapo.ava_2_final.Utils.loadLanguageSelection;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import es.beatkapo.ava_2_final.db.repository.medida.MedidaRepositorySQLite;
import es.beatkapo.ava_2_final.db.repository.trabajo.TrabajoRepositorySQLite;
import es.beatkapo.ava_2_final.model.Manga;
import es.beatkapo.ava_2_final.model.Medida;
import es.beatkapo.ava_2_final.model.Trabajo;

public class FormularioMedida extends AppCompatActivity {
    private TextView title, textAntebrazo;
    private boolean modoEdicion;
    private int idMedida, idTrabajo;
    private EditText editEspalda, editPecho, editCintura, editFalda, editBrazo, editAntebrazo, editLargo;
    private float espalda, pecho, cintura, falda, brazo, antebrazo, largo;
    private Manga.Tipo tipo;
    private Spinner spinner;
    private Medida medida;
    private Trabajo trabajo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguageSelection(this);

        setContentView(R.layout.activity_formulario_medida);
        initializeComponents();
        modoEdicion = getIntent().getBooleanExtra("modoEdicion", false);
        title = findViewById(R.id.textTitleMedida);

        if (modoEdicion) {
            idMedida = getIntent().getIntExtra("idMedida", 0);
            medida = new MedidaRepositorySQLite(this).getById(idMedida);
            title.setText(getString(R.string.editar) + " " + getString(R.string.medida_mayus));
            rellenarCampos();
        }
    }

    private void initializeComponents() {
        idTrabajo = getIntent().getIntExtra("idTrabajo", 0);
        trabajo = new TrabajoRepositorySQLite(this).getById(idTrabajo);
        medida = new Medida();
        editEspalda = findViewById(R.id.editEspalda);
        editPecho = findViewById(R.id.editPecho);
        editCintura = findViewById(R.id.editCintura);
        editFalda = findViewById(R.id.editFalda);
        editBrazo = findViewById(R.id.editBrazo);
        editAntebrazo = findViewById(R.id.editAntebrazo);
        editLargo = findViewById(R.id.editLargo);
        spinner = findViewById(R.id.spinner);
        textAntebrazo = findViewById(R.id.textView18);
        String[] tipos = {getString(R.string.corta), getString(R.string.larga), getString(R.string.semi)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, tipos);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinner.getSelectedItem().toString().equals(getString(R.string.corta))) {
                    textAntebrazo.setVisibility(View.GONE);
                    editAntebrazo.setVisibility(View.GONE);
                } else {
                    textAntebrazo.setVisibility(View.VISIBLE);
                    editAntebrazo.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    private void rellenarCampos() {
        editEspalda.setText(String.valueOf(medida.getEspalda()));
        editPecho.setText(String.valueOf(medida.getPecho()));
        editCintura.setText(String.valueOf(medida.getCintura()));
        editFalda.setText(String.valueOf(medida.getFalda()));
        editBrazo.setText(String.valueOf(medida.getManga().getAnchoBrazo()));
        editAntebrazo.setText(String.valueOf(medida.getManga().getAnchoAnteBrazo()));
        editLargo.setText(String.valueOf(medida.getManga().getLargoManga()));

        switch (medida.getManga().getTipo()) {
            case CORTA:
                spinner.setSelection(0);
                break;
            case LARGA:
                spinner.setSelection(1);
                break;
            case SEMI:
                spinner.setSelection(2);
                break;
        }

    }

    public void save(View view) {
        if (validateFields()) {
            medida.setEspalda(Float.parseFloat(editEspalda.getText().toString()));
            medida.setPecho(Float.parseFloat(editPecho.getText().toString()));
            medida.setCintura(Float.parseFloat(editCintura.getText().toString()));
            medida.setFalda(Float.parseFloat(editFalda.getText().toString()));
            medida.setManga(new Manga());
            medida.getManga().setAnchoBrazo(Float.parseFloat(editBrazo.getText().toString()));
            medida.getManga().setLargoManga(Float.parseFloat(editLargo.getText().toString()));
            medida.getManga().setTipo(Manga.Tipo.valueOf(spinner.getSelectedItem().toString().toUpperCase()));
            if (spinner.getSelectedItem().toString().equals(getString(R.string.larga))) {
                medida.getManga().setAnchoAnteBrazo(Float.parseFloat(editAntebrazo.getText().toString()));
            }
            medida.setTrabajoId(idTrabajo);
            MedidaRepositorySQLite medidaRepositorySQLite = new MedidaRepositorySQLite(this);
            TrabajoRepositorySQLite trabajoRepositorySQLite = new TrabajoRepositorySQLite(this);
            if (modoEdicion) {
                medida.setId(idMedida);
                medidaRepositorySQLite.update(medida);
                Toast.makeText(this, getString(R.string.medida_actualizada), Toast.LENGTH_SHORT).show();
            } else {

                medidaRepositorySQLite.insert(medida);
                medida = medidaRepositorySQLite.getByTrabajo(idTrabajo);
                trabajo.setMedida(medida);
                trabajo.setEstado(Trabajo.Estado.EN_PROCESO);
                trabajoRepositorySQLite.update(trabajo);
                Log.i("TRABAJO", trabajo.getMedida().toString());
                Toast.makeText(this, getString(R.string.medida_guardada), Toast.LENGTH_SHORT).show();
            }
            finish();
        } else {
            Toast.makeText(this, getString(R.string.rellena_todo), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean validateFields() {
        if (spinner.getSelectedItem() == null) {
            return false;
        }
        if (editEspalda.getText().toString().isEmpty() || editPecho.getText().toString().isEmpty() || editCintura.getText().toString().isEmpty() || editFalda.getText().toString().isEmpty() || editBrazo.getText().toString().isEmpty() || editLargo.getText().toString().isEmpty()) {
            return false;
        }
        if (spinner.getSelectedItem().toString().equals(getString(R.string.larga)) && editAntebrazo.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }
}