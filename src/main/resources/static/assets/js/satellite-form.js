document.addEventListener('DOMContentLoaded', function() {
    // Messaggio condiviso usato quando l'ordine delle date non è valido.
    const DATE_ERROR_MESSAGE = 'La data di rientro non puo essere precedente alla data di lancio.';

    // Lo script viene eseguito solo nei form satellite che espongono questo attributo.
    const form = document.querySelector('form[data-satellite-form]');

    if (!form) {
        return;
    }

    // Raccolta centralizzata dei riferimenti ai campi del form.
    const fields = {
        denominazione: document.getElementById('denominazione'),
        codice: document.getElementById('codice'),
        dataLancio: document.getElementById('dataLancio'),
        dataRientro: document.getElementById('dataRientro'),
        stato: document.getElementById('stato'),
        submitButton: document.getElementById('submit'),
        dataRientroError: document.getElementById('dataRientroError')
    };

    if (Object.values(fields).some(function(field) { return !field; })) {
        return;
    }

    // Controlla se un campo testuale/data contiene un valore effettivo.
    function hasValue(field) {
        return field.value.trim() !== '';
    }

    // Mostra l'errore sulla data di rientro sia lato browser sia a video.
    function showDateError(message) {
        fields.dataRientro.setCustomValidity(message);
        fields.dataRientro.classList.add('input-error');
        fields.dataRientroError.textContent = message;
        fields.dataRientroError.style.display = 'block';
    }

    // Ripulisce completamente l'errore personalizzato associato alla data di rientro.
    function clearDateError() {
        fields.dataRientro.setCustomValidity('');
        fields.dataRientro.classList.remove('input-error');
        fields.dataRientroError.textContent = '';
        fields.dataRientroError.style.display = 'none';
    }

    // Abilita/disabilita dataRientro e stato in base alla presenza di dataLancio.
    // Se dataLancio viene svuotata, resetta anche i campi dipendenti.
    function updateFields() {
        const hasDataLancio = hasValue(fields.dataLancio);

        fields.dataRientro.disabled = !hasDataLancio;
        fields.stato.disabled = !hasDataLancio;

        if (!hasDataLancio) {
            fields.dataRientro.value = '';
            fields.stato.value = '';
            clearDateError();
        }
    }

    // Il submit è consentito solo quando i due campi obbligatori lato client sono compilati.
    function updateSubmitState() {
        const canSubmit = hasValue(fields.denominazione) && hasValue(fields.codice);
        fields.submitButton.disabled = !canSubmit;
    }

    // Verifica che, se entrambe presenti, la data di rientro non sia precedente alla data di lancio.
    function areDatesValid() {
        if (!hasValue(fields.dataLancio) || !hasValue(fields.dataRientro)) {
            clearDateError();
            return true;
        }

        const lancioDate = new Date(fields.dataLancio.value);
        const rientroDate = new Date(fields.dataRientro.value);

        if (rientroDate >= lancioDate) {
            clearDateError();
            return true;
        }

        showDateError(DATE_ERROR_MESSAGE);
        return false;
    }

    // Quando cambia dataLancio va ricalcolato sia lo stato dei campi dipendenti sia la validità delle date.
    function handleLaunchDateChange() {
        updateFields();
        areDatesValid();
    }

    // Registra tutti gli event listener del form in un unico punto.
    function bindEvents() {
        fields.dataLancio.addEventListener('input', handleLaunchDateChange);
        fields.dataLancio.addEventListener('change', handleLaunchDateChange);
        fields.dataRientro.addEventListener('input', areDatesValid);
        fields.dataRientro.addEventListener('change', areDatesValid);
        fields.denominazione.addEventListener('input', updateSubmitState);
        fields.denominazione.addEventListener('change', updateSubmitState);
        fields.codice.addEventListener('input', updateSubmitState);
        fields.codice.addEventListener('change', updateSubmitState);

        form.addEventListener('submit', function(event) {
            // Prima del submit riallinea lo stato della UI e riesegue le validazioni client.
            updateFields();
            updateSubmitState();

            if (fields.submitButton.disabled || !areDatesValid()) {
                event.preventDefault();

                // Se il blocco dipende dalle date, mostra il messaggio nativo del browser.
                if (!fields.submitButton.disabled) {
                    fields.dataRientro.reportValidity();
                }
            }
        });
    }

    // Inizializzazione unica del comportamento client del form.
    function init() {
        bindEvents();
        updateFields();
        updateSubmitState();
        areDatesValid();
    }

    init();
});
