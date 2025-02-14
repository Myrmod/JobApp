package myrmod.jobms.Service;

import myrmod.jobms.Model.Job;
import myrmod.jobms.Repository.JobRepository;
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
class JobServiceImplTest {

	@Mock
	private JobRepository jobRepository;

	@InjectMocks
	private JobServiceImpl jobService;

	private Job job;

	@BeforeEach
	void setUp() {
		job = new Job();
		job.setId(1L);
		job.setTitle("Software Engineer");
	}

	@Test
	void findById_ShouldReturnJob_WhenExists() {
		when(jobRepository.findById(1L)).thenReturn(Optional.of(job));

		StepVerifier.create(jobService.findById(1L))
			.expectNext(job)
			.verifyComplete();

		verify(jobRepository, times(1)).findById(1L);
	}

	@Test
	void findById_ShouldReturnEmpty_WhenNotFound() {
		when(jobRepository.findById(2L)).thenReturn(Optional.empty());

		StepVerifier.create(jobService.findById(2L))
			.verifyComplete();

		verify(jobRepository, times(1)).findById(2L);
	}

	@Test
	void findAll_ShouldReturnJobs() {
		when(jobRepository.findAll()).thenReturn(List.of(job));

		StepVerifier.create(jobService.findAll())
			.expectNext(job)
			.verifyComplete();

		verify(jobRepository, times(1)).findAll();
	}

	@Test
	void save_ShouldReturnSavedJob() {
		when(jobRepository.save(job)).thenReturn(job);

		StepVerifier.create(jobService.save(job))
			.expectNext(job)
			.verifyComplete();

		verify(jobRepository, times(1)).save(job);
	}

	@Test
	void delete_ShouldComplete_WhenJobExists() {
		when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
		doNothing().when(jobRepository).delete(job);

		StepVerifier.create(jobService.delete(1L))
			.verifyComplete();

		verify(jobRepository, times(1)).delete(job);
	}
}
