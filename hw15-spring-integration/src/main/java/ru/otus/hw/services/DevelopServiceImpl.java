package ru.otus.hw.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.domain.Pupa;

@Slf4j
@Service
public class DevelopServiceImpl implements DevelopService {

	@Override
	public Caterpillar grow(Caterpillar caterpillar) {
		log.info("Caterpillar {} grow", caterpillar.name());
		return caterpillar;
	}

	@Override
	public Pupa sleep(Pupa pupa) {
		log.info("Pupa {} sleep", pupa.name());
		return pupa;
	}

	@Override
	public Butterfly fly(Butterfly butterfly) {
		log.info("Butterfly {} fly", butterfly.name());
		return butterfly;
	}
}
