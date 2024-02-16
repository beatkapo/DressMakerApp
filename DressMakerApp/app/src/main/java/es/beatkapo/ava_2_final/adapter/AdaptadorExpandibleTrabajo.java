package es.beatkapo.ava_2_final.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.List;

import es.beatkapo.ava_2_final.FormularioTrabajo;
import es.beatkapo.ava_2_final.R;
import es.beatkapo.ava_2_final.VerTrabajo;
import es.beatkapo.ava_2_final.model.Cliente;
import es.beatkapo.ava_2_final.model.DetalleTrabajo;
import es.beatkapo.ava_2_final.model.Trabajo;

public class AdaptadorExpandibleTrabajo extends BaseExpandableListAdapter {
    private Context context;
    private List<Trabajo> trabajos;

    public AdaptadorExpandibleTrabajo(Context context, List<Trabajo> trabajos) {
        this.context = context;
        this.trabajos = trabajos;
    }

    @Override
    public int getGroupCount() {
        return trabajos.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return trabajos.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        Trabajo trabajo = trabajos.get(groupPosition);
        DetalleTrabajo detalleTrabajo = new DetalleTrabajo(
                new SimpleDateFormat("dd/MM/yyyy").format(trabajo.getFechaFin()),
                trabajo.getPrecioTotal() - trabajo.getPagado(),
                trabajo.getEstado()
        );
        return detalleTrabajo;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return trabajos.get(groupPosition).getId();
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @SuppressLint("ResourceAsColor")
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Trabajo trabajo = (Trabajo) getGroup(groupPosition);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.group_trabajo, null);
        }
        ImageView imageView = convertView.findViewById(R.id.imageDesplegable);
        if (isExpanded) {
            imageView.animate().rotation(180).setDuration(250).start();
        } else {
            imageView.animate().rotation(0).setDuration(250).start();
        }
        TextView title = convertView.findViewById(R.id.textViewTitle);
        String text = (trabajo.getTipo() == Trabajo.Tipo.TRAJE ? context.getString(R.string.traje) : context.getString(R.string.arreglo)) + " - " + trabajo.getCliente().getNombre();
        title.setText(text);
        ImageView ivEstado = convertView.findViewById(R.id.imageView);
        switch (trabajo.getEstado()) {
            case TOMAR_MEDIDAS:
                ivEstado.setImageResource(R.drawable.ruler_triangle);
                ivEstado.setColorFilter(ContextCompat.getColor(context, R.color.measure_icon));
                break;
            case EN_PROCESO:
                ivEstado.setImageResource(R.drawable.gears);
                ivEstado.setColorFilter(ContextCompat.getColor(context, R.color.white));
                break;
            case PRUEBA:
                ivEstado.setImageResource(R.drawable.test);
                ivEstado.setColorFilter(ContextCompat.getColor(context, R.color.test_icon));
                break;
            case TERMINADO:
                ivEstado.setImageResource(R.drawable.check_3917749);
                ivEstado.setColorFilter(ContextCompat.getColor(context, R.color.call_button));
                break;
        }

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        DetalleTrabajo detalle = (DetalleTrabajo) getChild(groupPosition, childPosition);
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_trabajo, parent, false);
        }
        TextView tvFecha = convertView.findViewById(R.id.textNombreCliente);
        TextView tvDinero = convertView.findViewById(R.id.textPecho);
        TextView tvEstado = convertView.findViewById(R.id.textTelefonoCliente);

        tvFecha.setText(detalle.getFechaEntrega());
        tvDinero.setText(String.valueOf(detalle.getDineroPorPagar()) + " €");
        String estado = "";
        switch (detalle.getEstado()) {
            case TOMAR_MEDIDAS:
                estado = context.getString(R.string.tomar_medidas);
                break;
            case EN_PROCESO:
                estado = context.getString(R.string.en_proceso);
                break;
            case PRUEBA:
                estado = context.getString(R.string.prueba);
                break;
            case TERMINADO:
                estado = context.getString(R.string.terminado);
                break;
        }
        tvEstado.setText(estado);

        ImageButton callButton = convertView.findViewById(R.id.callButton);
        ImageButton editButton = convertView.findViewById(R.id.deleteButton);
        ImageButton moreButton = convertView.findViewById(R.id.moreButton);
        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cliente c = trabajos.get(groupPosition).getCliente();
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + c.getTelefono()));
                context.startActivity(intent);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FormularioTrabajo.class);
                intent.putExtra("modoEdicion", true);
                intent.putExtra("idTrabajo", String.valueOf(trabajos.get(groupPosition).getId()));

                context.startActivity(intent);
            }
        });
        moreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VerTrabajo.class);
                intent.putExtra("idTrabajo", trabajos.get(groupPosition).getId());
                context.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
