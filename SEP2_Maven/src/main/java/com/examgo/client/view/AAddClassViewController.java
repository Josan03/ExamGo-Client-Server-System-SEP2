package com.examgo.client.view;

import com.examgo.client.model.Account;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import com.examgo.client.model.Teacher;
import com.examgo.client.viewmodel.AAddClassViewModel;
import com.examgo.client.viewmodel.ViewModel;

/**
 * A ViewController for a window where administrator adds a class
 *
 * @author Michael Leo && Mihai Mihaila
 * @version 1.0
 */
public class AAddClassViewController implements ViewController
{
  @FXML private TextField classNameTextField;
  @FXML private ComboBox<Account> teacherList;
  @FXML private Button addButton;
  private String name = "a_a_c";
  private ViewHandler viewHandler;
  private AAddClassViewModel aAddClassViewModel;
  private Region root;

  private SimpleListProperty<Account> teachers;

  /**
   * FXML method that is run when the "Add" button pressed.
   */
  @FXML public void addButtonPressed()
  {
    if (!classNameTextField.getText().trim().isEmpty()
        && teacherList.getSelectionModel().getSelectedIndex() != -1)
    {
      aAddClassViewModel.addClass(classNameTextField.getText(),
          teacherList.getSelectionModel().getSelectedItem());

      AlertControl.showInformation("Class added", null,
          "Class added. Check the main menu. Remember that if you tried to add a class that already exists, no changes will occur.");
      classNameTextField.clear();
      teacherList.getSelectionModel().select(-1);
      addButton.setDisable(true);
    }
    else
    {
      AlertControl.showError("Empty fields", null,
          "You must fill all the spaces.");
    }
  }

  /**
   * FXML method that is run when the "Return" button is pressed, returns to the main menu.
   */
  @FXML public void returnButtonPressed()
  {
    viewHandler.openView("a_m_m");
  }

  /**
   * Overridden method that is implemented from ViewController interface, initializes the controller.
   *
   * @param viewHandler ViewHandler of the application
   * @param viewModel   specific ViewModel of the application
   * @param root        root of the FXMLLoader
   */
  @Override public void init(ViewHandler viewHandler, ViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.aAddClassViewModel = (AAddClassViewModel) viewModel;
    this.root = root;
    this.teachers = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    addButton.setDisable(true);
    aAddClassViewModel.bindTeachers(teachers);
    System.out.println(teachers.get());
    teacherList.setItems(teachers);
    teacherList.getSelectionModel().selectedItemProperty()
        .addListener((options, oldValue, newValue) -> {
          if (newValue != null)
          {
            addButton.setDisable(false);
          }
          else
            addButton.setDisable(true);
        });
  }

  /**
   * Overridden method that is implemented from ViewController interface to get the root.
   *
   * @return the root of this view
   */
  @Override public Region getRoot()
  {
    return root;
  }

  /**
   * Overridden method that is implemented from ViewController interface to reset the view.
   */
  @Override public void reset()
  {

  }
}
