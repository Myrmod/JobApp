package myrmod.jobms.Repository;

import myrmod.jobms.Model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepository extends JpaRepository<Job, Long> {
	public List<Job> findByCompanyId(Long companyId);

}
