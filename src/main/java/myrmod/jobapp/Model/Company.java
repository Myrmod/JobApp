package myrmod.jobapp.Model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "companies")
public class Company {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	@NotBlank(message = "Name is required")
	private String name;

	@Column(name = "description")
	@NotBlank(message = "Description is required")
	private String description;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
	@JsonManagedReference
	private List<Job> jobs;

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "company")
	@JsonManagedReference
	private List<Review> reviews;

	public Company(Long id, String name, String description, List<Job> jobs, List<Review> reviews) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.jobs = jobs;
		this.reviews = reviews;
	}

	public Company(Long id, String name, String description) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public Company() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Job> getJobs() {
		return jobs;
	}

	public void setJobs(List<Job> jobs) {
		this.jobs = jobs;
	}

	public List<Review> getReviews() {
		return reviews;
	}

	public void setReviews(List<Review> reviews) {
		this.reviews = reviews;
	}
}
