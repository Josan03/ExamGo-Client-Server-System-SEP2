package com.examgo.client.view;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import com.examgo.client.model.*;
import com.examgo.client.viewmodel.AMainMenuViewModel;
import com.examgo.client.viewmodel.ViewModel;

/**
 * A ViewController of a main window for administrator, where he can choose what to do next
 *
 * @author Michael Leo && Mihai Mihaila && Dan Turcan && Cristian Josan
 * @version 1.0
 */
public class AMainMenuViewController implements ViewController
{
  private String name = "a_m_m";

  @FXML Label administratorName;
  @FXML Label className;

  //tests
  @FXML TextField testSearchField;
  @FXML Button createSession;

  @FXML TextField searchSessionField;
  @FXML Button deleteTest;
  @FXML Button stopSession;
  @FXML TableView<Test> testsTable;
  @FXML TableColumn<Test, Integer> testIDColumn;
  @FXML TableColumn<Test, String> subjectColumn;

  //sessions
  @FXML TextField searchTextField;
  @FXML Button searchTestButton;
  @FXML TableView<Session> sessionsTable;
  @FXML TableColumn<Session, Integer> sessionIDColumn;
  @FXML TableColumn<Session, Boolean> activeColumn;

  //classes
  @FXML TextField searchClassField;
  @FXML Button searchClassButton;
  @FXML Button createClass;
  @FXML Button editClass;
  @FXML Button deleteClass;
  @FXML TableView<ClassName> classesTable;
  @FXML TableColumn<ClassName, String> classNameColumn;

  //students
  @FXML TextField searchStudentField;
  @FXML Button searchStudentButton;
  @FXML Button addStudent;
  @FXML Button editStudent;
  @FXML Button deleteStudent;
  @FXML TableView<StudentAccount> studentsTable;
  @FXML TableColumn<StudentAccount, Integer> studentIDColumn;
  @FXML TableColumn<StudentAccount, String> firstNameColumn;
  @FXML TableColumn<StudentAccount, String> lastNameColumn;

  //teacher
  @FXML TextField searchTeacherField;
  @FXML Button searchTeacherButton;
  @FXML Button addTeacher;
  @FXML Button editTeacher;
  @FXML Button deleteTeacher;
  @FXML TableView<Account> teachersTable;
  @FXML TableColumn<Account, Integer> teacherIDColumn;
  @FXML TableColumn<Account, String> lastNameColumnT;
  @FXML TableColumn<Account, String> firstNameColumnT;

  private ViewHandler viewHandler;
  private AMainMenuViewModel aMainMenuViewModel;
  private Region root;

  private SimpleObjectProperty<Account> account;
  private SimpleListProperty<Test> tests;
  private SimpleListProperty<Session> sessions;
  private SimpleListProperty<StudentAccount> students;
  private SimpleListProperty<Account> teachers;
  private SimpleListProperty<ClassName> classNames;

  private boolean checkClass;
  private boolean checkSession;
  private boolean checkTest;
  private boolean checkStudent;
  private boolean checkTeacher;

  /**
   * FXML method that is run when the "Delete Student" button pressed in the Students view
   */
  @FXML public void deleteStudentPressed()
  {
    if (checkStudent)
      aMainMenuViewModel.removeStudent();
  }

  /**
   * FXML method that is run when the "Search" button pressed in the Tests view
   */
  @FXML public void testSearchButtonPressed()
  {
    aMainMenuViewModel.getTestsAll(testSearchField.getText());
  }

  /**
   * FXML method that is run when the "Search" button pressed in the Sessions view
   */
  @FXML public void sessionSearchButtonPressed()
  {
    aMainMenuViewModel.getSessionAll(searchSessionField.getText());
  }

  /**
   * FXML method that is run when the "Search" button pressed in the Classes view
   */
  @FXML public void classSearchButtonPressed()
  {
    aMainMenuViewModel.getClassAll(searchClassField.getText());
  }

  /**
   * FXML method that is run when the "Search" button pressed in the Students view
   */
  @FXML public void studentSearchButtonPressed()
  {
    aMainMenuViewModel.getStudentAll(searchStudentField.getText());
  }

  /**
   * FXML method that is run when the "Search" button pressed in the Teachers view
   */
  @FXML public void teacherSearchButtonPressed()
  {
    aMainMenuViewModel.getTeacherAll(searchTeacherField.getText());
  }

  /**
   * FXML method that is run when the "Edit" button pressed in the Students view
   */
  @FXML public void editStudentPressed()
  {
    if (checkStudent)
      viewHandler.openView("a_e_s");
  }

  /**
   * FXML method that is run when the "Add" button pressed in the Students view
   */
  @FXML public void addStudentPressed()
  {
    viewHandler.openView("a_a_s");
  }

  /**
   * FXML method that is run when the "Delete" button pressed in the Classes view
   */
  @FXML public void deleteClassPressed()
  {
    if (checkClass)
      aMainMenuViewModel.deleteClass();
  }

  /**
   * FXML method that is run when the "Add" button pressed in the Class view
   */
  @FXML public void createClassPressed()
  {
    viewHandler.openView("a_a_c");
  }

  /**
   * FXML method that is run when the "Edit" button pressed in the Students view
   */
  @FXML public void editClassPressed()
  {
    if (checkClass)
    {
      viewHandler.openView("a_e_c");
    }
  }

  /**
   * FXML method that is run when the "Add" button pressed in the Teachers view
   */
  @FXML public void addTeacherPressed()
  {
    viewHandler.openView("a_a_t");
  }

  /**
   * FXML method that is run when the "Delete" button pressed in the Teachers view
   */
  @FXML public void deleteTeacherPressed()
  {
    if (checkTeacher)
    {
      aMainMenuViewModel.removeTeacher();
    }
  }

  /**
   * FXML method that is run when the "Edit" button pressed in the Teachers view
   */
  @FXML public void editTeacherPressed()
  {
    if (checkTeacher)
    {
      viewHandler.openView("a_e_t");
    }
  }

  /**
   * FXML method that is run when the "Create" button pressed in the Sessions view
   */
  @FXML public void createSessionPressed()
  {
    if (checkTest)
    {
      aMainMenuViewModel.createSession();
      AlertControl.showInformation("Session created", null,
          "Session created. Check the main menu -> Sessions section.");
    }

  }

  /**
   * FXML method that is run when the "Delete" button pressed in the Sessions view
   */
  @FXML public void deleteTestPressed()
  {
    if (checkTest)
      aMainMenuViewModel.removeTest();
  }

  /**
   * FXML method that is run when the "Stop" button pressed in the Sessions view
   */
  @FXML public void stopSessionPressed()
  {
    if (checkSession)
      aMainMenuViewModel.stopSession();
  }

  /**
   * Overridden method that is implemented from ViewController interface, initializes the controller.
   *
   * @param viewHandler ViewHandler of the application
   * @param viewModel   specific ViewModel of the application
   * @param root        root of the FXMLLoader
   */
  @Override public void init(ViewHandler viewHandler, ViewModel viewModel,
      Region root)
  {
    this.viewHandler = viewHandler;
    this.aMainMenuViewModel = (AMainMenuViewModel) viewModel;
    this.root = root;
    account = new SimpleObjectProperty<>(null);
    tests = new SimpleListProperty<>(FXCollections.observableArrayList());
    sessions = new SimpleListProperty<>(FXCollections.observableArrayList());
    students = new SimpleListProperty<>(FXCollections.observableArrayList());
    teachers = new SimpleListProperty<>(FXCollections.observableArrayList());
    classNames = new SimpleListProperty<>(FXCollections.observableArrayList());

    aMainMenuViewModel.bindAccount(account);
    aMainMenuViewModel.bindTests(tests);
    aMainMenuViewModel.bindSessions(sessions);
    aMainMenuViewModel.bindTeachers(teachers);
    aMainMenuViewModel.bindClassNames(classNames);
    aMainMenuViewModel.bindStudents(students);

    checkClass = false;
    checkSession = false;
    checkStudent = false;
    checkTeacher = false;
    checkTest = false;

    administratorName.setText(
        account.get().getFirstName() + " " + account.get().getLastName());

    testsTable.setItems(tests);
    testIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    subjectColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
    testsTable.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null)
          {
            checkTest = true;
            aMainMenuViewModel.setTest(newSelection);
            deleteTest.setDisable(false);
            createSession.setDisable(false);
          }
          else
          {
            checkTest = false;
            aMainMenuViewModel.setTest(null);
            deleteTest.setDisable(true);
            createSession.setDisable(true);
          }
        });

    sessionsTable.setItems(sessions);
    sessionIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    activeColumn.setCellValueFactory(new PropertyValueFactory<>("status"));
    sessionsTable.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null)
          {
            checkSession = true;
            aMainMenuViewModel.setSession(newSelection);
            stopSession.setDisable(false);

          }
          else
          {
            checkSession = false;
            aMainMenuViewModel.setSession(null);
            stopSession.setDisable(true);

          }
        });

    classesTable.setItems(classNames);
    classNameColumn.setCellValueFactory(
        new PropertyValueFactory<>("className"));
    classesTable.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null)
          {
            aMainMenuViewModel.setClass(newSelection);
            checkClass = true;
            editClass.setDisable(false);
            deleteClass.setDisable(false);
          }
          else
          {
            aMainMenuViewModel.setClass(null);
            checkClass = false;
            editClass.setDisable(true);
            deleteClass.setDisable(true);
          }
        });

    studentsTable.setItems(students);
    studentIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    firstNameColumn.setCellValueFactory(
        new PropertyValueFactory<>("firstName"));
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    studentsTable.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null)
          {
            aMainMenuViewModel.setStudent(newSelection);
            checkStudent = true;
            editStudent.setDisable(false);
            deleteStudent.setDisable(false);
          }
          else
          {
            aMainMenuViewModel.setStudent(null);
            checkStudent = false;
            editStudent.setDisable(true);
            deleteStudent.setDisable(true);
          }
        });

    teachersTable.setItems(teachers);
    teacherIDColumn.setCellValueFactory(new PropertyValueFactory<>("ID"));
    firstNameColumnT.setCellValueFactory(
        new PropertyValueFactory<>("firstName"));
    lastNameColumnT.setCellValueFactory(new PropertyValueFactory<>("lastName"));
    teachersTable.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null)
          {
            aMainMenuViewModel.setTeacher(newSelection);
            checkTeacher = true;
            editTeacher.setDisable(false);
            deleteTeacher.setDisable(false);
          }
          else
          {
            aMainMenuViewModel.setTeacher(null);
            checkTeacher = false;
            editTeacher.setDisable(true);
            deleteTeacher.setDisable(true);
          }
        });
  }

  /**
   * Overridden method that is implemented from ViewController interface to get the root.
   *
   * @return the root of this view
   */
  @Override public Region getRoot()
  {
    return root;
  }

  /**
   * Overridden method that is implemented from ViewController interface to reset the view.
   */
  @Override public void reset()
  {

  }
}
