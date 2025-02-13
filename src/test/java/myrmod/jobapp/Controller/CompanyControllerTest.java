package myrmod.jobapp.Controller;

import myrmod.jobapp.Model.Company;
import myrmod.jobapp.Model.Job;
import myrmod.jobapp.Service.CompanyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@WebFluxTest(CompanyController.class)
class CompanyControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private CompanyService companyService;

	@Test
	void testFindAll() {
		Company company1 = new Company(1L, "Company 1", "Description 1");
		Company company2 = new Company(2L, "Company 2", "Description 2");

		when(companyService.findAll()).thenReturn(Flux.just(company1, company2));

		List<Company> responseBody = webTestClient.get().uri("/companies")
			.exchange()
			.expectStatus().isOk()
			.returnResult(Company.class)
			.getResponseBody()
			.collectList()
			.block();

		assertNotNull(responseBody);
		assertEquals(2, responseBody.size());
		assertEquals(company1.getId(), responseBody.get(0).getId());
		assertEquals(company1.getName(), responseBody.get(0).getName());
		assertEquals(company2.getId(), responseBody.get(1).getId());
	}

	@Test
	void testFindById_Found() {
		Company company = new Company(1L, "Company 1", "Description 1");

		when(companyService.findById(1L)).thenReturn(Mono.just(company));

		Company responseBody = webTestClient.get().uri("/companies/1")
			.exchange()
			.expectStatus().isOk()
			.returnResult(Company.class)
			.getResponseBody()
			.blockFirst();

		assertNotNull(responseBody);
		assertEquals(company.getId(), responseBody.getId());
		assertEquals(company.getName(), responseBody.getName());
		assertEquals(company.getDescription(), responseBody.getDescription());
	}

	@Test
	void testFindById_NotFound() {
		when(companyService.findById(1L)).thenReturn(Mono.empty());

		webTestClient.get().uri("/companies/1")
			.exchange()
			.expectStatus().isNotFound();
	}

	@Test
	void testCreateCompany() {
		Company companyRequest = new Company(null, "Company 11", "Description 11"); // No ID
		Company savedCompany = new Company(11L, "Company 11", "Description 11"); // With ID

		when(companyService.save(any(Company.class))).thenReturn(Mono.just(savedCompany));

		Company responseBody = webTestClient.post().uri("/companies")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(companyRequest)
			.exchange()
			.expectStatus().isCreated()
			.returnResult(Company.class)
			.getResponseBody()
			.blockFirst();

		assertNotNull(responseBody);
		assertEquals(savedCompany.getId(), responseBody.getId());
		assertEquals(savedCompany.getName(), responseBody.getName());
		assertEquals(savedCompany.getDescription(), responseBody.getDescription());
		assertEquals(savedCompany.getJobs(), responseBody.getJobs());
	}

	@Test
	void testUpdateCompany_Found() {
		Company existingCompany = new Company(1L, "Old Company", "Old Description");
		Company updatedCompany = new Company(1L, "Updated Company", "Updated Description");

		when(companyService.findById(1L)).thenReturn(Mono.just(existingCompany));
		when(companyService.save(any(Company.class))).thenReturn(Mono.just(updatedCompany));

		Company responseBody = webTestClient.patch().uri("/companies/1")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(updatedCompany)
			.exchange()
			.expectStatus().isOk()
			.returnResult(Company.class)
			.getResponseBody()
			.blockFirst();

		assertNotNull(responseBody);
		assertEquals(updatedCompany.getId(), responseBody.getId());
		assertEquals(updatedCompany.getName(), responseBody.getName());
		assertEquals(updatedCompany.getDescription(), responseBody.getDescription());
		assertEquals(updatedCompany.getJobs(), responseBody.getJobs());
	}

	@Test
	void testUpdateCompany_NotFound() {
		Company updatedCompany = new Company(1L, "Updated Company", "Updated Description");

		when(companyService.findById(1L)).thenReturn(Mono.empty());

		webTestClient.patch().uri("/companies/1")
			.bodyValue(updatedCompany)
			.exchange()
			.expectStatus().isNotFound();
	}

	@Test
	void testDeleteCompany_Found() {
		Company existingCompany = new Company(1L, "Company to Delete", "Description");

		when(companyService.findById(1L)).thenReturn(Mono.just(existingCompany));
		when(companyService.delete(1L)).thenReturn(Mono.empty());

		webTestClient.delete().uri("/companies/1")
			.exchange()
			.expectStatus().isNoContent();
	}

	@Test
	void testDeleteCompany_NotFound() {
		when(companyService.findById(1L)).thenReturn(Mono.empty());

		webTestClient.delete().uri("/companies/1")
			.exchange()
			.expectStatus().isNotFound();
	}

	@Test
	void testAddJob_Success() {
		Long companyId = 1L;
		Job newJob = new Job(null, "Software Engineer", "Develop software", "50000€", "70000€", "Germany");
		Job savedJob = new Job(1L, "Software Engineer", "Develop software", "50000€", "70000€", "Germany");

		when(companyService.addJob(eq(companyId), any(Job.class)))
			.thenReturn(Mono.just(savedJob));

		Job responseBody = webTestClient.post().uri("/companies/" + companyId + "/jobs")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(newJob)
			.exchange()
			.expectStatus().isCreated()
			.returnResult(Job.class)
			.getResponseBody()
			.blockFirst();

			assertNotNull(responseBody);
			assertEquals(savedJob.getId(), responseBody.getId());
			assertEquals(savedJob.getTitle(), responseBody.getTitle());
			assertEquals(savedJob.getDescription(), responseBody.getDescription());
			assertEquals(savedJob.getMinSalary(), responseBody.getMinSalary());
			assertEquals(savedJob.getMaxSalary(), responseBody.getMaxSalary());
			assertEquals(savedJob.getLocation(), responseBody.getLocation());
	}

	@Test
	void testAddJob_NotFound() {
		Long companyId = 2L;
		Job newJob = new Job(null, "Software Engineer", "Develop software", "50000€", "70000€", "Germany");

		when(companyService.addJob(eq(companyId), any(Job.class)))
			.thenReturn(Mono.empty());

		webTestClient.post().uri("/companies/" + companyId + "/jobs")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(newJob)
			.exchange()
			.expectStatus().isNotFound();
	}
}

