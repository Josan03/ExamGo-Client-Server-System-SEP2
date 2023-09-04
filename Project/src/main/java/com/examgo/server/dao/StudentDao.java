package com.examgo.server.dao;

import com.examgo.client.model.Answer;
import com.examgo.client.model.Session;
import com.examgo.client.model.StudentAccount;
import com.examgo.client.model.StudentSession;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * An interface that represents the structure of a student data object access
 *
 * @author Cristian Josan
 * @version 1.0
 */
public interface StudentDao
{
  /**
   * Gets the student from the database
   *
   * @param id an int value is used to check if there is such student
   * @return a StudentAccount object which connected to the server
   * @throws RemoteException
   */
  StudentAccount getStudent(int id) throws RemoteException;

  /**
   * Creates a student session with the status true (connects a specific student to a specific session)
   * The connection between a student and a session is done only once
   *
   * @param sessionId an int value is used for a specific session to search
   * @param studentId an int value is used for a specific student to connect
   * @return a Session object to which a student connected
   * @throws RemoteException
   */
  Session connectSession(int sessionId, int studentId) throws RemoteException;

  /**
   * Sends and stores the answers to the questions from a test to the database
   *
   * @param sessionId an int value is used for a specific session
   * @param answers an ArrayList of all the answers to the questions
   * @throws RemoteException
   */
  void sendAnswers(int sessionId, ArrayList<Answer> answers, int studentID) throws RemoteException;

  /**
   * Gets all the sessions to which a student connected from the database
   *
   * @param studentId an int value is used for a specific student to search for the connection to a session
   * @return an ArrayList of all the student sessions
   * @throws RemoteException
   */
  ArrayList<StudentSession> getSessionsByStudent(int studentId) throws RemoteException;
}
