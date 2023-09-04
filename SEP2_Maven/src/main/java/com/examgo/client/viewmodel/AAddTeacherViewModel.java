package com.examgo.client.viewmodel;

import com.examgo.client.model.ModelManager;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel for a window where administrator adds a teacher
 *
 * @author Mihai Mihaila
 * @version 1.0
 */
public class AAddTeacherViewModel implements ViewModel
{
  private ModelManager model;
  private final PropertyChangeSupport support;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public AAddTeacherViewModel(ModelManager model)
  {
    this.model = model;
    this.support = new PropertyChangeSupport(this);
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
   * A method to add a single teacher into the model
   * @param firstName first name of the student to add
   * @param lastName last name of the student to add
   */
  public void addSingleTeacher(String firstName, String lastName)
  {
    model.addSingleTeacher(firstName, lastName);
  }

  /**
   * A method to add multiple teachers into the model through the file
   * @param fileName specified name of the file that contains the data
   */
  public void addMultipleTeachers(String fileName)
  {
    model.addMultipleTeachers(fileName);
  }
}

