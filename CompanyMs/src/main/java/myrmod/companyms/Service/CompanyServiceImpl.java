package myrmod.companyms.Service;

import myrmod.companyms.Model.Company;
import myrmod.companyms.Repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CompanyServiceImpl implements CompanyService {
	private final CompanyRepository companyRepository;

	@Autowired
	public CompanyServiceImpl(CompanyRepository companyRepository) {
		this.companyRepository = companyRepository;
	}

	@Override
	public Mono<Company> findById(Long id) {
		return Mono.justOrEmpty(companyRepository.findById(id));
	}

	@Override
	public Flux<Company> findAll() {
		return Flux.fromIterable(companyRepository.findAll());
	}

	@Override
	public Mono<Company> save(Company company) {
		return Mono.just(companyRepository.save(company));
	}

	@Override
	public Mono<Void> delete(Long id) {
		return Mono.justOrEmpty(companyRepository.findById(id))
			.flatMap(existingCompany -> Mono.fromRunnable(() -> companyRepository.delete(existingCompany)))
			.then();
	}
}
