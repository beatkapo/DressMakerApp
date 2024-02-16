package es.beatkapo.ava_2_final;

import static es.beatkapo.ava_2_final.Utils.loadLanguageSelection;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import es.beatkapo.ava_2_final.adapter.AdaptadorExpandibleMedida;
import es.beatkapo.ava_2_final.adapter.AdaptadorExpandiblePrueba;
import es.beatkapo.ava_2_final.db.repository.trabajo.TrabajoRepositorySQLite;
import es.beatkapo.ava_2_final.model.Trabajo;

public class VerTrabajo extends AppCompatActivity {

    private TextView textEstado, textFechaCreacion, textFechaEntrega, textNombreCliente, textTelefonoCliente, textPrecio, textRecibido, textDeuda, title;
    private ExpandableListView medida, pruebas;
    private ImageButton editarButton, eliminarButton, pagarButton, llamarButton, editEstado;
    private Button historialPagosButton;
    private ImageView imgEstado;
    private Trabajo trabajo;
    private int idTrabajo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadLanguageSelection(this);
        setContentView(R.layout.activity_ver_trabajo);
        idTrabajo = getIntent().getIntExtra("idTrabajo", 0);
        actualizarTrabajo();
        initializeComponents();
    }

    private void actualizarTrabajo() {
        trabajo = new TrabajoRepositorySQLite(this).getById(idTrabajo);
    }

    @Override
    protected void onResume() {
        super.onResume();
        actualizarTrabajo();
        initializeComponents();
    }

    private void initializeComponents() {
        title = findViewById(R.id.textTitle);
        textEstado = findViewById(R.id.textEstado);
        textFechaCreacion = findViewById(R.id.textCreacion);
        textFechaEntrega = findViewById(R.id.textFechaEntrega);
        textNombreCliente = findViewById(R.id.textNombreCliente);
        textTelefonoCliente = findViewById(R.id.textTelefonoCliente);
        textPrecio = findViewById(R.id.textPrecio);
        textRecibido = findViewById(R.id.textRecibido);
        textDeuda = findViewById(R.id.textARecibir);
        medida = findViewById(R.id.medida);
        pruebas = findViewById(R.id.expandableListViewPruebas);
        editarButton = findViewById(R.id.editButton);
        eliminarButton = findViewById(R.id.deleteButton);
        pagarButton = findViewById(R.id.payButton);
        historialPagosButton = findViewById(R.id.pagosButton);
        imgEstado = findViewById(R.id.imageViewEstado);
        llamarButton = findViewById(R.id.callCliente);
        editEstado = findViewById(R.id.editEstadoButton);

        String estado = "";
        int estadoResource = 0;
        switch (trabajo.getEstado()) {
            case TOMAR_MEDIDAS:
                estado = getString(R.string.tomar_medidas);
                estadoResource = R.drawable.ruler_triangle;
                break;
            case EN_PROCESO:
                estado = getString(R.string.en_proceso);
                estadoResource = R.drawable.gears;
                break;
            case PRUEBA:
                estado = getString(R.string.prueba);
                estadoResource = R.drawable.test;
                break;
            case TERMINADO:
                estado = getString(R.string.terminado);
                estadoResource = R.drawable.check_3917749;
                break;
        }
        imgEstado.setImageResource(estadoResource);
        textEstado.setText(estado);
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        textFechaCreacion.setText(sdf.format(trabajo.getFechaPedido()));
        textFechaEntrega.setText(sdf.format(trabajo.getFechaFin()));
        textNombreCliente.setText(trabajo.getCliente().getNombre());
        textTelefonoCliente.setText(trabajo.getCliente().getTelefono());
        textPrecio.setText(String.format("%.2f €", trabajo.getPrecioTotal()));
        textRecibido.setText(String.format("%.2f €", trabajo.getPagado()));
        textDeuda.setText(String.format("%.2f €", trabajo.getPrecioTotal() - trabajo.getPagado()));


        List<Trabajo> lista = new ArrayList<>();
        title.setText(getString(R.string.trabajo) + " - " + trabajo.getCliente().getNombre());
        lista.add(trabajo);

        AdaptadorExpandibleMedida adaptadorExpandibleMedida = new AdaptadorExpandibleMedida(this, lista);
        medida.setAdapter(adaptadorExpandibleMedida);

        AdaptadorExpandiblePrueba adaptadorExpandiblePrueba = new AdaptadorExpandiblePrueba(this, lista);
        pruebas.setAdapter(adaptadorExpandiblePrueba);
    }

    public void edit(View view) {
        Intent intent = new Intent(this, FormularioTrabajo.class);
        intent.putExtra("modoEdicion", true);
        intent.putExtra("idTrabajo", String.valueOf(idTrabajo));
        startActivity(intent);
    }

    public void delete(View view) {
        AlertDialog confirmDialog = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.eliminar_trabajo))
                .setMessage(getString(R.string.eliminar_trabajo_confirmar))
                .setPositiveButton(getString(R.string.si), (dialog, which) -> {
                    new TrabajoRepositorySQLite(this).delete(trabajo.getId());
                    finish();
                })
                .setNegativeButton("No", null)
                .create();
        confirmDialog.show();
    }

    public void pay(View view) {
        mostrarDialogoPago();
    }

    public void historialPagos(View view) {

    }

    public void editEstado(View view) {
        mostrarDialogoEstado();
    }

    public void call(View view) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(android.net.Uri.parse("tel:" + trabajo.getCliente().getTelefono()));
        startActivity(intent);
    }

    private void mostrarDialogoPago() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_pago, null);
        builder.setView(dialogView);

        EditText editTextMontoPago = dialogView.findViewById(R.id.editTextCantidad);
        ImageButton btnGuardarPago = dialogView.findViewById(R.id.confirmPayButton);
        ImageButton btnCancelarPago = dialogView.findViewById(R.id.cancelPayButton);

        AlertDialog dialog = builder.create();


        btnGuardarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obtener el monto del pago ingresado
                String cantidadStr = editTextMontoPago.getText().toString();
                if (!cantidadStr.isEmpty()) {
                    float cantidad = Float.parseFloat(cantidadStr);
                    AlertDialog confirmDialog = new AlertDialog.Builder(VerTrabajo.this)
                            .setTitle(getString(R.string.registrar_pago))
                            .setMessage(getString(R.string.pago_confirmar) + cantidad + " €?")
                            .setPositiveButton(getString(R.string.si), (dialog, which) -> {
                                trabajo.pagar(cantidad);
                                new TrabajoRepositorySQLite(VerTrabajo.this).update(trabajo);
                                Toast.makeText(VerTrabajo.this, getString(R.string.pago_realizado), Toast.LENGTH_SHORT).show();
                                initializeComponents();
                                dialog.dismiss();
                            })
                            .setNegativeButton("No", null)
                            .create();
                    confirmDialog.show();
                    dialog.dismiss();
                } else {
                    Toast.makeText(VerTrabajo.this, getString(R.string.ingresar_cantidad), Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnCancelarPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Cerrar el diálogo
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    private void mostrarDialogoEstado() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = getLayoutInflater().inflate(R.layout.dialog_estado, null);
        builder.setView(dialogView);

        Button btnTomarMedidas = dialogView.findViewById(R.id.tomarMedidasButton);
        Button btnEnProceso = dialogView.findViewById(R.id.enProcesoButton);
        Button btnPrueba = dialogView.findViewById(R.id.pruebaButton);
        Button btnTerminado = dialogView.findViewById(R.id.terminadoButton);
        ImageButton btnCancelar = dialogView.findViewById(R.id.cancelEstadoButton);

        AlertDialog dialog = builder.create();

        btnTomarMedidas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trabajo.setEstado(Trabajo.Estado.TOMAR_MEDIDAS);
                new TrabajoRepositorySQLite(VerTrabajo.this).update(trabajo);
                initializeComponents();
                dialog.dismiss();
            }
        });
        btnEnProceso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trabajo.setEstado(Trabajo.Estado.EN_PROCESO);
                new TrabajoRepositorySQLite(VerTrabajo.this).update(trabajo);
                initializeComponents();
                dialog.dismiss();
            }
        });
        btnPrueba.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trabajo.setEstado(Trabajo.Estado.PRUEBA);
                new TrabajoRepositorySQLite(VerTrabajo.this).update(trabajo);
                initializeComponents();
                dialog.dismiss();
            }
        });
        btnTerminado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                trabajo.setEstado(Trabajo.Estado.TERMINADO);
                new TrabajoRepositorySQLite(VerTrabajo.this).update(trabajo);
                initializeComponents();
                dialog.dismiss();
            }
        });
        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

}