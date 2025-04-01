package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.MessageChannelSpec;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.scheduling.PollerMetadata;

import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Caterpillar;
import ru.otus.hw.domain.Pupa;
import ru.otus.hw.services.DevelopService;

@Configuration
public class IntegrationConfig {

	@Bean
	public MessageChannelSpec<?, ?> caterpillarChannel() {
		return MessageChannels.queue(10);
	}

	@Bean
	public MessageChannelSpec<?, ?> butterflyChannel() {
		return MessageChannels.publishSubscribe();
	}

	@Bean(name = PollerMetadata.DEFAULT_POLLER)
	public PollerSpec poller() {
		return Pollers.fixedRate(100).maxMessagesPerPoll(2);
	}

	@Bean
	public IntegrationFlow natureFlow(DevelopService developService) {
		return IntegrationFlow
				.from(caterpillarChannel())
				.split()
				.handle(developService, "grow")
				.<Caterpillar, Pupa>transform(f -> new Pupa(f.name().toLowerCase()))
				.handle(developService, "sleep")
				.<Pupa, Butterfly>transform(f -> new Butterfly(f.name().toUpperCase()))
				.handle(developService, "fly")
				.aggregate()
				.channel(butterflyChannel())
				.get();
	}
}
