package com.examgo.client.view;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import com.examgo.client.model.Account;
import com.examgo.client.model.ClassName;
import com.examgo.client.viewmodel.AEditTeacherViewModel;
import com.examgo.client.viewmodel.ViewModel;

/**
 * A ViewController for a window where administrator edits a teacher
 *
 * @author Dan Turcan && Cristian Josan
 * @version 1.0
 */
public class AEditTeacherViewController implements ViewController
{

  @FXML private TextField firstName;
  @FXML private TextField lastName;

  @FXML private TextField password;
  @FXML private Button confirmButton;
  @FXML private Button returnButton;
  private String name = "a_e_t";

  private ViewHandler viewHandler;

  private AEditTeacherViewModel aEditTeacherViewModel;
  private Region root;

  private SimpleListProperty<ClassName> classNames;
  private SimpleObjectProperty<Account> teacher;

  /**
   * FXML method that is run when the "Confirm" button pressed.
   */
  @FXML public void confirmButtonPressed()
  {
    if (!firstName.getText().trim().isEmpty() && !lastName.getText().trim()
        .isEmpty() && !password.getText().trim().isEmpty())
    {
      aEditTeacherViewModel.editTeacher(teacher.get().getID(),
          firstName.getText(), lastName.getText(), password.getText());
      AlertControl.showInformation("Teacher edited", null,
          "Teacher edited. Check the main menu.");
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
    this.aEditTeacherViewModel = (AEditTeacherViewModel) viewModel;
    this.root = root;
    this.classNames = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    this.teacher = new SimpleObjectProperty<>(null);
    aEditTeacherViewModel.bindAccount(teacher);

    firstName.setText(teacher.get().getFirstName());
    lastName.setText(teacher.get().getLastName());
    password.setText(teacher.get().getPassword());

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
