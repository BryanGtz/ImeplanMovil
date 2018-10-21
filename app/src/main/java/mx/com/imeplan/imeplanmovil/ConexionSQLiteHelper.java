package mx.com.imeplan.imeplanmovil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import mx.com.imeplan.imeplanmovil.utilidades.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public ConexionSQLiteHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Utilidades.CREAR_TABLA_CATEGORIA);
        db.execSQL(Utilidades.CREAR_TABLA_SUBCATEGORIA);
        db.execSQL(Utilidades.CREAR_TABLA_REPORTE);

        db.execSQL(Utilidades.INSERTAR_CATEGORIAS);
        db.execSQL(Utilidades.INSERTAR_SUBCATEGORIAS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int versionAntigua, int versionNueva) {
        db.execSQL("drop table if exists "+Utilidades.TABLA_CATEGORIA);
        db.execSQL("drop table if exists "+Utilidades.TABLA_SUBCATEGORIA);
        db.execSQL("drop table if exists "+Utilidades.TABLA_REPORTE);
        onCreate(db);
    }

}
