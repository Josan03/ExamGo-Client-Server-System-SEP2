package com.examgo.server.dao;

import com.examgo.client.model.Account;
import com.examgo.client.model.Question;
import com.examgo.client.model.Teacher;
import com.examgo.client.model.Test;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * An interface that represents the structure of a test data object access
 *
 * @author Cristian Josan
 * @version 1.0
 */
public interface TestDao
{
  /**
   * Creates a question in the database
   *
   * @param test a Test object is used for a specific test in which the question is created
   * @param question a String value is used for the content of the question
   * @param time a double value is used for the time of the question
   * @param points an int value is used for the maximum value of points for the question
   * @return a Question object which was created
   * @throws SQLException
   */
  Question createQuestion(Test test, String question, double time, int points)
      throws SQLException;

  /**
   * Creates a test in the database if it does not exist
   *
   * @param test a Test object is used for creation
   * @return a Test object with all the properties from the database
   * @throws RemoteException
   */
  Test storeTest(Test test) throws RemoteException;

  /**
   * Gets all the tests created by a teacher from the database
   *
   * @param teacher a Teacher object is used to search by specific teacher
   * @return an ArrayList of all the tests created by a specified teacher
   * @throws RemoteException
   */
  ArrayList<Test> getTestsByTeacher(Account teacher) throws RemoteException;

  /**
   * Gets all the tests from the database
   *
   * @return an ArrayList of all the existing tests in the database
   * @throws RemoteException
   */
  ArrayList<Test> getAllTests() throws RemoteException;

  /**
   * Sets a flag, status to false if the test is removed (it does not remove it from the database)
   *
   * @param test a Test object is used to remove the specific test
   * @return a boolean value, if the status was updated to false - true, else - false
   * @throws RemoteException
   */
  boolean deleteTest(Test test) throws RemoteException;

  /**
   * Removes the question from the test and from the database
   *
   * @param questionId an int value is used to remove the specific question
   * @return a boolean value, if the question was removed - true, else - false
   * @throws RemoteException
   */
  boolean deleteQuestion(int questionId) throws RemoteException;

  /**
   * Updates the test in the database with the new properties
   *
   * @param test a Test object is used to update a specific test with new properties
   * @param question a Question object is used to update a specific question with new properties
   * @throws RemoteException
   */
  void updateTest(Test test, Question question) throws RemoteException;

}
