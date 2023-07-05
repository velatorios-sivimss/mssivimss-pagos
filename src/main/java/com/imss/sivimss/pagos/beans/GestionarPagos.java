package com.imss.sivimss.pagos.beans;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.pagos.model.request.BusquedaDto;
import com.imss.sivimss.pagos.model.response.ModificaResponse;
import com.imss.sivimss.pagos.util.AppConstantes;
import com.imss.sivimss.pagos.util.DatosRequest;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class GestionarPagos {
	
	private Integer idFlujo;
	private Integer idPago;
	
	private static final Integer NIVEL_DELEGACION = 2;
	private static final Integer NIVEL_VELATORIO = 3;
	
	private String formatoFecLocal;
	
	public GestionarPagos(Integer idFlujo, Integer idPago) {
		this.idFlujo = idFlujo;
		this.idPago = idPago;
	}
	
    public DatosRequest foliosOds(BusquedaDto busqueda) throws UnsupportedEncodingException {
    	DatosRequest request = new DatosRequest();
    	Map<String, Object> parametro = new HashMap<>();
    	
		StringBuilder query = new StringBuilder("SELECT OS.ID_ORDEN_SERVICIO AS id, OS.CVE_FOLIO AS folio \n");
	    query.append("FROM SVC_ORDEN_SERVICIO OS \n");
		query.append("JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = OS.ID_ORDEN_SERVICIO \n");
		query.append("WHERE OS.ID_ESTATUS_ORDEN_SERVICIO IN (0, 2, 4) \n");
		query.append("AND PB.CVE_ESTATUS_PAGO IN (2, 4, 5) \n");
		query.append("AND PB.ID_FLUJO_PAGOS = '1' ");
        if (busqueda.getIdVelatorio() != null) {
    		query.append("AND OS.ID_VELATORIO = " + busqueda.getIdVelatorio());
    	}
	
        String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes("UTF-8"));
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
    }
    
    
    public DatosRequest foliosPf(BusquedaDto busqueda) throws UnsupportedEncodingException {
    	DatosRequest request = new DatosRequest();
    	Map<String, Object> parametro = new HashMap<>();
    	
    	StringBuilder query = new StringBuilder("SELECT PF.ID_CONVENIO_PF AS id, PF.DES_FOLIO AS folio \n");
    	query.append("FROM SVT_CONVENIO_PF PF \n");
    	query.append("JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = PF.ID_CONVENIO_PF \n");
    	query.append("WHERE PF.ID_ESTATUS_CONVENIO IN ('2','3') \n");
    	query.append("AND PB.CVE_ESTATUS_PAGO IN (4, 5) \n");
    	query.append("AND PB.ID_FLUJO_PAGOS = '2' ");
    	if (busqueda.getIdVelatorio() != null) {
    		query.append("AND PF.ID_VELATORIO = " + busqueda.getIdVelatorio());
    	}
    	
    	String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes("UTF-8"));
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
    }
    
    public DatosRequest foliosRpf(BusquedaDto busqueda) throws UnsupportedEncodingException {
    	DatosRequest request = new DatosRequest();
    	Map<String, Object> parametro = new HashMap<>();
    	
    	StringBuilder query = new StringBuilder("SELECT RPF.ID_RENOVACION_CONVENIO_PF AS id, PF.DES_FOLIO AS folio \n");
    	query.append("FROM SVT_RENOVACION_CONVENIO_PF RPF \n");
    	query.append("JOIN SVT_CONVENIO_PF PF ON PF.ID_CONVENIO_PF = RPF.ID_CONVENIO_PF \n");
    	query.append("JOIN SVT_PAGO_BITACORA PB ON RPF.ID_RENOVACION_CONVENIO_PF = PB.ID_REGISTRO \n");
    	query.append("WHERE RPF.IND_ESTATUS = 1 \n");
    	query.append("AND PB.CVE_ESTATUS_PAGO IN (0, 2, 3, 4, 5) \n");
    	query.append("AND PB.ID_FLUJO_PAGOS = '3' ");
        if (busqueda.getIdVelatorio() != null) {
        	query.append("AND PF.ID_VELATORIO = " + busqueda.getIdVelatorio());
    	}
    	
        String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes("UTF-8"));
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
    }
    
    public DatosRequest consultaPagos(DatosRequest request, BusquedaDto busqueda, String formatoFecha) throws UnsupportedEncodingException {
    	this.formatoFecLocal = formatoFecha;
    	// ODS
    	StringBuilder queryCompleto = consultaOds();
    	if (busqueda.getIdOficina() == NIVEL_DELEGACION) {
    		queryCompleto.append(" AND VEL.ID_DELEGACION = ").append(busqueda.getIdDelegacion());
    	} else if (busqueda.getIdOficina() == NIVEL_VELATORIO) {
    		queryCompleto.append(" AND OS.ID_VELATORIO = ").append(busqueda.getIdVelatorio());
    	}
    	queryCompleto.append(groupByOds());
    	
    	// PF
    	queryCompleto.append("UNION \n");
    	queryCompleto.append(consultaPf());
    	if (busqueda.getIdOficina() == NIVEL_DELEGACION) {
    		queryCompleto.append(" AND VEL.ID_DELEGACION = ").append(busqueda.getIdDelegacion());
    	} else if (busqueda.getIdOficina() == NIVEL_VELATORIO) {
    		queryCompleto.append(" AND PF.ID_VELATORIO = ").append(busqueda.getIdVelatorio());
    	}
    	queryCompleto.append(groupByPf());
    	
    	// RPF
    	queryCompleto.append("UNION \n");
    	queryCompleto.append(consultaRpf());
    	if (busqueda.getIdOficina() == NIVEL_DELEGACION) {
    		queryCompleto.append(" AND VEL.ID_DELEGACION = ").append(busqueda.getIdDelegacion());
    	} else if (busqueda.getIdOficina() == NIVEL_VELATORIO) {
    		queryCompleto.append(" AND PF.ID_VELATORIO = ").append(busqueda.getIdVelatorio());
    	}
    	queryCompleto.append(groupByRpf());
    	
    	String encoded = DatatypeConverter.printBase64Binary(queryCompleto.toString().getBytes("UTF-8"));
		request.getDatos().put(AppConstantes.QUERY, encoded);
    	
    	return request;
    }
    
    public DatosRequest buscaPagos(DatosRequest request, BusquedaDto busqueda, String formatoFecha) throws UnsupportedEncodingException {
    	this.formatoFecLocal = formatoFecha;
    	// ODS
    	StringBuilder queryCompleto = consultaOds();
    	if (busqueda.getIdVelatorio() != null) {
    		queryCompleto.append(" AND OS.ID_VELATORIO = ").append(busqueda.getIdVelatorio());
    	}
    	if (busqueda.getFolioODS() != null) {
    		queryCompleto.append(" AND OS.CVE_FOLIO = '").append(busqueda.getFolioODS()).append("' ");
    	} 
    	if (busqueda.getNomContratante() != null) {
    		queryCompleto.append(" AND PB.NOM_CONTRATANTE LIKE '%").append(busqueda.getNomContratante()).append("%'");
    	}
    	if (busqueda.getFechaIni() != null) {
    		queryCompleto.append(" AND OS.FEC_ALTA BETWEEN STR_TO_DATE('" + busqueda.getFechaIni() + "','" + formatoFecha + "') AND STR_TO_DATE('" + busqueda.getFechaFin() + "','" + formatoFecha + "')");
    	}
    	queryCompleto.append(groupByOds());
    	
    	// PF
    	queryCompleto.append("UNION \n");
    	queryCompleto.append(consultaPf());
    	if (busqueda.getIdVelatorio() != null) {
    		queryCompleto.append(" AND PF.ID_VELATORIO = ").append(busqueda.getIdVelatorio());
    	}
    	if (busqueda.getFolioPF() != null) {
    		queryCompleto.append(" AND PF.DES_FOLIO = '").append(busqueda.getFolioPF()).append("' ");
    	} 
    	if (busqueda.getNomContratante() != null) {
    		queryCompleto.append(" AND PB.NOM_CONTRATANTE LIKE '%").append(busqueda.getNomContratante()).append("%'");
    	}
    	if (busqueda.getFechaIni() != null) {
    		queryCompleto.append(" AND PF.FEC_ALTA BETWEEN STR_TO_DATE('" + busqueda.getFechaIni() + "','" + formatoFecha + "') AND STR_TO_DATE('" + busqueda.getFechaFin() + "','" + formatoFecha + "')");
    	}
        queryCompleto.append(groupByPf());
    	
    	// RPF
    	queryCompleto.append("UNION \n");
    	queryCompleto.append(consultaRpf());
    	if (busqueda.getIdVelatorio() != null) {
    		queryCompleto.append(" AND PF.ID_VELATORIO = ").append(busqueda.getIdVelatorio());
    	}
    	if (busqueda.getFolioRPF() != null) {
    		queryCompleto.append(" AND RPF.DES_FOLIO_ADENDA = '").append(busqueda.getFolioRPF()).append("' ");
    	} 
    	if (busqueda.getNomContratante() != null) {
    		queryCompleto.append(" AND PB.NOM_CONTRATANTE LIKE '%").append(busqueda.getNomContratante()).append("%'");
    	}
    	if (busqueda.getFechaIni() != null) {
    		queryCompleto.append(" AND RPF.FEC_ALTA BETWEEN STR_TO_DATE('" + busqueda.getFechaIni() + "','" + formatoFecha + "') AND STR_TO_DATE('" + busqueda.getFechaFin() + "','" + formatoFecha + "')");
    	}
        queryCompleto.append(groupByRpf());
        
        String encoded = DatatypeConverter.printBase64Binary(queryCompleto.toString().getBytes("UTF-8"));
		request.getDatos().put(AppConstantes.QUERY, encoded);
    	
    	return request;
    }
    
    public DatosRequest detalleGeneral(DatosRequest request, String formatoFecha) throws UnsupportedEncodingException {
    	StringBuilder query = new StringBuilder();
    	switch (this.idFlujo) {
    	   case 1:
    		   query.append("SELECT OS.ID_ORDEN_SERVICIO AS id, DATE_FORMAT(OS.FEC_ALTA,'" + formatoFecha + "') AS fecha, OS.CVE_FOLIO AS folio, ");
    		   query.append("PB.NOM_CONTRATANTE AS nomContratante, 1 AS idFlujo, 'Pago de Orden de Servicio' AS desFlujo, DATE_FORMAT(PB.FEC_ALTA,'" + formatoFecha + "') AS fecPago, ");
    		   query.append("OS.ID_ESTATUS_ORDEN_SERVICIO AS idEstatus, EODS.DES_ESTATUS desEstatus, PB.CVE_ESTATUS_PAGO AS idEstatusPago, ");
    		   query.append("EPAG.DES_ESTATUS desEstatusPago, PB.ID_PAGO_BITACORA AS idPagoBitacora \n");
    		   query.append("FROM SVC_ORDEN_SERVICIO OS ");
    		   query.append("JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = OS.ID_ORDEN_SERVICIO ");
    		   query.append("JOIN SVC_ESTATUS_ORDEN_SERVICIO EODS ON EODS.ID_ESTATUS_ORDEN_SERVICIO = OS.ID_ESTATUS_ORDEN_SERVICIO ");
    		   query.append("JOIN SVC_ESTATUS_ORDEN_SERVICIO EPAG ON EPAG.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO \n");
    		   query.append("WHERE OS.ID_ESTATUS_ORDEN_SERVICIO IN (0, 4) AND PB.CVE_ESTATUS_PAGO IN (4, 5) ");
    		   query.append("AND OS.ID_ORDEN_SERVICIO = " + this.idPago);
    		   query.append(" AND PB.ID_FLUJO_PAGOS = '1' ");
    		   break;
    	   case 2:
    		   query.append("SELECT PF.ID_CONVENIO_PF AS id, DATE_FORMAT(PF.FEC_ALTA,'" + formatoFecha + "') AS fecha, PF.DES_FOLIO AS folio, ");
    	       query.append("PB.NOM_CONTRATANTE AS nomContratante, 2 AS idFlujo, 'Pago de Prevision Funeraria' AS desFlujo, DATE_FORMAT(PB.FEC_ALTA,'" + formatoFecha + "') AS fecPago, ");
    	       query.append("PF.ID_ESTATUS_CONVENIO AS idEstatus, ECPF.DES_ESTATUS desEstatus, PB.CVE_ESTATUS_PAGO AS idEstatusPago, ");
    	       query.append("EPAG.DES_ESTATUS desEstatusPago, PB.ID_PAGO_BITACORA AS idPagoBitacora \n");
    	       query.append("FROM SVT_CONVENIO_PF PF ");
    	       query.append("JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = PF.ID_CONVENIO_PF ");
    	       query.append("JOIN SVC_ESTATUS_CONVENIO_PF ECPF ON ECPF.ID_ESTATUS_CONVENIO_PF = PF.ID_ESTATUS_CONVENIO ");
    	       query.append("JOIN SVC_ESTATUS_ORDEN_SERVICIO EPAG ON EPAG.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO \n");
    	       query.append("WHERE PF.ID_ESTATUS_CONVENIO IN ('2','3') AND PB.CVE_ESTATUS_PAGO IN (4, 5) ");
    	       query.append("AND PF.ID_CONVENIO_PF = " + this.idPago);
    	       query.append(" AND PB.ID_FLUJO_PAGOS = '2' ");
    	       break;
    	   default:
    		   query.append("SELECT RPF.ID_RENOVACION_CONVENIO_PF AS id, DATE_FORMAT(RPF.FEC_ALTA,'" + formatoFecha + "') AS fecha, RPF.DES_FOLIO_ADENDA AS folio, ");
    	       query.append("PB.NOM_CONTRATANTE AS nomContratante, 3 AS idFlujo,'Pago de Renovacion Previsión Funeraria' AS desFlujo, DATE_FORMAT(PB.FEC_ALTA,'" + formatoFecha + "') AS fecPago, ");
    	       query.append("PF.ID_ESTATUS_CONVENIO AS idEstatus, ECPF.DES_ESTATUS desEstatus, PB.CVE_ESTATUS_PAGO AS idEstatusPago, ");
    	       query.append("EPAG.DES_ESTATUS desEstatusPago, PB.ID_PAGO_BITACORA AS idPagoBitacora \n");
    	       query.append("FROM SVT_RENOVACION_CONVENIO_PF RPF ");
    	       query.append("JOIN SVT_CONVENIO_PF PF ON PF.ID_CONVENIO_PF = RPF.ID_CONVENIO_PF ");
    	       query.append("JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = PF.ID_CONVENIO_PF ");
    	       query.append("JOIN SVC_ESTATUS_CONVENIO_PF ECPF ON ECPF.ID_ESTATUS_CONVENIO_PF = PF.ID_ESTATUS_CONVENIO ");
    	       query.append("JOIN SVC_ESTATUS_ORDEN_SERVICIO EPAG ON EPAG.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO \n");
    	       query.append("WHERE RPF.IND_ESTATUS = 1 AND PB.CVE_ESTATUS_PAGO IN (4, 5) ");
    	       query.append("AND RPF.ID_RENOVACION_CONVENIO_PF = " + this.idPago);
    	       query.append(" AND PB.ID_FLUJO_PAGOS = '3' ");
    	}
    	
    	String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes("UTF-8"));
    	request.getDatos().remove("idFlujo");
    	request.getDatos().remove("idPago");
		request.getDatos().put(AppConstantes.QUERY, encoded);
		
		return request;
    }
    
    public DatosRequest detallePagos(DatosRequest request, String formatoFecha, Integer idPagoBitacora) throws UnsupportedEncodingException {
    	StringBuilder query = new StringBuilder("SELECT PD.ID_PAGO_DETALLE AS idPagoDetalle, PD.ID_METODO_PAGO AS idMetodoPago, MP.DESC_METODO_PAGO AS desMetodoPago, ");
    	query.append("PD.IMP_IMPORTE AS importe, DATE_FORMAT(PD.FEC_PAGO,'" + formatoFecha + "') AS fecPago, PD.NUM_AUTORIZACION AS numAutorizacion, ");
    	query.append("PD.DES_BANCO AS desBanco, DATE_FORMAT(PD.FEC_VALE_AGF,'" + formatoFecha + "') AS fecValeAgf \n");
    	query.append("FROM SVT_PAGO_DETALLE PD ");
    	query.append("JOIN SVC_METODO_PAGO MP ON MP.ID_METODO_PAGO = PD.ID_METODO_PAGO \n");
    	query.append("WHERE PD.ID_PAGO_BITACORA = " + idPagoBitacora);
    	
    	String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes("UTF-8"));
        request.getDatos().put(AppConstantes.QUERY, encoded);
		
		return request;
    }
    
    public DatosRequest modifica(ModificaResponse modificaResponse) throws UnsupportedEncodingException {
    	DatosRequest request = new DatosRequest();
		Map<String, Object> parametro = new HashMap<>();
		String query =" UPDATE SVT_PAGO_DETALLE SET ID_USUARIO_MODIFICA = " + modificaResponse.getIdUsuarioModifica() +
				", FEC_ACTUALIZACION = CURRENT_TIMESTAMP(), DES_MOTIVO_MODIFICA = '" + modificaResponse.getMotivoModifica() + "' " +
				"WHERE ID_PAGO_DETALLE = " + modificaResponse.getIdPagoDetalle();
		
		String encoded = DatatypeConverter.printBase64Binary(query.getBytes("UTF-8"));
		parametro.put(AppConstantes.QUERY, encoded);
		request.setDatos(parametro);
		return request;
    }
    
    public DatosRequest validaCancela(DatosRequest request, Integer idPagoBitacora) throws UnsupportedEncodingException {
    	StringBuilder query = new StringBuilder("SELECT COUNT(PD.ID_PAGO_DETALLE) AS total ");
    	query.append("FROM SVT_PAGO_DETALLE PD ");
    	query.append("WHERE ID_PAGO_BITACORA = " + idPagoBitacora);
    	query.append(" AND ID_METODO_PAGO > 2");
    	
    	String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes("UTF-8"));
        request.getDatos().put(AppConstantes.QUERY, encoded);
		
		return request;
    }
    
    public Map<String, Object> generarReporte(BusquedaDto reporteDto,String nombrePdfReportes, String formatoFecha){
		Map<String, Object> envioDatos = new HashMap<>();
		StringBuilder condicion1 = new StringBuilder(" ");
		StringBuilder condicion2 = new StringBuilder(" ");
		StringBuilder condicion3 = new StringBuilder(" ");
		// ODS
		if (reporteDto.getIdVelatorio() != null) {
			condicion1.append(" AND os.ID_VELATORIO = ").append(reporteDto.getIdVelatorio());
		}
		if (reporteDto.getFolioODS() != null) {
    		condicion1.append(" AND OS.CVE_FOLIO = '").append(reporteDto.getFolioODS()).append("' ");
    	} 
    	if (reporteDto.getNomContratante() != null) {
    		condicion1.append(" AND PB.NOM_CONTRATANTE LIKE '%").append(reporteDto.getNomContratante()).append("%'");
    	}
    	if (reporteDto.getFechaIni() != null) {
    		condicion1.append(" AND OS.FEC_ALTA BETWEEN STR_TO_DATE('" + reporteDto.getFechaIni() + "','" + formatoFecha + "') AND STR_TO_DATE('" + reporteDto.getFechaFin() + "','" + formatoFecha + "')");
    	}
    	// PF
    	if (reporteDto.getIdVelatorio() != null) {
    		condicion2.append(" AND PF.ID_VELATORIO = ").append(reporteDto.getIdVelatorio());
    	}
    	if (reporteDto.getFolioPF() != null) {
    		condicion2.append(" AND PF.DES_FOLIO = '").append(reporteDto.getFolioPF()).append("' ");
    	} 
    	if (reporteDto.getNomContratante() != null) {
    		condicion2.append(" AND PB.NOM_CONTRATANTE LIKE '%").append(reporteDto.getNomContratante()).append("%'");
    	}
    	if (reporteDto.getFechaIni() != null) {
    		condicion2.append(" AND PF.FEC_ALTA BETWEEN STR_TO_DATE('" + reporteDto.getFechaIni() + "','" + formatoFecha + "') AND STR_TO_DATE('" + reporteDto.getFechaFin() + "','" + formatoFecha + "')");
    	}
    	// RPF
    	if (reporteDto.getIdVelatorio() != null) {
    		condicion3.append(" AND PF.ID_VELATORIO = ").append(reporteDto.getIdVelatorio());
    	}
    	if (reporteDto.getFolioRPF() != null) {
    		condicion3.append(" AND RPF.DES_FOLIO_ADENDA = '").append(reporteDto.getFolioRPF()).append("' ");
    	} 
    	if (reporteDto.getNomContratante() != null) {
    		condicion3.append(" AND PB.NOM_CONTRATANTE LIKE '%").append(reporteDto.getNomContratante()).append("%'");
    	}
    	if (reporteDto.getFechaIni() != null) {
    		condicion3.append(" AND RPF.FEC_ALTA BETWEEN STR_TO_DATE('" + reporteDto.getFechaIni() + "','" + formatoFecha + "') AND STR_TO_DATE('" + reporteDto.getFechaFin() + "','" + formatoFecha + "')");
    	}
    	
		envioDatos.put("condicion1", condicion1.toString());
		envioDatos.put("condicion2", condicion2.toString());
		envioDatos.put("condicion3", condicion3.toString());
		envioDatos.put("tipoReporte", reporteDto.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfReportes);
		
		return envioDatos;
	}
    
    private StringBuilder consultaOds() {
    	StringBuilder query = new StringBuilder("SELECT OS.ID_ORDEN_SERVICIO AS id, DATE_FORMAT(OS.FEC_ALTA,'" + formatoFecLocal + "') AS fecha, OS.CVE_FOLIO AS folio, ");
    	query.append("PB.NOM_CONTRATANTE AS nomContratante, 1 AS idFlujo, 'Pago de Orden de Servicio' AS desFlujo, ");
    	query.append("SUM(PD.IMP_IMPORTE) AS total, OS.ID_ESTATUS_ORDEN_SERVICIO AS idEstatus, ");
    	query.append("EODS.DES_ESTATUS desEstatus, PB.CVE_ESTATUS_PAGO AS idEstatusPago, ");
    	query.append("EPAG.DES_ESTATUS desEstatusPago, PD.ID_METODO_PAGO AS idMetodoPago, ");
    	query.append("MET.DESC_METODO_PAGO AS desMetodoPago, ");
    	query.append("CASE WHEN OS.ID_ESTATUS_ORDEN_SERVICIO = 2 THEN 1 "
    		       + "     WHEN PB.CVE_ESTATUS_PAGO = 2 THEN 1  ELSE 0 "
    			   + "END AS soloVisual \n");
    	query.append("FROM SVC_ORDEN_SERVICIO OS ");
    	query.append("JOIN SVC_VELATORIO VEL ON VEL.ID_VELATORIO = OS.ID_VELATORIO ");
    	query.append("JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = OS.ID_ORDEN_SERVICIO ");
    	query.append("JOIN SVT_PAGO_DETALLE PD ON PD.ID_PAGO_BITACORA = PB.ID_PAGO_BITACORA ");
    	query.append("JOIN SVC_ESTATUS_ORDEN_SERVICIO EODS ON EODS.ID_ESTATUS_ORDEN_SERVICIO = OS.ID_ESTATUS_ORDEN_SERVICIO ");
    	query.append("JOIN SVC_ESTATUS_ORDEN_SERVICIO EPAG ON EPAG.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO ");
    	query.append("JOIN SVC_METODO_PAGO MET ON MET.ID_METODO_PAGO = PD.ID_METODO_PAGO \n");
    	query.append("WHERE OS.ID_ESTATUS_ORDEN_SERVICIO IN (0, 2, 4) ");
    	query.append("AND PB.CVE_ESTATUS_PAGO IN (2, 4, 5) ");
    	query.append("AND PB.ID_FLUJO_PAGOS = '1' \n");
    	
    	return query;
    }
    
    private StringBuilder consultaPf() {
    	StringBuilder query = new StringBuilder("SELECT PF.ID_CONVENIO_PF AS id, DATE_FORMAT(PF.FEC_ALTA,'" + formatoFecLocal + "') AS fecha, PF.DES_FOLIO AS folio, ");
    	query.append("PB.NOM_CONTRATANTE AS nomContratante, 2 AS idFlujo, 'Pago de Prevision Funeraria' AS desFlujo, ");
    	query.append("SUM(PD.IMP_IMPORTE) AS total, PF.ID_ESTATUS_CONVENIO AS idEstatus, ");
    	query.append("ECPF.DES_ESTATUS desEstatus, PB.CVE_ESTATUS_PAGO AS idEstatusPago, ");
    	query.append("EPAG.DES_ESTATUS desEstatusPago, PD.ID_METODO_PAGO AS idMetodoPago, ");
    	query.append("MET.DESC_METODO_PAGO AS desMetodoPago, ");
    	query.append("CASE WHEN PF.ID_ESTATUS_CONVENIO = 3 THEN 1 "
    			   + "     WHEN PF.ID_ESTATUS_CONVENIO = 4 THEN 1 "
    			   + "	   WHEN PB.CVE_ESTATUS_PAGO = 2 THEN 1 "
    			   + "	   WHEN PB.CVE_ESTATUS_PAGO = 3 THEN 1 "
    			   + "	   ELSE 0 "
    			   + "END AS soloVisual \n");
    	query.append("FROM SVT_CONVENIO_PF PF ");
    	query.append("JOIN SVC_VELATORIO VEL ON VEL.ID_VELATORIO = PF.ID_VELATORIO ");
    	query.append("JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = PF.ID_CONVENIO_PF ");
    	query.append("JOIN SVT_PAGO_DETALLE PD ON PD.ID_PAGO_BITACORA = PB.ID_PAGO_BITACORA ");
    	query.append("JOIN SVC_ESTATUS_CONVENIO_PF ECPF ON ECPF.ID_ESTATUS_CONVENIO_PF = PF.ID_ESTATUS_CONVENIO ");
    	query.append("JOIN SVC_ESTATUS_ORDEN_SERVICIO EPAG ON EPAG.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO ");
    	query.append("JOIN SVC_METODO_PAGO MET ON MET.ID_METODO_PAGO = PD.ID_METODO_PAGO \n");
    	query.append("WHERE PB.CVE_ESTATUS_PAGO IN (0, 2, 3, 4, 5) ");
    	query.append("AND PB.ID_FLUJO_PAGOS = '2' \n");
    	
    	return query;
    }
    
    private StringBuilder consultaRpf() {
    	StringBuilder query = new StringBuilder("SELECT RPF.ID_RENOVACION_CONVENIO_PF AS id, DATE_FORMAT(RPF.FEC_ALTA,'" + formatoFecLocal + "') AS fecha, RPF.DES_FOLIO_ADENDA AS folio, ");
    	query.append("PB.NOM_CONTRATANTE AS nomContratante, 3 AS idFlujo,'Pago de Renovacion Previsión Funeraria' AS desFlujo, ");
    	query.append("SUM(PD.IMP_IMPORTE) AS total, PF.ID_ESTATUS_CONVENIO AS idEstatus,  ");
    	query.append("ECPF.DES_ESTATUS desEstatus, PB.CVE_ESTATUS_PAGO AS idEstatusPago, ");
    	query.append("EPAG.DES_ESTATUS desEstatusPago, PD.ID_METODO_PAGO AS idMetodoPago, ");
    	query.append("MET.DESC_METODO_PAGO AS desMetodoPago,  ");
    	query.append("CASE WHEN PF.IND_RENOVACION = 0 THEN 1 "
    			   + "	   WHEN PB.CVE_ESTATUS_PAGO = 2 THEN 1 "
    		       + "	   WHEN PB.CVE_ESTATUS_PAGO = 3 THEN 1 "
    			   + "     ELSE 0 "
    			   + "END AS soloVisual \n");
    	query.append("FROM SVT_RENOVACION_CONVENIO_PF RPF ");
    	query.append("JOIN SVT_CONVENIO_PF PF ON PF.ID_CONVENIO_PF = RPF.ID_CONVENIO_PF ");
    	query.append("JOIN SVC_VELATORIO VEL ON VEL.ID_VELATORIO = PF.ID_VELATORIO ");
    	query.append("JOIN SVT_PAGO_BITACORA PB ON RPF.ID_RENOVACION_CONVENIO_PF = PB.ID_REGISTRO ");
    	query.append("JOIN SVT_PAGO_DETALLE PD ON PD.ID_PAGO_BITACORA = PB.ID_PAGO_BITACORA ");
    	query.append("JOIN SVC_ESTATUS_CONVENIO_PF ECPF ON ECPF.ID_ESTATUS_CONVENIO_PF = PF.ID_ESTATUS_CONVENIO ");
    	query.append("JOIN SVC_ESTATUS_ORDEN_SERVICIO EPAG ON EPAG.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO ");
    	query.append("JOIN SVC_METODO_PAGO MET ON MET.ID_METODO_PAGO = PD.ID_METODO_PAGO \n");
    	query.append("WHERE RPF.IND_ESTATUS = 1 ");
    	query.append("AND PB.CVE_ESTATUS_PAGO IN (2, 3, 4, 5) ");
    	query.append("AND PB.ID_FLUJO_PAGOS = '3' \n");
    	
    	return query;
    }
    
    private String groupByOds() {
    	return " GROUP BY OS.ID_ORDEN_SERVICIO, OS.FEC_ALTA, OS.CVE_FOLIO, PB.NOM_CONTRATANTE, "
    	     + "OS.ID_ESTATUS_ORDEN_SERVICIO, PB.CVE_ESTATUS_PAGO, PD.ID_METODO_PAGO \n";
    }
    
    private String groupByPf() {
    	return " GROUP BY PF.ID_CONVENIO_PF, PF.FEC_ALTA, PF.DES_FOLIO, PB.NOM_CONTRATANTE, "
    	     + "PF.ID_ESTATUS_CONVENIO, PB.CVE_ESTATUS_PAGO, PD.ID_METODO_PAGO \n";
    }
   
    private String groupByRpf() {
    	return " GROUP BY RPF.ID_RENOVACION_CONVENIO_PF, RPF.FEC_ALTA, RPF.DES_FOLIO_ADENDA, PB.NOM_CONTRATANTE, "
    	     + "PF.ID_ESTATUS_CONVENIO, PB.CVE_ESTATUS_PAGO, PD.ID_METODO_PAGO ";
    }
    
}
