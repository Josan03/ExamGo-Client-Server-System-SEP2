package com.examgo.client.viewmodel;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import com.examgo.client.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel for a window where administrator edits students
 *
 * @author Mihai Mihaila && Michael Leo
 * @version 1.0
 */
public class AEditStudentViewModel implements ViewModel
{
  private ModelManager model;
  private final PropertyChangeSupport support;

  private PropertyChangeListener listener;

  private PropertyChangeListener listener2;

  private SimpleListProperty<ClassName> classNames;

  private SimpleObjectProperty<StudentAccount> student;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public AEditStudentViewModel(ModelManager model)
  {
    this.model = model;
    this.support = new PropertyChangeSupport(this);
    this.classNames = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    this.student = new SimpleObjectProperty<>(null);

    listener = evt -> {
      student.set((Student) evt.getNewValue());
    };

    listener2 = evt -> {
      getClassNames();
    };

    model.addPropertyChangeListener("changedStudentA", listener);
    model.addPropertyChangeListener("classname", listener2);


    getClassNames();
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
   * A method to bind the list with Classes.
   * @param property property to be bound
   */
  public void bindClassNames(SimpleListProperty<ClassName> property)
  {
    property.bindBidirectional(classNames);
  }

  /**
   * A method to bind the student.
   * @param property property to be bound
   */
  public void bindAccount(SimpleObjectProperty<StudentAccount> property)
  {
    property.bindBidirectional(student);
  }

  /**
   * A method to update the Classes in the view from the model
   */
  public void getClassNames()
  {
    classNames.setAll(model.getAllClassNames());
  }

  /**
   * A method to update a student in the model
   * @param id ID of the student
   * @param class_id name of the class he is in
   * @param firstName first name of the student
   * @param firstName first name of the student
   * @param password his password to log in
   */
  public void editStudent(int id, String firstName, String lastName,
      String password, String class_id)
  {
    model.updateStudent(id, firstName, lastName, password, class_id);
  }
}

