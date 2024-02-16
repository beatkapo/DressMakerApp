package es.beatkapo.ava_2_final.db.repository.prueba;

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
import es.beatkapo.ava_2_final.model.Prueba;

public class PruebaRepositorySQLite implements CRUDRepository<Prueba> {
    DbHelper helper;
    SQLiteDatabase db;
    Context context;

    public PruebaRepositorySQLite(Context context) {
        this.context = context;
        helper = new DbHelper(context);
    }

    @Override
    public long insert(Prueba entity) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("fecha", entity.getFecha().toString());
        values.put("trabajo_id", entity.getTrabajoId());
        values.put("detalles", entity.getDetalles());
        SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
        values.put("fecha", sdf2.format(entity.getFecha()));

        long id = db.insert("Prueba", null, values);
        db.close();
        return id;

    }

    @Override
    public Prueba getById(int id) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Prueba", null, "id = ?", new String[]{String.valueOf(id)}, null, null, null);
        Prueba p = new Prueba();
        if (cursor != null && cursor.moveToFirst()) {
            p = rellenarPrueba(cursor, p);
        }
        db.close();
        return p;
    }

    @Override
    public void update(Prueba entity) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
        values.put("fecha", sdf.format(entity.getFecha()));
        values.put("trabajo_id", entity.getTrabajoId());
        values.put("detalles", entity.getDetalles());
        String where = "id = ?";
        String[] args = {String.valueOf(entity.getId())};

        db.update("Prueba", values, where, args);
        db.close();
    }

    @Override
    public void delete(int id) {
        db = helper.getWritableDatabase();
        db.delete("Prueba", "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    @Override
    public List<Prueba> getAll() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Prueba", null, null, null, null, null, null);
        List<Prueba> pruebas = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Prueba p = new Prueba();
                p = rellenarPrueba(cursor, p);
                pruebas.add(p);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return pruebas;

    }

    public List<Prueba> getByTrabajoId(int trabajoId) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Prueba", null, "trabajo_id = ?", new String[]{String.valueOf(trabajoId)}, null, null, null);
        List<Prueba> pruebas = new ArrayList<>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Prueba p = new Prueba();
                p = rellenarPrueba(cursor, p);
                pruebas.add(p);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();
        return pruebas;
    }

    private Prueba rellenarPrueba(Cursor cursor, Prueba p) {
        int idColumn = cursor.getColumnIndex("id");
        int fechaColumn = cursor.getColumnIndex("fecha");
        int trabajoIdColumn = cursor.getColumnIndex("trabajo_id");
        int detallesColumn = cursor.getColumnIndex("detalles");

        if (idColumn != -1 && fechaColumn != -1 && trabajoIdColumn != -1 && detallesColumn != -1) {
            p.setId(cursor.getInt(idColumn));
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                p.setFecha(sdf.parse(cursor.getString(fechaColumn)));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            p.setTrabajoId(cursor.getInt(trabajoIdColumn));
            p.setDetalles(cursor.getString(detallesColumn));
        }
        return p;
    }

}
