package com.examgo.client.viewmodel;

import com.examgo.client.model.ModelManager;
import com.examgo.client.model.Question;
import javafx.beans.property.SimpleObjectProperty;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;


public class TEditTestViewModelTest
{
  private ModelManager model;
  private TEditTestViewModel viewModel;
  private SimpleObjectProperty<com.examgo.client.model.Test> test;

  @BeforeEach
  void setUp()
  {
    model = Mockito.mock(ModelManager.class);
    viewModel = new TEditTestViewModel(model);
    test = new SimpleObjectProperty<>();
    viewModel.bindTest(test);
  }

  @Test
  void editingTheTest()
  {
    int index = 1;
    double time = 15.0;
    String question = "What is your name?";
    int points = 5;
    String title = "English";

    viewModel.editTest(index, time, question, points, title);
    Mockito.verify(model).editTest(index, time, question, points, title);
  }

  @Test
  void addingSingleQuestion()
  {
    double time = 30.0;
    String question = "What is the capital of Moldova?";
    int points = 5;

    viewModel.addSingleQuestion(time, question, points);
    Mockito.verify(model).addQuestionSingle(time, question, points);
  }

  @Test
  void removingQuestion()
  {
    Question question = new Question(1, 30, "What is the capital of Romania?", 5);
    viewModel.removeQuestion(question);
    Mockito.verify(model).removeQuestion(question);
  }
}
