package myrmod.jobapp.Controller;

import myrmod.jobapp.Model.Job;
import myrmod.jobapp.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/jobs")
public class JobController {
	private final JobRepository jobRepository;

	@Autowired
	public JobController(JobRepository jobRepository) {
		this.jobRepository = jobRepository;
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Job>>> findAll() {
		return Mono.just(ResponseEntity.ok(this.jobRepository.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Job>> findById(@PathVariable Long id) {
		return this.jobRepository.findById(id)
			.map(ResponseEntity::ok)
			.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<Job>> create(@RequestBody Job job) {
		return this.jobRepository.save(job)
			.map(savedJob -> ResponseEntity.status(HttpStatus.CREATED).body(savedJob));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Job>> update(@PathVariable Long id, @RequestBody Job job) {
		return this.jobRepository.findById(id)
			.flatMap(existingJob -> {
				if (job.getTitle() != null) existingJob.setTitle(job.getTitle());
				if (job.getDescription() != null) existingJob.setDescription(job.getDescription());
				if (job.getMinSalary() != null) existingJob.setMinSalary(job.getMinSalary());
				if (job.getMaxSalary() != null) existingJob.setMaxSalary(job.getMaxSalary());
				if (job.getLocation() != null) existingJob.setLocation(job.getLocation());

				return jobRepository.save(existingJob);
			})
			.map(ResponseEntity::ok)
			.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> delete(@PathVariable Long id) {
		return this.jobRepository.findById(id)
			.flatMap(existingJob -> jobRepository.delete(existingJob.getId())
				.then(Mono.just(ResponseEntity.noContent().build())))
			.defaultIfEmpty(ResponseEntity.notFound().build());
	}
}
