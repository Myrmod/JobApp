package myrmod.jobapp.Controller;

import myrmod.jobapp.Exception.ResourceNotFoundException;
import myrmod.jobapp.Model.Job;
import myrmod.jobapp.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/jobs")
public class JobController {
	private final myrmod.jobapp.Service.JobService jobService;

	@Autowired
	public JobController(JobService jobService) {
		this.jobService = jobService;
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Job>>> findAll() {
		return Mono.just(ResponseEntity.ok(jobService.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Job>> findById(@PathVariable Long id) {
		return jobService.findById(id)
			.map(ResponseEntity::ok)
			.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<Job>> create(@RequestBody Job job) {
		return jobService.save(job)
			.map(savedJob -> ResponseEntity.status(HttpStatus.CREATED).body(savedJob));
	}

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Job>> update(@PathVariable Long id, @RequestBody Job job) {
		return jobService.findById(id)
			.flatMap(existingJob -> {
				if (job.getTitle() != null) existingJob.setTitle(job.getTitle());
				if (job.getDescription() != null) existingJob.setDescription(job.getDescription());
				if (job.getMinSalary() != null) existingJob.setMinSalary(job.getMinSalary());
				if (job.getMaxSalary() != null) existingJob.setMaxSalary(job.getMaxSalary());
				if (job.getLocation() != null) existingJob.setLocation(job.getLocation());

				return jobService.save(existingJob);
			})
			.map(ResponseEntity::ok)
			.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> delete(@PathVariable Long id) {
		return jobService.findById(id)
			.flatMap(existingJob -> jobService.delete(existingJob.getId())
				.then(Mono.just(ResponseEntity.noContent().build())))
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Job with ID " + id + " not found")));
	}
}
