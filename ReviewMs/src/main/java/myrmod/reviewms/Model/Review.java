package myrmod.reviewms.Model;

import jakarta.persistence.*;
import org.hibernate.validator.constraints.Range;

@Entity
@Table(name = "reviews")
public class Review {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "title")
	private String title;

	@Column(name = "description")
	private String description;

	@Column(name = "rating")
	@Range(min = 1, max = 5, message = "The rating should be between 1 and 5")
	private int rating;

	@Column(name = "company_id")
	private Long companyId;

	public Review(Long id, String title, String description, int rating, Long companyId) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.rating = rating;
		this.companyId = companyId;
	}

	public Review() {}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
