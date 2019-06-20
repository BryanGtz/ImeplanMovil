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
    public static final String R_CAMPO_DIRECCION = "direccion";

    public static final String CREAR_TABLA_CATEGORIA =
            "create table if not exists "+TABLA_CATEGORIA+" ("+C_CAMPO_ID+" INTEGER PRIMARY KEY, "+C_CAMPO_CATEGORIA+" TEXT)";

    public static final String CREAR_TABLA_SUBCATEGORIA =
            "create table if not exists "+TABLA_SUBCATEGORIA+" ("+SC_CAMPO_ID+" INTEGER PRIMARY KEY, "+SC_CAMPO_CATEGORIA+" INTEGER, " +
                    SC_CAMPO_SUBCATEGORIA+" TEXT, FOREIGN KEY("+SC_CAMPO_CATEGORIA+") REFERENCES "+TABLA_CATEGORIA+"("+C_CAMPO_ID+"))";

    public static final String CREAR_TABLA_REPORTE =
            "create table if not exists "+TABLA_REPORTE+" ("+
                    R_CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    R_CAMPO_SUBCATEGORIA+" INTEGER, "+
                    R_CAMPO_LATITUD+" TEXT, " +
                    R_CAMPO_LONGITUD+" TEXT, "+
                    R_CAMPO_DIRECCION+" TEXT, "+
                    R_CAMPO_FOTO+" TEXT, "+
                    R_CAMPO_FECHA+" DATE, "+
                    R_CAMPO_ESTADO+" INTEGER, " +
                    "FOREIGN KEY("+R_CAMPO_SUBCATEGORIA+") REFERENCES "+TABLA_SUBCATEGORIA+"("+SC_CAMPO_ID+"))";

    // Llenar tabla Categorias
    public static final String INSERTAR_CATEGORIAS =
            "insert into "+TABLA_CATEGORIA+" ("+C_CAMPO_ID+","+C_CAMPO_CATEGORIA+") values" +
                    "(1,'Vialidad'),"+
                    "(2,'Obras públicas'),"+
                    "(3,'Cuadrilla ecológica'),"+
                    "(4,'Servicios públicos'),"+
                    "(5,'COMAPA'),"+
                    "(6,'Protección civil'),"+
                    "(7,'Transporte público');";

    // Llenar tabla SubCategorias
    public static final String INSERTAR_SUBCATEGORIAS =
            "insert into "+TABLA_SUBCATEGORIA+" values" +
                    "(1,1,'Accidente'),"+
                    "(2,1,'Tope'),"+
                    "(3,1,'Semáforo sin funcionar'),"+
                    "(4,2,'Calle sin pavimentar'),"+
                    "(5,2,'Desechos de construcción'),"+
                    "(6,2,'Reencarpetado'),"+
                    "(7,2,'Bache'),"+
                    "(8,3,'Árbol caído'),"+
                    "(9,3,'Maltrato animal'),"+
                    "(10,3,'Reporte sanitario'),"+
                    "(11,4,'Recolección de basura'),"+
                    "(12,4,'Alumbrado público'),"+
                    "(13,4,'Poste caído'),"+
                    "(14,4,'Mantenimiento de áreas'),"+
                    "(15,5,'Fuga de agua'),"+
                    "(16,5,'Corte de agua'),"+
                    "(17,5,'Azolve de drenaje'),"+
                    "(18,6,'Lagartos y cocodrilos'),"+
                    "(19,6,'Bomberos'),"+
                    "(20,6,'Inundaciones'),"+
                    "(21,7,'Violación de ruta'),"+
                    "(22,7,'Violación de cuota'),"+
                    "(23,7,'Mal manejo');";

    // Ver reporte
    public static final String VER_REPORTE = "select "+TABLA_REPORTE+"."+R_CAMPO_ID+","+TABLA_SUBCATEGORIA+"."+SC_CAMPO_SUBCATEGORIA
            +","+TABLA_CATEGORIA+"."+C_CAMPO_CATEGORIA+","+TABLA_REPORTE+"."+R_CAMPO_LATITUD+","+TABLA_REPORTE+"."+R_CAMPO_LONGITUD+","
            +TABLA_REPORTE+"."+R_CAMPO_FOTO+","+TABLA_REPORTE+"."+R_CAMPO_FECHA+","+TABLA_REPORTE+"."+R_CAMPO_ESTADO+","+TABLA_REPORTE+"."+R_CAMPO_DIRECCION+
            " from " +TABLA_CATEGORIA+" join "+TABLA_SUBCATEGORIA+" join "+TABLA_REPORTE+
            " on "+TABLA_CATEGORIA+"."+C_CAMPO_ID+"="+TABLA_SUBCATEGORIA+"."+SC_CAMPO_CATEGORIA+
            " and "+TABLA_SUBCATEGORIA+"."+SC_CAMPO_ID+"="+TABLA_REPORTE+"."+R_CAMPO_SUBCATEGORIA+"" +
            " where "+R_CAMPO_ESTADO+" = 1;";

    // Ver borrador
    public static final String VER_BORRADOR = "select "+TABLA_REPORTE+"."+R_CAMPO_ID+","+TABLA_SUBCATEGORIA+"."+SC_CAMPO_SUBCATEGORIA
            +","+TABLA_CATEGORIA+"."+C_CAMPO_CATEGORIA+","+TABLA_REPORTE+"."+R_CAMPO_LATITUD+","+TABLA_REPORTE+"."+R_CAMPO_LONGITUD+","
            +TABLA_REPORTE+"."+R_CAMPO_FOTO+","+TABLA_REPORTE+"."+R_CAMPO_FECHA+","+TABLA_REPORTE+"."+R_CAMPO_ESTADO+","+TABLA_REPORTE+"."+R_CAMPO_DIRECCION+
            " from "+TABLA_CATEGORIA+" join "+TABLA_SUBCATEGORIA+" join "+TABLA_REPORTE+
            " on "+TABLA_CATEGORIA+"."+C_CAMPO_ID+"="+TABLA_SUBCATEGORIA+"."+SC_CAMPO_CATEGORIA+
            " and "+TABLA_SUBCATEGORIA+"."+SC_CAMPO_ID+"="+TABLA_REPORTE+"."+R_CAMPO_SUBCATEGORIA+"" +
            " where "+R_CAMPO_ESTADO+" = 0;";

    // Ver ultimo reporte
    public static final String VER_ULTIMO_REPORTE = "select "+TABLA_REPORTE+"."+R_CAMPO_ID+","+TABLA_SUBCATEGORIA+"."+SC_CAMPO_SUBCATEGORIA
            +","+TABLA_CATEGORIA+"."+C_CAMPO_CATEGORIA+","+TABLA_REPORTE+"."+R_CAMPO_LATITUD+","+TABLA_REPORTE+"."+R_CAMPO_LONGITUD+","
            +TABLA_REPORTE+"."+R_CAMPO_FOTO+","+TABLA_REPORTE+"."+R_CAMPO_FECHA+","+TABLA_REPORTE+"."+R_CAMPO_ESTADO+","+TABLA_REPORTE+"."+R_CAMPO_DIRECCION+
            " from "+TABLA_CATEGORIA+" join "+TABLA_SUBCATEGORIA+" join "+TABLA_REPORTE+
            " on "+TABLA_CATEGORIA+"."+C_CAMPO_ID+"="+TABLA_SUBCATEGORIA+"."+SC_CAMPO_CATEGORIA+
            " and "+TABLA_SUBCATEGORIA+"."+SC_CAMPO_ID+"="+TABLA_REPORTE+"."+R_CAMPO_SUBCATEGORIA+"" +
            " where Reporte."+R_CAMPO_ID+" = (select MAX(Reporte.id) from Reporte);";

}
