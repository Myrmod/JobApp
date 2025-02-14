package myrmod.companyms.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

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
}
