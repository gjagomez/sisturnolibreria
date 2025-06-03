module com.umg.mydbconection {
 requires java.sql;
    requires java.base;
    
    // Si estás usando JavaFX (como parece por tus dependencias)
    requires javafx.controls;
    requires javafx.graphics;
      requires org.mongodb.driver.sync.client;
    requires org.mongodb.bson;
    requires org.mongodb.driver.core;
    exports com.umg.mydbconection;
}
