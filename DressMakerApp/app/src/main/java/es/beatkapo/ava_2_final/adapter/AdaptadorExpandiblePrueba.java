package es.beatkapo.ava_2_final.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import es.beatkapo.ava_2_final.FormularioPrueba;
import es.beatkapo.ava_2_final.R;
import es.beatkapo.ava_2_final.model.DetallePrueba;
import es.beatkapo.ava_2_final.model.Prueba;
import es.beatkapo.ava_2_final.model.Trabajo;

public class AdaptadorExpandiblePrueba extends BaseExpandableListAdapter {
    private Context context;
    private Trabajo trabajo;
    private LinearLayout primeraPrueba;
    private LinearLayout segundaPrueba;
    private LinearLayout terceraPrueba;
    private View primerDivider;
    private View segundoDivider;

    private ImageButton addPrueba;
    private List<Prueba> pruebas;

    private TextView detalles1;
    private TextView detalles2;
    private TextView detalles3;
    private ImageButton edit1;
    private ImageButton edit2;
    private ImageButton edit3;

    public AdaptadorExpandiblePrueba(Context context, List<Trabajo> trabajos) {
        this.context = context;
        this.trabajo = trabajos.get(0);
        if (trabajo.getPruebas() == null) {
            trabajo.setPruebas(new ArrayList<Prueba>());
        }
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
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        DetallePrueba detallePrueba = new DetallePrueba();
        detallePrueba.setDetalles(trabajo.getPruebas().get(childPosition).getDetalles());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        String fecha = sdf.format(trabajo.getPruebas().get(childPosition).getFecha());
        detallePrueba.setFecha(fecha);
        detallePrueba.setTrabajoId(trabajo.getId());
        return detallePrueba;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return 0;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return trabajo.getPruebas().get(childPosition).getId();
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.group_prueba, null);
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
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_prueba, null);
        }
        setComponents(convertView);

        switch (pruebas.size()) {
            case 0:
                setVisibility(false, false, false, false, false, true);
                setTexts(false, false, false);
                break;
            case 1:
                setVisibility(true, true, false, false, false, true);
                setTexts(true, false, false);
                break;
            case 2:
                setVisibility(true, true, true, true, false, true);
                setTexts(true, true, false);
                break;
            case 3:
                setVisibility(true, true, true, true, true, false);
                setTexts(true, true, true);
                break;
        }

        return convertView;
    }

    private void setComponents(View convertView) {
        primeraPrueba = convertView.findViewById(R.id.primeraPrueba);
        segundaPrueba = convertView.findViewById(R.id.segundaPrueba);
        terceraPrueba = convertView.findViewById(R.id.terceraPrueba);

        primerDivider = convertView.findViewById(R.id.dividerPrimera);
        segundoDivider = convertView.findViewById(R.id.dividerSegunda);

        addPrueba = convertView.findViewById(R.id.addPruebaButton);

        pruebas = trabajo.getPruebas();

        detalles1 = convertView.findViewById(R.id.textDetalles1);
        detalles2 = convertView.findViewById(R.id.textDetalles2);
        detalles3 = convertView.findViewById(R.id.textDetalles3);
        edit1 = convertView.findViewById(R.id.editButton1);
        edit2 = convertView.findViewById(R.id.editButton2);
        edit3 = convertView.findViewById(R.id.editButton3);
    }

    private void setTexts(boolean primera, boolean segunda, boolean tercera) {
        if (primera) {
            detalles1.setText(pruebas.get(0).getDetalles());
            edit1.setOnClickListener(this::onClick);
        }
        if (segunda) {
            detalles2.setText(pruebas.get(1).getDetalles());
            edit2.setOnClickListener(this::onClick);
        }
        if (tercera) {
            detalles3.setText(pruebas.get(2).getDetalles());
            edit3.setOnClickListener(this::onClick);
        }
        addPrueba.setOnClickListener(this::onClick);
    }

    private void setVisibility(boolean primera, boolean divider1, boolean segunda, boolean divider2, boolean tercera, boolean addPruebaButton) {
        primeraPrueba.setVisibility(primera ? View.VISIBLE : View.GONE);
        primerDivider.setVisibility(divider1 ? View.VISIBLE : View.GONE);
        segundaPrueba.setVisibility(segunda ? View.VISIBLE : View.GONE);
        segundoDivider.setVisibility(divider2 ? View.VISIBLE : View.GONE);
        terceraPrueba.setVisibility(tercera ? View.VISIBLE : View.GONE);
        addPrueba.setVisibility(addPruebaButton ? View.VISIBLE : View.GONE);
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    private void onClick(View v) {

        Intent intent = new Intent(context, FormularioPrueba.class);


        if (v.getId() == R.id.editButton1) {
            intent.putExtra("modoEdicion", true);
            intent.putExtra("idPrueba", pruebas.get(0).getId());
        } else if (v.getId() == R.id.editButton2) {
            intent.putExtra("modoEdicion", true);
            intent.putExtra("idPrueba", pruebas.get(1).getId());
        } else if (v.getId() == R.id.editButton3) {
            intent.putExtra("modoEdicion", true);
            intent.putExtra("idPrueba", pruebas.get(2).getId());
        } else if (v.getId() == R.id.addPruebaButton) {
            intent.putExtra("modoEdicion", false);
            intent.putExtra("idTrabajo", trabajo.getId());
        }

        context.startActivity(intent);
    }
}
