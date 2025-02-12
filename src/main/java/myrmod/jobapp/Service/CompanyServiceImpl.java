package myrmod.jobapp.Service;

import myrmod.jobapp.Model.Company;
import myrmod.jobapp.Model.Job;
import myrmod.jobapp.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
	private final CompanyRepository companyRepository;
	private final JobService jobService;

	@Autowired
	public CompanyServiceImpl(CompanyRepository companyRepository, JobService jobService) {
		this.companyRepository = companyRepository;
		this.jobService = jobService;
	}

	@Override
	public Mono<Company> findById(Long id) {
		return Mono.justOrEmpty(companyRepository.findById(id));
	}

	@Override
	public Flux<Company> findAll() {
		return Flux.fromIterable(companyRepository.findAll());
	}

	@Override
	public Mono<Company> save(Company company) {
		return Mono.just(companyRepository.save(company));
	}

	@Override
	public Mono<Job> addJob(Long id, Job job) {
		Optional<Company> company = companyRepository.findById(id);

		return Mono.justOrEmpty(companyRepository.findById(id))
				.flatMap(existingCompany -> {
					job.setCompany(existingCompany);
					return jobService.save(job);
				});
	}

	@Override
	public Mono<Void> delete(Long id) {
		return Mono.justOrEmpty(companyRepository.findById(id))
			.flatMap(existingCompany -> Mono.fromRunnable(() -> companyRepository.delete(existingCompany)))
			.then();
	}
}
