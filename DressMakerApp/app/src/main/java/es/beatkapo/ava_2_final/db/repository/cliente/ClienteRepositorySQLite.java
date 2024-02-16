package es.beatkapo.ava_2_final.db.repository.cliente;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.beatkapo.ava_2_final.db.DbHelper;
import es.beatkapo.ava_2_final.db.repository.CRUDRepository;
import es.beatkapo.ava_2_final.model.Cliente;

public class ClienteRepositorySQLite implements CRUDRepository<Cliente> {
    private DbHelper helper;
    private SQLiteDatabase db;

    public ClienteRepositorySQLite(Context context) {
        this.helper = new DbHelper(context);
    }

    @Override
    public long insert(Cliente entity) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", entity.getNombre());
        values.put("telefono", entity.getTelefono());

        return db.insert("Cliente", null, values);
    }

    @Override
    public Cliente getById(int id) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Cliente", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        Cliente cliente = new Cliente();
        if (cursor != null && cursor.moveToFirst()) {
            cliente = rellenarCliente(cursor, cliente);
        }
        return cliente;
    }

    @Override
    public void update(Cliente entity) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("nombre", entity.getNombre());
        values.put("telefono", entity.getTelefono());
        String where = "id = ?";
        String[] args = {String.valueOf(entity.getId())};

        int filasAfectadas = db.update("Cliente", values, where, args);

        if (filasAfectadas > 0) {
            Log.d("UPDATE", "Cliente actualizado con éxito.");
        } else {
            Log.e("UPDATE", "Error al actualizar el cliente");
        }
    }

    @Override
    public void delete(int id) {
        db = helper.getWritableDatabase();
        String where = "id = ?";
        String[] args = {String.valueOf(id)};

        int filasAfectadas = db.delete("Cliente", where, args);

        if (filasAfectadas > 0) {
            Log.d("DELETE", "Cliente eliminado con éxito");
        } else {
            Log.e("DELETE", "Error al eliminar el cliente");
        }
    }

    @Override
    public List<Cliente> getAll() {
        List<Cliente> clientes = new ArrayList<>();
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Cliente", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Cliente cliente = rellenarCliente(cursor, new Cliente());
                clientes.add(cliente);

            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }

        db.close();
        return clientes;
    }

    private Cliente rellenarCliente(Cursor cursor, Cliente c) {
        //Creo que es mejor utilizar el metodo getColumnIndex, de este modo no he de recordar en que posicion estaban.
        c = new Cliente();
        int idColumnIndex = cursor.getColumnIndex("id");
        int nombreColumnIndex = cursor.getColumnIndex("nombre");
        int telefonoColumnIndex = cursor.getColumnIndex("telefono");

        if (idColumnIndex != -1 && nombreColumnIndex != -1 && telefonoColumnIndex != -1) { //Se debe controlar que el metodo getColumIndex no devuelve -1, de lo contrario el programa peta.
            c.setId(cursor.getInt(idColumnIndex));
            c.setNombre(cursor.getString(nombreColumnIndex));
            c.setTelefono(cursor.getString(telefonoColumnIndex));
        }
        return c;
    }
}
