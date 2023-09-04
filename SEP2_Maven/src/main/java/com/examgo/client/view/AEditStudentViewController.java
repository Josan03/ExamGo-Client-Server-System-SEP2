package com.examgo.client.view;

import com.examgo.client.model.StudentAccount;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import com.examgo.client.model.Account;
import com.examgo.client.model.ClassName;
import com.examgo.client.model.Student;
import com.examgo.client.viewmodel.AEditStudentViewModel;
import com.examgo.client.viewmodel.ViewModel;

/**
 * A ViewController for a window where administrator edits a student
 *
 * @author Michael Leo && Mihai Mihaila
 * @version 1.0
 */
public class AEditStudentViewController implements ViewController
{

  @FXML private TextField firstName;
  @FXML private TextField lastName;

  @FXML private TextField password;
  @FXML private ComboBox<ClassName> classList;
  @FXML private Button confirmButton;
  @FXML private Button returnButton;
  private String name = "a_e_s";

  private ViewHandler viewHandler;
  private AEditStudentViewModel aEditStudentViewModel;
  private Region root;

  private SimpleListProperty<ClassName> classNames;
  private SimpleObjectProperty<StudentAccount> student;

  /**
   * FXML method that is run when the "Confirm" button is pressed.
   */
  @FXML public void confirmButtonPressed()
  {
    if (!firstName.getText().trim().isEmpty() && !lastName.getText().trim()
        .isEmpty() && !password.getText().trim().isEmpty())
    {
      aEditStudentViewModel.editStudent(student.get().getID(),
          firstName.getText(), lastName.getText(), password.getText(),
          classList.getSelectionModel().getSelectedItem().getClassName());
      AlertControl.showInformation("Student edited", null,
          "Student edited. Check the main menu.");
      viewHandler.openView("a_m_m");
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
   * @param viewHandler ViewHandler of the application
   * @param viewModel specific ViewModel of the application
   * @param root root of the FXMLLoader
   */
  @Override public void init(ViewHandler viewHandler, ViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.aEditStudentViewModel = (AEditStudentViewModel) viewModel;
    this.root = root;
    this.classNames = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    this.student = new SimpleObjectProperty<>(null);
    aEditStudentViewModel.bindClassNames(classNames);
    aEditStudentViewModel.bindAccount(student);
    classList.setItems(classNames);
    firstName.setText(student.get().getFirstName());
    lastName.setText(student.get().getLastName());
    password.setText(student.get().getPassword());
    classList.getSelectionModel()
        .select((student.get()).getClassNameObj());
  }

  /**
   * Overridden method that is implemented from ViewController interface to get the root.
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
