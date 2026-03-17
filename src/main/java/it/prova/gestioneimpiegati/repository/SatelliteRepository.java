package it.prova.gestioneimpiegati.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.repository.CrudRepository;

import it.prova.gestioneimpiegati.model.Satellite;
import it.prova.gestioneimpiegati.model.StatoSatellite;

public interface SatelliteRepository extends CrudRepository<Satellite, Long>,JpaSpecificationExecutor<Satellite> {
	List<Satellite> findByDenominazioneIgnoreCaseContainingOrCodiceIgnoreCaseContainingOrderByDenominazioneAsc(String cognomeTerm,
																								 String nomeTerm);

	Satellite findFirstByDenominazioneIgnoreCase(String denominazione);

	Satellite findFirstByCodiceIgnoreCase(String codice);

	boolean existsByDenominazioneIgnoreCaseAndIdNot(String denominazione, Long id);

	boolean existsByCodiceIgnoreCaseAndIdNot(String codice, Long id);

	@Query("from Satellite s where s.dataLancio is not null and s.dataLancio <= :limite and s.stato <> :stato order by s.denominazione asc")
	List<Satellite> findLanciatiDaPiuDiDueAnniNonDisattivati(@Param("limite") LocalDateTime limite,
			@Param("stato") StatoSatellite stato);

	List<Satellite> findByStatoAndDataRientroIsNullOrderByDenominazioneAsc(StatoSatellite stato);

	@Query("from Satellite s where s.dataLancio is not null and s.dataLancio <= :limite and s.dataRientro is null and s.stato = :stato order by s.denominazione asc")
	List<Satellite> findInOrbitaDaDieciAnniEFissi(@Param("limite") LocalDateTime limite,
			@Param("stato") StatoSatellite stato);
}
