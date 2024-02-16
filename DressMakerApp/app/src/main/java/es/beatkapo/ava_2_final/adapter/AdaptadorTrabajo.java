package es.beatkapo.ava_2_final.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.text.SimpleDateFormat;
import java.util.List;

import es.beatkapo.ava_2_final.R;
import es.beatkapo.ava_2_final.model.Trabajo;

public class AdaptadorTrabajo extends BaseAdapter {
    private List<Trabajo> trabajos;
    private Context context;

    public AdaptadorTrabajo(Context context, List<Trabajo> trabajos) {
        this.context = context;
        this.trabajos = trabajos;
    }

    @Override
    public int getCount() {
        return trabajos.size();
    }

    @Override
    public Object getItem(int position) {
        return trabajos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return trabajos.get(position).getId();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_trabajo, parent, false);
        }

        Trabajo trabajo = trabajos.get(position);

        TextView textViewCliente = convertView.findViewById(R.id.textCustomer);
        TextView textViewFecha = convertView.findViewById(R.id.textDate);
        ImageView imageView = convertView.findViewById(R.id.imageStatus);

        textViewCliente.setText((trabajo.getTipo() == Trabajo.Tipo.TRAJE ? context.getString(R.string.traje) : context.getString(R.string.arreglo)) + " - " + trabajo.getCliente().getNombre());
        textViewFecha.setText(new SimpleDateFormat("dd/MM/yyyy").format(trabajo.getFechaFin()));

        switch (trabajo.getEstado()) {
            case TOMAR_MEDIDAS:
                imageView.setImageResource(R.drawable.ruler_triangle);
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.measure_icon));
                break;
            case EN_PROCESO:
                imageView.setImageResource(R.drawable.gears);
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.white));
                break;
            case PRUEBA:
                imageView.setImageResource(R.drawable.test);
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.test_icon));
                break;
            case TERMINADO:
                imageView.setImageResource(R.drawable.check_3917749);
                imageView.setColorFilter(ContextCompat.getColor(context, R.color.call_button));
                break;
        }

        return convertView;
    }
}
