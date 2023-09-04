package com.examgo.client.viewmodel;

import com.examgo.client.model.Account;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import com.examgo.client.model.ClassName;
import com.examgo.client.model.ModelManager;
import com.examgo.client.model.Teacher;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel for a window where administrator edits a class
 *
 * @author Mihai Mihaila
 * @version 1.0
 */
public class AEditClassViewModel implements ViewModel
{
  private ModelManager model;
  private final PropertyChangeSupport support;

  private SimpleListProperty<Account> teachers;

  private PropertyChangeListener listener;

  private PropertyChangeListener listener2;

  private SimpleObjectProperty<ClassName> className;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public AEditClassViewModel(ModelManager model)
  {
    this.model = model;
    this.support = new PropertyChangeSupport(this);
    this.teachers = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    getTeachers();
    this.className = new SimpleObjectProperty<>(null);

    listener2 = evt -> {
      getTeachers();
    };

    listener = evt -> {
      className.set((ClassName) evt.getNewValue());
    };

    model.addPropertyChangeListener("classname2", listener);
    model.addPropertyChangeListener("classname", listener2);
    System.out.println(teachers.get());
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
   * A method to update a chosen Class in the model
   */
  public void editClass(String oldId, String id, Account teacher)
  {
    model.updateClassName(oldId, id, teacher);
  }

  /**
   * A method to update the teachers in the view from the model
   */
  public void getTeachers()
  {
    teachers.setAll(model.getAllTeachers());
  }

  /**
   * A method to bind the Class.
   * @param property property to be bound
   */
  public void bindClass(SimpleObjectProperty<ClassName> property)
  {
    property.bindBidirectional(className);
  }

  /**
   * A method to bind the list with teachers.
   * @param property property to be bound
   */
  public void bindTeachers(SimpleListProperty<Account> property)
  {
    property.bindBidirectional(teachers);
  }
}
