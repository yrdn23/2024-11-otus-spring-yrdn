package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.TestRunnerService;

@ShellComponent(value = "Test Application Commands")
@RequiredArgsConstructor
public class TestCommands {

    private final TestRunnerService testRunnerService;

    @ShellMethod(value = "Start testing command", key = {"s", "start"})
    public void start() {
        testRunnerService.run();
    }

}
