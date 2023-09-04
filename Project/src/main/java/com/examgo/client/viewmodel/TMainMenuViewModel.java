package com.examgo.client.viewmodel;

import javafx.application.Platform;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import com.examgo.client.model.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel of a main window for teacher, where he can choose what to do next
 *
 * @author Michael Leo && Mihai Mihaila && Cristian Josan
 * @version 1.0
 */
public class TMainMenuViewModel implements PropertyChangeListener, ViewModel
{
  private final ModelManager model;

  private PropertyChangeSupport support;

  private SimpleObjectProperty<Account> account;
  private SimpleListProperty<Test> tests;

  private SimpleListProperty<Session> sessions;

  private SimpleListProperty<ClassName> classNames;

  private SimpleListProperty<StudentAccount> students;
  private PropertyChangeListener listener;
  private PropertyChangeListener listener2;

  private PropertyChangeListener listener3;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public TMainMenuViewModel(ModelManager model)
  {
    this.tests = new SimpleListProperty<>(FXCollections.observableArrayList());
    this.account = new SimpleObjectProperty<>(null);
    this.sessions = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    this.classNames = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    this.students = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    listener = evt -> {
      account.set((Account) evt.getNewValue());
      tests.setAll(model.getTestsT());
      sessions.setAll(model.getAllSessionsByTeacher());
      students.setAll(model.getStudentsByTeacher());
      classNames.setAll(model.getClassesByTeacher());

    };

    listener2 = evt -> {
      tests.setAll(model.getTestsT());
    };

    listener3 = evt -> {
      sessions.setAll(model.getAllSessionsByTeacher());
    };

    this.model = model;

    model.addPropertyChangeListener("teacher", listener);
    model.addPropertyChangeListener("tests", listener2);
    model.addPropertyChangeListener("sessions", listener3);

    support = new PropertyChangeSupport(this);
  }

  /**
   * A method to bind the Classes.
   * @param property property to be bound
   */
  public void bindClasses(SimpleListProperty<ClassName> property)
  {
    property.bindBidirectional(classNames);
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
   * A method to bind the sessions.
   * @param property property to be bound
   */
  public void bindSessions(SimpleListProperty<Session> property)
  {
    property.bindBidirectional(sessions);
  }

  /**
   * A method to bind the account of the teacher.
   * @param property property to be bound
   */
  public void bindAccount(SimpleObjectProperty<Account> property)
  {
    property.bindBidirectional(account);
  }

  /**
   * A method to bind the tests.
   * @param property property to be bound
   */
  public void bindTests(SimpleListProperty<Test> property)
  {
    property.bindBidirectional(tests);
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

  /**
   * A method that returns the account of the teacher
   * @return account
   */
  public Account getAccount()
  {
    return account.get();
  }

  /**
   * A method to remove a test from the model
   */
  public void removeTest()
  {
    model.removeTest();
  }

  /**
   * A method to update the test in the model
   * @param test the new test
   */
  public void setTest(Test test)
  {
    model.setTest(test);
  }

  /**
   * A method to update the session in the model
   * @param session the new session
   */
  public void setSession(Session session)
  {
    model.setSelectedSession(session);
  }

  /**
   * A method to create a new session in the model
   */
  public void createSession()
  {
    model.createSession();
  }

  /**
   * A method to stop a session in the model
   */
  public void stopSession()
  {
    model.stopSession();
  }

  /**
   * A method to update the account from the model
   */
  public void update()
  {
    account.set(model.getCurrentAccount());
  }

  /**
   * A method to update the account in the model
   */
  public void reset()
  {
    account.set(model.getCurrentAccount());
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(() -> update());
  }

  /**
   * Update the tests in the view with the data from the model
   * @param query the text that user entered into the search field
   */
  public void getTestsBySearchT(String query)
  {
    tests.setAll(model.getTestsBySearchT(query));
  }

  /**
   * Update the sessions in the view with the data from the model
   * @param query the text that user entered into the search field
   */
  public void getSessionsBySearchT(String query)
  {
    sessions.setAll(model.getSessionsBySearchT(query));
  }

  /**
   * Update the classes in the view with the data from the model
   * @param query the text that user entered into the search field
   */
  public void getClassesBySearchT(String query)
  {
    classNames.setAll(model.getClassesBySearchT(query));
  }

  /**
   * Update the students in the view with the data from the model
   * @param query the text that user entered into the search field
   */
  public void getStudentsBySearchT(String query)
  {
    students.setAll(model.getStudentsBySearchT(query));
  }
}

