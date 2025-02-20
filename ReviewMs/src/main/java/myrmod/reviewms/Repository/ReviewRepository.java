package myrmod.reviewms.Repository;

import myrmod.reviewms.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import reactor.core.publisher.Flux;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
	public List<Review> findByCompanyId(Long companyId);
}
