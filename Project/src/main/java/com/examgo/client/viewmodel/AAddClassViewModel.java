package com.examgo.client.viewmodel;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import com.examgo.client.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel for a window where administrator adds a class
 *
 * @author Mihai Mihaila && Michael Leo
 * @version 1.0
 */
public class AAddClassViewModel implements ViewModel
{
  private ModelManager model;
  private final PropertyChangeSupport support;

  private PropertyChangeListener listener;

  private SimpleListProperty<Account> teachers;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public AAddClassViewModel(ModelManager model)
  {
    this.model = model;
    this.support = new PropertyChangeSupport(this);
    this.teachers = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    getTeachers();

    listener = evt -> {
      getTeachers();
    };
    model.addPropertyChangeListener("classname", listener);

  }

  @Override public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(propertyName, listener);
  }

  @Override public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.removePropertyChangeListener(propertyName, listener);
  }

  /**
   * A method to add a "class" in the model
   * @param id ID of the "class"
   * @param teacher teacher assigned for this "class"
   */
  public void addClass(String id, Account teacher)
  {
    model.addClassName(id, teacher);
  }

  /**
   * A method to update the Teachers in the view from the model
   */
  public void getTeachers()
  {
    teachers.setAll(model.getAllTeachers());
  }

  /**
   * A method to bind the ListView with teachers.
   * @param property property to be bound
   */
  public void bindTeachers(SimpleListProperty<Account> property)
  {
    property.bindBidirectional(teachers);
  }
}
