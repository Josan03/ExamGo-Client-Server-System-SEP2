package com.examgo.client.viewmodel;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import com.examgo.client.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel of a main window for administrator, where he can choose what to do next
 *
 * @author Dan Turcan && Mihai Mihaila && Michael Leo
 * @version 1.0
 */
public class AMainMenuViewModel implements ViewModel
{
  private ModelManager model;

  private PropertyChangeListener accountListener;
  private PropertyChangeListener classListener;
  private PropertyChangeListener testListener;
  private PropertyChangeListener sessionListener;

  private SimpleObjectProperty<Account> account;

  private SimpleListProperty<Test> tests;

  private SimpleListProperty<Session> sessions;

  private SimpleListProperty<StudentAccount> students;

  private SimpleListProperty<Account> teachers;

  private SimpleListProperty<ClassName> classNames;
  private final PropertyChangeSupport support;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public AMainMenuViewModel(ModelManager model)
  {
    this.model = model;
    this.support = new PropertyChangeSupport(this);

    account = new SimpleObjectProperty<>(null);
    tests = new SimpleListProperty<>(FXCollections.observableArrayList());
    sessions = new SimpleListProperty<>(FXCollections.observableArrayList());
    students = new SimpleListProperty<>(FXCollections.observableArrayList());
    teachers = new SimpleListProperty<>(FXCollections.observableArrayList());
    classNames = new SimpleListProperty<>(FXCollections.observableArrayList());

    accountListener = evt -> {
      account.set((Account) evt.getNewValue());
      tests.setAll(model.getAllTests());
      sessions.setAll(model.getAllSessions());
      students.setAll(model.getAllStudents());
      teachers.setAll(model.getAllTeachers());
      classNames.setAll(model.getAllClassNames());
    };

    classListener = evt -> {
      classNames.setAll(model.getAllClassNames());
      students.setAll(model.getAllStudents());
      teachers.setAll(model.getAllTeachers());
    };

    testListener = evt -> {
      tests.setAll(model.getAllTests());
    };

    sessionListener = evt -> {
      sessions.setAll(model.getAllSessions());
    };

    model.addPropertyChangeListener("admin", accountListener);
    model.addPropertyChangeListener("classname", classListener);
    model.addPropertyChangeListener("testsA", testListener);
    model.addPropertyChangeListener("sessionsA", sessionListener);

  }

  /**
   * A method to bind an account
   * @param property property to be bound
   */
  public void bindAccount(SimpleObjectProperty<Account> property)
  {
    property.bindBidirectional(account);
  }

  /**
   * A method to bind the list of tests
   * @param property property to be bound
   */
  public void bindTests(SimpleListProperty<Test> property)
  {
    property.bindBidirectional(tests);
  }

  /**
   * A method to bind the list of sessions
   * @param property property to be bound
   */
  public void bindSessions(SimpleListProperty<Session> property)
  {
    property.bindBidirectional(sessions);
  }


  /**
   * A method to bind the list of teachers
   * @param property property to be bound
   */
  public void bindTeachers(SimpleListProperty<Account> property)
  {
    property.bindBidirectional(teachers);
  }

  /**
   * A method to bind the list of classnames
   * @param property property to be bound
   */
  public void bindClassNames(SimpleListProperty<ClassName> property)
  {
    property.bindBidirectional(classNames);
  }

  /**
   * A method to bind the list of students
   * @param property property to be bound
   */
  public void bindStudents(SimpleListProperty<StudentAccount> property)
  {
    property.bindBidirectional(students);
  }

  /**
   * A method to delete the Class from the model
   */
  public void deleteClass()
  {
    model.removeClass();
  }

  /**
   * Update the Class according to the selection
   */
  public void setClass(ClassName className)
  {
    model.setSelectedClass(className);
  }

  /**
   * Update the test according to the selection
   */
  public void setTest(Test test)
  {
    model.setTest(test);
  }

  /**
   * Update the session according to the selection
   */
  public void setSession(Session session)
  {
    model.setSelectedSession(session);
  }

  /**
   * Update the teacher according to the selection
   */
  public void setTeacher(Account teacher)
  {
    model.setSelectedTeacher(teacher);
  }

  /**
   * Update the student according to the selection
   */
  public void setStudent(StudentAccount student)
  {
    model.setSelectedStudent(student);
  }

  /**
   * Remove the test from the model
   */
  public void removeTest()
  {
    model.removeTest();
  }

  /**
   * Create a session in the model
   */
  public void createSession()
  {
    model.createSession();
  }

  /**
   * Stop the session in the model
   */
  public void stopSession()
  {
    model.stopSession();
  }

  /**
   * Remove the student from the model
   */
  public void removeStudent()
  {
    model.removeStudent();
  }

  /**
   * Remove the teacher from the model
   */
  public void removeTeacher()
  {
    model.removeTeacher();
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
   * Update the tests in the view with the data from the model
   * @param query the text that user entered into the search field
   */
  public void getTestsAll(String query)
  {
    tests.setAll(model.getTestsBySearchA(query));
  }

  /**
   * Update sessions the view with the data from the model
   * @param query the text that user entered into the search field
   */
  public void getSessionAll(String query)
  {
    sessions.setAll(model.getSessionsBySearchA(query));
  }

  /**
   * Update the classes in the view with the data from the model
   * @param query the text that user entered into the search field
   */
  public void getClassAll(String query)
  {
    classNames.setAll(model.getClassBySearchA(query));
  }

  /**
   * Update the students in the with the data from the model
   * @param query the text that user entered into the search field
   */
  public void getStudentAll(String query)
  {
    students.setAll(model.getStudentBySearchA(query));
  }

  /**
   * Update the teachers in the view with the data from the model
   * @param query the text that user entered into the search field
   */
  public void getTeacherAll(String query)
  {
    teachers.setAll(model.getTeacherBySearchA(query));
  }
}
