package myrmod.jobapp.Controller;

import myrmod.jobapp.Exception.ResourceNotFoundException;
import myrmod.jobapp.Model.Company;
import myrmod.jobapp.Model.Job;
import myrmod.jobapp.Service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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

	@PutMapping("/{id}")
	public Mono<ResponseEntity<Company>> update(@PathVariable Long id, @RequestBody Company company) {
		return companyService.findById(id)
			.flatMap(existingCompany -> {
				if (company.getName() != null) existingCompany.setName(company.getName());
				if (company.getDescription() != null) existingCompany.setDescription(company.getDescription());
				if (company.getJobs() != null) existingCompany.setJobs(company.getJobs());

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
