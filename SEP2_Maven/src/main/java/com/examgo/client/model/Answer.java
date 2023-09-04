package com.examgo.client.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class that stores information about an answer
 *
 * @author Mihai Mihaila && Michael Leo
 * @version 1.0
 */
public class Answer implements Serializable
{
  private int answer_id;
  private int question_id;
  private String answer;
  private int points;

  /**
   * Two-argument constructor initializing information for Answer object
   *
   * @param question_id question_id to initialize
   * @param answer answer to initialize
   */
  public Answer(int question_id, String answer)
  {
    this.question_id = question_id;
    this.answer = answer;
  }

  /**
   * Four-argument constructor initializing information for Answer object
   *
   * @param answer_id answer_id to initialize
   * @param question_id question_id to initialize
   * @param answer answer to initialize
   * @param points points to initialize
   */
  public Answer(int answer_id, int question_id, String answer, int points)
  {
    this.answer_id = answer_id;
    this.question_id = question_id;
    this.answer = answer;
    this.points = points;
  }

  /**
   * Returns the question id characteristic
   *
   * @return an int value which represents the question id
   */
  public int getQuestion_id()
  {
    return question_id;
  }

  /**
   * Returns the answer characteristic
   *
   * @return an Answer value which represents the answer
   */
  public String getAnswer()
  {
    return answer;
  }

  /**
   * Sets the answer characteristic
   *
   * @param answer used to set the answer characteristic
   */
  public void setAnswer(String answer)
  {
    this.answer = answer;
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

  /**
   * Sets the points characteristic
   *
   * @param points used to set the points characteristic
   */
  public void setPoints(int points)
  {
    this.points = points;
  }

  /**
   * Returns the answer id characteristic
   *
   *  @return an int value which represents the answer id
   */
  public int getAnswer_id()
  {
    return answer_id;
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (!(o instanceof Answer answer1))
      return false;
    return getQuestion_id() == answer1.getQuestion_id() && Objects.equals(
        getAnswer(), answer1.getAnswer());
  }

  @Override public int hashCode()
  {
    return Objects.hash(getQuestion_id(), getAnswer());
  }
}
