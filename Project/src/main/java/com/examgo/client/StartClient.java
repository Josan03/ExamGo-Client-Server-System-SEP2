package com.examgo.client;

import javafx.application.Application;
import javafx.stage.Stage;
import com.examgo.client.model.ModelManager;
import com.examgo.client.shared.ExamClient;
import com.examgo.client.view.ViewHandler;
import com.examgo.client.viewmodel.ViewModelFactory;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * A class that starts and connects the client to the server
 *
 * @author Cristian Josan
 * @version 1.0
 */
public class StartClient extends Application
{
  @Override public void start(Stage primaryStage) throws Exception
  {
    // localhost (for Local Address)
    // 192.168.87.155 (home IPv4 Address)
    // 10.154.198.97 (VIA IPv4 Address)
    Registry registry = LocateRegistry.getRegistry("localhost", 8090);
    ExamClient exam = (ExamClient) registry.lookup("exam");
    ModelManager modelManager = ModelManager.getInstance(exam);
    ViewModelFactory viewModelFactory = new ViewModelFactory(modelManager);
    ViewHandler viewHandler = new ViewHandler(viewModelFactory);
    viewHandler.start(primaryStage);
  }

  /**
   * The method which launches the application
   *
   * @param args a command line argument array which is used to retrieve input from the console
   */
  public static void main(String[] args)
  {
    launch();
  }
}
