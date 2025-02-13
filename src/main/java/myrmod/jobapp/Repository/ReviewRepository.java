package myrmod.jobapp.Repository;

import myrmod.jobapp.Model.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

}
