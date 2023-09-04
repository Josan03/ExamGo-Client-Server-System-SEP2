package com.examgo.client.viewmodel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import com.examgo.client.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel of a window where teacher checks the students' answers
 *
 * @author Michael Leo && Mihai Mihaila
 * @version 1.0
 */
public class TCheckSessionViewModel implements ViewModel
{
  private final ModelManager model;
  private PropertyChangeSupport support;

  private PropertyChangeListener currentSession;
  private PropertyChangeListener currentGrade;
  private PropertyChangeListener currentStudent;
  //  private PropertyChangeListener updPointsListener;
  private PropertyChangeListener updGradeListener;
  private SimpleIntegerProperty grade;

  private SimpleListProperty<StudentAccount> students;

  private SimpleListProperty<Answer> answers;
  private SimpleObjectProperty<Session> session;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public TCheckSessionViewModel(ModelManager model)
  {
    this.model = model;
    this.support = new PropertyChangeSupport(this);
    this.grade = new SimpleIntegerProperty(-5);
    this.answers = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    this.students = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    this.session = new SimpleObjectProperty<>(null);
    currentSession = evt -> {
      session.set((Session) evt.getNewValue());
      students.setAll(model.getStudentsBySession());
    };
    currentGrade = evt -> {
      grade.set(model.getGradeByStudentSession());
    };

    currentStudent = evt -> {
      grade.set(model.getGradeByStudentSession());
      answers.setAll(model.getAnswersByStudentSession());
    };

    //    updPointsListener = evt -> {
    //      session.set((Session) evt.getNewValue());
    //    };
    //
    updGradeListener = evt -> {
      grade.set((Integer) evt.getNewValue());
    };

    model.addPropertyChangeListener("cSession", currentSession);
    model.addPropertyChangeListener("grade", currentGrade);
    model.addPropertyChangeListener("changedStudent", currentStudent);
    //    model.addPropertyChangeListener("points", updPointsListener);
    model.addPropertyChangeListener("grade2", updGradeListener);

  }

  /**
   * A method to bind the sessions.
   * @param property property to be bound
   */
  public void bindSession(SimpleObjectProperty<Session> property)
  {
    property.bindBidirectional(session);
  }

  /**
   * A method to bind the grade.
   * @param property property to be bound
   */
  public void bindGrade(SimpleIntegerProperty property)
  {
    property.bindBidirectional(grade);
  }

  /**
   * A method to bind the students.
   * @param property property to be bound
   */
  public void bindStudents(SimpleListProperty<StudentAccount> property)
  {
    property.bindBidirectional(students);
  }

  /**
   * A method to bind the answers.
   * @param property property to be bound
   */
  public void bindAnswers(SimpleListProperty<Answer> property)
  {
    property.bindBidirectional(answers);
  }

  /**
   * A method to set the selection in the model
   * @param student  the student to select
   */
  public void setSelectedStudent(StudentAccount student)
  {
    model.setSelectedStudent(student);
  }

  /**
   * A method to update the grade in the model
   * @param grade the new grade
   */
  public void updateGrade(int grade)
  {
    model.updateGrade(grade);
  }

  /**
   * A method to update the points in the model
   * @param answer_id the ID of the answer
   * @param points the new points
   */
  public void updatePoints(int answer_id, int points)
  {
    model.updatePoints(answer_id, points);
  }

  /**
   * A method that returns the answer for the particular question
   * @param question_id ID of the question to identify it
   * @return the answer
   */
  public String getAnswerByQuestion(int question_id)
  {
    return model.getAnswerByQuestion(question_id);
  }

  /**
   * A method that returns points for the particular question
   * @param question_id ID of the question to identify it
   * @return points
   */
  public int getPointsByQuestion(int question_id)
  {
    return model.getPointsByQuestion(question_id);
  }

  /**
   * A method that returns the answer ID for the particular question
   * @param question_id ID of the question to identify it
   * @return the answer ID
   */
  public int getAnswerIDByQuestion(int question_id)
  {
    return model.getAnswerIDByQuestion(question_id);
  }

  /**
   * A method that returns the sum of all points
   * @return the sum of points
   */
  public int getTotalPoints()
  {
    return model.getTotalPoints();
  }

  /**
   * A method that returns current points
   * @return current points
   */
  public int getCurrentPoints()
  {
    return model.getCurrentPoints();
  }

  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(propertyName, listener);
  }

  public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(propertyName, listener);
  }

}
