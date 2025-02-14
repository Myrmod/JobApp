package myrmod.jobms.Service;

import myrmod.jobms.Model.Job;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface JobService {
	Mono<Job> findById(Long id);

	Flux<Job> findAll();

	Mono<Job> save(Job job);

	Mono<Void> delete(Long id);
}
