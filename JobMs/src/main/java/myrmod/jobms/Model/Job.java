package myrmod.jobms.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "jobs")
public class Job {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "title")
	@NotBlank(message = "Title is required")
	private String title;

	@Column(name = "description")
	@NotBlank(message = "Description is required")
	private String description;

	@Column(name = "min_salary")
	private String minSalary;

	@Column(name = "max_salary")
	private String maxSalary;

	@Column(name = "location")
	@NotBlank(message = "Location is required")
	private String location;

	@Column(name = "company_id")
	@NotBlank(message = "Company ID is required")
	private Long companyId;

	public Job(Long id, String title, String description, String minSalary, String maxSalary, String location, Long companyId) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.minSalary = minSalary;
		this.maxSalary = maxSalary;
		this.location = location;
		this.companyId = companyId;
	}

	public Job() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getMinSalary() {
		return minSalary;
	}

	public void setMinSalary(String minSalary) {
		this.minSalary = minSalary;
	}

	public String getMaxSalary() {
		return maxSalary;
	}

	public void setMaxSalary(String maxSalary) {
		this.maxSalary = maxSalary;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
}
