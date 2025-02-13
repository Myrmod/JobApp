package myrmod.jobapp.Controller;

import myrmod.jobapp.Exception.ResourceNotFoundException;
import myrmod.jobapp.Model.Company;
import myrmod.jobapp.Model.Job;
import myrmod.jobapp.Model.Review;
import myrmod.jobapp.Service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/companies")
public class CompanyController {
	private final CompanyService companyService;

	@Autowired
	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}

	@GetMapping
	public Mono<ResponseEntity<Flux<Company>>> findAll() {
		return Mono.just(ResponseEntity.ok(companyService.findAll()));
	}

	@GetMapping("/{id}")
	public Mono<ResponseEntity<Company>> findById(@PathVariable Long id) {
		return companyService.findById(id)
			.map(ResponseEntity::ok)
			.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@PostMapping
	public Mono<ResponseEntity<Company>> create(@RequestBody Company company) {
		return companyService.save(company)
			.map(savedCompany -> ResponseEntity.status(HttpStatus.CREATED).body(savedCompany));
	}

	@PatchMapping("/{id}")
	public Mono<ResponseEntity<Company>> update(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
		return companyService.findById(id)
			.flatMap(existingCompany -> {
				updates.forEach((key, value) -> {
					switch (key) {
						case "title":
							existingCompany.setName((String) value);
							break;
						case "description":
							existingCompany.setDescription((String) value);
							break;
						case "jobs":
							if (value instanceof List<?>) {
								List<Job> jobList = ((List<?>) value).stream()
									.filter(Job.class::isInstance) // Ensure each item is a Job
									.map(Job.class::cast)         // Cast safely
									.toList();
								existingCompany.setJobs(jobList);
							}
							break;
						case "reviews":
							if (value instanceof List<?>) {
								List<Review> reviewList = ((List<?>) value).stream()
									.filter(Review.class::isInstance)
									.map(Review.class::cast)
									.toList();
								existingCompany.setReviews(reviewList);
							}
							break;
					}});

				return companyService.save(existingCompany);
			})
			.map(ResponseEntity::ok)
			.defaultIfEmpty(ResponseEntity.notFound().build());
	}

	@DeleteMapping("/{id}")
	public Mono<ResponseEntity<Object>> delete(@PathVariable Long id) {
		return companyService.findById(id)
			.flatMap(existingCompany -> companyService.delete(existingCompany.getId())
				.then(Mono.just(ResponseEntity.noContent().build())))
				.switchIfEmpty(Mono.error(new ResourceNotFoundException("Company with ID " + id + " not found")));
	}

	@PostMapping("/{companyId}/jobs")
	public Mono<ResponseEntity<Job>> addJob(@PathVariable Long companyId, @RequestBody Job job) {
		return companyService.addJob(companyId, job)
			.map(savedJob -> ResponseEntity.status(HttpStatus.CREATED).body(savedJob))
			.switchIfEmpty(Mono.error(new ResourceNotFoundException("Company with ID " + companyId + " not found")));
	}
}
