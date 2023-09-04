package com.examgo.client.viewmodel;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import com.examgo.client.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewController of a main window for student, where he can choose what to do next
 *
 * @author Dan Turcan && Mihai Mihaila
 * @version 1.0
 */
public class SMainMenuViewModel implements ViewModel
{
  private ModelManager model;

  private SimpleObjectProperty<StudentAccount> account;
  private SimpleListProperty<StudentSession> studentSessions;
  private final PropertyChangeSupport support;

  private PropertyChangeListener listener;

  private PropertyChangeListener listener2;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public SMainMenuViewModel(ModelManager model)
  {
    this.model = model;
    this.support = new PropertyChangeSupport(this);
    account = new SimpleObjectProperty<>(null);
    studentSessions = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    listener = evt -> {
      account.set((StudentAccount) evt.getNewValue());
      studentSessions.setAll(model.getStudentSessions());
    };

    listener2 = evt -> {
      studentSessions.setAll(model.getStudentSessions());
    };

    model.addPropertyChangeListener("student", listener);
    model.addPropertyChangeListener("student-sessions", listener2);

  }

  /**
   * A method to bind the sessions.
   * @param property property to be bound
   */
  public void bindSessions(SimpleListProperty<StudentSession> property)
  {
    property.bindBidirectional(studentSessions);
  }

  /**
   * A method to bind the account of student.
   * @param property property to be bound
   */
  public void bindAccount(SimpleObjectProperty<StudentAccount> property)
  {
    property.bindBidirectional(account);
  }

  /**
   * A method to connect to the session
   */
  public boolean connectSession(int session_id)
  {
    return model.connectSession(session_id);
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