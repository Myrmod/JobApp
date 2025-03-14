package myrmod.reviewms.Controller;

import myrmod.reviewms.Exception.ResourceNotFoundException;
import myrmod.reviewms.Model.Review;
import myrmod.reviewms.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
	public Mono<ResponseEntity<Flux<Review>>> findAll(@RequestParam(required = false) Long companyId) {
		Flux<Review> reviews = (companyId != null)
			? reviewService.findByCompanyId(companyId)
			: reviewService.findAll();

		return Mono.just(ResponseEntity.ok(reviews));
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

	@PostMapping
	public Mono<ResponseEntity<Review>> create(@RequestBody Review review) {
		return reviewService.save(review)
			.map(savedReview -> ResponseEntity.status(HttpStatus.CREATED).body(savedReview));
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> delete(@PathVariable Long id) {
		return reviewService.findById(id)
			.flatMap(existingReview -> reviewService.delete(existingReview.getId())
				.then(Mono.just(ResponseEntity.noContent().build())))
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Review with ID " + id + " not found")));
	}
}
