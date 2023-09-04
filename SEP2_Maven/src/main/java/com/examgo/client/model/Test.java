package com.examgo.client.model;


import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class that stores information about a test
 *
 * @author Michael Leo && Cristian Josan && Mihai Mihaila
 * @version 1.0
 */
public class Test implements Serializable
{
  private ArrayList<Question> questions;
  private int id;
  private String title;
  private Account teacher;

  /**
   * Two-argument constructor initializing information for Test object
   *
   * @param title title to initialize
   * @param teacher teacher to initialize
   */
  public Test(String title, Account teacher)
  {
    questions = new ArrayList<Question>();
    this.id = -1;
    this.title = title;
    this.teacher = teacher;
  }

  /**
   * Three-argument constructor initializing information for Test object
   *
   * @param title title to initialize
   * @param teacher teacher to initialize
   */
  public Test(int id, String title, Account teacher)
  {
    questions = new ArrayList<Question>();
    this.id = id;
    this.title = title;
    this.teacher = teacher;
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
   * Returns the title characteristic
   *
   * @return a String value which represents the title
   */
  public String getTitle()
  {
    return title;
  }

  /**
   * Returns the teacher characteristic
   *
   * @return an Account value which represents the teacher
   */
  public Account getTeacher()
  {
    return teacher;
  }


  /**
   * Add a question to the ArrayList of questions
   *
   * @param question used to add the question to the array list
   */
  public void addQuestion(Question question)
  {
    questions.add(question);
  }


  /**
   * Sets the questions characteristic
   *
   * @param questions used to set the questions characteristic
   */
  public void setQuestions(ArrayList<Question> questions)
  {
    this.questions = questions;
  }

  /**
   * Returns the questions characteristic
   *
   * @return an ArrayList of Question value which represents the questions
   */
  public ArrayList<Question> getQuestions()
  {
    return questions;
  }
}
