package com.examgo.server.dao;

import com.examgo.client.model.Question;
import com.examgo.client.model.Teacher;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.ArrayList;

public class SqlTestDaoIntegrationTest
{
  private TestDao dao;
  private com.examgo.client.model.Test existingTest;
  private com.examgo.client.model.Test newTest;
  private Teacher teacher;
  private Teacher otherTeacher;


  @BeforeEach
  void setUp() throws SQLException
  {
    dao = SqlTestDao.getInstance();
    teacher = new Teacher("Mihai", "Mihaila", "teacher", 4);
    otherTeacher = new Teacher("Mihai", "Mihail", "12345", 5);
    Question questionToExistingTest = new Question(14, 5.0, "Test", 1);
    existingTest = new com.examgo.client.model.Test(14, "Test", teacher);
    existingTest.addQuestion(questionToExistingTest);
  }

  @AfterEach
  void tearDown() throws RemoteException
  {
    if (newTest != null)
    {
      dao.deleteTest(newTest);
      newTest = null;
    }
  }

  @Test
  void aCorrectQuestionIsInsertedToTheDatabase() throws SQLException
  {
    Question question = dao.createQuestion(existingTest, "What time is it?", 10.0, 5);
    existingTest.addQuestion(question);
    assertTrue(existingTest.getQuestions().contains(question));
  }

  @Test
  void anIncorrectQuestionIsInsertedAndAnExceptionIsThrown()
  {
    assertThrows(SQLException.class, () -> dao.createQuestion(existingTest, "What time is it?", 999.0, -1));
    assertThrows(SQLException.class, () -> dao.createQuestion(existingTest, "What time is it?", 99999.0, -1));
  }

  @Test
  void aCorrectTestIsInsertedToTheDatabase() throws RemoteException
  {
    // Here we have already one test in the database which has the status true, that's why we expect the size to be 2
    newTest = dao.storeTest(new com.examgo.client.model.Test("JUnit", teacher));
    assertEquals(2, dao.getAllTests().size());
  }

  @Test
  void anIncorrectTestIsInsertedAndAnExceptionIsThrown() throws RemoteException
  {
    assertThrows(RemoteException.class, () -> dao.storeTest(new com.examgo.client.model.Test("JUnitERROR", new Teacher("Unknown", "Unknown", "????", 1))));
  }

  @Test
  void getAllTestsAfterAddingATest() throws RemoteException
  {
    assertEquals(1, dao.getAllTests().size());
    newTest = dao.storeTest(new com.examgo.client.model.Test("JUnit", teacher));
    assertEquals(2, dao.getAllTests().size());
  }

  @Test
  void getTestsByTeacherAfterAddingOtherOneFromThatTeacher()
      throws RemoteException
  {
    // Because we have already a test made by the 4th teacher we expect 2 tests in the size of the arraylist
    com.examgo.client.model.Test test1 = dao.storeTest(new com.examgo.client.model.Test("JUnit 5th Teacher", otherTeacher));
    com.examgo.client.model.Test test2 = dao.storeTest(new com.examgo.client.model.Test("JUnit 5th Teacher, second", otherTeacher));
    com.examgo.client.model.Test test3 = dao.storeTest(new com.examgo.client.model.Test("JUnit 4th Teacher", teacher));

    ArrayList<com.examgo.client.model.Test> tests4TH = dao.getTestsByTeacher(teacher);
    ArrayList<com.examgo.client.model.Test> tests5TH = dao.getTestsByTeacher(otherTeacher);

    dao.deleteTest(test1);
    dao.deleteTest(test2);
    dao.deleteTest(test3);

    assertEquals(2, tests4TH.size());
    assertEquals(2, tests5TH.size());
  }

  @Test
  void getExistingTestFromDatabase() throws RemoteException
  {
    com.examgo.client.model.Test test = dao.storeTest(existingTest);
    assertEquals(14, test.getId());
  }

  @Test
  void storeATestWithQuestions() throws RemoteException
  {
    Question question = new Question(100, "Who are you?", 10);
    newTest = new com.examgo.client.model.Test("JUnit Test With Questions", otherTeacher);
    newTest.addQuestion(question);
    com.examgo.client.model.Test test = dao.storeTest(newTest);
    dao.deleteTest(test);
    assertEquals(1, test.getQuestions().size());
  }

  @Test
  void updateAnExistingTestTitle() throws RemoteException
  {
    // There is already an existing test with id 14, now we will change the title of test
    com.examgo.client.model.Test test = new com.examgo.client.model.Test(14, "Test from JUnit", teacher);
    // And this is the existing question from the test with id 14 in the database
    Question question = new Question(39, 5.0, "Test", 1);

    dao.updateTest(test, question);
    assertEquals("Test from JUnit", dao.storeTest(test).getTitle());

    // This updates the data back how it was
    dao.updateTest(new com.examgo.client.model.Test(14, "Test", teacher), question);
  }

  @Test
  void updateAnExistingQuestion() throws RemoteException
  {
    // There is already an existing test with id 14, now we will change the title of test
    com.examgo.client.model.Test test = new com.examgo.client.model.Test(14, "Test", teacher);

    // And this is the existing question with the id 39 from the test with id 14 in the database
    // Let's update the question content
    Question question = new Question(39, 5.0, "Question from JUnit", 1);

    dao.updateTest(test, question);
    ArrayList<com.examgo.client.model.Test> tests = dao.getAllTests();

    Question checkQuestion = null;

    for (int i = 0; i < tests.size(); i++)
    {
      for (int j = 0; j < tests.get(i).getQuestions().size(); j++)
      {
        if (tests.get(i).getQuestions().get(j).getId() == 39)
        {
          checkQuestion = tests.get(i).getQuestions().get(j);
        }
      }
    }
    assertEquals("Question from JUnit", checkQuestion.getQuestion());

    // This updates the data back how it was
    dao.updateTest(test, new Question(39, 5.0, "Test", 1));
  }

  // This test is actually deleting the question from the database, not setting the status to false like for the test
  @Test
  void deleteQuestionFromDatabase() throws RemoteException
  {
    // Let's create a question and then delete it
    Question question = new Question(1.0, "Question", 1);
    newTest = new com.examgo.client.model.Test("JUnit Delete Question", teacher);
    newTest.addQuestion(question);

    com.examgo.client.model.Test testAfterInserting = dao.storeTest(newTest);
    dao.deleteQuestion(testAfterInserting.getQuestions().get(0).getId());
    ArrayList<com.examgo.client.model.Test> tests = dao.getAllTests();
    int size = -1;

    for (int i = 0; i < tests.size(); i++)
    {
      if (tests.get(i).getId() == testAfterInserting.getId())
      {
        size = tests.get(i).getQuestions().size();
      }
    }

    assertEquals(0, size);

    // This delete method just sets the status to false in the database
    dao.deleteTest(testAfterInserting);
  }
}
