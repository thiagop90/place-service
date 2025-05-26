package com.psthiago.place_service;

import com.psthiago.place_service.api.PlaceRequest;
import com.psthiago.place_service.domain.Place;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Import(TestConfig.class)
public class PlaceServiceApplicationTests {
	public static final Place AVENIDA_PAULISTA = new Place(
			1L, "Avenida Paulista", "avenida-paulista", "São Paulo", "SP", null, null);

	@Autowired
	WebTestClient webTestClient;

	@Test
	public void testCreatePlaceSuccess() {
		final String name = "Valid name";
		final String city = "Valid city";
		final String state = "Valid state";
		final String slug = "valid-name";

		webTestClient
				.post()
				.uri("/places")
				.bodyValue(new PlaceRequest(name, city, state))
				.exchange()
				.expectStatus().isCreated()
				.expectBody()
				.jsonPath("name").isEqualTo(name)
				.jsonPath("city").isEqualTo(city)
				.jsonPath("state").isEqualTo(state)
				.jsonPath("slug").isEqualTo(slug)
				.jsonPath("createdAt").isNotEmpty()
				.jsonPath("updatedAt").isNotEmpty();
	}

	@Test
	public void testCreatePlaceFailure() {
		final String name = "";
		final String state = "";
		final String city = "";

		webTestClient
				.post()
				.uri("/places")
				.bodyValue(new PlaceRequest(name, city, state))
				.exchange()
				.expectStatus().isBadRequest();
	}

	@Test
	public void testEditPlaceSuccess() {
		final String newName = "New Name";
		final String newCity = "New City";
		final String newState = "New State";
		final String newSlug = "new-name";

		// Updates name, city and state.
		webTestClient
				.patch()
				.uri("/places/1")
				.bodyValue(
						new PlaceRequest(newName, newCity, newState))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("name").isEqualTo(newName)
				.jsonPath("city").isEqualTo(newCity)
				.jsonPath("state").isEqualTo(newState)
				.jsonPath("slug").isEqualTo(newSlug)
				.jsonPath("createdAt").isNotEmpty()
				.jsonPath("updatedAt").isNotEmpty();

		// Updates only name
		webTestClient
				.patch()
				.uri("/places/1")
				.bodyValue(
						new PlaceRequest(AVENIDA_PAULISTA.name(), null, null))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("name").isEqualTo(AVENIDA_PAULISTA.name())
				.jsonPath("city").isEqualTo(newCity)
				.jsonPath("state").isEqualTo(newState)
				.jsonPath("slug").isEqualTo(AVENIDA_PAULISTA.slug())
				.jsonPath("createdAt").isNotEmpty()
				.jsonPath("updatedAt").isNotEmpty();

		// Updates only city
		webTestClient
				.patch()
				.uri("/places/1")
				.bodyValue(
						new PlaceRequest(null, AVENIDA_PAULISTA.city(), null))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("name").isEqualTo(AVENIDA_PAULISTA.name())
				.jsonPath("city").isEqualTo(AVENIDA_PAULISTA.city())
				.jsonPath("state").isEqualTo(newState)
				.jsonPath("slug").isEqualTo(AVENIDA_PAULISTA.slug())
				.jsonPath("createdAt").isNotEmpty()
				.jsonPath("updatedAt").isNotEmpty();

		// Updates only state
		webTestClient
				.patch()
				.uri("/places/1")
				.bodyValue(
						new PlaceRequest(null, null, AVENIDA_PAULISTA.state()))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("name").isEqualTo(AVENIDA_PAULISTA.name())
				.jsonPath("city").isEqualTo(AVENIDA_PAULISTA.city())
				.jsonPath("state").isEqualTo(AVENIDA_PAULISTA.state())
				.jsonPath("slug").isEqualTo(AVENIDA_PAULISTA.slug())
				.jsonPath("createdAt").isNotEmpty()
				.jsonPath("updatedAt").isNotEmpty();
	}

	@Test
	public void testGetByIdSuccess() {
		webTestClient
				.get()
				.uri("/places/1")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("name").isEqualTo(AVENIDA_PAULISTA.name())
				.jsonPath("city").isEqualTo(AVENIDA_PAULISTA.city())
				.jsonPath("state").isEqualTo(AVENIDA_PAULISTA.state())
				.jsonPath("slug").isEqualTo(AVENIDA_PAULISTA.slug())
				.jsonPath("createdAt").isNotEmpty()
				.jsonPath("updatedAt").isNotEmpty();
	}

	@Test
	public void testGetByIdFailure() {
		webTestClient
				.get()
				.uri("/places/999")
				.exchange()
				.expectStatus().isNotFound();
	}

	@Test
	public void testGetByNameSuccess() {
		webTestClient
				.get()
				.uri("/places?name=%s".formatted(AVENIDA_PAULISTA.name()))
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$.length()").isEqualTo(1)
				.jsonPath("$[0].name").isEqualTo(AVENIDA_PAULISTA.name())
				.jsonPath("$[0].slug").isEqualTo(AVENIDA_PAULISTA.slug())
				.jsonPath("$[0].city").isEqualTo(AVENIDA_PAULISTA.city())
				.jsonPath("$[0].state").isEqualTo(AVENIDA_PAULISTA.state())
				.jsonPath("$[0].createdAt").isNotEmpty()
				.jsonPath("$[0].updatedAt").isNotEmpty();
	}

	@Test
	public void testGetByNameNotFound() {
		webTestClient
				.get()
				.uri("/places?name=name")
				.exchange()
				.expectStatus().isOk()
				.expectBody()
				.jsonPath("$").isArray()
				.jsonPath("$.length()").isEqualTo(0);
	}

}
