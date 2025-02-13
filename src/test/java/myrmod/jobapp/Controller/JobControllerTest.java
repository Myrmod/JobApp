package myrmod.jobapp.Controller;

import myrmod.jobapp.Model.Job;
import myrmod.jobapp.Service.JobService;
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
import static org.mockito.Mockito.when;

@WebFluxTest(JobController.class)
class JobControllerTest {

	@Autowired
	private WebTestClient webTestClient;

	@MockitoBean
	private JobService jobService;

	@Test
	void testFindAll() {
		Job job1 = new Job(1L, "Job 1", "Description 1", "40000€", "50000€", "Germany");
		Job job2 = new Job(2L, "Job 2", "Description 2", "40000€", "50000€", "Germany");

		when(jobService.findAll()).thenReturn(Flux.just(job1, job2));

		List<Job> responseBody = webTestClient.get().uri("/jobs")
			.exchange()
			.expectStatus().isOk()
			.returnResult(Job.class)
			.getResponseBody()
			.collectList()
			.block();

		assertNotNull(responseBody);
		assertEquals(2, responseBody.size());
		assertEquals(job1.getId(), responseBody.get(0).getId());
		assertEquals(job1.getTitle(), responseBody.get(0).getTitle());
		assertEquals(job2.getId(), responseBody.get(1).getId());
	}

	@Test
	void testFindById_Found() {
		Job job = new Job(1L, "Job 1", "Description 1", "40000€", "50000€", "Germany");

		when(jobService.findById(1L)).thenReturn(Mono.just(job));

		Job responseBody = webTestClient.get().uri("/jobs/1")
			.exchange()
			.expectStatus().isOk()
			.returnResult(Job.class)
			.getResponseBody()
			.blockFirst();

		assertNotNull(responseBody);
		assertEquals(job.getId(), responseBody.getId());
		assertEquals(job.getTitle(), responseBody.getTitle());
		assertEquals(job.getDescription(), responseBody.getDescription());
	}

	@Test
	void testFindById_NotFound() {
		when(jobService.findById(1L)).thenReturn(Mono.empty());

		webTestClient.get().uri("/jobs/1")
			.exchange()
			.expectStatus().isNotFound();
	}

	@Test
	void testUpdateJob_Found() {
		Job existingJob = new Job(1L, "Old Job", "Old Description", "40000€", "50000€", "Germany");
		Job updatedJob = new Job(1L, "Updated Job", "Updated Description", "50000€", "60000€", "Germany");

		when(jobService.findById(1L)).thenReturn(Mono.just(existingJob));
		when(jobService.save(any(Job.class))).thenReturn(Mono.just(updatedJob));

		Job responseBody = webTestClient.patch().uri("/jobs/1")
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(updatedJob)
			.exchange()
			.expectStatus().isOk()
			.returnResult(Job.class)
			.getResponseBody()
			.blockFirst();

		assertNotNull(responseBody);
		assertEquals(updatedJob.getId(), responseBody.getId());
		assertEquals(updatedJob.getTitle(), responseBody.getTitle());
		assertEquals(updatedJob.getDescription(), responseBody.getDescription());
		assertEquals(updatedJob.getMinSalary(), responseBody.getMinSalary());
		assertEquals(updatedJob.getMaxSalary(), responseBody.getMaxSalary());
		assertEquals(updatedJob.getLocation(), responseBody.getLocation());
	}

	@Test
	void testUpdateJob_NotFound() {
		Job updatedJob = new Job(1L, "Updated Job", "Updated Description", "50000€", "60000€", "Germany");

		when(jobService.findById(1L)).thenReturn(Mono.empty());

		webTestClient.patch().uri("/jobs/1")
			.bodyValue(updatedJob)
			.exchange()
			.expectStatus().isNotFound();
	}

	@Test
	void testDeleteJob_Found() {
		Job existingJob = new Job(1L, "Job to Delete", "Description", "40000€", "50000€", "Germany");

		when(jobService.findById(1L)).thenReturn(Mono.just(existingJob));
		when(jobService.delete(1L)).thenReturn(Mono.empty());

		webTestClient.delete().uri("/jobs/1")
			.exchange()
			.expectStatus().isNoContent();
	}

	@Test
	void testDeleteJob_NotFound() {
		when(jobService.findById(1L)).thenReturn(Mono.empty());

		webTestClient.delete().uri("/jobs/1")
			.exchange()
			.expectStatus().isNotFound();
	}
}

