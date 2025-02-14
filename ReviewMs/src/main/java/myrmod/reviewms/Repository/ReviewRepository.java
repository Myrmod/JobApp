package myrmod.reviewms.Repository;

import myrmod.reviewms.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
