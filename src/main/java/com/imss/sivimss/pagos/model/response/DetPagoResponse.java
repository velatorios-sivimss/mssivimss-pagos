package com.imss.sivimss.pagos.model.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetPagoResponse {
	
	private Integer id;
	private String fecha;
	private String folio;
	private String nomContratante;
	private Integer idFlujo;
	private String desFlujo;
	private String fecPago;
	private String desEstatus;
	private String desEstatusPago;
	private Integer idPagoBitacora;
	private Double montoTotal; 
	private List<Map<String, Object>> metodosPago;

}
