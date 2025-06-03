package com.umg.mydbconection;

import MongoDBLogger.MongoDBLogger;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class DatabaseOperations {
    private Connection connection;
    private MongoDBLogger mongoLogger;
    
    /**
     * Constructor que recibe una conexión existente
     * 
     * @param connection La conexión a la base de datos
     */
    public DatabaseOperations(Connection connection) {
        this.connection = connection;
    }
    
    /**
     * Constructor que crea una nueva conexión y configura el logger de MongoDB
     * 
     * @param mysqlHost Servidor MySQL
     * @param mysqlPort Puerto MySQL
     * @param mysqlDB Nombre de la base de datos MySQL
     * @param mysqlUser Usuario MySQL
     * @param mysqlPassword Contraseña MySQL
     * @param mongoConnectionString URL de conexión a MongoDB
     * @param mongoDBName Nombre de la base de datos MongoDB
     * @param mongoCollectionName Nombre de la colección MongoDB
     * @throws SQLException Si hay error al conectar a MySQL
     */
    public DatabaseOperations(String mysqlHost, int mysqlPort, String mysqlDB, 
                             String mysqlUser, String mysqlPassword,
                             String mongoConnectionString, String mongoDBName, 
                             String mongoCollectionName) throws SQLException {
        try {
            // Establecer conexión a MySQL
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://" + mysqlHost + ":" + mysqlPort + "/" + mysqlDB + 
                         "?useSSL=false&serverTimezone=UTC";
            this.connection = DriverManager.getConnection(url, mysqlUser, mysqlPassword);
            
            // Inicializar logger de MongoDB
            this.mongoLogger = new MongoDBLogger(mongoConnectionString, mongoDBName, mongoCollectionName);
            
        } catch (ClassNotFoundException e) {
            throw new SQLException("Error al cargar el driver MySQL: " + e.getMessage());
        }
    }
    
    /**
     * Establece el logger de MongoDB
     * 
     * @param mongoLogger El logger de MongoDB a utilizar
     */
    public void setMongoLogger(MongoDBLogger mongoLogger) {
        this.mongoLogger = mongoLogger;
    }
    
    /**
     * Función para insertar datos en la base de datos
     * 
     * @param sqlInsert Consulta SQL INSERT completa
     * @return Número de filas afectadas o -1 si hay error
     */
    public int IntoDB(String sqlInsert) {
        int affectedRows = -1;
        boolean success = false;
        String errorMessage = null;
        
        try (Statement stmt = connection.createStatement()) {
            affectedRows = stmt.executeUpdate(sqlInsert);
            success = true;
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            System.err.println("Error al ejecutar INSERT: " + errorMessage);
        }
        
        // Registrar la transacción en MongoDB si el logger está disponible
        if (mongoLogger != null) {
            mongoLogger.logTransaction("INSERT", sqlInsert, success, affectedRows, errorMessage);
        }
        
        return affectedRows;
    }
    
    /**
     * Función para insertar datos y recuperar la clave generada
     * 
     * @param sqlInsert Consulta SQL INSERT completa
     * @return ID generado por la inserción o -1 si hay error
     */
    public long IntoDBGetId(String sqlInsert) {
        long generatedId = -1;
        boolean success = false;
        String errorMessage = null;
        
        try (Statement stmt = connection.createStatement()) {
            int affectedRows = stmt.executeUpdate(sqlInsert, Statement.RETURN_GENERATED_KEYS);
            
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getLong(1);
                        success = true;
                    }
                }
            }
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            System.err.println("Error al ejecutar INSERT con ID: " + errorMessage);
        }
        
        // Registrar la transacción en MongoDB si el logger está disponible
        if (mongoLogger != null) {
            mongoLogger.logTransaction("INSERT", sqlInsert, success, (generatedId != -1 ? 1 : 0), errorMessage);
        }
        
        return generatedId;
    }
    
    /**
     * Función para actualizar datos en la base de datos
     * 
     * @param sqlUpdate Consulta SQL UPDATE completa
     * @return Número de filas afectadas o -1 si hay error
     */
    public int UpdateDB(String sqlUpdate) {
        int affectedRows = -1;
        boolean success = false;
        String errorMessage = null;
        
        try (Statement stmt = connection.createStatement()) {
            affectedRows = stmt.executeUpdate(sqlUpdate);
            success = true;
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            System.err.println("Error al ejecutar UPDATE: " + errorMessage);
        }
        
        // Registrar la transacción en MongoDB si el logger está disponible
        if (mongoLogger != null) {
            mongoLogger.logTransaction("UPDATE", sqlUpdate, success, affectedRows, errorMessage);
        }
        
        return affectedRows;
    }
    
    /**
     * Función para eliminar datos de la base de datos
     * 
     * @param sqlDelete Consulta SQL DELETE completa
     * @return Número de filas afectadas o -1 si hay error
     */
    public int DeleteDB(String sqlDelete) {
        int affectedRows = -1;
        boolean success = false;
        String errorMessage = null;
        
        try (Statement stmt = connection.createStatement()) {
            affectedRows = stmt.executeUpdate(sqlDelete);
            success = true;
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            System.err.println("Error al ejecutar DELETE: " + errorMessage);
        }
        
        // Registrar la transacción en MongoDB si el logger está disponible
        if (mongoLogger != null) {
            mongoLogger.logTransaction("DELETE", sqlDelete, success, affectedRows, errorMessage);
        }
        
        return affectedRows;
    }
    
    /**
     * Función para ejecutar consultas SELECT y obtener resultados
     * 
     * @param sqlSelect Consulta SQL SELECT completa
     * @return Lista de mapas donde cada mapa representa una fila del resultado
     */
    public Stack<Map<String, Object>> RunSql(String sqlSelect) {
        Stack<Map<String, Object>> resultStack = new Stack<>();
        boolean success = false;
        String errorMessage = null;

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sqlSelect)) {

            ResultSetMetaData metaData = rs.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (rs.next()) {
                Map<String, Object> row = new HashMap<>();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object value = rs.getObject(i);
                    row.put(columnName, value);
                }

                resultStack.push(row); // Inserta en la pila
            }

            success = true;
        } catch (SQLException e) {
            errorMessage = e.getMessage();
            System.err.println("Error al ejecutar SELECT: " + errorMessage);
        }

        if (mongoLogger != null) {
            mongoLogger.logTransaction("SELECT", sqlSelect, success, resultStack.size(), errorMessage);
        }

        return resultStack;
    }
   
    
    /**
     * Cierra las conexiones a MySQL y MongoDB
     */
    public void closeConnections() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("Conexión a MySQL cerrada correctamente");
            }
        } catch (SQLException e) {
            System.err.println("Error al cerrar la conexión a MySQL: " + e.getMessage());
        }
        
        if (mongoLogger != null) {
            mongoLogger.close();
        }
    }
    
    /**
     * Verifica si la conexión a MySQL está activa
     * 
     * @return true si la conexión está activa, false en caso contrario
     */
    public boolean isConnected() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }
}