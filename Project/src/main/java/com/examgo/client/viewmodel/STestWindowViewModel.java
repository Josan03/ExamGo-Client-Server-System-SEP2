package com.examgo.client.viewmodel;

import javafx.beans.property.*;
import com.examgo.client.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewController for a window where student fills in the test questions
 *
 * @author Dan Turcan && Mihai Mihaila
 * @version 1.0
 */
public class STestWindowViewModel implements ViewModel
{
  private ModelManager model;
  private final PropertyChangeSupport support;

  private SimpleObjectProperty<Session> session;

  private SimpleStringProperty question;

  private SimpleStringProperty time;
  private PropertyChangeListener listener;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public STestWindowViewModel(ModelManager model)
  {
    this.model = model;
    this.support = new PropertyChangeSupport(this);
    this.session = new SimpleObjectProperty<>(null);
    this.question = new SimpleStringProperty("");
    this.time = new SimpleStringProperty("");
    listener = evt -> {
      session.set((Session) evt.getNewValue());
      System.out.println(((Session) evt.getNewValue()).getTest().getId());
      support.firePropertyChange("start-session", null, evt.getNewValue());
      System.out.println("VIEWMODEL ALO");
    };

    model.addPropertyChangeListener("session", listener);
  }

  /**
   * A method to add the answer to the model
   */
  public void addAnswer(int question_id, String text)
  {
    model.addAnswer(question_id, text);
  }

  /**
   * A method to send the answers
   */
  public void sendAnswers()
  {
    model.sendAnswers();
  }

  /**
   * A method to bind the session.
   * @param property property to be bound
   */
  public void bindSession(SimpleObjectProperty<Session> property)
  {
    property.bindBidirectional(session);
  }

  /**
   * A method to bind the question.
   * @param property property to be bound
   */
  public void bindQuestion(StringProperty property)
  {
    property.bindBidirectional(question);
  }

  /**
   * A method to bind the time countdown.
   * @param property property to be bound
   */
  public void bindTime(StringProperty property)
  {
    property.bindBidirectional(time);
  }

  /**
   * Store the question locally
   * @param question the question to store
   */
  public void setQuestion(String question)
  {
    this.question.set(question);
  }

  /**
   * Set the time locally
   * @param time current time
   */
  public void setTime(String time)
  {
    this.time.set(time);
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