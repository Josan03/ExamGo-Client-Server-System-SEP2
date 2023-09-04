package com.examgo.client.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Region;
import com.examgo.client.model.*;
import com.examgo.client.viewmodel.TCheckSessionViewModel;
import com.examgo.client.viewmodel.ViewModel;

/**
 * A ViewController for a window where teacher checks the students' answers
 *
 * @author Michael Leo && Mihai Mihaila
 * @version 1.0
 */
public class TCheckSessionViewController implements ViewController
{
  private String name = "t_c_s";

  @FXML ComboBox<StudentAccount> studentList;
  @FXML ListView<Question> questionList;
  @FXML Label questionText;
  @FXML ComboBox<Integer> pointList;
  @FXML Label currentPointsText;
  @FXML Label totalPointsText;
  @FXML ComboBox<Integer> gradeList;
  @FXML TextArea answerText;
  @FXML Label currentGrade;
  @FXML Label currentPoints;
  private ViewHandler viewHandler;
  private TCheckSessionViewModel tCheckSessionViewModel;
  private Region root;
  private SimpleIntegerProperty grade;
  private SimpleListProperty<StudentAccount> students;

  private SimpleListProperty<Answer> answers;
  private SimpleObjectProperty<Session> session;

  /**
   * FXML method that is run when the "Return" button is pressed.
   */
  @FXML public void returnButtonPressed()
  {
    viewHandler.openView("t_m_m");
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
    this.root = root;
    this.viewHandler = viewHandler;
    this.tCheckSessionViewModel = (TCheckSessionViewModel) viewModel;

    this.grade = new SimpleIntegerProperty(-5);
    this.answers = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    this.students = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    this.session = new SimpleObjectProperty<>(null);

    gradeList.getItems().addAll(-3, 0, 2, 4, 7, 10, 12);
    gradeList.setDisable(true);
    pointList.setDisable(true);
    questionList.setDisable(true);
    tCheckSessionViewModel.bindSession(session);
    tCheckSessionViewModel.bindAnswers(answers);
    tCheckSessionViewModel.bindStudents(students);
    tCheckSessionViewModel.bindGrade(grade);
    totalPointsText.setText(
        Integer.toString(tCheckSessionViewModel.getTotalPoints()));
    currentGrade.setText(Integer.toString(grade.get()));

    studentList.setItems(students);

    studentList.getSelectionModel().selectedItemProperty()
        .addListener((options, oldValue, newValue) -> {
          if (newValue != null)
          {
            gradeList.setDisable(false);
            questionList.setDisable(false);
            tCheckSessionViewModel.setSelectedStudent(newValue);
            currentGrade.setText(Integer.toString(grade.get()));
            currentPointsText.setText(
                Integer.toString(tCheckSessionViewModel.getCurrentPoints()));
          }
          else
          {
            gradeList.setDisable(true);
            questionList.setDisable(true);
            tCheckSessionViewModel.setSelectedStudent(null);
            currentGrade.setText("");
          }
        });

    questionList.setItems(FXCollections.observableArrayList(
        session.get().getTest().getQuestions()));
    System.out.println(session.get().getTest().getQuestions());
    questionList.getSelectionModel().selectedItemProperty()
        .addListener((options, oldValue, newValue) -> {
          if (newValue != null)
          {
            questionText.setText(
                questionList.getSelectionModel().getSelectedItem()
                    .getQuestion());
            answerText.setText(
                tCheckSessionViewModel.getAnswerByQuestion(newValue.getId()));

            currentPoints.setText(Integer.toString(
                tCheckSessionViewModel.getPointsByQuestion(newValue.getId())));
            pointList.getItems().clear();
            for (int i = 0; i <= newValue.getPoints(); i++)
            {
              pointList.getItems().add(i);
            }
            pointList.setDisable(false);

          }
          else
          {
            questionText.setText("");
            answerText.clear();
            currentPoints.setText("");
            pointList.setDisable(true);
          }
        });

    pointList.getSelectionModel().selectedItemProperty()
        .addListener((options, oldValue, newValue) -> {
          if (newValue != null && newValue != Integer.parseInt(
              currentPoints.getText()))
          {
            tCheckSessionViewModel.updatePoints(
                tCheckSessionViewModel.getAnswerIDByQuestion(
                    questionList.getSelectionModel().getSelectedItem().getId()),
                newValue);
            currentPoints.setText(Integer.toString(
                tCheckSessionViewModel.getPointsByQuestion(
                    questionList.getSelectionModel().getSelectedItem()
                        .getId())));
            currentPointsText.setText(
                Integer.toString(tCheckSessionViewModel.getCurrentPoints()));
          }
        });

    gradeList.getSelectionModel().selectedItemProperty()
        .addListener((options, oldValue, newValue) -> {
          if (newValue != null && newValue != Integer.parseInt(
              currentGrade.getText()))
          {
            tCheckSessionViewModel.updateGrade(newValue);
            currentGrade.setText(Integer.toString(grade.get()));
          }
        });

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
