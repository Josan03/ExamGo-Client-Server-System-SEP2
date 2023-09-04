package com.examgo.client.viewmodel;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.examgo.client.model.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel for a window where user should log in. It is the first view to appear in the app
 *
 * @author Dan Turcan && Cristian Josan && Mihai Mihaila
 * @version 1.0
 */
public class LoginWindowViewModel implements PropertyChangeListener, ViewModel
{
  private ModelManager modelManager;
  private final PropertyChangeSupport support;

  private SimpleStringProperty id;
  private SimpleStringProperty password;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param modelManager ModelManager of the application
   */
  public LoginWindowViewModel(ModelManager modelManager)
  {
    id = new SimpleStringProperty("");
    password = new SimpleStringProperty("");
    this.modelManager = modelManager;
    this.support = new PropertyChangeSupport(this);
    this.modelManager.addPropertyChangeListener("id", this);
    this.modelManager.addPropertyChangeListener("password", this);

  }

  /**
   * A method to bind the login field
   * @param property property to be bound
   */
  public void bindName(StringProperty property)
  {
    property.bindBidirectional(id);
  }

  /**
   * A method to bind the password field
   * @param property property to be bound
   */
  public void bindPassword(StringProperty property)
  {
    property.bindBidirectional(password);
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

  public Account connect(int id, String password)
  {
    Account result = modelManager.connect(id, password);
    return result;
  }

  /**
   * A method to update the view
   */
  public void update()
  {
    id.set("");
    password.set("");
  }

  /**
   * A method to reset the view.
   */
  public void reset()
  {
    id.set("");
    password.set("");
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {
    Platform.runLater(() -> update());
  }

}