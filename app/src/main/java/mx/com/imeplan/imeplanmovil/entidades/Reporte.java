package mx.com.imeplan.imeplanmovil.entidades;

/**
 * Entidad reporte
 */

public class Reporte {
    private int id;
    private String subcategoria;
    private String categoria;
    private String latitud;
    private String longitud;
    private String foto;
    private String fecha;
    private int estado;
    private String direccion;

    public Reporte(int id, String subcategoria, String categoria, String latitud, String longitud,
                   String foto, String fecha, int estado,String direccion) {
        this.id = id;
        this.subcategoria = subcategoria;
        this.categoria = categoria;
        this.latitud = latitud;
        this.longitud = longitud;
        this.foto = foto;
        this.fecha = fecha;
        this.estado = estado;
        this.direccion = direccion;
    }

    public Reporte(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubcategoria() {
        return subcategoria;
    }

    public void setSubcategoria(String subcategoria) {
        this.subcategoria = subcategoria;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }
}
