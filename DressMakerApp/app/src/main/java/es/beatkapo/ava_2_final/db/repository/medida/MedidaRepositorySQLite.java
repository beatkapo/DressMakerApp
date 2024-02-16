package es.beatkapo.ava_2_final.db.repository.medida;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import es.beatkapo.ava_2_final.db.DbHelper;
import es.beatkapo.ava_2_final.db.repository.CRUDRepository;
import es.beatkapo.ava_2_final.model.Manga;
import es.beatkapo.ava_2_final.model.Medida;

public class MedidaRepositorySQLite implements CRUDRepository<Medida> {
    private DbHelper helper;
    private SQLiteDatabase db;

    public MedidaRepositorySQLite(Context context) {
        this.helper = new DbHelper(context);
    }

    @Override
    public long insert(Medida entity) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("espalda", entity.getEspalda());
        values.put("pecho", entity.getPecho());
        values.put("cintura", entity.getCintura());
        values.put("falda", entity.getFalda());
        values.put("manga_tipo", entity.getManga().getTipo().toString());
        values.put("manga_anchoBrazo", entity.getManga().getAnchoBrazo());
        values.put("manga_anchoAnteBrazo", entity.getManga().getAnchoAnteBrazo());
        values.put("manga_largoManga", entity.getManga().getLargoManga());
        values.put("trabajo_id", entity.getTrabajoId());


        return db.insert("Medida", null, values);
    }

    @Override
    public Medida getById(int id) {
        Medida m = new Medida();
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Medida", null, "id=" + String.valueOf(id), null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            m = rellenarMedida(cursor, m);
        }
        return m;
    }

    @Override
    public void update(Medida entity) {
        db = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("espalda", entity.getEspalda());
        values.put("pecho", entity.getPecho());
        values.put("cintura", entity.getCintura());
        values.put("falda", entity.getFalda());
        values.put("manga_tipo", entity.getManga().getTipo().toString());
        values.put("manga_anchoBrazo", entity.getManga().getAnchoBrazo());
        values.put("manga_anchoAnteBrazo", entity.getManga().getAnchoAnteBrazo());
        values.put("manga_largoManga", entity.getManga().getLargoManga());
        values.put("trabajo_id", entity.getTrabajoId());

        db.update("Medida", values, "id = ?", new String[]{String.valueOf(entity.getId())});

    }

    @Override
    public void delete(int id) {
        db = helper.getWritableDatabase();
        db.delete("Medida", "id = ?", new String[]{String.valueOf(id)});
    }

    @Override
    public List<Medida> getAll() {
        List<Medida> medidas = new ArrayList<>();
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Medida", null, null, null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                Medida m = new Medida();
                m = rellenarMedida(cursor, m);
                medidas.add(m);
            } while (cursor.moveToNext());
        }
        if (cursor != null) {
            cursor.close();
        }
        db.close();

        return medidas;
    }

    private Medida rellenarMedida(Cursor cursor, Medida m) {
        int idColumnIndex = cursor.getColumnIndex("id");
        int espaldaColumnIndex = cursor.getColumnIndex("espalda");
        int pechoColumnIndex = cursor.getColumnIndex("pecho");
        int cinturaColumnIndex = cursor.getColumnIndex("cintura");
        int faldaColumnIndex = cursor.getColumnIndex("falda");
        int manga_tipoColumnIndex = cursor.getColumnIndex("manga_tipo");
        int manga_anchoBrazoColumnIndex = cursor.getColumnIndex("manga_anchoBrazo");
        int manga_anchoAnteBrazoColumnIndex = cursor.getColumnIndex("manga_anchoAnteBrazo");
        int manga_largoMangaColumnIndex = cursor.getColumnIndex("manga_largoManga");
        int trabajoIdColumnIndex = cursor.getColumnIndex("trabajo_id");

        if (idColumnIndex != -1 && espaldaColumnIndex != -1 && pechoColumnIndex != -1 && cinturaColumnIndex != -1 && faldaColumnIndex != -1 && manga_tipoColumnIndex != -1 && manga_anchoBrazoColumnIndex != -1 && manga_anchoAnteBrazoColumnIndex != -1 && manga_largoMangaColumnIndex != -1) {
            m = new Medida();
            m.setId(cursor.getInt(idColumnIndex));
            m.setEspalda(cursor.getFloat(espaldaColumnIndex));
            m.setPecho(cursor.getFloat(pechoColumnIndex));
            m.setCintura(cursor.getFloat(cinturaColumnIndex));
            m.setFalda(cursor.getFloat(faldaColumnIndex));
            m.setManga(new Manga(
                    cursor.getFloat(manga_anchoBrazoColumnIndex),
                    cursor.getFloat(manga_anchoAnteBrazoColumnIndex),
                    cursor.getFloat(manga_largoMangaColumnIndex),
                    Manga.Tipo.valueOf(cursor.getString(manga_tipoColumnIndex)))
            );
            m.setTrabajoId(cursor.getInt(trabajoIdColumnIndex));
        }
        return m;
    }

    public Medida getByTrabajo(int idTrabajo) {
        Medida m = null; // Inicializamos con null para detectar si no se encontraron resultados
        db = helper.getReadableDatabase();
        Cursor cursor = db.query("Medida", null, "trabajo_id=" + String.valueOf(idTrabajo), null, null, null, null);
        if (cursor != null) {
            Log.d("CursorCount", "Number of rows: " + cursor.getCount());
            if (cursor.moveToFirst()) {
                m = new Medida();
                m = rellenarMedida(cursor, m);
            }
        }
        cursor.close(); // Importante cerrar el cursor cuando hayamos terminado con él
        return m;
    }
}
