package myrmod.jobapp.Service;

import myrmod.jobapp.Model.Company;
import myrmod.jobapp.Model.Job;
import myrmod.jobapp.Repository.CompanyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CompanyServiceImplTest {

	@Mock
	private CompanyRepository companyRepository;

	@Mock
	private JobService jobService;

	@InjectMocks
	private CompanyServiceImpl companyService;

	private Company company;

	@BeforeEach
	void setUp() {
		company = new Company();
		company.setId(1L);
		company.setName("Test Company");
	}

	@Test
	void findById_ShouldReturnCompany_WhenExists() {
		when(companyRepository.findById(1L)).thenReturn(Optional.of(company));

		StepVerifier.create(companyService.findById(1L))
			.expectNext(company)
			.verifyComplete();

		verify(companyRepository, times(1)).findById(1L);
	}

	@Test
	void findById_ShouldReturnEmpty_WhenNotFound() {
		when(companyRepository.findById(2L)).thenReturn(Optional.empty());

		StepVerifier.create(companyService.findById(2L))
			.verifyComplete();

		verify(companyRepository, times(1)).findById(2L);
	}

	@Test
	void findAll_ShouldReturnCompanies() {
		when(companyRepository.findAll()).thenReturn(List.of(company));

		StepVerifier.create(companyService.findAll())
			.expectNext(company)
			.verifyComplete();

		verify(companyRepository, times(1)).findAll();
	}

	@Test
	void save_ShouldReturnSavedCompany() {
		when(companyRepository.save(company)).thenReturn(company);

		StepVerifier.create(companyService.save(company))
			.expectNext(company)
			.verifyComplete();

		verify(companyRepository, times(1)).save(company);
	}

	@Test
	void delete_ShouldComplete_WhenCompanyExists() {
		when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
		doNothing().when(companyRepository).delete(company);

		StepVerifier.create(companyService.delete(1L))
			.verifyComplete();

		verify(companyRepository, times(1)).delete(company);
	}

	@Test
	void addJob_ShouldReturnSavedJob_WhenCompanyExists() {
		Job job = new Job();
		job.setId(1L);
		job.setTitle("Software Engineer");

		when(companyRepository.findById(1L)).thenReturn(Optional.of(company));
		when(jobService.save(any(Job.class))).thenReturn(Mono.just(job));

		StepVerifier.create(companyService.addJob(1L, job))
			.expectNext(job)
			.verifyComplete();

		verify(jobService, times(1)).save(any(Job.class));
	}
}
