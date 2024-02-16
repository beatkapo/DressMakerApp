package es.beatkapo.ava_2_final.db.repository.imagen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.beatkapo.ava_2_final.db.DbHelper;
import es.beatkapo.ava_2_final.db.repository.CRUDRepository;

public class ImagenRepositorySQLite implements CRUDRepository<String> {
    DbHelper helper;
    SQLiteDatabase db;
    Context context;

    public ImagenRepositorySQLite(Context context) {
        this.context = context;
        helper = new DbHelper(context);
    }

    @Override
    public long insert(String entity) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ruta", entity);
        db.close();
        return db.insert("Imagen", null, values);
    }

    @Override
    public String getById(int id) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Imagen", null, "id=" + new String[]{String.valueOf(id)}, null, null, null, null);
        String ruta = "";
        int rutaColumnIndex = cursor.getColumnIndex("ruta");

        if (cursor != null && cursor.moveToFirst()) {
            ruta = cursor.getString(rutaColumnIndex);
        }
        db.close();
        return ruta;
    }

    @Override
    public void update(String entity) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ruta", entity);
        db.close();
    }

    @Override
    public void delete(int id) {
        db = helper.getWritableDatabase();
        String where = "id = ?";
        String[] args = {String.valueOf(id)};
        db.delete("Imagen", where, args);
        db.close();
    }

    @Override
    public List<String> getAll() {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Imagen", null, null, null, null, null, null);
        List<String> rutas = new ArrayList<>();
        int rutaColumnIndex = cursor.getColumnIndex("ruta");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                rutas.add(cursor.getString(rutaColumnIndex));
            } while (cursor.moveToNext());
        }
        db.close();
        return rutas;
    }

    public long insert(String ruta, int pruebaId) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ruta", ruta);
        values.put("prueba_id", pruebaId);
        long id = db.insert("Imagen", null, values);
        db.close();
        return id;
    }

    public void update(String ruta, int pruebaId) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("ruta", ruta);
        String where = "prueba_id = ?";
        String[] args = {String.valueOf(pruebaId)};
        db.update("Imagen", values, where, args);
        db.close();
    }

    public void getByPrueba(int pruebaId) {
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Imagen", null, "prueba_id = ?", new String[]{String.valueOf(pruebaId)}, null, null, null);
        List<String> rutas = new ArrayList<>();
        int rutaColumnIndex = cursor.getColumnIndex("ruta");
        if (cursor != null && cursor.moveToFirst()) {
            do {
                rutas.add(cursor.getString(rutaColumnIndex));
            } while (cursor.moveToNext());
        }
        db.close();
    }
}
