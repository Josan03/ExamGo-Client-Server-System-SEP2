package com.examgo.client.view;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import com.examgo.client.model.*;
import com.examgo.client.viewmodel.TMainMenuViewModel;
import com.examgo.client.viewmodel.ViewModel;

/**
 * A ViewController of a main window for teacher, where he can choose what to do next
 *
 * @author Michael Leo && Mihai Mihaila && Dan Turcan && Cristian Josan
 * @version 1.0
 */
public class TMainMenuViewController implements ViewController
{
  //tests
  @FXML public Label teacherName;
  @FXML public TextField testSearchField;
  @FXML public Button testSearchButton;
  @FXML public Button createTest;
  @FXML public Button createSession;
  @FXML public Button deleteTest;
  @FXML public Button editTest;
  @FXML public TableView<Test> testsTable;
  @FXML public TableColumn<Test, Integer> testIDColumn;
  @FXML public TableColumn<Test, String> testSubjectColumn;

  //sessions
  @FXML public TextField searchSessionField;
  @FXML public Button searchSessionButton;
  @FXML public Button deleteSession;
  @FXML public Button editSession;
  @FXML public TableView<Session> sessionsTable;
  @FXML public TableColumn<Session, Integer> sessionIDColumn;
  @FXML public TableColumn<Session, Boolean> sessionStatusColumn;

  //classes
  @FXML public TextField searchClassField;
  @FXML public Button searchClassButton;
  @FXML public TableView<ClassName> classesTable;
  @FXML public TableColumn<ClassName, String> classNameColumn;

  //students
  @FXML public TextField searchStudentField;
  @FXML public Button searchStudentButton;
  @FXML public TableView<StudentAccount> studentsTable;

  @FXML public TableColumn<StudentAccount, Integer> studentIDColumn;
  @FXML public TableColumn<StudentAccount, String> firstNameColumn;
  @FXML public TableColumn<StudentAccount, String> lastNameColumn;

  private ViewHandler viewHandler;
  private TMainMenuViewModel tMainMenuViewModel;
  private Region root;
  private String name = "t_m_m";

  private SimpleObjectProperty<Account> account;

  private SimpleListProperty<Session> sessions;
  private SimpleListProperty<Test> tests;

  private SimpleListProperty<ClassName> classNames;

  private SimpleListProperty<StudentAccount> students;

  private boolean checkTest;
  private boolean checkSession;

  /**
   * FXML method that is run when the "Search" button is pressed in the Sessions view.
   */
  @FXML public void searchSessionButtonPressed()
  {
    tMainMenuViewModel.getSessionsBySearchT(searchSessionField.getText());
  }

  /**
   * FXML method that is run when the "Search" button is pressed in the Classes view.
   */
  @FXML public void searchClassButtonPressed()
  {
    tMainMenuViewModel.getClassesBySearchT(searchClassField.getText());
  }

  /**
   * FXML method that is run when the "Search" button is pressed in the Students view.
   */
  @FXML public void searchStudentButtonPressed()
  {
    tMainMenuViewModel.getStudentsBySearchT(searchStudentField.getText());
  }

  /**
   * FXML method that is run when the "Create" button is pressed in the Tests view.
   */
  @FXML public void createTestPressed()
  {
    viewHandler.openView("t_c_t");
  }

  /**
   * FXML method that is run when the "Search" button is pressed in the Tests view.
   */
  @FXML public void testSearchButtonPressed()
  {
    tMainMenuViewModel.getTestsBySearchT(testSearchField.getText());
  }

  /**
   * FXML method that is run when the "Edit" button is pressed in the Tests view.
   */
  @FXML public void editTestPressed()
  {
    if (checkTest)
      viewHandler.openView("t_e_t");
  }

  /**
   * FXML method that is run when the "Delete" button is pressed in the Tests view.
   */
  @FXML public void deleteTestPressed()
  {
    tMainMenuViewModel.removeTest();
  }

  /**
   * FXML method that is run when the "Edit" button is pressed in the Sessions view.
   */
  @FXML public void editSessionPressed()
  {
    if (checkSession)
      viewHandler.openView("t_c_s");
  }

  /**
   * FXML method that is run when the "Delete" button is pressed in the Sessions view.
   */
  @FXML public void deleteSessionPressed()
  {
    tMainMenuViewModel.stopSession();
  }

  /**
   * FXML method that is run when the "Create" button is pressed in the Sessions view.
   */
  @FXML public void createSessionPressed()
  {
    if (checkTest)
    {
      tMainMenuViewModel.createSession();
      AlertControl.showInformation("Session created", null,
          "Session created. Check the main menu -> Sessions section.");
    }
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
    this.tMainMenuViewModel = (TMainMenuViewModel) viewModel;
    this.root = root;
    account = new SimpleObjectProperty<>(null);
    tests = new SimpleListProperty<>(FXCollections.observableArrayList());
    sessions = new SimpleListProperty<>(FXCollections.observableArrayList());
    classNames = new SimpleListProperty<>(FXCollections.observableArrayList());
    students = new SimpleListProperty<>(FXCollections.observableArrayList());
    checkTest = false;
    checkSession = false;
    tMainMenuViewModel.bindAccount(account);
    teacherName.setText(
        account.get().getFirstName() + " " + account.get().getLastName());
    tMainMenuViewModel.bindTests(tests);
    tMainMenuViewModel.bindSessions(sessions);
    tMainMenuViewModel.bindClasses(classNames);
    tMainMenuViewModel.bindStudents(students);
    studentsTable.setItems(students);
    classesTable.setItems(classNames);
    testsTable.setItems(tests);

    classNameColumn.setCellValueFactory(
        new PropertyValueFactory<>("className"));

    studentIDColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
    firstNameColumn.setCellValueFactory(
        new PropertyValueFactory<>("firstName"));
    lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

    testIDColumn.setCellValueFactory(
        new PropertyValueFactory<Test, Integer>("id"));
    testSubjectColumn.setCellValueFactory(
        new PropertyValueFactory<Test, String>("title"));

    sessionsTable.setItems(sessions);
    sessionIDColumn.setCellValueFactory(
        new PropertyValueFactory<Session, Integer>("id"));
    sessionStatusColumn.setCellValueFactory(
        new PropertyValueFactory<Session, Boolean>("status"));

    testsTable.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null)
          {
            tMainMenuViewModel.setTest(newSelection);
            editTest.setDisable(false);
            deleteTest.setDisable(false);
            createSession.setDisable(false);
            checkTest = true;
            // DO NOT DELETE
          }
          else
          {
            checkTest = false;
            editTest.setDisable(true);
            deleteTest.setDisable(true);
            createSession.setDisable(true);
          }
        });

    sessionsTable.getSelectionModel().selectedItemProperty()
        .addListener((obs, oldSelection, newSelection) -> {
          if (newSelection != null)
          {
            checkSession = true;
            tMainMenuViewModel.setSession(newSelection);
            editSession.setDisable(false);
            deleteSession.setDisable(false);
          }
          else
          {
            checkSession = false;
            editSession.setDisable(true);
            deleteSession.setDisable(true);
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
