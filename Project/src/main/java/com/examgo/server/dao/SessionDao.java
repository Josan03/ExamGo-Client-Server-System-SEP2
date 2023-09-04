package com.examgo.server.dao;

import com.examgo.client.model.Account;
import com.examgo.client.model.Session;
import com.examgo.client.model.Test;

import java.rmi.RemoteException;
import java.util.ArrayList;

/**
 * An interface that represents the structure of a session data object access
 *
 * @author Cristian Josan
 * @version 1.0
 */
public interface SessionDao
{
  /**
   * Creates a session in the database for a specific test
   *
   * @param test a Test object is used to create the session
   * @return a Session object if it was created in the database
   * @throws RemoteException
   */
  Session createSession(Test test) throws RemoteException;

  /**
   * Stops the session, setting the status to false in the database
   *
   * @param id an int value is used to stop a specific session
   * @return a boolean value, if the status was updated to false - true, else - false
   * @throws RemoteException
   */
  boolean stopSession(int id) throws RemoteException;

  /**
   * Gets all the existing sessions from the database
   *
   * @return an ArrayList of all the sessions
   * @throws RemoteException
   */
  ArrayList<Session> getAllSessions() throws RemoteException;

  /**
   * Gets all the sessions created by a specific teacher from the database
   *
   * @param teacher a Teacher object is used to search for all the sessions created by that specific teacher
   * @return an ArrayList of all the sessions by that specific teacher
   * @throws RemoteException
   */
  ArrayList<Session> getSessionsByTeacher(Account teacher) throws RemoteException;
}
