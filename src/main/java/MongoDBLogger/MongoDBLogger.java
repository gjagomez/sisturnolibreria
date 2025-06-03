/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package MongoDBLogger;


import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

import java.util.Date;

/**
 * Clase para registrar transacciones en MongoDB
 */
public class MongoDBLogger {
    private MongoClient mongoClient;
    private MongoDatabase database;
    private MongoCollection<Document> transactionCollection;
    
    /**
     * Constructor para el logger de MongoDB
     * 
     * @param connectionString URL de conexión a MongoDB
     * @param databaseName Nombre de la base de datos
     * @param collectionName Nombre de la colección
     */
    public MongoDBLogger(String connectionString, String databaseName, String collectionName) {
        try {
            // Conectar a MongoDB
            mongoClient = MongoClients.create(connectionString);
            database = mongoClient.getDatabase(databaseName);
            transactionCollection = database.getCollection(collectionName);
            System.out.println("Conexión a MongoDB establecida correctamente");
        } catch (Exception e) {
            System.err.println("Error al conectar a MongoDB: " + e.getMessage());
        }
    }
    
    /**
     * Registra una transacción en MongoDB
     * 
     * @param operationType Tipo de operación (INSERT, UPDATE, DELETE, SELECT)
     * @param sqlQuery Consulta SQL ejecutada
     * @param success Indica si la operación fue exitosa
     * @param affectedRows Número de filas afectadas (para INSERT, UPDATE, DELETE)
     * @param errorMessage Mensaje de error (si lo hay)
     */
    public void logTransaction(String operationType, String sqlQuery, boolean success, 
                              int affectedRows, String errorMessage) {
        try {
            Document document = new Document();
            document.append("timestamp", new Date());
            document.append("operation", operationType);
            document.append("query", sqlQuery);
            document.append("success", success);
            document.append("affectedRows", affectedRows);
            
            if (errorMessage != null && !errorMessage.isEmpty()) {
                document.append("error", errorMessage);
            }
            
            transactionCollection.insertOne(document);
        } catch (Exception e) {
            System.err.println("Error al registrar transacción en MongoDB: " + e.getMessage());
        }
    }
    
    /**
     * Cierra la conexión con MongoDB
     */
    public void close() {
        if (mongoClient != null) {
            mongoClient.close();
            System.out.println("Conexión a MongoDB cerrada correctamente");
        }
    }
}