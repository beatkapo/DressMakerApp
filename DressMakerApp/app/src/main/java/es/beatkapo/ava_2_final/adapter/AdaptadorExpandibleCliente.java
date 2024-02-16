package es.beatkapo.ava_2_final.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.List;

import es.beatkapo.ava_2_final.FormularioTrabajo;
import es.beatkapo.ava_2_final.R;
import es.beatkapo.ava_2_final.db.repository.cliente.ClienteRepositorySQLite;
import es.beatkapo.ava_2_final.db.repository.trabajo.TrabajoRepositorySQLite;
import es.beatkapo.ava_2_final.model.Cliente;
import es.beatkapo.ava_2_final.model.DetalleCliente;

public class AdaptadorExpandibleCliente extends BaseExpandableListAdapter {
    Context context;
    List<Cliente> clientes;

    public AdaptadorExpandibleCliente(Context context, List<Cliente> clientes) {
        this.context = context;
        this.clientes = clientes;
    }

    @Override
    public int getGroupCount() {
        return clientes.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return clientes.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        float deuda = new TrabajoRepositorySQLite(context).getDeudaCliente(clientes.get(groupPosition));

        DetalleCliente detalleCliente = new DetalleCliente(
                clientes.get(groupPosition).getNombre(),
                clientes.get(groupPosition).getTelefono(),
                deuda
        );

        return detalleCliente;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return clientes.get(groupPosition).getId();
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
        Cliente cliente = clientes.get(groupPosition);

        if (convertView == null) {
            convertView = View.inflate(context, R.layout.group_cliente, null);
        }
        TextView textView = convertView.findViewById(R.id.textView);
        ImageView imageView = convertView.findViewById(R.id.imageViewDesplegable);
        textView.setText(cliente.getNombre());
        if (isExpanded) {
            imageView.animate().rotation(180).setDuration(250).start();
        } else {
            imageView.animate().rotation(0).setDuration(250).start();
        }
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        DetalleCliente detalleCliente = (DetalleCliente) getChild(groupPosition, childPosition);
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_cliente, null);
        }
        TextView textViewNombre = convertView.findViewById(R.id.textNombreCliente);
        TextView textViewTelefono = convertView.findViewById(R.id.textTelefonoCliente);
        TextView textViewDeuda = convertView.findViewById(R.id.textPecho);
        textViewNombre.setText(detalleCliente.getNombre());
        textViewTelefono.setText(detalleCliente.getTelefono());
        textViewDeuda.setText(String.valueOf(detalleCliente.getDeuda()));

        ImageButton callButton = convertView.findViewById(R.id.callButton);
        ImageButton deleteButton = convertView.findViewById(R.id.deleteButton);
        ImageButton editButton = convertView.findViewById(R.id.deleteButton);

        callButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Cliente c = clientes.get(groupPosition);
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + c.getTelefono()));
                context.startActivity(intent);
            }
        });
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FormularioTrabajo.class);
                intent.putExtra("modoEdicion", true);
                intent.putExtra("idCliente", String.valueOf(clientes.get(groupPosition).getId()));

                context.startActivity(intent);
            }
        });
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog dialog = new AlertDialog.Builder(context)
                        .setTitle(context.getString(R.string.eliminar_cliente))
                        .setMessage(context.getString(R.string.eliminar_cliente_confirmar))
                        .setPositiveButton(context.getString(R.string.si), (dialog1, which) -> {
                            new ClienteRepositorySQLite(context).delete(clientes.get(groupPosition).getId());
                            notifyDataSetChanged();
                            Toast.makeText(context, context.getString(R.string.cliente_eliminado), Toast.LENGTH_SHORT).show();
                        })
                        .setNegativeButton("No", null)
                        .create();

                dialog.show();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
