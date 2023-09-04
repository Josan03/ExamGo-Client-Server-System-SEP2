package com.examgo.server;

import com.examgo.server.dao.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * A class that starts the rmi server
 *
 * @author Cristian Josan
 * @version 1.0
 */
public class ExamServer
{
  /**
   * The method which starts the whole rmi server
   *
   * @param args a command line argument array which is used to retrieve input from the console
   * @throws Exception
   */
  public static void main(String[] args) throws Exception
  {
    AdministratorDao administratorDao = SqlAdministratorDao.getInstance();
    TeacherDao teacherDao = SqlTeacherDao.getInstance();
    StudentDao studentDao = SqlStudentDao.getInstance();
    TestDao testDao = SqlTestDao.getInstance();
    SessionDao sessionDao = SqlSessionDao.getInstance();
    ExamImplementation exam = new ExamImplementation(administratorDao, teacherDao, studentDao, testDao, sessionDao);
    Registry registry = LocateRegistry.createRegistry(8090);
    registry.bind("exam", exam);
    System.out.println("Server is running");
  }
}
