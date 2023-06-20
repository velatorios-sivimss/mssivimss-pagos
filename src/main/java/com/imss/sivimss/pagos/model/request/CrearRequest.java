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
public class CrearRequest {

	private String idPagoBitacora;
	private String idMetodoPago;
	private Integer idFlujoPago;
	private String idRegistro;
	private Double importePago;
	private String numAutorizacion;
	private String descBanco;
	private String fechaPago;
	private String fechaValeAGF;
	private Double importeRegistro;
	
}
