package com.examgo.client.viewmodel;

import javafx.beans.property.SimpleObjectProperty;
import com.examgo.client.model.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * A ViewModel of a window where teacher edits a test
 *
 * @author Michael Leo && Mihai Mihaila && Cristian Josan
 * @version 1.0
 */
public class TEditTestViewModel implements ViewModel
{
  private final ModelManager model;

  private PropertyChangeSupport support;

  private PropertyChangeListener selectedTest;

  private SimpleObjectProperty<Test> test;

  /**
   * One-argument constructor to initialize the ViewModel
   * @param model ModelManager of the application
   */
  public TEditTestViewModel(ModelManager model)
  {
    test = new SimpleObjectProperty<>(null);
    support = new PropertyChangeSupport(this);
    selectedTest = evt -> {
      test.set((Test) evt.getNewValue());
      support.firePropertyChange("ctest", null, (Test)evt.getNewValue());
    };
    this.model = model;
    model.addPropertyChangeListener("test", selectedTest);

  }

  /**
   * A method to bind the test.
   * @param property property to be bound
   */
  public void bindTest(SimpleObjectProperty<Test> property)
  {
    property.bindBidirectional(test);
  }

  public void addPropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {
    support.addPropertyChangeListener(propertyName, listener);
  }

  @Override public void removePropertyChangeListener(String propertyName,
      PropertyChangeListener listener)
  {

  }

  /**
   * A method to update the test in the model
   * @param index index of the question
   * @param time time given to complete the question
   * @param question new question text
   * @param points points that the question can give
   * @param subject subject of the question
   */
  public void editTest(int index, double time, String question, int points,
      String subject)
  {
    model.editTest(index, time, question, points, subject);
  }

  /**
   * A method to add a question in the model
   * @param time time given to complete the question
   * @param question new question text
   * @param point points that the question can give
   */
  public void addSingleQuestion(double time, String question, int point)
  {
    model.addQuestionSingle(time, question, point);
  }

  /**
   * A method to rempve a question in the model
   * @param question the question to remove
   */
  public void removeQuestion(Question question)
  {
    model.removeQuestion(question);
  }
}
