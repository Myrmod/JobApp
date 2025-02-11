package myrmod.jobapp.Repository;

import myrmod.jobapp.Model.Job;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JobRepository extends JpaRepository<Job, Long> {

}
