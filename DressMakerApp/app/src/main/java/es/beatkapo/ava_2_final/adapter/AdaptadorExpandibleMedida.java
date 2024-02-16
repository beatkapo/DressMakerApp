package es.beatkapo.ava_2_final.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import es.beatkapo.ava_2_final.FormularioMedida;
import es.beatkapo.ava_2_final.R;
import es.beatkapo.ava_2_final.db.repository.medida.MedidaRepositorySQLite;
import es.beatkapo.ava_2_final.model.DetalleMedida;
import es.beatkapo.ava_2_final.model.Manga;
import es.beatkapo.ava_2_final.model.Medida;
import es.beatkapo.ava_2_final.model.Trabajo;

public class AdaptadorExpandibleMedida extends BaseExpandableListAdapter {
    private Context context;
    private List<Trabajo> medidas;


    public AdaptadorExpandibleMedida(Context context, List<Trabajo> medidas) {
        this.context = context;
        this.medidas = medidas;
    }

    @Override
    public int getGroupCount() {
        return 1;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return medidas.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        DetalleMedida detalleMedida = null;
        Trabajo trabajo = medidas.get(groupPosition);
        if (trabajo.getMedida() != null) {
            String tipoManga = "";
            switch (trabajo.getMedida().getManga().getTipo()) {
                case CORTA:
                    tipoManga = "Corta";
                    break;
                case LARGA:
                    tipoManga = "Larga";
                    break;
                case SEMI:
                    tipoManga = "Semi";
                    break;
            }
            detalleMedida = new DetalleMedida(trabajo.getMedida().getEspalda(), trabajo.getMedida().getCintura(), trabajo.getMedida().getPecho(), trabajo.getMedida().getFalda(), tipoManga, trabajo.getMedida().getManga().getAnchoBrazo(), trabajo.getMedida().getManga().getAnchoAnteBrazo(), trabajo.getMedida().getManga().getLargoManga());

        }
        return detalleMedida;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return medidas.get(groupPosition).getId();
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.group_medida, null);
        }
        ImageView imagen = convertView.findViewById(R.id.imageDesplegable);
        if (isExpanded) {
            imagen.animate().setDuration(250).rotation(180);
        } else {
            imagen.animate().setDuration(250).rotation(0);
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        Trabajo trabajo = medidas.get(groupPosition);
        Medida medida = new MedidaRepositorySQLite(context).getByTrabajo(trabajo.getId());
        DetalleMedida detalleMedida = new DetalleMedida();
        if (medida != null) {
            detalleMedida = new DetalleMedida(medida.getEspalda(), medida.getCintura(), medida.getPecho(), medida.getFalda(), medida.getManga().getTipo().toString(), medida.getManga().getAnchoBrazo(), medida.getManga().getAnchoAnteBrazo(), medida.getManga().getLargoManga());
        }
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_medida, null);
        }
        TextView textEspalda = convertView.findViewById(R.id.textNombreCliente);
        TextView textPecho = convertView.findViewById(R.id.textPecho);
        TextView textCintura = convertView.findViewById(R.id.textTelefonoCliente);
        TextView textFalda = convertView.findViewById(R.id.textFalda);
        TextView textTipo = convertView.findViewById(R.id.textTipo);
        TextView textAnchoBrazo = convertView.findViewById(R.id.textBrazo);
        TextView textAnchoAnteBrazo = convertView.findViewById(R.id.textAntebrazo);
        TextView textLargoManga = convertView.findViewById(R.id.textLargo);

        LinearLayout bloque1 = convertView.findViewById(R.id.bloque1);
        TextView textManga = convertView.findViewById(R.id.textManga);
        LinearLayout bloque2 = convertView.findViewById(R.id.bloque2);
        ImageButton editarButton = convertView.findViewById(R.id.editMedidaButton);
        ImageButton agregarButton = convertView.findViewById(R.id.addMedidaButton);

        Runnable ajustarVisibilidad = () -> {
            int visibility = medida != null ? View.VISIBLE : View.GONE;
            bloque1.setVisibility(visibility);
            bloque2.setVisibility(visibility);
            textManga.setVisibility(visibility);
            editarButton.setVisibility(visibility);
            agregarButton.setVisibility(medida != null ? View.GONE : View.VISIBLE);
        };
        ajustarVisibilidad.run();
        if (medida == null) {
            agregarButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, FormularioMedida.class);
                intent.putExtra("idTrabajo", trabajo.getId());
                context.startActivity(intent);
            });
        } else {

            textEspalda.setText(String.format("%.2f cm", medida.getEspalda()));
            textPecho.setText(String.format("%.2f cm", medida.getPecho()));
            textCintura.setText(String.format("%.2f cm", medida.getCintura()));
            textFalda.setText(String.format("%.2f cm", medida.getFalda()));
            textTipo.setText(detalleMedida.getTipo());
            textAnchoBrazo.setText(String.format("%.2f cm", detalleMedida.getAnchoBrazo()));
            textAnchoAnteBrazo.setText(String.format("%.2f cm", detalleMedida.getAnchoAnteBrazo()));
            textLargoManga.setText(String.format("%.2f cm", detalleMedida.getLargoManga()));

            if (medida.getManga().getTipo() == Manga.Tipo.CORTA) {
                textAnchoAnteBrazo.setVisibility(View.GONE);
            }
            editarButton.setOnClickListener(v -> {
                Intent intent = new Intent(context, FormularioMedida.class);
                intent.putExtra("idMedida", medida.getId());
                intent.putExtra("modoEdicion", true);
                intent.putExtra("idTrabajo", trabajo.getId());
                context.startActivity(intent);
            });
        }


        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

}
