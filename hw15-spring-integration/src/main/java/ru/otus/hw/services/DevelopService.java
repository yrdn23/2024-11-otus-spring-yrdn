package ru.otus.hw.services;

import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.domain.Pupa;

public interface DevelopService {
	Caterpillar grow(Caterpillar caterpillar);

	Pupa sleep(Pupa pupa);

	Butterfly fly(Butterfly butterfly);
}
