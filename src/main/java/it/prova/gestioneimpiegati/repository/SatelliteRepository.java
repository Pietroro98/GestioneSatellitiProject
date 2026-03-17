package it.prova.gestioneimpiegati.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestioneimpiegati.model.Satellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long>,JpaSpecificationExecutor<Satellite> {
	List<Satellite> findByDenominazioneIgnoreCaseContainingOrCodiceIgnoreCaseContainingOrderByDenominazioneAsc(String cognomeTerm,
																								 String nomeTerm);

	Satellite findFirstByDenominazioneIgnoreCase(String denominazione);

	Satellite findFirstByCodiceIgnoreCase(String codice);

	boolean existsByDenominazioneIgnoreCaseAndIdNot(String denominazione, Long id);

	boolean existsByCodiceIgnoreCaseAndIdNot(String codice, Long id);
}
