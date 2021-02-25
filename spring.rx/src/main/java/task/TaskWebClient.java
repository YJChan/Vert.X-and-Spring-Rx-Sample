package task;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

public class TaskWebClient {
	private WebClient client = WebClient.create("http://localhost:8082");

	private Mono<ClientResponse> result = client.get()
			.uri("/api/todo")
			.accept(MediaType.APPLICATION_JSON)
			.exchange();

	public String getResult() {
		return ">> result = " + result.flatMap(res -> res.bodyToMono(String.class)).block();
	}
}
