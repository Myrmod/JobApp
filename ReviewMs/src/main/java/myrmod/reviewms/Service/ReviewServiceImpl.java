package myrmod.reviewms.Service;

import myrmod.reviewms.Model.Review;
import myrmod.reviewms.Repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ReviewServiceImpl implements ReviewService {
	private final ReviewRepository reviewRepository;

	@Autowired
	public ReviewServiceImpl(ReviewRepository reviewRepository) {
		this.reviewRepository = reviewRepository;
	}

	@Override
	public Mono<Review> findById(Long id) {
		return Mono.justOrEmpty(reviewRepository.findById(id));
	}

	@Override
	public Flux<Review> findByCompanyId(Long companyId) {
		return Flux.fromIterable(reviewRepository.findByCompanyId(companyId));
	}

	@Override
	public Flux<Review> findAll() {
		return Flux.fromIterable(reviewRepository.findAll());
	}

	@Override
	public Mono<Review> save(Review review) {
		return Mono.just(reviewRepository.save(review));
	}

	@Override
	public Mono<Void> delete(Long id) {
		return Mono.justOrEmpty(reviewRepository.findById(id))
			.flatMap(existingReview -> Mono.fromRunnable(() -> reviewRepository.delete(existingReview)))
			.then();
	}
}
