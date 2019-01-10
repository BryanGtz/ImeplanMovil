package mx.com.imeplan.imeplanmovil.utilidades;

public class Utilidades {

    // Constantes campos tabla Categoria
    public static final String TABLA_CATEGORIA = "Categoria";
    public static final String C_CAMPO_ID = "id";
    public static final String C_CAMPO_CATEGORIA = "categoria";

    // Constantes campos tabla SubCategoria
    public static final String TABLA_SUBCATEGORIA = "SubCategoria";
    public static final String SC_CAMPO_ID = "id";
    public static final String SC_CAMPO_CATEGORIA = "categoria";
    public static final String SC_CAMPO_SUBCATEGORIA = "subcategoria";

    // Constantes campos tabla Reporte
    public static final String TABLA_REPORTE = "Reporte";
    public static final String R_CAMPO_ID = "id";
    public static final String R_CAMPO_SUBCATEGORIA = "subcategoria";
    public static final String R_CAMPO_LATITUD = "latitud";
    public static final String R_CAMPO_LONGITUD = "longitud";
    public static final String R_CAMPO_FOTO = "foto";
    public static final String R_CAMPO_FECHA = "fecha";
    public static final String R_CAMPO_ESTADO = "estado";

    public static final String CREAR_TABLA_CATEGORIA =
            "create table if not exists "+TABLA_CATEGORIA+" ("+C_CAMPO_ID+" INTEGER PRIMARY KEY, "+C_CAMPO_CATEGORIA+" TEXT)";

    public static final String CREAR_TABLA_SUBCATEGORIA =
            "create table if not exists "+TABLA_SUBCATEGORIA+" ("+SC_CAMPO_ID+" INTEGER PRIMARY KEY, "+SC_CAMPO_CATEGORIA+" INTEGER, " +
                    SC_CAMPO_SUBCATEGORIA+" TEXT, FOREIGN KEY("+SC_CAMPO_CATEGORIA+") REFERENCES "+TABLA_CATEGORIA+"("+C_CAMPO_ID+"))";

    public static final String CREAR_TABLA_REPORTE =
            "create table if not exists "+TABLA_REPORTE+" ("+R_CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+R_CAMPO_SUBCATEGORIA+" INTEGER, "+R_CAMPO_LATITUD+" TEXT, " +
                    R_CAMPO_LONGITUD+" TEXT, "+R_CAMPO_FOTO+" TEXT, "+R_CAMPO_FECHA+" DATE, "+R_CAMPO_ESTADO+" INTEGER, " +
                    "FOREIGN KEY("+R_CAMPO_SUBCATEGORIA+") REFERENCES "+TABLA_SUBCATEGORIA+"("+SC_CAMPO_ID+"))";

    // Llenar tabla Categorias
    public static final String INSERTAR_CATEGORIAS =
            "insert into "+TABLA_CATEGORIA+" ("+C_CAMPO_ID+","+C_CAMPO_CATEGORIA+") values" +
                    "(1,'COMAPA'),"+
                    "(2,'SERVICIOS PUBLICOS'),"+
                    "(3,'CUADRILLA ECOLOGICA'),"+
                    "(4,'OBRAS PUBLICAS'),"+
                    "(5,'VIALIDAD'),"+
                    "(6,'OTROS');";

    // Llenar tabla SubCategorias
    public static final String INSERTAR_SUBCATEGORIAS =
            "insert into "+TABLA_SUBCATEGORIA+" values" +
                    "(1,1,'Fuga de agua'),"+
                    "(2,1,'Corte de agua'),"+
                    "(3,1,'Azolve de drenaje'),"+
                    "(4,2,'Recolección de basura'),"+
                    "(5,2,'Alumbrado publico'),"+
                    "(6,2,'Poste caido'),"+
                    "(7,2,'Bache'),"+
                    "(8,3,'Árbol caído'),"+
                    "(9,3,'Maltrato animal'),"+
                    "(10,3,'Reporte sanitario'),"+
                    "(11,4,'Calle sin pavimentar'),"+
                    "(12,4,'Desechos de construcción'),"+
                    "(13,4,'Reencarpetado'),"+
                    "(14,4,'Tope'),"+
                    "(15,5,'Accidente frecuente'),"+
                    "(16,5,'Vialidad peligrosa'),"+
                    "(17,5,'Semaforo sin funcionar'),"+
                    "(18,6,'General');";

    // Ver reporte
    public static final String VER_REPORTE = "select "+TABLA_REPORTE+"."+R_CAMPO_ID+","+TABLA_SUBCATEGORIA+"."+SC_CAMPO_SUBCATEGORIA
            +","+TABLA_CATEGORIA+"."+C_CAMPO_CATEGORIA+","+TABLA_REPORTE+"."+R_CAMPO_LATITUD+","+TABLA_REPORTE+"."+R_CAMPO_LONGITUD+","
            +TABLA_REPORTE+"."+R_CAMPO_FOTO+","+TABLA_REPORTE+"."+R_CAMPO_FECHA+","+TABLA_REPORTE+"."+R_CAMPO_ESTADO+" from "
            +TABLA_CATEGORIA+" join "+TABLA_SUBCATEGORIA+" join "+TABLA_REPORTE+
            " on "+TABLA_CATEGORIA+"."+C_CAMPO_ID+"="+TABLA_SUBCATEGORIA+"."+SC_CAMPO_CATEGORIA+
            " and "+TABLA_SUBCATEGORIA+"."+SC_CAMPO_ID+"="+TABLA_REPORTE+"."+R_CAMPO_SUBCATEGORIA+"" +
            " where "+R_CAMPO_ESTADO+" = 1;";

    // Ver borrador
    public static final String VER_BORRADOR = "select "+TABLA_REPORTE+"."+R_CAMPO_ID+","+TABLA_SUBCATEGORIA+"."+SC_CAMPO_SUBCATEGORIA
            +","+TABLA_CATEGORIA+"."+C_CAMPO_CATEGORIA+","+TABLA_REPORTE+"."+R_CAMPO_LATITUD+","+TABLA_REPORTE+"."+R_CAMPO_LONGITUD+","
            +TABLA_REPORTE+"."+R_CAMPO_FOTO+","+TABLA_REPORTE+"."+R_CAMPO_FECHA+","+TABLA_REPORTE+"."+R_CAMPO_ESTADO+" from "
            +TABLA_CATEGORIA+" join "+TABLA_SUBCATEGORIA+" join "+TABLA_REPORTE+
            " on "+TABLA_CATEGORIA+"."+C_CAMPO_ID+"="+TABLA_SUBCATEGORIA+"."+SC_CAMPO_CATEGORIA+
            " and "+TABLA_SUBCATEGORIA+"."+SC_CAMPO_ID+"="+TABLA_REPORTE+"."+R_CAMPO_SUBCATEGORIA+"" +
            " where "+R_CAMPO_ESTADO+" = 0;";

    // Ver ultimo reporte
    public static final String VER_ULTIMO_REPORTE = "select "+TABLA_REPORTE+"."+R_CAMPO_ID+","+TABLA_SUBCATEGORIA+"."+SC_CAMPO_SUBCATEGORIA
            +","+TABLA_CATEGORIA+"."+C_CAMPO_CATEGORIA+","+TABLA_REPORTE+"."+R_CAMPO_LATITUD+","+TABLA_REPORTE+"."+R_CAMPO_LONGITUD+","
            +TABLA_REPORTE+"."+R_CAMPO_FOTO+","+TABLA_REPORTE+"."+R_CAMPO_FECHA+","+TABLA_REPORTE+"."+R_CAMPO_ESTADO+" from "
            +TABLA_CATEGORIA+" join "+TABLA_SUBCATEGORIA+" join "+TABLA_REPORTE+
            " on "+TABLA_CATEGORIA+"."+C_CAMPO_ID+"="+TABLA_SUBCATEGORIA+"."+SC_CAMPO_CATEGORIA+
            " and "+TABLA_SUBCATEGORIA+"."+SC_CAMPO_ID+"="+TABLA_REPORTE+"."+R_CAMPO_SUBCATEGORIA+"" +
            " where Reporte."+R_CAMPO_ID+" = (select MAX(Reporte.id) from Reporte);";

}
