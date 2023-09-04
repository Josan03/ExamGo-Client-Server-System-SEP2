package com.examgo.client.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class that stores information about an answer
 *
 * @author Mihai Mihaila && Dan Turcan && Cristian Josan
 * @version 1.0
 */
public class Question implements Serializable
{
  private int id;
  private double time;
  private String question;

  private int points;

  /**
   * Three-argument constructor initializing information for Question object
   *
   * @param time     time to initialize
   * @param question quiestion to initialize
   * @param points   points to initialize
   */
  public Question(double time, String question, int points)
  {
    this.id = -1;
    this.question = question;
    this.time = time;
    this.points = points;
  }

  /**
   * Four-argument constructor initializing information for Question object
   *
   * @param id       id to initialize
   * @param time     time to initialize
   * @param question quiestion to initialize
   * @param points   points to initialize
   */
  public Question(int id, double time, String question, int points)
  {
    this.id = id;
    this.question = question;
    this.time = time;
    this.points = points;
  }

  /**
   * Sets the id characteristic
   *
   * @param id used to set the id characteristic
   */
  public void setId(int id)
  {
    this.id = id;
  }

  /**
   * Returns the id characteristic
   *
   * @return an int value which represents the id
   */
  public int getId()
  {
    return id;
  }

  /**
   * Returns the time characteristic
   *
   * @return a double value which represents the time
   */
  public double getTime()
  {
    return time;
  }

  /**
   * Returns the question characteristic
   *
   * @return a String value which represents the question
   */
  public String getQuestion()
  {
    return question;
  }

  /**
   * Sets the question characteristic
   *
   * @param question used to set the question characteristic
   */
  public void setQuestion(String question)
  {
    this.question = question;
  }

  /**
   * Sets the time characteristic
   *
   * @param time used to set the time characteristic
   */
  public void setTime(double time)
  {
    this.time = time;
  }

  /**
   * Returns the points characteristic
   *
   * @return an int value which represents the points
   */
  public int getPoints()
  {
    return points;
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Question question1 = (Question) o;
    return id == question1.id && Double.compare(question1.time, time) == 0
        && points == question1.points && question.equals(question1.question);
  }

  @Override public int hashCode()
  {
    return Objects.hash(id, time, question, points);
  }

  @Override public String toString()
  {
    return question;
  }
}
