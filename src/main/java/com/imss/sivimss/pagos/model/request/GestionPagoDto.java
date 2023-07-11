package com.imss.sivimss.pagos.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class GestionPagoDto {
	
	private Integer idFlujo;
	private Integer idPago;

}
