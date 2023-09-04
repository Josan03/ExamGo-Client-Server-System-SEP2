package com.examgo.client.view;

import javafx.scene.Scene;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import com.examgo.client.viewmodel.ViewModelFactory;

/**
 * A class that opens the views
 *
 * @author Michael Leo
 * @version 1.0
 */
public class ViewHandler
{
  private Stage stageMain;
  private Scene sceneMain;
  private ViewFactory viewFactory;

  /**
   * One-argument constructor initializing ViewHandler.
   * @param viewModelFactory ViewModelFactory of the application
   */
  public ViewHandler(ViewModelFactory viewModelFactory){
    this.viewFactory = new ViewFactory(this, viewModelFactory);
    sceneMain = new Scene(new Region());
  }

  /**
   * A method that sets up the stage and opens the first view.
   * @param primaryStage the stage that application is working with
   */
  public void start(Stage primaryStage){
    stageMain = primaryStage;
    openView(ViewFactory.LOGIN_WINDOW);
  }

  /**
   * A method that opens the specified view
   * @param id shortened unique name of view controller to identify it
   */
  public void openView(String id){
    Region root = viewFactory.load(id);
    sceneMain.setRoot(root);
    if (root.getUserData() == null) {
      stageMain.setTitle("");
    } else {
      stageMain.setTitle(root.getUserData().toString());
    }
    stageMain.setScene(root.getScene());
    stageMain.sizeToScene();
    stageMain.show();
  }
}
