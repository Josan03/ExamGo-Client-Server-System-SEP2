package com.examgo.client.view;

import javafx.fxml.FXML;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import com.examgo.client.model.ClassName;
import com.examgo.client.viewmodel.AAddStudentViewModel;
import com.examgo.client.viewmodel.ViewModel;

import java.io.File;

/**
 * A ViewController for a window where administrator adds students
 *
 * @author Mihai Mihaila
 * @version 1.0
 */
public class AAddStudentViewController implements ViewController
{

  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private ComboBox<ClassName> classList;
  @FXML private Button addStudentButton;
  @FXML private Button uploadFileButton;
  @FXML private Button returnButton;

  private String name = "a_a_s";
  private ViewHandler viewHandler;
  private AAddStudentViewModel aAddStudentViewModel;
  private Region root;

  private SimpleListProperty<ClassName> classNames;

  /**
   * FXML method that is run when the "Add student" button pressed.
   */
  @FXML public void addStudentButtonPressed()
  {
    if (!firstName.getText().trim().isEmpty() && !lastName.getText().trim()
        .isEmpty() && classList.getSelectionModel().getSelectedIndex() != -1)
    {
      aAddStudentViewModel.addSingleStudent(firstName.getText(),
          lastName.getText(),
          classList.getSelectionModel().getSelectedItem().getClassName());

      AlertControl.showInformation("Student added", null,
          "Student added. Check the main menu");
      addStudentButton.setDisable(true);
      firstName.clear();
      lastName.clear();
      classList.getSelectionModel().clearSelection();
    }
    else
    {
      AlertControl.showError("Empty fields", null,
          "You must fill all the spaces.");
    }
  }

  /**
   * FXML method that is run when the "Upload File" button is pressed.
   */
  @FXML public void uploadFileButtonPressed()
  {
    FileChooser fc = new FileChooser();
    fc.getExtensionFilters()
        .add(new FileChooser.ExtensionFilter("txt files", "*.txt"));
    File f = fc.showOpenDialog(null);

    if (f != null)
    {
      System.out.println(f.getAbsoluteFile());
      AlertControl.showInformation("File uploaded",
          "File uploaded successfully",
          "Check the main menu. Students that had classes which do not exist will not be added. The students wont be added if the column are not correctly made");
      aAddStudentViewModel.addMultipleStudents(f.getAbsolutePath());
    }
    else
    {
      AlertControl.showError("File error", null,
          "Wrong type of file / File is corrupted");
    }
  }

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
    this.aAddStudentViewModel = (AAddStudentViewModel) viewModel;
    this.root = root;
    this.classNames = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    aAddStudentViewModel.bindClassNames(classNames);
    classList.setItems(classNames);

    classList.getSelectionModel().selectedItemProperty()
        .addListener((options, oldValue, newValue) -> {
          if (newValue != null)
          {
            addStudentButton.setDisable(false);
          }
          else
            addStudentButton.setDisable(true);
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
