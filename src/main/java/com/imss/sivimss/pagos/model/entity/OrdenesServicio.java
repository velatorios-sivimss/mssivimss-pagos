package com.imss.sivimss.pagos.model.entity;

import lombok.Data;

@Data
public class OrdenesServicio {
    
    private Integer ID_ORDEN_SERVICIO;
	private String CVE_FOLIO;
	private Integer ID_CONTRATANTE;
	private Integer ID_PARENTESCO;
	private Integer ID_VELATORIO;
	private Integer ID_OPERADOR;
	private Integer ID_ESTATUS_ORDEN_SERVICIO;
	private String CVE_TAREA;
	private Integer ID_USUARIO_ALTA;
	private String FEC_ALTA;
	private Integer ID_USUARIO_MODIFICA;
	private String FEC_ACTUALIZACION;
	private String FEC_BAJA;
	private Integer ID_USUARIO_BAJA;
	private Integer ID_CONTRATANTE_PF;
	private String DES_MOTIVO_CANCELACION;

    
}

