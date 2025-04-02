package ru.otus.hw.services;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Caterpillar;

@Slf4j
@Service
public class NatureServiceImpl implements NatureService {
	private static final String[] SPECIES = {
			"Monarch Butterfly", "Swallowtail Butterfly", "Painted Lady", "Morpho Butterfly",
			"Blue Butterfly", "Cabbage White", "Glasswing Butterfly", "Atlas Moth"};

	private final NatureGateway nature;

	public NatureServiceImpl(NatureGateway nature) {
		this.nature = nature;
	}

	@Override
	public void startGenerateCaterpillarLoop() {
		ForkJoinPool pool = ForkJoinPool.commonPool();
		for (int i = 0; i < 10; i++) {
			int num = i + 1;
			pool.execute(() -> {
				List<Caterpillar> items = generateCaterpillars();
				log.info("{}, New caterpillars: {}", num,
						items.stream().map(Caterpillar::name)
								.collect(Collectors.joining(",")));
				List<Butterfly> butterfly = nature.process(items);
				log.info("{}, Ready butterflies: {}", num, butterfly.stream()
						.map(Butterfly::name)
						.collect(Collectors.joining(",")));
			});
			delay();
		}
	}

	private static Caterpillar generateCaterpillar() {
		return new Caterpillar(SPECIES[RandomUtils.nextInt(0, SPECIES.length)]);
	}

	private static List<Caterpillar> generateCaterpillars() {
		List<Caterpillar> items = new ArrayList<>();
		for (int i = 0; i < RandomUtils.nextInt(1, 5); ++i) {
			items.add(generateCaterpillar());
		}
		return items;
	}

	private void delay() {
		try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

	}
}
