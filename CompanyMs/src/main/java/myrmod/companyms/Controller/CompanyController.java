package myrmod.companyms.Controller;

import myrmod.companyms.Exception.ResourceNotFoundException;
import myrmod.companyms.Model.Company;
import myrmod.companyms.Service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

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
}
