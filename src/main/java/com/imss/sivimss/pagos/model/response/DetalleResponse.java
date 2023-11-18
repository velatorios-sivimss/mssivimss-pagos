package com.imss.sivimss.pagos.model.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class DetalleResponse {
	
	private String idPagoBitacora;
	private String idFlujoPago;
	private String idRegistro;
	private String folio;
	private String idEstatusPago;
	private String estatusPago;
	private Double totalAPagar;
	private Double totalPagado;
	private Double totalPorCubrir;
	private String tipoPago;
	private String fechaUltimaPago;
	private String valeP;
	private String nss;
	private String idFinado;
	private String generarPagare;
	private List<Map<String, Object>> metodosPago;
}
