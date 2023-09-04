package com.examgo.client.view;

import javafx.scene.control.Alert;

public class AlertControl
{
  private static Alert alert;

  public static void showWarning(String title, String header, String text)
  {
    alert = new Alert(Alert.AlertType.WARNING);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(text);
    alert.showAndWait();
  }

  public static void showInformation(String title, String header, String text)
  {
    alert = new Alert(Alert.AlertType.INFORMATION);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(text);
    alert.showAndWait();
  }

  public static void showError(String title, String header, String text)
  {
    alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle(title);
    alert.setHeaderText(header);
    alert.setContentText(text);
    alert.showAndWait();
  }
}
