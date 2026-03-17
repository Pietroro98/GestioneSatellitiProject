package it.prova.gestioneimpiegati.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.criteria.Predicate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import it.prova.gestioneimpiegati.model.Satellite;
import it.prova.gestioneimpiegati.model.StatoSatellite;
import it.prova.gestioneimpiegati.repository.SatelliteRepository;

@Service
public class SatelliteServiceImpl implements SatelliteService {

	@Autowired
	private SatelliteRepository repository;

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> listAllElements() {
		return (List<Satellite>) repository.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Satellite caricaSingoloElemento(Long id) {
		return repository.findById(id).orElse(null);
	}

	@Override
	@Transactional
	public void aggiorna(Satellite satelliteInstance) {
		repository.save(satelliteInstance);
	}

	@Override
	@Transactional
	public void inserisciNuovo(Satellite satelliteInstance) {
		repository.save(satelliteInstance);

	}

	@Override
	@Transactional
	public void rimuovi(Long idSatellite) {
		repository.deleteById(idSatellite);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> cercaByDenominazioneECodiceILike(String denominazioneTerm, String codiceTerm) {
		return repository.findByDenominazioneIgnoreCaseContainingOrCodiceIgnoreCaseContainingOrderByDenominazioneAsc(denominazioneTerm,
				codiceTerm);
	}

	@Override
	@Transactional(readOnly = true)
	public Satellite findByDenominazione(String denominazione) {
		return repository.findFirstByDenominazioneIgnoreCase(denominazione);
	}

	@Override
	@Transactional(readOnly = true)
	public Satellite findByCodice(String codice) {
		return repository.findFirstByCodiceIgnoreCase(codice);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsAnotherByDenominazione(String denominazione, Long id) {
		return repository.existsByDenominazioneIgnoreCaseAndIdNot(denominazione, id);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsAnotherByCodice(String codice, Long id) {
		return repository.existsByCodiceIgnoreCaseAndIdNot(codice, id);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> findLanciatiDaPiuDiDueAnniNonDisattivati() {
		return repository.findLanciatiDaPiuDiDueAnniNonDisattivati(LocalDateTime.now().minusYears(2),
				StatoSatellite.DISATTIVATO);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> findDisattivatiMaNonRientrati() {
		return repository.findByStatoAndDataRientroIsNullOrderByDenominazioneAsc(StatoSatellite.DISATTIVATO);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> findInOrbitaDaDieciAnniEFissi() {
		return repository.findInOrbitaDaDieciAnniEFissi(LocalDateTime.now().minusYears(10), StatoSatellite.FISSO);
	}

	@Override
	@Transactional(readOnly = true)
	public List<Satellite> findByExample(Satellite example) {
		Specification<Satellite> specificationCriteria = (root, query, cb) -> {

			List<Predicate> predicates = new ArrayList<Predicate>();

			if (StringUtils.isNotEmpty(example.getDenominazione()))
				predicates.add(cb.like(cb.upper(root.get("denominazione")), "%" + example.getDenominazione().toUpperCase() + "%"));

			if (StringUtils.isNotEmpty(example.getCodice()))
				predicates.add(cb.like(cb.upper(root.get("codice")), "%" + example.getCodice().toUpperCase() + "%"));

			if (example.getStato() != null)
				predicates.add(cb.equal(root.get("stato"), example.getStato()));

			if (example.getDataLancio() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataLancio"), example.getDataLancio()));


			if (example.getDataRientro() != null)
				predicates.add(cb.greaterThanOrEqualTo(root.get("dataRientro"), example.getDataRientro()));

			return cb.and(predicates.toArray(new Predicate[predicates.size()]));
		};

		return repository.findAll(specificationCriteria);
	}

}
