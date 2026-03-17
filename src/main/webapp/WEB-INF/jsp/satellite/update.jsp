<%@ taglib prefix = "fmt" uri = "http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!doctype html>
<html lang="it" class="h-100" >
	 <head>
	 
	 	<!-- Common imports in pages -->
	 	<jsp:include page="../header.jsp" />
		<style>
			.error_field {
				color: red;
			}
		</style>

	<title>Aggiorna Elemento</title>
	 </head>
	   <body class="d-flex flex-column h-100">
	   
	   		<!-- Fixed navbar -->
	   		<jsp:include page="../navbar.jsp"></jsp:include>
	    
			
			<!-- Begin page content -->
			<main class="flex-shrink-0">
			  <div class="container">
			  
			  		<spring:hasBindErrors name="update_satellite_attr">
						<div class="alert alert-danger " role="alert">
							Attenzione!! Sono presenti errori di validazione
						</div>
					</spring:hasBindErrors>
			  
			  		<div class="alert alert-danger alert-dismissible fade show ${errorMessage==null?'d-none':'' }" role="alert">
					  ${errorMessage}
					  <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close" ></button>
					</div>
			  
					  <div class='card'>
						    <div class='card-header'>
						        <h5>Aggiorna elemento</h5> 
						    </div>
						    <div class='card-body'>
				
									<h6 class="card-title">I campi con <span class="text-danger">*</span> sono obbligatori</h6>
				
									<form:form modelAttribute="update_satellite_attr" method="post" action="${pageContext.request.contextPath}/satellite/update" class="row g-3" novalidate="novalidate">
										<form:hidden path="id"/>
									
										<div class="col-md-6">
											<label for="denominazione" class="form-label">Denominazione <span class="text-danger">*</span></label>
											<spring:bind path="denominazione">
												<input type="text" name="denominazione" id="denominazione" class="form-control ${status.error ? 'is-invalid' : ''}" placeholder="Inserire la denominazione" value="${update_satellite_attr.denominazione }" required>
											</spring:bind>
											<form:errors path="denominazione" cssClass="error_field" />
										</div>
										
										<div class="col-md-6">
											<label for="codice" class="form-label">Codice <span class="text-danger">*</span></label>
											<spring:bind path="codice">
												<input type="text" name="codice" id="codice" class="form-control ${status.error ? 'is-invalid' : ''}" placeholder="Inserire il codice" value="${update_satellite_attr.codice }" required>
											</spring:bind>
											<form:errors path="codice" cssClass="error_field" />
										</div>
										
										<div class="col-md-3">
											<label for="dataLancio" class="form-label">Data di Lancio <span class="text-danger">*</span></label>
		                        			<spring:bind path="dataLancio">
			                        		<input class="form-control ${status.error ? 'is-invalid' : ''}" id="dataLancio" type="datetime-local" placeholder="dd/MM/yy"
			                            		title="formato : gg/mm/aaaa" name="dataLancio" required
												   value="${not empty update_satellite_attr.dataLancio ? fn:substring(update_satellite_attr.dataLancio, 0, 16) : ''}">
				                            </spring:bind>
			                            	<form:errors path="dataLancio" cssClass="error_field" />
										</div>

										<div class="col-md-3">
											<label for="dataRientro" class="form-label">Data di Rientro <span class="text-danger">*</span></label>
											<spring:bind path="dataRientro">
												<input class="form-control ${status.error ? 'is-invalid' : ''}" id="dataRientro" type="datetime-local" placeholder="dd/MM/yy"
													   title="formato : gg/mm/aaaa" name="dataRientro" required
													   value="${not empty update_satellite_attr.dataRientro ? fn:substring(update_satellite_attr.dataRientro, 0, 16) : ''}">
											</spring:bind>
											<form:errors path="dataRientro" cssClass="error_field" />
										</div>

										<div class="col-12">
											<form:errors path="dataLancioBeforeDataRientro" cssClass="error_field" />
										</div>


										<div class="col-md-3">
											<label for="stato" class="form-label">Stato <span class="text-danger">*</span></label>
										    <spring:bind path="stato">
											    <select class="form-select ${status.error ? 'is-invalid' : ''}" id="stato" name="stato" required>
											    	<option value=""> - Selezionare - </option>
											    	<option value="IN_MOVIMENTO" ${update_satellite_attr.stato == 'IN_MOVIMENTO'?'selected':''}>IN MOVIMENTO</option>
											      	<option value="FISSO" ${update_satellite_attr.stato == 'FISSO'?'selected':''}>FISSO</option>
											      	<option value="DISATTIVATO" ${update_satellite_attr.stato == 'DISATTIVATO'?'selected':''}>DISATTIVATO</option>
										    	</select>
										    </spring:bind>
										    <form:errors path="stato" cssClass="error_field" />
										</div>
										
										<div class="col-12">
											<button type="submit" name="submit" value="submit" id="submit" class="btn btn-primary">Conferma</button>
											<a href="${pageContext.request.contextPath}/satellite" class="btn btn-outline-secondary">Annulla</a>
										</div>
				
								</form:form>
		  
							</div>
						</div>
			  </div>
			</main>
			
			<!-- Footer -->
			<jsp:include page="../footer.jsp" />
	  </body>
</html>
