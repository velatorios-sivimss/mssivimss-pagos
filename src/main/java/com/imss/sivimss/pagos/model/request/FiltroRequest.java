package com.imss.sivimss.pagos.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
@Getter
@JsonIgnoreType(value = true)
public class FiltroRequest {

	private String idVelatorio;
	private String idFlujoPagos;
	private String folio;
	private String nomContratante;
	private String fechaInicio;
	private String fechaFin;
	
}
