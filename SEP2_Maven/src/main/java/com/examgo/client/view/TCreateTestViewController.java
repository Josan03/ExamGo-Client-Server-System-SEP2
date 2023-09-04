package com.examgo.client.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import com.examgo.client.viewmodel.TCreateTestViewModel;
import com.examgo.client.viewmodel.ViewModel;

/**
 * A ViewController of a window where teacher creates a test
 *
 * @author Dan Turcan && Cristian Josan
 * @version 1.0
 */
public class TCreateTestViewController implements ViewController
{
  @FXML TextField subjectField;
  @FXML Button confirmButton;
  @FXML Button cancelButton1;
  @FXML TextArea questionContentField;
  @FXML TextField timeField;
  @FXML TextField pointsField;
  @FXML Button addQuestionButton;
  @FXML Button confirmQuestionsButton;
  @FXML Button cancelButton2;
  @FXML Button confirmTestButton;
  @FXML Button cancelButton3;

  @FXML VBox scene1;

  @FXML VBox scene2;

  @FXML VBox scene3;

  private ViewHandler viewHandler;
  private TCreateTestViewModel tCreateTestViewModel;
  private Region root;
  private String name = "t_c_t";

  /**
   * FXML method that is run when the "Cancel" button is pressed.
   */
  @FXML public void cancelButtonPressed()
  {
    tCreateTestViewModel.clearQuestions();
    viewHandler.openView("t_m_m");
  }

  /**
   * FXML method that is run when the "Confirm" button is pressed for the subject name to be saved.
   */
  @FXML public void confirmSubjectPressed()
  {
    if (!subjectField.getText().trim().isEmpty())
    {
      scene1.setDisable(true);
      scene2.setDisable(false);
    }
    else
    {
      AlertControl.showError("Empty fields", null,
          "You must fill all the spaces.");
    }
  }

  /**
   * FXML method that is run when the "Add" button is pressed.
   */
  @FXML public void addQuestionPressed()
  {
    if (!questionContentField.getText().trim().isEmpty() && !timeField.getText()
        .trim().isEmpty() && !pointsField.getText().trim().isEmpty())
    {
      tCreateTestViewModel.addQuestion(Double.parseDouble(timeField.getText()),
          questionContentField.getText(),
          Integer.parseInt(pointsField.getText()));

      questionContentField.clear();
      timeField.clear();
      pointsField.clear();
    }
    else
    {
      AlertControl.showError("Empty fields", null,
          "You must fill all the spaces.");
    }
  }

  /**
   * FXML method that is run when the "Confirm" button is pressed for the questions to be saved.
   */
  @FXML public void confirmQuestionsPressed()
  {
    scene2.setDisable(true);
    scene3.setDisable(false);
  }

  /**
   * FXML method that is run when the "Confirm" button is pressed for the test to be saved.
   */
  @FXML public void confirmTestPressed()
  {
    tCreateTestViewModel.addTest(subjectField.getText());
    scene3.setDisable(true);
    scene1.setDisable(false);
    AlertControl.showInformation("Test created", null,
        "Test created. Check the main menu");
    viewHandler.openView("t_m_m");
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
    this.tCreateTestViewModel = (TCreateTestViewModel) viewModel;
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
