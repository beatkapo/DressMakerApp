package es.beatkapo.ava_2_final.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "tu_basededatos.db";
    private static final int DATABASE_VERSION = 1;

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        crearTablas(db);
    }

    private void crearTablas(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Cliente (id INTEGER PRIMARY KEY, nombre TEXT, telefono TEXT);");
        db.execSQL("CREATE TABLE Trabajo (id INTEGER PRIMARY KEY, tipo TEXT, precioTotal REAL, pagado REAL, estado TEXT, fechaPedido DATE, fechaFin DATE, cliente_id INTEGER REFERENCES Cliente(id), medidas_id INTEGER REFERENCES Medida(id));");
        db.execSQL("CREATE TABLE Medida (id INTEGER PRIMARY KEY, trabajo_id REFERENCES Trabajo(id), espalda REAL, pecho REAL, cintura REAL, falda REAL, manga_tipo TEXT, manga_anchoBrazo REAL, manga_anchoAnteBrazo REAL, manga_largoManga REAL);");
        db.execSQL("CREATE TABLE Prueba (id INTEGER PRIMARY KEY, trabajo_id INTEGER REFERENCES Trabajo(id), detalles TEXT, fecha DATE);");
        db.execSQL("CREATE TABLE Imagen (id INTEGER PRIMARY KEY, prueba_id INTEGER REFERENCES Prueba(id), ruta TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        crearTablas(sqLiteDatabase);
    }
}
