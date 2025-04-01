package ru.otus.hw.services;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.hw.domain.Butterfly;
import ru.otus.hw.domain.Caterpillar;

import java.util.List;

@MessagingGateway
public interface NatureGateway {

	@Gateway(requestChannel = "caterpillarChannel", replyChannel = "butterflyChannel")
	List<Butterfly> process(List<Caterpillar> caterpillar);
}
