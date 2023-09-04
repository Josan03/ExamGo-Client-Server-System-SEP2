package com.examgo.client.view;

import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Region;
import com.examgo.client.model.Question;
import com.examgo.client.model.Session;
import com.examgo.client.viewmodel.STestWindowViewModel;
import com.examgo.client.viewmodel.ViewModel;

import java.util.ArrayList;

/**
 * A ViewController for a window where student fills in the test questions
 *
 * @author Michael Leo && Mihai Mihaila && Cristian Josan
 * @version 1.0
 */
public class STestWindowViewController implements ViewController
{
  @FXML Label questionText;
  @FXML Label timeLeft;
  @FXML ProgressBar timeLeftBar;
  @FXML TextArea answerText;
  @FXML Button submitButton;
  private ViewHandler viewHandler;
  private STestWindowViewModel sTestWindowViewModel;

  private Region root;
  private String name = "s_t_w";

  private SimpleObjectProperty<Session> session;

  private int current_question;
  private double time;
  private boolean isStopped;

  private Thread exam = new Thread(new Timer());

  /**
   * FXML method that is run when the "Submit" button is pressed.
   */
  @FXML public void submitButtonPressed()
  {

    sTestWindowViewModel.addAnswer(
        session.get().getTest().getQuestions().get(current_question).getId(),
        answerText.getText());
    ArrayList<Question> currentQuestions = session.get().getTest()
        .getQuestions();
    answerText.setDisable(false);
    answerText.clear();
    if (currentQuestions.size() - 1 == current_question)
    {
      isStopped = true;
      sTestWindowViewModel.sendAnswers();
      AlertControl.showInformation("Test ended", "",
          "The test has ended. You are being moved to the main menu.");
      viewHandler.openView("s_m_m");
    }
    else
    {
      current_question += 1;
      Platform.runLater(() -> sTestWindowViewModel.setQuestion(
          session.get().getTest().getQuestions().get(current_question)
              .getQuestion()));
      time = 0;
      Platform.runLater(() -> sTestWindowViewModel.setTime(String.valueOf(
          session.get().getTest().getQuestions().get(current_question).getTime()
              - time)));
      timeLeftBar.setProgress(0);
      submitButton.setDisable(true);

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
    this.sTestWindowViewModel = (STestWindowViewModel) viewModel;
    this.root = root;
    isStopped = false;

    sTestWindowViewModel.bindTime(timeLeft.textProperty());
    sTestWindowViewModel.bindQuestion(questionText.textProperty());

    session = new SimpleObjectProperty<>(null);

    sTestWindowViewModel.bindSession(session);

    exam.start();
    current_question = -1;
    time = -1;

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

  private class Timer implements Runnable
  {
    private void check()
    {
      if (current_question == -1)
      {

        current_question += 1;
        Platform.runLater(() -> sTestWindowViewModel.setQuestion(
            session.get().getTest().getQuestions().get(current_question)
                .getQuestion()));

        time = 0;
        Platform.runLater(() -> sTestWindowViewModel.setTime(String.valueOf(
            session.get().getTest().getQuestions().get(current_question)
                .getTime() - time)));
        timeLeftBar.setProgress(0);

      }

      Question currentQuestion = session.get().getTest().getQuestions()
          .get(current_question);
      if (time == currentQuestion.getTime())
      {
        answerText.setDisable(true);
      }
      else
      {
        time += 1;
        Platform.runLater(() -> sTestWindowViewModel.setTime(String.valueOf(
            session.get().getTest().getQuestions().get(current_question)
                .getTime() - time)));
        submitButton.setDisable(false);
        timeLeftBar.setProgress(time / currentQuestion.getTime());

      }
    }

    @Override public void run()
    {
      ArrayList<Question> currentQuestion = session.get().getTest()
          .getQuestions();
      while (!isStopped)
      {
        check();
        try
        {
          Thread.sleep(1000);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
      }
    }
  }
}
