package myrmod.reviewms.Service;

import myrmod.reviewms.Model.Review;
import myrmod.reviewms.Repository.ReviewRepository;
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
	private Review review2;

	@BeforeEach
	void setUp() {
		review = new Review();
		review.setId(1L);
		review.setTitle("Great Company");
		review.setRating(5);
		review.setCompanyId(1L);
		review2 = new Review();
		review2.setId(2L);
		review2.setTitle("Great Company");
		review2.setRating(5);
		review2.setCompanyId(2L);
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
	void findByCompanyId_ShouldReturnReviews() {
		when(reviewRepository.findByCompanyId(1L)).thenReturn(List.of(review));

		StepVerifier.create(reviewService.findByCompanyId(1L))
			.expectNext(review)
			.verifyComplete();

		verify(reviewRepository, times(1)).findByCompanyId(1L);
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
