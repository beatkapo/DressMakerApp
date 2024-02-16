package es.beatkapo.ava_2_final;

import static es.beatkapo.ava_2_final.Utils.loadLanguageSelection;
import static es.beatkapo.ava_2_final.Utils.saveLanguageSelection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import es.beatkapo.ava_2_final.adapter.AdaptadorTrabajo;
import es.beatkapo.ava_2_final.db.repository.trabajo.TrabajoRepositorySQLite;
import es.beatkapo.ava_2_final.model.Trabajo;

public class Principal extends AppCompatActivity {

    private Button buttonVerTodo;
    private FloatingActionButton buttonAgregar;
    private TextView textViewTotal, textViewPorCompletar;
    private ListView listView;
    private List<Trabajo> trabajos;

    Bundle savedInstanceState;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.savedInstanceState = savedInstanceState;
        loadLanguageSelection(this);
        setContentView(R.layout.activity_principal);
        inicializarComponentes();
    }

    @Override
    protected void onResume() {
        super.onResume();
        inicializarTextViews();
        actualizarLista();
    }

    public void verTodo(View view) {
        Intent intent = new Intent(this, VerTodo.class);
        startActivity(intent);
    }

    public void onAdd(View view) {
        Intent intent = new Intent(this, FormularioTrabajo.class);
        startActivity(intent);
    }

    private void inicializarComponentes() {
        buttonVerTodo = findViewById(R.id.button);
        buttonAgregar = findViewById(R.id.fabButton);
        textViewTotal = findViewById(R.id.textViewTotales);
        textViewPorCompletar = findViewById(R.id.textViewPorCompletar);
        listView = findViewById(R.id.listViewTrabajosPrincipal);
        inicializarTextViews();
        trabajos = obtenerTrabajos();
        actualizarLista();

    }

    private List<Trabajo> obtenerTrabajos() {
        TrabajoRepositorySQLite trabajoRepositorySQLite = new TrabajoRepositorySQLite(this);
        return trabajoRepositorySQLite.getAll();
    }

    private void actualizarLista() {
        trabajos = obtenerTrabajos();

        ordenarLista();
        AdaptadorTrabajo adaptadorTrabajo = new AdaptadorTrabajo(this, trabajos);
        listView.setAdapter(adaptadorTrabajo);
    }

    private void ordenarLista() {
        trabajos.sort((o1, o2) -> o1.getEstado().compareTo(o2.getEstado()));
    }

    private void inicializarTextViews() {
        TrabajoRepositorySQLite trabajoRepositorySQLite = new TrabajoRepositorySQLite(this);
        textViewTotal.setText(String.valueOf((int) trabajoRepositorySQLite.getCount()));
        textViewPorCompletar.setText(String.valueOf((int) trabajoRepositorySQLite.getCountPorCompletar()));
    }

    public void settings(View view) {
        // Usar dialog_ajustes para seleccionar el idioma
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_ajustes, null);
        builder.setView(dialogView);

        ImageButton cancel = dialogView.findViewById(R.id.buttonCancel);
        Button spanish = dialogView.findViewById(R.id.buttonSpanish);
        Button english = dialogView.findViewById(R.id.buttonEnglish);

        AlertDialog dialog = builder.create();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        spanish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLanguageSelection(Principal.this, "es");
                recreate();
                dialog.dismiss();
            }
        });
        english.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveLanguageSelection(Principal.this, "en");
                recreate();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}