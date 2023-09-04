package com.examgo.client.viewmodel;

import javafx.beans.property.SimpleObjectProperty;
import com.examgo.client.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel for a window where administrator adds a class
 *
 * @author Mihai Mihaila && Michael Leo
 * @version 1.0
 */
public class AEditTeacherViewModel implements ViewModel
{
  private ModelManager model;
  private final PropertyChangeSupport support;

  private PropertyChangeListener listener;

  private PropertyChangeListener listener2;

  private SimpleObjectProperty<Account> teacher;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public AEditTeacherViewModel(ModelManager model)
  {
    this.model = model;
    this.support = new PropertyChangeSupport(this);
    this.teacher = new SimpleObjectProperty<>(null);

    listener = evt -> {
      teacher.set((Teacher) evt.getNewValue());
    };

    model.addPropertyChangeListener("steacher", listener);

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
   * A method to bind the teacher
   * @param property property to be bound
   */
  public void bindAccount(SimpleObjectProperty<Account> property)
  {
    property.bindBidirectional(teacher);
  }

  /**
   * A method to add a single teacher into the model
   * @param id ID of the student
   * @param firstName first name of the student
   * @param firstName first name of the student
   * @param password his password to log in
   */
  public void editTeacher(int id, String firstName, String lastName,
      String password)
  {
    model.updateTeacher(id, firstName, lastName, password);
  }
}


