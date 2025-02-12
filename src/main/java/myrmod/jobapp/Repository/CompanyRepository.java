package myrmod.jobapp.Repository;

import myrmod.jobapp.Model.Company;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CompanyRepository extends JpaRepository<Company, Long> {
	@EntityGraph(attributePaths = "jobs")
	public List<Company> findAll();

	@EntityGraph(attributePaths = "jobs")
	public Optional<Company> findById(@Param("id") Long id);
}
