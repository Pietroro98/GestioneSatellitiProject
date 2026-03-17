package it.prova.gestioneimpiegati.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "satellite")
public class Satellite {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@NotBlank(message = "{impiegato.denominazione.notblank}")
	@Column(name = "denominazione")
	private String denominazione;

	@NotBlank(message = "{impiegato.codice.notblank}")
	@Column(name = "codice")
	private String codice;

	@Column(name = "dataLancio")
	private LocalDateTime dataLancio;

	@Column(name = "dataRientro")
	private LocalDateTime dataRientro;

	@Column(name = "stato")
	@Enumerated(EnumType.STRING)
	private StatoSatellite stato;


	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDenominazione() {
		return denominazione;
	}

	public void setDenominazione(String denominazione) {
		this.denominazione = denominazione;
	}

	public String getCodice() {
		return codice;
	}

	public void setCodice(String codice) {
		this.codice = codice;
	}

	public LocalDateTime getDataLancio() {
		return dataLancio;
	}

	public void setDataLancio(LocalDateTime dataLancio) {
		this.dataLancio = dataLancio;
	}

	public LocalDateTime getDataRientro() {
		return dataRientro;
	}

	public void setDataRientro(LocalDateTime dataRientro) {
		this.dataRientro = dataRientro;
	}

	public StatoSatellite getStato() {
		return stato;
	}

	public void setStato(StatoSatellite stato) {
		this.stato = stato;
	}
}
