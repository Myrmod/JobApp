package myrmod.reviewms.Controller;

import myrmod.reviewms.Model.Review;
import myrmod.reviewms.Service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebFluxTest(ReviewController.class)
class ReviewControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private ReviewService reviewService;

	@Test
	void testFindAll() {
		Review review1 = new Review(1L, "Review 1", "Description 1", 3, 1L);
		Review review2 = new Review(2L, "Review 2", "Description 2", 2, 1L);

		when(reviewService.findAll()).thenReturn(Flux.just(review1, review2));

		List<Review> responseBody = webTestClient.get().uri("/reviews")
			.exchange()
			.expectStatus().isOk()
			.returnResult(Review.class)
			.getResponseBody()
			.collectList()
			.block();

		assertNotNull(responseBody);
		assertEquals(2, responseBody.size());
		assertEquals(review1.getId(), responseBody.get(0).getId());
		assertEquals(review1.getTitle(), responseBody.get(0).getTitle());
		assertEquals(review2.getId(), responseBody.get(1).getId());
	}

	@Test
	void testFindById_Found() {
		Review review = new Review(1L, "Review 1", "Description 1", 5, 1L);

		when(reviewService.findById(1L)).thenReturn(Mono.just(review));

		Review responseBody = webTestClient.get().uri("/reviews/1")
			.exchange()
			.expectStatus().isOk()
			.returnResult(Review.class)
			.getResponseBody()
			.blockFirst();

		assertNotNull(responseBody);
		assertEquals(review.getId(), responseBody.getId());
		assertEquals(review.getTitle(), responseBody.getTitle());
		assertEquals(review.getDescription(), responseBody.getDescription());
	}

	@Test
	void testFindById_NotFound() {
		when(reviewService.findById(1L)).thenReturn(Mono.empty());

		webTestClient.get().uri("/reviews/1")
			.exchange()
			.expectStatus().isNotFound();
	}

	@Test
	void testCreateReview() {
		Review companyRequest = new Review(null, "Review 11", "Description 11", 4, 1L); // No ID
		Review savedReview = new Review(11L, "Review 11", "Description 11", 4, 1L); // With ID

		when(reviewService.save(any(Review.class))).thenReturn(Mono.just(savedReview));

		Review responseBody = webTestClient.post().uri("/reviews")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(companyRequest)
			.exchange()
			.expectStatus().isCreated()
			.returnResult(Review.class)
			.getResponseBody()
			.blockFirst();

		assertNotNull(responseBody);
		assertEquals(savedReview.getId(), responseBody.getId());
		assertEquals(savedReview.getTitle(), responseBody.getTitle());
		assertEquals(savedReview.getDescription(), responseBody.getDescription());
		assertEquals(savedReview.getRating(), responseBody.getRating());
	}

	@Test
	void testUpdateReview_Found() {
		Review existingReview = new Review(1L, "Old Review", "Old Description", 3, 1L);
		Review updatedReview = new Review(1L, "Updated Review", "Updated Description", 5, 1L);

		when(reviewService.findById(1L)).thenReturn(Mono.just(existingReview));
		when(reviewService.save(any(Review.class))).thenReturn(Mono.just(updatedReview));

		Review responseBody = webTestClient.patch().uri("/reviews/1")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(updatedReview)
			.exchange()
			.expectStatus().isOk()
			.returnResult(Review.class)
			.getResponseBody()
			.blockFirst();

		assertNotNull(responseBody);
		assertEquals(updatedReview.getId(), responseBody.getId());
		assertEquals(updatedReview.getTitle(), responseBody.getTitle());
		assertEquals(updatedReview.getDescription(), responseBody.getDescription());
		assertEquals(updatedReview.getRating(), responseBody.getRating());
	}

	@Test
	void testUpdateReview_NotFound() {
		Review updatedReview = new Review(1L, "Updated Review", "Updated Description", 5, 1L);

		when(reviewService.findById(1L)).thenReturn(Mono.empty());

		webTestClient.patch().uri("/reviews/1")
			.bodyValue(updatedReview)
			.exchange()
			.expectStatus().isNotFound();
	}

	@Test
	void testDeleteReview_Found() {
		Review existingReview = new Review(1L, "Review to Delete", "Description", 3, 1L);

		when(reviewService.findById(1L)).thenReturn(Mono.just(existingReview));
		when(reviewService.delete(1L)).thenReturn(Mono.empty());

		webTestClient.delete().uri("/reviews/1")
			.exchange()
			.expectStatus().isNoContent();
	}

	@Test
	void testDeleteReview_NotFound() {
		when(reviewService.findById(1L)).thenReturn(Mono.empty());

		webTestClient.delete().uri("/reviews/1")
			.exchange()
			.expectStatus().isNotFound();
	}
}

