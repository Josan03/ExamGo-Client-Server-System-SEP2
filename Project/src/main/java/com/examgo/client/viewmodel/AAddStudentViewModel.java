package com.examgo.client.viewmodel;

import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import com.examgo.client.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel for a window where administrator adds students
 *
 * @author Mihai Mihaila
 * @version 1.0
 */
public class AAddStudentViewModel implements ViewModel
{
  private ModelManager model;
  private final PropertyChangeSupport support;

  private PropertyChangeListener listener;

  private SimpleListProperty<ClassName> classNames;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public AAddStudentViewModel(ModelManager model)
  {
    this.model = model;
    this.support = new PropertyChangeSupport(this);
    this.classNames = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    getClassNames();

    listener = evt -> {
      getClassNames();
    };

    model.addPropertyChangeListener("classname", listener);
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
   * A method to update the Classes in the view from the model
   */
  public void getClassNames()
  {
    classNames.setAll(model.getAllClassNames());
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
   * A method to add a single student into the model
   * @param firstName first name of the student to add
   * @param lastName last name of the student to add
   * @param class_id name of the class
   */
  public void addSingleStudent(String firstName, String lastName,
      String class_id)
  {
    model.addSingleStudent(firstName, lastName, class_id);
  }

  /**
   * A method to add multiple student into the model through the file
   * @param fileName specified name of the file that contains the data
   */
  public void addMultipleStudents(String fileName)
  {
    model.addMultipleStudents(fileName);
  }
}
