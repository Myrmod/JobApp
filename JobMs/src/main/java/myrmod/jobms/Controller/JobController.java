package myrmod.jobms.Controller;

import myrmod.jobms.Exception.ResourceNotFoundException;
import myrmod.jobms.Model.Job;
import myrmod.jobms.Service.JobService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@RestController
@RequestMapping("/jobs")
public class JobController {
	private final myrmod.jobms.Service.JobService jobService;

	@Autowired
	public JobController(JobService jobService) {
		this.jobService = jobService;
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Job>>> findAll(@RequestParam(required = false) Long companyId) {
		Flux<Job> jobs = (companyId != null)
			? jobService.findByCompanyId(companyId)
			: jobService.findAll();

		return Mono.just(ResponseEntity.ok(jobs));
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

	@PostMapping
	public Mono<ResponseEntity<Job>> create(@RequestBody Job review) {
		return jobService.save(review)
			.map(savedJob -> ResponseEntity.status(HttpStatus.CREATED).body(savedJob));
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> delete(@PathVariable Long id) {
		return jobService.findById(id)
			.flatMap(existingJob -> jobService.delete(existingJob.getId())
				.then(Mono.just(ResponseEntity.noContent().build())))
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Job with ID " + id + " not found")));
	}
}
