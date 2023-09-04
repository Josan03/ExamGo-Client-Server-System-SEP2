package com.examgo.client.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import com.examgo.client.viewmodel.AAddTeacherViewModel;
import com.examgo.client.viewmodel.ViewModel;

import java.io.File;

/**
 * A ViewController for a window where administrator adds a teacher
 *
 * @author Mihai Mihaila
 * @version 1.0
 */
public class AAddTeacherViewController implements ViewController
{

  @FXML private TextField firstName;
  @FXML private TextField lastName;
  @FXML private Button addTeacherButton;
  @FXML private Button uploadFileButton;
  @FXML private Button returnButton;

  private String name = "a_a_t";
  private ViewHandler viewHandler;
  private AAddTeacherViewModel aAddTeacherViewModel;
  private Region root;

  /**
   * FXML method that is run when the "Add teacher" button pressed.
   */
  @FXML public void addTeacherButtonPressed()
  {
    if (!firstName.getText().trim().isEmpty() && !lastName.getText().trim()
        .isEmpty())
    {
      aAddTeacherViewModel.addSingleTeacher(firstName.getText(),
          lastName.getText());
      AlertControl.showInformation("Teacher added", null,
          "Teacher added. Check the main menu");
      addTeacherButton.setDisable(true);
      firstName.clear();
      lastName.clear();

    }
    else
    {
      AlertControl.showError("Empty fields", null,
          "You must fill all the spaces");
    }
  }

  /**
   * FXML method that is run when the "Upload File" button pressed.
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
          "Check the main menu. The teachers wont be added if the column are not correctly made");
      aAddTeacherViewModel.addMultipleTeachers(f.getAbsolutePath());
    }
    else
    {
      AlertControl.showError("Empty fields", null,
          "You must fill all the spaces.");
    }
  }

  /**
   * FXML method that is run when the "Return" button pressed, returns to the main menu view.
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
    this.aAddTeacherViewModel = (AAddTeacherViewModel) viewModel;
    this.root = root;
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
