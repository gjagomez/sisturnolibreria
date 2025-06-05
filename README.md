
📌 # Libreria jar
**mydbconection**

📝 ## Descripción
Este proyecto es una implementación sencilla de conexión a bases de datos MySQL utilizando Java. Proporciona una clase `ConexionMySQL` que permite establecer, cerrar y verificar conexiones con una base de datos MySQL. El objetivo principal es facilitar la integración de operaciones de base de datos en aplicaciones Java.

✨ ## Características Principales

🔗 ### Conexión a MySQL:
- La clase `ConexionMySQL` gestiona la conexión a una base de datos MySQL.
- Utiliza la URL de conexión estándar de JDBC para MySQL.
- Gestiona errores al cargar el driver o establecer la conexión.

♻️ ### Gestión de Conexiones:
- Permite abrir y cerrar conexiones de manera controlada.
- Verifica si una conexión está activa antes de realizar operaciones.

🔒 ### Seguridad:
- Los detalles de la conexión (host, puerto, usuario, contraseña) se pasan como parámetros al constructor, evitando hardcodearlos directamente en el código.

⚙️ ### Compatibilidad:
- Compatible con Java SE 11 y versiones posteriores.
- Utiliza Maven para gestionar dependencias.

🗂️ ## Estructura del Proyecto

```
mydbconection/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com.umg.mydbconection/
│   │           ├── ConexionMySQL.java
│   │           ├── DatabaseOperations.java
│   │           └── MongoDBLogger.java
│   └── test/
│       └── java/
│           └── com.umg.mydbconection/
│               └── TestDatabaseOperations.java
├── pom.xml
└── target/
    └── generated-sources/
        └── annotations/
```

- `src/main/java`: Contiene las clases principales del proyecto.
  - `ConexionMySQL.java`: Clase principal para manejar la conexión a MySQL.
  - `DatabaseOperations.java`: Clase para realizar operaciones CRUD en la base de datos.
  - `MongoDBLogger.java`: Clase auxiliar para registrar operaciones en MongoDB (opcional).
- `src/test/java`: Contiene los archivos de prueba unitarios.
  - `TestDatabaseOperations.java`: Pruebas unitarias para `DatabaseOperations`.
- `pom.xml`: Archivo de configuración de Maven para gestionar dependencias y build.




⚙️ ### Configurar Dependencias:
Asegúrate de tener Maven instalado en tu sistema. Luego, ejecuta:
```bash
mvn clean install
```

🧾 ### Configurar Variables de Entorno:
Antes de ejecutar el proyecto, proporciona los detalles de conexión a MySQL:
- Host
- Puerto
- Nombre de la base de datos
- Usuario
- Contraseña

🖥️ ### Ejecutar el Código:
Puedes ejecutar el proyecto desde tu IDE favorito (Eclipse, IntelliJ IDEA) o mediante Maven.

🧪 ## Uso de la Clase ConexionMySQL

```java
import com.umg.mydbconection.ConexionMySQL;

public class Main {
    public static void main(String[] args) {
        // Detalles de conexión
        String host = "localhost";
        int puerto = 3306;
        String baseDatos = "mi_base_de_datos";
        String usuario = "root";
        String password = "contraseña";

        // Crear instancia de ConexionMySQL
        ConexionMySQL conexionMySQL = new ConexionMySQL(host, puerto, baseDatos, usuario, password);

        try {
            // Establecer conexión
            Connection connection = conexionMySQL.conectar();
            if (connection != null) {
                System.out.println("Conexión exitosa!");
                // Realizar operaciones en la base de datos aquí
                conexionMySQL.cerrar();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
```

📦 ## Dependencias Maven

```xml
<dependencies>
    <!-- MySQL Connector -->
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.30</version>
    </dependency>

    <!-- JUnit for testing -->
    <dependency>
        <groupId>junit</groupId>
        <artifactId>junit</artifactId>
        <version>4.13.2</version>
        <scope>test</scope>
    </dependency>
</dependencies>
```


