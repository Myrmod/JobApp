package myrmod.jobapp.Service;

import myrmod.jobapp.Model.Review;
import myrmod.jobapp.Repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.test.StepVerifier;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReviewServiceImplTest {

	@Mock
	private ReviewRepository reviewRepository;

	@InjectMocks
	private ReviewServiceImpl reviewService;

	private Review review;

	@BeforeEach
	void setUp() {
		review = new Review();
		review.setId(1L);
		review.setTitle("Great Company");
		review.setRating(5);
	}

	@Test
	void findById_ShouldReturnReview_WhenExists() {
		when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));

		StepVerifier.create(reviewService.findById(1L))
			.expectNext(review)
			.verifyComplete();

		verify(reviewRepository, times(1)).findById(1L);
	}

	@Test
	void findById_ShouldReturnEmpty_WhenNotFound() {
		when(reviewRepository.findById(2L)).thenReturn(Optional.empty());

		StepVerifier.create(reviewService.findById(2L))
			.verifyComplete();

		verify(reviewRepository, times(1)).findById(2L);
	}

	@Test
	void findAll_ShouldReturnReviews() {
		when(reviewRepository.findAll()).thenReturn(List.of(review));

		StepVerifier.create(reviewService.findAll())
			.expectNext(review)
			.verifyComplete();

		verify(reviewRepository, times(1)).findAll();
	}

	@Test
	void save_ShouldReturnSavedReview() {
		when(reviewRepository.save(review)).thenReturn(review);

		StepVerifier.create(reviewService.save(review))
			.expectNext(review)
			.verifyComplete();

		verify(reviewRepository, times(1)).save(review);
	}

	@Test
	void delete_ShouldComplete_WhenReviewExists() {
		when(reviewRepository.findById(1L)).thenReturn(Optional.of(review));
		doNothing().when(reviewRepository).delete(review);

		StepVerifier.create(reviewService.delete(1L))
			.verifyComplete();

		verify(reviewRepository, times(1)).delete(review);
	}
}
