package csam.pruebatecnica.adea.inicio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@ComponentScan(basePackages = {
		"csam.pruebatecnica.adea.configuration",
		"csam.pruebatecnica.adea.controller", 
		"csam.pruebatecnica.adea.service", 
		"csam.pruebatecnica.adea.dao", 
		"csam.pruebatecnica.adea.model"} )
@EntityScan (basePackages = {"csam.pruebatecnica.adea.model"})
@EnableJpaRepositories (basePackages = {"csam.pruebatecnica.adea.dao"} )
@SpringBootApplication
public class PrbTecAdeAApplication {

	public static void main(String[] args) {
		SpringApplication.run(PrbTecAdeAApplication.class, args);
	}

}
