package myrmod.jobapp.Service;

import myrmod.jobapp.Model.Company;
import myrmod.jobapp.Model.Job;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface CompanyService {
	Mono<Company> findById(Long id);

	Flux<Company> findAll();

	Mono<Company> save(Company company);

	Mono<Job> addJob(Long id, Job job);

	Mono<Void> delete(Long id);
}
