package com.imss.sivimss.pagos.model.entity;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;

@Data
public class PagoBitacora {
    
    private int ID_PAGO_BITACORA;
    private int ID_REGISTRO;
    private int ID_FLUJO_PAGOS;
    private int ID_VELATORIO;
    private String FEC_ODS;
    private String NOM_CONTRATANTE;
    private String CVE_FOLIO;
    private BigDecimal IMP_VALOR;
    private String CVE_ESTATUS_PAGO;
    private int ID_USUARIO_ALTA;
    private String FEC_ALTA;
    private Integer ID_USUARIO_BAJA;
    private String FEC_BAJA;
    private Integer ID_USUARIO_MODIFICA;
    private String FEC_ACTUALIZACION;
    private char IND_GEN_PAGARE;
    private char IND_MOD_PAGO;
    private int ID_PLATAFORMA;

    
}

