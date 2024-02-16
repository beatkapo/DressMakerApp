package es.beatkapo.ava_2_final.db.repository.trabajo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import es.beatkapo.ava_2_final.db.DbHelper;
import es.beatkapo.ava_2_final.db.repository.CRUDRepository;
import es.beatkapo.ava_2_final.db.repository.cliente.ClienteRepositorySQLite;
import es.beatkapo.ava_2_final.db.repository.medida.MedidaRepositorySQLite;
import es.beatkapo.ava_2_final.db.repository.prueba.PruebaRepositorySQLite;
import es.beatkapo.ava_2_final.model.Cliente;
import es.beatkapo.ava_2_final.model.Trabajo;

public class TrabajoRepositorySQLite implements CRUDRepository<Trabajo> {
    private Context context;
    private DbHelper helper;
    private SQLiteDatabase db;

    public TrabajoRepositorySQLite(Context context) {
        this.context = context;
        this.helper = new DbHelper(context);
    }

    @Override
    public long insert(Trabajo entity) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("precioTotal", entity.getPrecioTotal());
        values.put("pagado", entity.getPagado());
        values.put("estado", entity.getEstado().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        values.put("fechaPedido", sdf.format(entity.getFechaPedido()));
        values.put("fechaFin", sdf.format(entity.getFechaFin()));
        values.put("cliente_id", entity.getCliente().getId());
        values.put("tipo", entity.getTipo().toString());

        return db.insert("Trabajo", null, values);
    }

    @Override
    public Trabajo getById(int id) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Trabajo", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        Trabajo t = new Trabajo();
        if (cursor != null && cursor.moveToFirst()) {
            t = rellenarTrabajo(cursor, t);
        }
        return t;
    }

    @Override
    public void update(Trabajo entity) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("precioTotal", entity.getPrecioTotal());
        values.put("pagado", entity.getPagado());
        values.put("estado", entity.getEstado().toString());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        values.put("fechaPedido", sdf.format(entity.getFechaPedido()));
        values.put("fechaFin", sdf.format(entity.getFechaFin()));
        values.put("cliente_id", entity.getCliente().getId());
        values.put("tipo", entity.getTipo().toString());
        if(entity.getMedida() != null) {
            values.put("medidas_id", entity.getMedida().getId());
        }
        String where = "id = ?";
        String[] args = {String.valueOf(entity.getId())};
        db.update("Trabajo", values, where, args);
    }

    @Override
    public void delete(int id) {
        db = helper.getWritableDatabase();
        String where = "id = ?";
        String[] args = {String.valueOf(id)};

        db.delete("Trabajo", where, args);
    }

    @Override
    public List<Trabajo> getAll() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Trabajo", null, null, null, null, null, null);
        List<Trabajo> trabajos = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Trabajo t = new Trabajo();
                t = rellenarTrabajo(cursor, t);
                trabajos.add(t);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return trabajos;
    }

    private Trabajo rellenarTrabajo(Cursor cursor, Trabajo t) {
        int idColumnIndex = cursor.getColumnIndex("id");
        int precioTotalColumnIndex = cursor.getColumnIndex("precioTotal");
        int pagadoColumnIndex = cursor.getColumnIndex("pagado");
        int estadoColumnIndex = cursor.getColumnIndex("estado");
        int fechaPedidoColumnIndex = cursor.getColumnIndex("fechaPedido");
        int fechaFinColumnIndex = cursor.getColumnIndex("fechaFin");
        int tipoColumnIndex = cursor.getColumnIndex("tipo");
        int clienteIdColumnIndex = cursor.getColumnIndex("cliente_id");
        int medidaIdColumnIndex = cursor.getColumnIndex("medidas_id");


        if (idColumnIndex != -1 && precioTotalColumnIndex != -1 && pagadoColumnIndex != -1 && estadoColumnIndex != -1 && fechaPedidoColumnIndex != -1 && fechaFinColumnIndex != -1 && tipoColumnIndex != -1) {
            t.setId(cursor.getInt(idColumnIndex));
            t.setPrecioTotal(cursor.getFloat(precioTotalColumnIndex));
            t.setPagado(cursor.getFloat(pagadoColumnIndex));
            t.setEstado(Trabajo.Estado.valueOf(cursor.getString(estadoColumnIndex)));
            t.setTipo(Trabajo.Tipo.valueOf(cursor.getString(tipoColumnIndex)));
            t.setCliente(new ClienteRepositorySQLite(context).getById(cursor.getInt(clienteIdColumnIndex)));
            t.setMedida(new MedidaRepositorySQLite(context).getById(cursor.getInt(medidaIdColumnIndex)));
            t.setPruebas(new PruebaRepositorySQLite(context).getByTrabajoId(t.getId()));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                t.setFechaPedido(sdf.parse(cursor.getString(fechaPedidoColumnIndex)));
                t.setFechaFin(sdf.parse(cursor.getString(fechaFinColumnIndex)));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return t;
    }

    public long getCount() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Trabajo", null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        db.close();
        return count;
    }

    public long getCountPorCompletar() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM Trabajo WHERE estado != 'TERMINADO'", null);
        cursor.moveToFirst();
        long count = cursor.getLong(0);
        cursor.close();
        db.close();
        return count;
    }

    public float getDeudaCliente(Cliente c) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(precioTotal-pagado) FROM Trabajo WHERE cliente_id = ?", new String[]{String.valueOf(c.getId())});
        cursor.moveToFirst();
        float deuda = cursor.getFloat(0);
        cursor.close();
        db.close();
        return deuda;
    }
}
