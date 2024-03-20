package com.imss.sivimss.pagos.model.entity;

import lombok.Data;

@Data
public class PagoBitacoraDetalles {
    
    private Integer ID_PAGO_DETALLE;
	private Integer ID_PAGO_BITACORA;
	private Integer ID_METODO_PAGO;
	private Double IMP_PAGO;
	private String NUM_AUTORIZACION;
	private String REF_BANCO;
	private String FEC_PAGO;
	private String FEC_VALE_AGF;
	private Integer CVE_ESTATUS;
	private Integer ID_USUARIO_ALTA;
	private String FEC_ALTA;
	private String FEC_ACTUALIZACION;
	private String FEC_BAJA;
	private Integer ID_USUARIO_MODIFICA;
	private Integer ID_USUARIO_BAJA;
	private String REF_MOTIVO_MODIFICA;
	private String REF_MOTIVO_CANCELA;
	private String FEC_CIERRE_CAJA;
	private Integer IND_ESTATUS_CAJA;

    
}

