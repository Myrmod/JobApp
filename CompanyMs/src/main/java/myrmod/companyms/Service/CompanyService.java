package myrmod.companyms.Service;

import myrmod.companyms.Model.Company;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface CompanyService {
	Mono<Company> findById(Long id);

	Flux<Company> findAll();

	Mono<Company> save(Company company);

	Mono<Void> delete(Long id);
}
