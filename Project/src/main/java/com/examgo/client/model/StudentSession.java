package com.examgo.client.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * A class that stores information about a student session
 *
 * @author Mihai Mihaila && Michael Leo
 * @version 1.0
 */
public class StudentSession implements Serializable
{
  private int session_id;
  private String subject;
  private int grade;

  /**
   * Three-argument constructor initializing information for StudentSession object
   *
   * @param session_id session id to initialize
   * @param subject    subject to initialize
   * @param grade      grade to initialize
   */
  public StudentSession(int session_id, String subject, int grade)
  {
    this.grade = grade;
    this.session_id = session_id;
    this.subject = subject;
  }

  /**
   * Returns the session id characteristic
   *
   * @return an int value which represents the session id
   */
  public int getSession_id()
  {
    return session_id;
  }

  /**
   * Returns the grade characteristic
   *
   * @return an int value which represents the grade
   */
  public int getGrade()
  {
    return grade;
  }

  /**
   * Returns the subject characteristic
   *
   * @return a String value which represents the subject
   */
  public String getSubject()
  {
    return subject;
  }

  /**
   * Sets the session_id characteristic
   *
   * @param session_id used to set the session id
   */
  public void setSession_id(int session_id)
  {
    this.session_id = session_id;
  }

  /**
   * Sets the grade characteristic
   *
   * @param grade used to set the grade
   */
  public void setGrade(int grade)
  {
    this.grade = grade;
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (!(o instanceof StudentSession that))
      return false;
    return getSession_id() == that.getSession_id()
        && getGrade() == that.getGrade() && getSubject().equals(
        that.getSubject());
  }

  @Override public int hashCode()
  {
    return Objects.hash(getSession_id(), getSubject(), getGrade());
  }
}
