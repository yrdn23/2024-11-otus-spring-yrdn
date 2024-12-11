package ru.otus.hw.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    private TestService testService;

    @Mock
    private IOService ioService;
    @Mock
    private QuestionDao questionDao;

    @BeforeEach
    void setUp() {
        testService = new TestServiceImpl(ioService, questionDao);
    }

    @Test
    void executeTestFor_Successfully() {
        var student = new Student("firstName", "secondName");
        var questions = List.of(
                new Question("Q1", List.of(
                        new Answer("A11", false),
                        new Answer("A12", true)
                )),
                new Question("Q2", List.of(
                        new Answer("A21", true),
                        new Answer("A22", false)
                )),
                new Question("Q3", List.of(
                        new Answer("A31", false),
                        new Answer("A32", false),
                        new Answer("A33", true)
                ))
        );

        when(questionDao.findAll())
                .thenReturn(questions);
        when(ioService.readStringWithPrompt(any()))
                .thenReturn("A12");

        var testResult = testService.executeTestFor(student);

        assertEquals(student, testResult.getStudent());
        assertEquals(questions, testResult.getAnsweredQuestions());
        assertEquals(1, testResult.getRightAnswersCount());

        verify(ioService, times(14)).printLine(anyString());
        verify(ioService, times(1)).printFormattedLine(anyString());
        verify(ioService, times(3)).readStringWithPrompt(anyString());
        verify(questionDao, times(1)).findAll();
    }
}