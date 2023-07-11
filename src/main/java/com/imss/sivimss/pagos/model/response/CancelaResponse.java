package com.imss.sivimss.pagos.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CancelaResponse {
	
	private Integer idFlujo;
	private Integer idPago;
	private Integer idPagoDetalle;
	private Integer idUsuarioCancela;
	private String motivoCancela;

}
