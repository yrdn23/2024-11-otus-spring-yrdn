package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;

import java.util.List;

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
    void executeTest_Successfully() {
        when(questionDao.findAll())
                .thenReturn(List.of());

        testService.executeTest();

        verify(ioService, times(1)).printLine(any());
        verify(ioService, times(1)).printFormattedLine(any());
        verify(questionDao, times(1)).findAll();
    }
  
}