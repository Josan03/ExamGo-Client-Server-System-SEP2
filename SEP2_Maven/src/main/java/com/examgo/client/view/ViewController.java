package com.examgo.client.view;

import javafx.scene.layout.Region;
import com.examgo.client.viewmodel.ViewModel;

/**
 * A ViewController interface is used to ease the loading process in the ViewFactory class.
 * Is implemented by all the view controllers.
 *
 * @author Michael Leo
 * @version 1.0
 */
public interface ViewController
{

  /**
   * A method that initializes the controller you are working with.
   * @param viewHandler ViewHandler of the application
   * @param viewModel specific ViewModel for the controller
   * @param root root of the FXMLLoader
   */
  void init(ViewHandler viewHandler, ViewModel viewModel, Region root);

  /**
   * A method to get the root of the controller.
   * @return the root of the view you are working with
   */
  Region getRoot();

  /**
   * A method to reset the view.
   */
  void reset();
}

