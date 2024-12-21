package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var questions = questionDao.findAll();
        var testResult = new TestResult(student);

        for (var question: questions) {
            ioService.printLine(question.text());
            for (var answer: question.answers()) {
                ioService.printLine("- %s".formatted(answer.text()));
            }
            var answerIndex = ioService.readIntForRangeWithPromptLocalized(1, question.answers().size(),
                    "TestService.answer.the.question", "TestService.answer.invalid.answer");
            var isAnswerValid = question.answers().get(answerIndex - 1).isCorrect();
            ioService.printLine("");
            testResult.applyAnswer(question, isAnswerValid);
        }
        return testResult;
    }

}
