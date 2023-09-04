module com.examgo {
  requires javafx.controls;
  requires javafx.fxml;
  requires java.rmi;
  requires java.sql;
  requires org.postgresql.jdbc;
  requires java.desktop;

  opens com.examgo.client.view to javafx.fxml;
  opens com.examgo.server to javafx.fxml;
  opens com.examgo.client.shared to java.rmi;
  opens com.examgo.server.dao to java.rmi;
  opens com.examgo.client.model to javafx.base;
  opens com.examgo.images to javafx.fxml;

  exports com.examgo.client;
  exports com.examgo.server;
  exports com.examgo.client.model;
}