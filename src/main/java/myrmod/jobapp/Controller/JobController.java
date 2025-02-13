package myrmod.jobapp.Controller;

import myrmod.jobapp.Exception.ResourceNotFoundException;
import myrmod.jobapp.Model.Job;
import myrmod.jobapp.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

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

	@PatchMapping("/{id}")
	public Mono<ResponseEntity<Job>> update(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
		return jobService.findById(id)
			.flatMap(existingJob -> {
				updates.forEach((key, value) -> {
					switch (key) {
						case "title":
							existingJob.setTitle((String) value);
							break;
						case "description":
							existingJob.setDescription((String) value);
							break;
						case "minSalary":
							existingJob.setMinSalary((String) value);
						case "maxSalary":
							existingJob.setMaxSalary((String) value);
						case "location":
							existingJob.setLocation((String) value);
							break;
					}});

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
