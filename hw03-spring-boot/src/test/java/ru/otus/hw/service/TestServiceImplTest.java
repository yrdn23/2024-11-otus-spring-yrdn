package ru.otus.hw.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private LocalizedIOService ioService;
    @Mock
    private QuestionDao questionDao;

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
        when(ioService.readIntForRangeWithPromptLocalized(anyInt(), anyInt(), any(), any()))
                .thenReturn(2);

        var testResult = testService.executeTestFor(student);

        assertEquals(student, testResult.getStudent());
        assertEquals(questions, testResult.getAnsweredQuestions());
        assertEquals(1, testResult.getRightAnswersCount());

        verify(ioService, times(15)).printLine(anyString());
        verify(ioService, times(1)).printLineLocalized(anyString());
        verify(ioService, times(3)).readIntForRangeWithPromptLocalized(anyInt(), anyInt(), any(), any());
        verify(questionDao, times(1)).findAll();
    }
}