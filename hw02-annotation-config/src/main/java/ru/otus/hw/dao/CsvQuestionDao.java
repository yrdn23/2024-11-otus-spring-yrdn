package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBeanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Objects;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<Question> questions;

        try (InputStream stream = getClass().getClassLoader().getResourceAsStream(fileNameProvider.getTestFileName())) {
            if (Objects.isNull(stream)) {
                throw new QuestionReadException("File %s not found".formatted(fileNameProvider.getTestFileName()));
            }
            try (var reader = new BufferedReader(new InputStreamReader(stream))) {
                var csvToBean = new CsvToBeanBuilder<QuestionDto>(reader)
                        .withSkipLines(1)
                        .withType(QuestionDto.class)
                        .withSeparator(';')
                        .build();

                questions = csvToBean.stream()
                        .map(QuestionDto::toDomainObject)
                        .toList();
            }
        } catch (IOException e) {
            throw new QuestionReadException("Can't read file %s".formatted(fileNameProvider.getTestFileName()), e);
        }
        return questions;
    }
}
