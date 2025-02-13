package myrmod.jobapp.Controller;

import myrmod.jobapp.Exception.ResourceNotFoundException;
import myrmod.jobapp.Model.Review;
import myrmod.jobapp.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
	private final ReviewService reviewService;

	@Autowired
	public ReviewController(ReviewService reviewService) {
		this.reviewService = reviewService;
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Review>>> findAll() {
		return Mono.just(ResponseEntity.ok(reviewService.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Review>> findById(@PathVariable Long id) {
		return reviewService.findById(id)
			.map(ResponseEntity::ok)
			.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PatchMapping("/{id}")
	public Mono<ResponseEntity<Review>> update(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
		return reviewService.findById(id)
			.flatMap(existingReview -> {
				updates.forEach((key, value) -> {
					switch (key) {
						case "title":
							existingReview.setTitle((String) value);
						break;
						case "description":
							existingReview.setDescription((String) value);
						break;
						case "rating":
							existingReview.setRating((Integer) value);
						break;
					}
				});
				return reviewService.save(existingReview);
			})
			.map(ResponseEntity::ok)
			.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> delete(@PathVariable Long id) {
		return reviewService.findById(id)
			.flatMap(existingReview -> reviewService.delete(existingReview.getId())
				.then(Mono.just(ResponseEntity.noContent().build())))
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Review with ID " + id + " not found")));
	}
}
