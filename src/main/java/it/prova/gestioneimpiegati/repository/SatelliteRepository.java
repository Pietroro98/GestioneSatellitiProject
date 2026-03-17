package it.prova.gestioneimpiegati.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestioneimpiegati.model.Satellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long>,JpaSpecificationExecutor<Satellite> {
	List<Satellite> findByDenominazioneIgnoreCaseContainingOrCodiceIgnoreCaseContainingOrderByDenominazioneAsc(String cognomeTerm,
																								 String nomeTerm);
}
