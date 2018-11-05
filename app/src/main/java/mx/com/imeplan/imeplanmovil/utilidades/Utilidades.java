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
            "create table "+TABLA_CATEGORIA+" ("+C_CAMPO_ID+" INTEGER PRIMARY KEY, "+C_CAMPO_CATEGORIA+" TEXT)";

    public static final String CREAR_TABLA_SUBCATEGORIA =
            "create table "+TABLA_SUBCATEGORIA+" ("+SC_CAMPO_ID+" INTEGER PRIMARY KEY, "+SC_CAMPO_CATEGORIA+" INTEGER, " +
                    SC_CAMPO_SUBCATEGORIA+" TEXT, FOREIGN KEY("+SC_CAMPO_CATEGORIA+") REFERENCES "+TABLA_CATEGORIA+"("+C_CAMPO_ID+"))";

    public static final String CREAR_TABLA_REPORTE =
            "create table "+TABLA_REPORTE+" ("+R_CAMPO_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+R_CAMPO_SUBCATEGORIA+" INTEGER, "+R_CAMPO_LATITUD+" TEXT, " +
                    R_CAMPO_LONGITUD+" TEXT, "+R_CAMPO_FOTO+" TEXT, "+R_CAMPO_FECHA+" DATE, "+R_CAMPO_ESTADO+" BOOLEAN, " +
                    "FOREIGN KEY("+R_CAMPO_SUBCATEGORIA+") REFERENCES "+TABLA_SUBCATEGORIA+"("+SC_CAMPO_ID+"))";

    // Llenar tabla Categorias
    public static final String INSERTAR_CATEGORIAS =
            "insert into "+TABLA_CATEGORIA+" ("+C_CAMPO_ID+","+C_CAMPO_CATEGORIA+") values" +
                    "(1,'COMAPA'),"+
                    "(2,'CFE'),"+
                    "(3,'RECOLECCION BASURA'),"+
                    "(4,'CUADRILLA ECOLOGICA'),"+
                    "(5,'PAVIMENTACION'),"+
                    "(6,'VIALIDAD'),"+
                    "(7,'OTROS');";

    // Llenar tabla SubCategorias
    public static final String INSERTAR_SUBCATEGORIAS =
            "insert into "+TABLA_SUBCATEGORIA+" values" +
                    "(1,1,'Fugas'),"+
                    "(2,1,'Corte de agua'),"+
                    "(3,2,'Cableado'),"+
                    "(4,2,'Corte de luz'),"+
                    "(5,2,'Poste caído'),"+
                    "(6,3,'Recolección de basura'),"+
                    "(7,4,'Limpieza'),"+
                    "(8,4,'Árbol caído'),"+
                    "(9,5,'Bache'),"+
                    "(10,5,'Calle sin pavimentar'),"+
                    "(11,6,'Vialidad peligrosa'),"+
                    "(12,6,'Semáforo'),"+
                    "(13,6,'Tope'),"+
                    "(14,6,'Accidente'),"+
                    "(15,7,'Anuncios'),"+
                    "(16,7,'Banqueta');";

    // Ver último registro
    public static final String ULTIMO_REGISTRO= "select max("+R_CAMPO_ID+") from "+TABLA_REPORTE;

    // Ver reporte
    public static final String VER_REPORTE = "select "+TABLA_REPORTE+"."+R_CAMPO_ID+","+TABLA_SUBCATEGORIA+"."+SC_CAMPO_SUBCATEGORIA
            +","+TABLA_CATEGORIA+"."+C_CAMPO_CATEGORIA+","+TABLA_REPORTE+"."+R_CAMPO_LATITUD+","+TABLA_REPORTE+"."+R_CAMPO_LONGITUD+","
            +TABLA_REPORTE+"."+R_CAMPO_FOTO+","+TABLA_REPORTE+"."+R_CAMPO_FECHA+","+TABLA_REPORTE+"."+R_CAMPO_ESTADO+" from "
            +TABLA_CATEGORIA+" join "+TABLA_SUBCATEGORIA+" join "+TABLA_REPORTE+
            " on "+TABLA_CATEGORIA+"."+C_CAMPO_ID+"="+TABLA_SUBCATEGORIA+"."+SC_CAMPO_CATEGORIA+
            " and "+TABLA_SUBCATEGORIA+"."+SC_CAMPO_ID+"="+TABLA_REPORTE+"."+R_CAMPO_SUBCATEGORIA+";";
}
