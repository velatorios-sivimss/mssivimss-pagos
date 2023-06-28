package com.imss.sivimss.pagos.beans;

import java.io.UnsupportedEncodingException;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.pagos.model.request.BusquedaDto;
import com.imss.sivimss.pagos.util.AppConstantes;
import com.imss.sivimss.pagos.util.DatosRequest;

import lombok.Builder;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Builder
public class GestionarPagos {
	
	private static final int NIVEL_VELATORIO = 3;
	
    public DatosRequest foliosOds(BusquedaDto busqueda) throws UnsupportedEncodingException {
    	DatosRequest request = new DatosRequest();
		StringBuilder query = new StringBuilder("SELECT OS.ID_ORDEN_SERVICIO AS id, OS.CVE_FOLIO AS folio, OS.ID_ESTATUS_ORDEN_SERVICIO \n");
	    query.append("FROM SVC_ORDEN_SERVICIO OS \n");
		query.append("JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = OS.ID_ORDEN_SERVICIO \n");
		query.append("WHERE OS.ID_ESTATUS_ORDEN_SERVICIO IN (0, 2, 4) \n");
		query.append("AND PB.CVE_ESTATUS_PAGO IN (2, 4, 5) \n");
		query.append("AND PB.ID_FLUJO_PAGOS = '1' ");
        if (busqueda.getIdOficina() == NIVEL_VELATORIO) {
    		query.append("AND OS.ID_VELATORIO = " + busqueda.getIdVelatorio());
    	}
	
		String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes("UTF-8"));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
    }
    
    
    public DatosRequest foliosPf(BusquedaDto busqueda) throws UnsupportedEncodingException {
    	DatosRequest request = new DatosRequest();
    	StringBuilder query = new StringBuilder("SELECT PF.ID_CONVENIO_PF AS id, PF.DES_FOLIO AS folio \n");
    	query.append("FROM SVT_CONVENIO_PF PF \n");
    	query.append("JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = PF.ID_CONVENIO_PF \n");
    	query.append("WHERE PF.ID_ESTATUS_CONVENIO IN ('2','3') \n");
    	query.append("AND PB.CVE_ESTATUS_PAGO IN (4, 5) \n");
    	query.append("AND PB.ID_FLUJO_PAGOS = '2' ");
    	if (busqueda.getIdOficina() == NIVEL_VELATORIO) {
    		query.append("AND PF.ID_VELATORIO = " + busqueda.getIdVelatorio());
    	}
    	
    	String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes("UTF-8"));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
    }
    
    public DatosRequest foliosRpf(BusquedaDto busqueda) throws UnsupportedEncodingException {
    	DatosRequest request = new DatosRequest();
    	StringBuilder query = new StringBuilder("SELECT  RPF.ID_RENOVACION_CONVENIO_PF AS id, PF.DES_FOLIO AS folio \n");
    	query.append("FROM SVT_RENOVACION_CONVENIO_PF RPF \n");
    	query.append("JOIN SVT_CONVENIO_PF PF ON PF.ID_CONVENIO_PF = RPF.ID_CONVENIO_PF \n");
    	query.append("JOIN SVT_PAGO_BITACORA PB ON RPF.ID_RENOVACION_CONVENIO_PF = PB.ID_REGISTRO \n");
    	query.append("WHERE RPF.IND_ESTATUS = 1 \n");
    	query.append("AND PB.CVE_ESTATUS_PAGO IN (0, 2, 3, 4, 5) \n");
    	query.append("AND PB.ID_FLUJO_PAGOS = '3' ");
        if (busqueda.getIdOficina() == NIVEL_VELATORIO) {
        	query.append("AND PF.ID_VELATORIO = " + busqueda.getIdVelatorio());
    	}
    	
    	String encoded = DatatypeConverter.printBase64Binary(query.toString().getBytes("UTF-8"));
		request.getDatos().put(AppConstantes.QUERY, encoded);
		return request;
    }
    
    public DatosRequest consultaPagos(DatosRequest request, BusquedaDto busqueda) throws UnsupportedEncodingException {
    	// ODS
    	StringBuilder queryCompleto = consultaOds();
    	if (busqueda.getIdOficina() == NIVEL_VELATORIO) {
    		queryCompleto.append(" AND OS.ID_VELATORIO = ").append(busqueda.getIdVelatorio());
    	}
    	queryCompleto.append(groupByOds());
    	
    	// PF
    	queryCompleto.append("UNION \n");
    	queryCompleto.append(consultaPf());
    	if (busqueda.getIdOficina() == NIVEL_VELATORIO) {
    		queryCompleto.append(" AND PF.ID_VELATORIO = ").append(busqueda.getIdVelatorio());
    	}
    	queryCompleto.append(groupByPf());
    	
    	// RPF
    	queryCompleto.append("UNION \n");
    	queryCompleto.append(consultaRpf());
    	if (busqueda.getIdOficina() == NIVEL_VELATORIO) {
    		queryCompleto.append(" AND PF.ID_VELATORIO = ").append(busqueda.getIdVelatorio());
    	}
    	queryCompleto.append(groupByRpf());
    	
    	String encoded = DatatypeConverter.printBase64Binary(queryCompleto.toString().getBytes("UTF-8"));
		request.getDatos().put(AppConstantes.QUERY, encoded);
    	
    	return request;
    }
    
    private StringBuilder consultaOds() {
    	StringBuilder query = new StringBuilder("SELECT OS.ID_ORDEN_SERVICIO AS id, OS.FEC_ALTA AS fecha, OS.CVE_FOLIO AS folio, ");
    	query.append("PB.NOM_CONTRATANTE AS nomContratante, 'Pago ODS' AS tipoPago, ");
    	query.append("SUM(PD.IMP_IMPORTE) AS total, OS.ID_ESTATUS_ORDEN_SERVICIO AS idEstatus, ");
    	query.append("EODS.DES_ESTATUS idEstausPago, PB.CVE_ESTATUS_PAGO AS idEstatusPago, ");
    	query.append("EPAG.DES_ESTATUS desEstatusPago, PD.ID_METODO_PAGO AS idMetodoPago, ");
    	query.append("MET.DESC_METODO_PAGO AS desMetodoPago, ");
    	query.append("CASE WHEN OS.ID_ESTATUS_ORDEN_SERVICIO = 2 THEN 1 "
    		       + "     WHEN PB.CVE_ESTATUS_PAGO = 2 THEN 1  ELSE 0 "
    			   + "END AS soloVisual \n");
    	query.append("FROM SVC_ORDEN_SERVICIO OS ");
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
    	StringBuilder query = new StringBuilder("");
    	query.append("SELECT PF.ID_CONVENIO_PF AS id, PF.FEC_ALTA AS fecha, PF.DES_FOLIO AS folio, ");
    	query.append("PB.NOM_CONTRATANTE AS nomContratante, 'Pago ODS' AS tipoPago, ");
    	query.append("SUM(PD.IMP_IMPORTE) AS total, PF.ID_ESTATUS_CONVENIO AS idEstatus, ");
    	query.append("ECPF.DES_ESTATUS idEstausPago, PB.CVE_ESTATUS_PAGO AS idEstatusPago, ");
    	query.append("EPAG.DES_ESTATUS desEstatusPago, PD.ID_METODO_PAGO AS idMetodoPago, ");
    	query.append("MET.DESC_METODO_PAGO AS desMetodoPago, ");
    	query.append("CASE WHEN PF.ID_ESTATUS_CONVENIO = 0 THEN 1 "
    			   + "	   WHEN PB.CVE_ESTATUS_PAGO = 2 THEN 1 "
    			   + "	   WHEN PB.CVE_ESTATUS_PAGO = 3 THEN 1 "
    			   + "	   ELSE 0 "
    			   + "END AS soloVisual \n");
    	query.append("FROM SVT_CONVENIO_PF PF ");
    	query.append("JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = PF.ID_CONVENIO_PF ");
    	query.append("JOIN SVT_PAGO_DETALLE PD ON PD.ID_PAGO_BITACORA = PB.ID_PAGO_BITACORA ");
    	query.append("JOIN SVC_ESTATUS_CONVENIO_PF ECPF ON ECPF.ID_ESTATUS_CONVENIO_PF = PF.ID_ESTATUS_CONVENIO ");
    	query.append("JOIN SVC_ESTATUS_ORDEN_SERVICIO EPAG ON EPAG.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO ");
    	query.append("JOIN SVC_METODO_PAGO MET ON MET.ID_METODO_PAGO = PD.ID_METODO_PAGO \n");
    	query.append("WHERE PF.ID_ESTATUS_CONVENIO IN ('2','3') ");
    	query.append("AND PB.CVE_ESTATUS_PAGO IN (2, 3, 4, 5) ");
    	query.append("AND PB.ID_FLUJO_PAGOS = '2' \n");
    	
    	return query;
    }
    
    private StringBuilder consultaRpf() {
    	StringBuilder query = new StringBuilder("SELECT RPF.ID_RENOVACION_CONVENIO_PF AS id, RPF.FEC_ALTA AS fecha, RPF.DES_FOLIO_ADENDA AS folio, ");
    	query.append("PB.NOM_CONTRATANTE AS nomContratante, 'Pago ODS' AS tipoPago, ");
    	query.append("SUM(PD.IMP_IMPORTE) AS total, PF.ID_ESTATUS_CONVENIO AS idEstatus,  ");
    	query.append("ECPF.DES_ESTATUS idEstausPago, PB.CVE_ESTATUS_PAGO AS idEstatusPago, ");
    	query.append("EPAG.DES_ESTATUS desEstatusPago, PD.ID_METODO_PAGO AS idMetodoPago, ");
    	query.append("MET.DESC_METODO_PAGO AS desMetodoPago,  ");
    	query.append("CASE WHEN PF.IND_RENOVACION = 0 THEN 1 "
    			   + "	   WHEN PB.CVE_ESTATUS_PAGO = 2 THEN 1 "
    		       + "	   WHEN PB.CVE_ESTATUS_PAGO = 3 THEN 1 "
    			   + "     ELSE 0 "
    			   + "END AS soloVisual \n");
    	query.append("FROM SVT_RENOVACION_CONVENIO_PF RPF ");
    	query.append("JOIN SVT_CONVENIO_PF PF ON PF.ID_CONVENIO_PF = RPF.ID_CONVENIO_PF ");
    	query.append("JOIN SVT_PAGO_BITACORA PB ON RPF.ID_RENOVACION_CONVENIO_PF = PB.ID_REGISTRO ");
    	query.append("JOIN SVT_PAGO_DETALLE PD ON PD.ID_PAGO_BITACORA = PB.ID_PAGO_BITACORA ");
    	query.append("JOIN SVC_ESTATUS_CONVENIO_PF ECPF ON ECPF.ID_ESTATUS_CONVENIO_PF = PF.ID_ESTATUS_CONVENIO ");
    	query.append("JOIN SVC_ESTATUS_ORDEN_SERVICIO EPAG ON EPAG.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO ");
    	query.append("JOIN SVC_METODO_PAGO MET ON MET.ID_METODO_PAGO = PD.ID_METODO_PAGO \n");
    	query.append("WHERE RPF.IND_ESTATUS = 1 ");
    	query.append("AND PF.ID_ESTATUS_CONVENIO IN (0, 2, 3, 4, 5) ");
    	query.append("AND PB.ID_FLUJO_PAGOS = '2' \n");
    	
    	return query;
    }
    
    private String groupByOds() {
    	return "GROUP BY OS.ID_ORDEN_SERVICIO, OS.FEC_ALTA, OS.CVE_FOLIO, PB.NOM_CONTRATANTE, "
    	     + "OS.ID_ESTATUS_ORDEN_SERVICIO, PB.CVE_ESTATUS_PAGO, PD.ID_METODO_PAGO ";
    }
    
    private String groupByPf() {
    	return "GROUP BY PF.ID_CONVENIO_PF, PF.FEC_ALTA, PF.DES_FOLIO, PB.NOM_CONTRATANTE, "
    	     + "PF.ID_ESTATUS_CONVENIO, PB.CVE_ESTATUS_PAGO, PD.ID_METODO_PAGO ";
    }
   
    private String groupByRpf() {
    	return "GROUP BY RPF.ID_RENOVACION_CONVENIO_PF, RPF.FEC_ALTA, RPF.DES_FOLIO_ADENDA, PB.NOM_CONTRATANTE, "
    	     + "PF.ID_ESTATUS_CONVENIO, PB.CVE_ESTATUS_PAGO, PD.ID_METODO_PAGO ";
    }
}
