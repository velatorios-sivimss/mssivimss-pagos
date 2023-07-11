package com.imss.sivimss.pagos.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Builder
@Setter
@Getter
@JsonIgnoreType(value = true)
public class BusquedaDto {
	
	private Integer idOficina;
	private Integer idDelegacion;
	private Integer idVelatorio;
	private String folioODS;
	private String folioPF;
	private String folioRPF;
	private String nomContratante;
	private String fechaIni;
	private String fechaFin;
	private String tipoReporte;

}
