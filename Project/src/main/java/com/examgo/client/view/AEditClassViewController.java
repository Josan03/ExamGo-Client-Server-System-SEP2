package com.examgo.client.view;

import com.examgo.client.model.Account;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import com.examgo.client.model.ClassName;
import com.examgo.client.model.Teacher;
import com.examgo.client.viewmodel.AEditClassViewModel;
import com.examgo.client.viewmodel.ViewModel;

/**
 * A ViewController for a window where administrator edits a class
 *
 * @author Michael Leo && Mihai Mihaila
 * @version 1.0
 */
public class AEditClassViewController implements ViewController
{
  @FXML private TextField classNameTextField;
  @FXML private ComboBox<Account> teacherList;
  @FXML private Button editButton;
  private String name = "a_e_c";
  private ViewHandler viewHandler;
  private AEditClassViewModel aEditClassViewModel;
  private Region root;

  private SimpleListProperty<Account> teachers;

  private SimpleObjectProperty<ClassName> className;

  /**
   * FXML method that is run when the "Edit" button pressed.
   */
  @FXML public void editButtonPressed()
  {
    if (!classNameTextField.getText().trim().isEmpty() && (
        !classNameTextField.getText().equals(className.get().getClassName())
            || teacherList.getSelectionModel().getSelectedItem()
            != className.get().getTeacher()))
    {
      aEditClassViewModel.editClass(className.get().getClassName(),
          classNameTextField.getText(),
          teacherList.getSelectionModel().getSelectedItem());
      AlertControl.showInformation("Class edited", null,
          "Class edited. Check the main menu.");
      viewHandler.openView("a_m_m");
    }
    else
    {
      AlertControl.showError("Empty fields", null,
          "You must fill all the spaces.");
    }
  }

  /**
   * FXML method that is run when the "Return" button pressed.
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
    this.aEditClassViewModel = (AEditClassViewModel) viewModel;
    this.root = root;
    this.teachers = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    this.className = new SimpleObjectProperty<>(null);

    aEditClassViewModel.bindClass(className);
    aEditClassViewModel.bindTeachers(teachers);
    System.out.println(teachers);
    System.out.println(className);
    teacherList.setItems(teachers);
    teacherList.getSelectionModel().select(className.get().getTeacher());
    classNameTextField.setText(className.get().getClassName());

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

