package ru.otus.hw.indicators;

import lombok.AllArgsConstructor;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;
import ru.otus.hw.services.BookService;

@Component
@AllArgsConstructor
public class LibraryHealthIndicator implements HealthIndicator {

    private final BookService bookService;

    @Override
    public Health health() {
        long bookCount = bookService.findAll().size();
        if (bookCount > 0) {
            return Health.up().build();
        }
        return Health.down().build();
    }
}
