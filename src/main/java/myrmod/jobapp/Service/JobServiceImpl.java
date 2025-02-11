package myrmod.jobapp.Service;

import myrmod.jobapp.Model.Job;
import myrmod.jobapp.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class JobServiceImpl implements JobService {
	private final JobRepository jobRepository;

	@Autowired
	public JobServiceImpl(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@Override
	public Mono<Job> findById(Long id) {
		return Mono.just(jobRepository.findById(id));
	}

	@Override
	public Flux<Job> findAll() {
		return Flux.fromIterable(jobRepository.findAll());
	}

	@Override
	public Mono<Job> save(Job job) {
		return Mono.just(jobRepository.save(job));
	}

	@Override
	public Mono<Void> delete(Long id) {
		return Mono.justOrEmpty(jobRepository.delete(id))
			.then();
	}
}
