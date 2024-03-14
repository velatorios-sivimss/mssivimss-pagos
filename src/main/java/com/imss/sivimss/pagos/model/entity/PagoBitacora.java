package com.imss.sivimss.pagos.model.entity;

import java.math.BigDecimal;
import java.sql.Date;

import lombok.Data;

@Data
public class PagoBitacora {
    
    private int idPagoBitacora;
    private int idRegistro;
    private int idFlujoPagos;
    private int idVelatorio;
    private Date fechaOds;
    private String nombreContratante;
    private String claveFolio;
    private BigDecimal importeValor;
    private String claveEstatusPago;
    private int idUsuarioAlta;
    private Date fechaAlta;
    private Integer idUsuarioBaja;
    private Date fechaBaja;
    private Integer idUsuarioModifica;
    private Date fechaActualizacion;
    private char indGenPagare;
    private char indModPago;
    private int idPlataforma;

    
}

