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
public class AgfDto {

	private Integer idODS;
	private Integer agf;
	private String nss;
	private Integer idFinado;
}
