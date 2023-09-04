package com.examgo.client.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Region;
import com.examgo.client.model.Question;
import com.examgo.client.model.Test;
import com.examgo.client.viewmodel.TEditTestViewModel;
import com.examgo.client.viewmodel.ViewModel;

import java.beans.PropertyChangeListener;

/**
 * A ViewController of a window where teacher edits a test
 *
 * @author Michael Leo && Mihai Mihaila && Dan Turcan && Cristian Josan
 * @version 1.0
 */
public class TEditTestViewController implements ViewController
{
  private String name = "t_e_t";
  @FXML TextArea questionContentField;
  @FXML TextField timeField;
  @FXML TextField pointsField;
  @FXML TextField testSubject;
  @FXML Button confimChanges;
  @FXML Button addQuestion;
  @FXML Button cancelButton;
  @FXML Button deleteQuestion;
  @FXML CheckBox addQuestionMode;
  @FXML ComboBox<Question> questionList;
  private ViewHandler viewHandler;
  private TEditTestViewModel tEditTestViewModel;
  private Region root;
  private SimpleObjectProperty<Test> test;

  private PropertyChangeListener listener;

  /**
   * FXML method that is run when the "Cancel" button is pressed.
   */
  @FXML public void cancelButtonPressed()
  {
    viewHandler.openView("t_m_m");
  }

  /**
   * FXML method that is run when the "Delete" button is pressed.
   */
  @FXML public void deleteQuestionPressed()
  {
    tEditTestViewModel.removeQuestion(
        questionList.getSelectionModel().getSelectedItem());
    AlertControl.showInformation("Question deleted", "Question deleted", null);
    questionList.getSelectionModel().select(-1);
    questionContentField.clear();
    timeField.clear();
    pointsField.clear();
  }

  /**
   * FXML method that is run when the "Confirm" button is pressed.
   */
  @FXML public void confirmChangesPressed()
  {
    if (!timeField.getText().trim().isEmpty() && !questionContentField.getText()
        .trim().isEmpty() && !pointsField.getText().trim().isEmpty()
        && !testSubject.getText().trim().isEmpty())
    {
      Question question = questionList.getValue();
      tEditTestViewModel.editTest(question.getId(),
          Double.parseDouble(timeField.getText()),
          questionContentField.getText(),
          Integer.parseInt(pointsField.getText()), testSubject.getText());
      AlertControl.showInformation("Question and title edited",
          "Question and title (if changed) edited", null);
      questionList.getSelectionModel().select(-1);
      questionContentField.clear();
      timeField.clear();
      pointsField.clear();
    }
    else if (!testSubject.getText().trim().isEmpty()
        && questionList.getSelectionModel().getSelectedIndex() == -1)
    {
      tEditTestViewModel.editTest(-100, 0, "", -3, testSubject.getText());
      AlertControl.showInformation("Title edited", "Title (if changed) edited",
          null);
    }
    else
    {
      AlertControl.showError("Empty fields", null,
          "You must fill all the spaces.");
    }
  }

  /**
   * FXML method that is run anytime the "Add" button is pressed to switch the visibility of the elements in the GUI.
   */
  @FXML public void addQuestionChanged()
  {
    boolean checked = addQuestionMode.isSelected();
    questionList.setDisable(checked);
    confimChanges.setDisable(checked);
    addQuestion.setDisable(!checked);
    testSubject.setDisable(checked);

    if (checked)
    {
      questionList.getSelectionModel().select(-1);
      questionContentField.clear();
      timeField.clear();
      pointsField.clear();

      questionContentField.setDisable(false);
      pointsField.setDisable(false);
      timeField.setDisable(false);
    }
    else if (questionList.getSelectionModel().getSelectedIndex() == -1)
    {
      questionContentField.setDisable(true);
      pointsField.setDisable(true);
      timeField.setDisable(true);
    }
  }

  /**
   * FXML method that is run when the "Add" button is pressed.
   */
  @FXML public void addQuestionPressed()
  {
    if (!timeField.getText().trim().isEmpty() && !questionContentField.getText()
        .trim().isEmpty() && !pointsField.getText().trim().isEmpty())
    {
      tEditTestViewModel.addSingleQuestion(
          Double.parseDouble(timeField.getText()),
          questionContentField.getText(),
          Integer.parseInt(pointsField.getText()));
      AlertControl.showInformation("Question added", "Question added", null);
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
    this.tEditTestViewModel = (TEditTestViewModel) viewModel;
    this.root = root;
    this.test = new SimpleObjectProperty<>();

    deleteQuestion.setDisable(true);
    addQuestion.setDisable(true);
    questionContentField.setDisable(true);
    pointsField.setDisable(true);
    timeField.setDisable(true);

    tEditTestViewModel.bindTest(test);

    questionList.setItems(
        FXCollections.observableArrayList(test.get().getQuestions()));
    testSubject.setText(test.get().getTitle());

    listener = evt -> {
      questionList.setItems(
          FXCollections.observableArrayList(test.get().getQuestions()));
      testSubject.setText(test.get().getTitle());
    };

    tEditTestViewModel.addPropertyChangeListener("ctest", listener);

    questionList.getSelectionModel().selectedItemProperty()
        .addListener((options, oldValue, newValue) -> {
          if (newValue != null && addQuestionMode.isSelected() == false)
          {
            questionContentField.setText(newValue.getQuestion());
            timeField.setText(Double.toString(newValue.getTime()));
            pointsField.setText(Integer.toString(newValue.getPoints()));

            confimChanges.setDisable(false);
            deleteQuestion.setDisable(false);
            addQuestion.setDisable(true);
            questionContentField.setDisable(false);
            pointsField.setDisable(false);
            timeField.setDisable(false);
          }
          else
          {

            deleteQuestion.setDisable(true);
            addQuestion.setDisable(false);
            questionContentField.setDisable(true);
            pointsField.setDisable(true);
            timeField.setDisable(true);
          }

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
