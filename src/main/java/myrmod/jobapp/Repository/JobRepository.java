package myrmod.jobapp.Repository;

import myrmod.jobapp.Model.Job;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class JobRepository {
	private static final Map<String, Job> JOB_DATA;

	static {
		JOB_DATA = new HashMap<>();
		JOB_DATA.put("1", new Job(1L, "Job 1", "Description 1", "40000€", "50000€", "Germany"));
		JOB_DATA.put("2", new Job(2L, "Job 2", "Description 2", "40000€", "50000€", "Germany"));
		JOB_DATA.put("3", new Job(3L, "Job 3", "Description 3", "40000€", "50000€", "Germany"));
		JOB_DATA.put("4", new Job(4L, "Job 4", "Description 4", "40000€", "50000€", "Germany"));
		JOB_DATA.put("5", new Job(5L, "Job 5", "Description 5", "40000€", "50000€", "Germany"));
		JOB_DATA.put("6", new Job(6L, "Job 6", "Description 6", "40000€", "50000€", "Germany"));
		JOB_DATA.put("7", new Job(7L, "Job 7", "Description 7", "40000€", "50000€", "Germany"));
		JOB_DATA.put("8", new Job(8L, "Job 8", "Description 8", "40000€", "50000€", "Germany"));
		JOB_DATA.put("9", new Job(9L, "Job 9", "Description 9", "40000€", "50000€", "Germany"));
		JOB_DATA.put("10", new Job(10L, "Job 10", "Description 10", "40000€", "50000€", "Germany"));
	}

	public Job findById(Long id) {
		return JOB_DATA.get(id.toString());
	}

	public List<Job> findAll() {
		return JOB_DATA.values().stream().toList();
	}

	public Job save(Job job) {
		JOB_DATA.put(job.getId().toString(), job);
		return job;
	}

	public Void delete(Long id) {
		JOB_DATA.remove(id.toString());

		return null;
	}
}
