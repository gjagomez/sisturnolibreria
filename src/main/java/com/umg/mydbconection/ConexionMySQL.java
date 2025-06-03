package com.umg.mydbconection;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionMySQL {
    private String host;
    private int puerto;
    private String baseDatos;
    private String usuario;
    private String password;
    private Connection conexion;
    
    /**
     * Constructor para la conexión a MySQL
     * 
     * @param host Host del servidor MySQL
     * @param puerto Puerto del servidor MySQL
     * @param baseDatos Nombre de la base de datos
     * @param usuario Usuario de MySQL
     * @param password Contraseña del usuario
     */
    public ConexionMySQL(String host, int puerto, String baseDatos, String usuario, String password) {
        this.host = host;
        this.puerto = puerto;
        this.baseDatos = baseDatos;
        this.usuario = usuario;
        this.password = password;
    }
    
    /**
     * Establece la conexión con la base de datos MySQL
     * 
     * @return Objeto Connection que representa la conexión
     * @throws SQLException Si ocurre un error al conectar
     */
    public Connection conectar() throws SQLException {
        if (conexion == null || conexion.isClosed()) {
            try {
                // Cargar el driver de MySQL
                Class.forName("com.mysql.cj.jdbc.Driver");
                
                // Crear la URL de conexión
                String url = "jdbc:mysql://" + host + ":" + puerto + "/" + baseDatos + 
                             "?useSSL=false&serverTimezone=UTC";
                
                // Establecer la conexión
                conexion = DriverManager.getConnection(url, usuario, password);
                
                System.out.println("Conexión establecida con éxito a " + baseDatos);
            } catch (ClassNotFoundException e) {
                throw new SQLException("Driver de MySQL no encontrado", e);
            }
        }
        return conexion;
    }
    
    /**
     * Cierra la conexión con la base de datos
     * 
     * @throws SQLException Si ocurre un error al cerrar la conexión
     */
    public void cerrar() throws SQLException {
        if (conexion != null && !conexion.isClosed()) {
            conexion.close();
            System.out.println("Conexión cerrada");
        }
    }
    
    /**
     * Verifica si la conexión está activa
     * 
     * @return true si la conexión está activa, false en caso contrario
     */
    public boolean estaConectado() {
        try {
            return conexion != null && !conexion.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}