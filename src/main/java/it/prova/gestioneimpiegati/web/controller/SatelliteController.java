package it.prova.gestioneimpiegati.web.controller;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.prova.gestioneimpiegati.model.Satellite;
import it.prova.gestioneimpiegati.model.StatoSatellite;
import it.prova.gestioneimpiegati.service.SatelliteService;

@Controller
@RequestMapping(value = "/satellite")
public class SatelliteController {

	@Autowired
	private SatelliteService satelliteService;

	@GetMapping
	public ModelAndView listAll() {
		ModelAndView mv = new ModelAndView();
		List<Satellite> results = satelliteService.listAllElements();
		mv.addObject("satellite_list_attribute", results);
		mv.setViewName("satellite/list");
		return mv;
	}

	@GetMapping("/search")
	public String search() {
		return "satellite/search";
	}

	@PostMapping("/list")
	public String listByExample(Satellite example, ModelMap model) {
		List<Satellite> results = satelliteService.findByExample(example);
		model.addAttribute("satellite_list_attribute", results);
		return "satellite/list";
	}

	@GetMapping("/insert")
	public String create(Model model) {
		model.addAttribute("insert_satellite_attr", new Satellite());
		return "satellite/insert";
	}

	@GetMapping("/edit/{idSatellite}")
	public String edit(@PathVariable(required = true) Long idSatellite, Model model,
			RedirectAttributes redirectAttrs) {
		Satellite satellite = satelliteService.caricaSingoloElemento(idSatellite);

		if (satellite == null) {
			redirectAttrs.addFlashAttribute("errorMessage", "Elemento non trovato");
			return "redirect:/satellite";
		}

		model.addAttribute("update_satellite_attr", satellite);
		return "satellite/update";
	}

	@PostMapping("/save")
	public String save(@Valid @ModelAttribute("insert_satellite_attr") Satellite satellite, BindingResult result,
					   RedirectAttributes redirectAttrs) {
		validateInsertRules(satellite, result);

		if (result.hasErrors())
			return "satellite/insert";

		Satellite satelliteGiaPresente = findExistingSatelliteForInsert(satellite);
		if (satelliteGiaPresente != null) {
			redirectAttrs.addFlashAttribute("errorMessage", "Satellite gia presente. Procedi con l'aggiornamento.");
			return "redirect:/satellite/edit/" + satelliteGiaPresente.getId();
		}

		satelliteService.inserisciNuovo(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

	@PostMapping("/update")
	public String update(@Valid @ModelAttribute("update_satellite_attr") Satellite satellite, BindingResult result,
						 RedirectAttributes redirectAttrs) {
		if (satellite.getId() == null) {
			redirectAttrs.addFlashAttribute("errorMessage", "Elemento non trovato");
			return "redirect:/satellite";
		}

		if (satelliteService.caricaSingoloElemento(satellite.getId()) == null) {
			redirectAttrs.addFlashAttribute("errorMessage", "Elemento non trovato");
			return "redirect:/satellite";
		}

		validateUpdateRules(satellite, result);

		if (result.hasErrors())
			return "satellite/update";

		satelliteService.aggiorna(satellite);

		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

	@GetMapping("/show/{idSatellite}")
	public String show(@PathVariable(required = true) Long idSatellite, Model model) {
		model.addAttribute("show_satellite_attr", satelliteService.caricaSingoloElemento(idSatellite));
		return "satellite/show";
	}

	@GetMapping("/delete/{idSatellite}")
	public String delete(@PathVariable(required = true) Long idSatellite, RedirectAttributes redirectAttrs) {
		Satellite satellite = satelliteService.caricaSingoloElemento(idSatellite);

		if (satellite == null) {
			redirectAttrs.addFlashAttribute("errorMessage", "Elemento non trovato");
			return "redirect:/satellite";
		}

		if (satellite.getStato() != StatoSatellite.DISATTIVATO) {
			redirectAttrs.addFlashAttribute("errorMessage", "Operazione consentita solo per satelliti DISATTIVATI");
			return "redirect:/satellite";
		}

		satelliteService.rimuovi(idSatellite);
		redirectAttrs.addFlashAttribute("successMessage", "Operazione eseguita correttamente");
		return "redirect:/satellite";
	}

	/**
	 * Cerca un satellite gia esistente per denominazione o codice prima
	 * dell'inserimento.
	 *
	 * @param satellite
	 * @return
	 */
	private Satellite findExistingSatelliteForInsert(Satellite satellite) {
		if (StringUtils.isNotBlank(satellite.getDenominazione())) {
			Satellite byDenominazione = satelliteService.findByDenominazione(satellite.getDenominazione());
			if (byDenominazione != null) {
				return byDenominazione;
			}
		}

		if (StringUtils.isNotBlank(satellite.getCodice())) {
			return satelliteService.findByCodice(satellite.getCodice());
		}

		return null;
	}

	/**
	 * Applica le regole di business specifiche dell'inserimento su date e stato.
	 *
	 * @param satellite
	 * @param result
	 */
	private void validateInsertRules(Satellite satellite, BindingResult result) {
		LocalDateTime now = LocalDateTime.now();

		if (satellite.getDataLancio() == null && satellite.getDataRientro() != null) {
			rejectIfNotPresent(result, "dataRientro", "satellite.dataRientro.withoutDataLancio");
		}

		if (satellite.getDataLancio() != null && satellite.getDataRientro() != null && satellite.getDataRientro().isBefore(satellite.getDataLancio())) {
			rejectIfNotPresent(result, "dataRientro", "satellite.dateorder.insert.invalid");
		}

		if (satellite.getDataLancio() == null && satellite.getDataRientro() == null && satellite.getStato() != null) {
			rejectIfNotPresent(result, "stato", "satellite.stato.insert.null");
		}

		if (satellite.getDataRientro() != null && satellite.getDataRientro().isAfter(now) && satellite.getStato() == StatoSatellite.DISATTIVATO) {
			rejectIfNotPresent(result, "stato", "satellite.stato.dataRientroFuture.invalid");
		}

		if (satellite.getDataRientro() != null && satellite.getDataRientro().isBefore(now) && satellite.getStato() != StatoSatellite.DISATTIVATO) {
			rejectIfNotPresent(result, "stato", "satellite.stato.dataRientroPast.invalid");
		}

		if (satellite.getDataLancio() != null && satellite.getDataLancio().isAfter(now) && satellite.getStato() != null) {
			rejectIfNotPresent(result, "stato", "satellite.stato.dataLancioFuture.invalid");
		}
	}

	/**
	 * Applica le regole di business specifiche dell'aggiornamento, inclusi i
	 * controlli di unicita.
	 *
	 * @param satellite
	 * @param result
	 */
	private void validateUpdateRules(Satellite satellite, BindingResult result) {
		if (StringUtils.isNotBlank(satellite.getDenominazione()) && satelliteService.existsAnotherByDenominazione(satellite.getDenominazione(), satellite.getId())) {
			rejectIfNotPresent(result, "denominazione", "satellite.denominazione.duplicate");
		}

		if (StringUtils.isNotBlank(satellite.getCodice()) && satelliteService.existsAnotherByCodice(satellite.getCodice(), satellite.getId())) {
			rejectIfNotPresent(result, "codice", "satellite.codice.duplicate");
		}

		if (satellite.getDataLancio() == null && satellite.getDataRientro() != null) {
			rejectIfNotPresent(result, "dataRientro", "satellite.dataRientro.withoutDataLancio");
		}

		if (satellite.getDataLancio() != null && satellite.getDataRientro() != null && !satellite.getDataRientro().isAfter(satellite.getDataLancio())) {
			rejectIfNotPresent(result, "dataRientro", "satellite.dateorder.update.invalid");
		}

		if (satellite.getDataLancio() != null && satellite.getDataLancio().isAfter(LocalDateTime.now()) && satellite.getStato() != null) {
			rejectIfNotPresent(result, "stato", "satellite.stato.dataLancioFuture.invalid");
		}
	}

	/**
	 * Aggiunge un errore di validazione sul campo solo se non e gia presente con
	 * lo stesso codice.
	 *
	 * @param result
	 * @param fieldName
	 * @param errorCode
	 */
	private void rejectIfNotPresent(BindingResult result, String fieldName, String errorCode) {
		for (FieldError fieldError : result.getFieldErrors(fieldName)) {
			if (errorCode.equals(fieldError.getCode())) {
				return;
			}
		}
		result.rejectValue(fieldName, errorCode);
	}

}
