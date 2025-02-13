package myrmod.jobapp.Service;

import myrmod.jobapp.Model.Review;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface ReviewService {
	Mono<Review> findById(Long id);

	Flux<Review> findAll();

	Mono<Review> save(Review review);

	Mono<Void> delete(Long id);
}
