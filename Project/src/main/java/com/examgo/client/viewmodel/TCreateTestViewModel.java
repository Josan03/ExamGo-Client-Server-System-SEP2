package com.examgo.client.viewmodel;

import com.examgo.client.model.*;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel of a window where teacher creates a test
 *
 * @author Michael Leo && Mihai Mihaila
 * @version 1.0
 */
public class TCreateTestViewModel implements PropertyChangeListener, ViewModel
{
  private final ModelManager model;

  private PropertyChangeSupport support;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public TCreateTestViewModel(ModelManager model)
  {
    this.model = model;
    support = new PropertyChangeSupport(this);
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
   * A method to add the question to the model
   * @param time time for the question completion
   * @param points amount of points the question can bring
   * @param question the text of the question
   */
  public void addQuestion(double time, String question, int points)
  {
    model.addQuestion(time, question, points);
  }

  /**
   * A method to add the test to the model
   * @param title the name of the test
   */
  public void addTest(String title)
  {
    model.addTest(title);
  }

  /**
   * A method to clear the questions in the model
   */
  public void clearQuestions()
  {
    model.clearQuestions();
  }

  @Override public void propertyChange(PropertyChangeEvent evt)
  {

  }
}
