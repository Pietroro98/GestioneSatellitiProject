package it.prova.gestioneimpiegati.service;

import java.util.List;

import it.prova.gestioneimpiegati.model.Satellite;

public interface SatelliteService {
	public List<Satellite> listAllElements();

	public Satellite caricaSingoloElemento(Long id);
	
	public void aggiorna(Satellite satelliteInstance);

	public void inserisciNuovo(Satellite satelliteInstance);

	public void rimuovi(Long idSatellite);
	
	public List<Satellite> findByExample(Satellite example);
	
	public List<Satellite> cercaByDenominazioneECodiceILike(String denominazioneTerm, String codiceTerm);
}
