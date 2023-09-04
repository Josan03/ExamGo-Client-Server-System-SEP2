package com.examgo.client.view;

import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import com.examgo.client.model.*;
import com.examgo.client.viewmodel.SMainMenuViewModel;
import com.examgo.client.viewmodel.ViewModel;

/**
 * A ViewController of a main window for student, where he can choose what to do next
 *
 * @author Michael Leo && Mihai Mihaila && Dan Turcan && Cristian Josan
 * @version 1.0
 */
public class SMainMenuViewController implements ViewController
{
  @FXML Label studentName;
  @FXML Label className;
  @FXML TextField sessionInput;
  @FXML Button connectButton;
  @FXML TableView<StudentSession> sessionTable;
  @FXML TableColumn<StudentSession, Integer> sessionIDColumn;
  @FXML TableColumn<StudentSession, String> testSubjectColumn;
  @FXML TableColumn<StudentSession, Integer> gradeColumn;
  private ViewHandler viewHandler;
  private SMainMenuViewModel sMainMenuViewModel;
  private Region root;

  private SimpleObjectProperty<StudentAccount> account;

  private SimpleListProperty<StudentSession> studentSessions;
  private String name = "s_m_m";

  /**
   * FXML method that is run when the "Connect" button is pressed.
   */
  @FXML public void connectButtonPressed()
  {
    String session_id = sessionInput.getText();
    if (!session_id.trim().isEmpty())
    {
      boolean result = sMainMenuViewModel.connectSession(
          Integer.parseInt(session_id));

      if (result)
      {
        AlertControl.showWarning("Connecting to the session",
            "Connecting to the session with ID: " + session_id,
            "Be ready, the test starts as soon as you press the button to continue");
        viewHandler.openView("s_t_w");

      }
      else
      {
        AlertControl.showError("Wrong session ID", null,
            "The session that you have tried to access is closed / does not exist, or you have already joined this session once");
      }
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
    this.sMainMenuViewModel = (SMainMenuViewModel) viewModel;
    this.root = root;
    account = new SimpleObjectProperty<>(null);
    studentSessions = new SimpleListProperty<>(
        FXCollections.observableArrayList());
    sMainMenuViewModel.bindSessions(studentSessions);
    sMainMenuViewModel.bindAccount(account);

    studentName.setText(
        account.get().getFirstName() + " " + account.get().getLastName());
    className.setText(account.get().getClassname());

    sessionTable.setItems(studentSessions);
    sessionIDColumn.setCellValueFactory(
        new PropertyValueFactory<StudentSession, Integer>("session_id"));
    testSubjectColumn.setCellValueFactory(
        new PropertyValueFactory<StudentSession, String>("subject"));
    gradeColumn.setCellValueFactory(
        new PropertyValueFactory<StudentSession, Integer>("grade"));

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
