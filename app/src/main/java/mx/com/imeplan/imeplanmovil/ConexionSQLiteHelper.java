package mx.com.imeplan.imeplanmovil;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import mx.com.imeplan.imeplanmovil.utilidades.Utilidades;

public class ConexionSQLiteHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME =  "bd_imeplanMovil.db";


    public ConexionSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
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

    public String[] getCategorias(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor;
        String[]categorias = {};
        try {
            //Select categoria from Categoria
            cursor = db.rawQuery("select " + Utilidades.C_CAMPO_CATEGORIA +
                    " from " + Utilidades.TABLA_CATEGORIA, null);
            categorias = new String[cursor.getCount()+1];
            categorias[0]="--Seleccione una categoria---";
            int i = 0;
            if(cursor.moveToFirst()) {
                do {
                    categorias[i + 1] = cursor.getString(0);
                    i++;
                } while (cursor.moveToNext() && i < cursor.getCount());
            }
            db.close();
            cursor.close();
        }catch (Exception e){
            Log.e("Error geting categories",e.getMessage());
        }
        return categorias;
    }

    public String[] getNombreSubcategorias (int idCategoria){
        SQLiteDatabase db = getReadableDatabase();
        //Select subcategoria from SubCategoria where categoria = idCategoria;
        Cursor cursor = db.rawQuery("select "+Utilidades.SC_CAMPO_SUBCATEGORIA+
                " from "+Utilidades.TABLA_SUBCATEGORIA +
                " where "+Utilidades.SC_CAMPO_CATEGORIA+" = "+ idCategoria,null);
        int count = cursor.getCount();
        String [] subcategorias = new String[count];
        int i = 0;
        if(cursor.moveToFirst()){
            do {
                subcategorias[i] = cursor.getString(0);
                i++;
            } while (cursor.moveToNext() && i < count);
        }
        db.close();
        cursor.close();
        return subcategorias;
    }

    public int getIdSubcategoria (String subnombre) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select " + Utilidades.SC_CAMPO_ID +
                " from " + Utilidades.TABLA_SUBCATEGORIA +
                " where " + Utilidades.SC_CAMPO_SUBCATEGORIA + " like '" + subnombre + "'", null);
        int num = cursor.getCount();
        Log.e("num", String.valueOf(num));
        int[] subcategorias = new int[num];
        int i = 0;
        if (cursor.moveToFirst()) {
            do {
                subcategorias[i] = cursor.getInt(0);
                i++;
            } while (cursor.moveToNext());
        }
        db.close();
        cursor.close();
        return  subcategorias[0];
    }

}
