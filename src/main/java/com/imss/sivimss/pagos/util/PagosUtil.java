package com.imss.sivimss.pagos.util;

import com.imss.sivimss.pagos.model.request.FiltroRequest;

public class PagosUtil {
	
	private static String CONSULTA_INI = "SELECT \r\n"
			+ "T.*,\r\n"
			+ "(T.total - T.totalPagado) AS diferenciasTotales\r\n"
			+ "FROM \r\n"
			+ "(SELECT\r\n"
			+ "PB.ID_PAGO_BITACORA AS idPagoBitacora,\r\n"
			+ "PB.FEC_ODS AS fechaPago,\r\n"
			+ "PB.CVE_FOLIO AS folio,\r\n"
			+ "CONCAT('Pago de ' , FP.DESC_FLUJO_PAGOS) AS tipoPago,\r\n"
			+ "CAST(PB.DESC_VALOR AS double) AS total,\r\n"
			+ "IFNULL( (SELECT SUM(PD.IMP_IMPORTE)\r\n"
			+ "FROM  SVT_PAGO_DETALLE PD \r\n"
			+ "WHERE \r\n"
			+ "PD.ID_PAGO_BITACORA = idPagoBitacora), 0) AS totalPagado\r\n"
			+ "FROM SVT_PAGO_BITACORA PB\r\n"
			+ "INNER JOIN SVC_FLUJO_PAGOS FP ON FP.ID_FLUJO_PAGOS = PB.ID_FLUJO_PAGOS\r\n";
	
	public String consultaTabla(FiltroRequest filtros) {
		
		StringBuilder query = new StringBuilder("");
		query.append( "SELECT\r\n"
				+ "T.*\r\n"
				+ "FROM\r\n"
				+ "(\r\n"
				+ "( SELECT \r\n"
				+ "PB.ID_PAGO_BITACORA AS idPagoBitacora, \r\n"
				+ "PB.FEC_ODS AS fecha, \r\n"
				+ "PB.CVE_FOLIO AS folio, \r\n"
				+ "PB.NOM_CONTRATANTE AS nomContratante, \r\n"
				+ "PB.ID_FLUJO_PAGOS AS idFlujoPagos, \r\n"
				+ "FP.DESC_FLUJO_PAGOS AS tipoPago,\r\n"
				+ "(SELECT \r\n"
				+ "GROUP_CONCAT( MP.DESC_METODO_PAGO SEPARATOR ', ' )\r\n"
				+ "FROM SVC_METODO_PAGO MP\r\n"
				+ "INNER JOIN SVT_PAGO_DETALLE PD ON PD.ID_METODO_PAGO = MP.ID_METODO_PAGO\r\n"
				+ "WHERE PD.ID_PAGO_BITACORA = idPagoBitacora\r\n"
				+ " ) AS metodoPago,\r\n"
				+ "PB.DESC_VALOR AS total, \r\n"
				+ "EOS.DES_ESTATUS AS estatus, \r\n"
				+ "EOSP.DES_ESTATUS AS estatusPago \r\n"
				+ "FROM SVT_PAGO_BITACORA PB \r\n"
				+ "INNER JOIN SVC_FLUJO_PAGOS FP ON FP.ID_FLUJO_PAGOS = PB.ID_FLUJO_PAGOS \r\n"
				+ "INNER JOIN SVC_ORDEN_SERVICIO OS ON OS.ID_ORDEN_SERVICIO = PB.ID_REGISTRO \r\n"
				+ "INNER JOIN SVC_ESTATUS_ORDEN_SERVICIO EOS ON EOS.ID_ESTATUS_ORDEN_SERVICIO = OS.ID_ESTATUS_ORDEN_SERVICIO \r\n"
				+ "INNER JOIN SVC_ESTATUS_ORDEN_SERVICIO EOSP ON EOSP.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO\r\n"
				+ "WHERE \r\n"
				+ "OS.ID_ESTATUS_ORDEN_SERVICIO IN (0,2)\r\n"
				+ "AND PB.ID_FLUJO_PAGOS = '1' )\r\n"
				+ "UNION ALL\r\n"
				+ "(\r\n"
				+ "SELECT \r\n"
				+ "PB.ID_PAGO_BITACORA AS idPagoBitacora, \r\n"
				+ "PB.FEC_ODS AS fecha, \r\n"
				+ "PB.CVE_FOLIO AS folio, \r\n"
				+ "PB.NOM_CONTRATANTE AS nomContratante, \r\n"
				+ "PB.ID_FLUJO_PAGOS AS idFlujoPagos,\r\n"
				+ "FP.DESC_FLUJO_PAGOS AS tipoPago,\r\n"
				+ "(SELECT \r\n"
				+ "GROUP_CONCAT( MP.DESC_METODO_PAGO SEPARATOR ', ' )\r\n"
				+ "FROM SVC_METODO_PAGO MP\r\n"
				+ "INNER JOIN SVT_PAGO_DETALLE PD ON PD.ID_METODO_PAGO = MP.ID_METODO_PAGO\r\n"
				+ "WHERE PD.ID_PAGO_BITACORA = idPagoBitacora\r\n"
				+ " ) AS metodoPago,\r\n"
				+ "PB.DESC_VALOR AS total, \r\n"
				+ "ECPF.DES_ESTATUS AS estatus, \r\n"
				+ "EOSP.DES_ESTATUS AS estatusPago \r\n"
				+ "FROM SVT_PAGO_BITACORA PB \r\n"
				+ "INNER JOIN SVC_FLUJO_PAGOS FP ON FP.ID_FLUJO_PAGOS = PB.ID_FLUJO_PAGOS\r\n"
				+ "INNER JOIN SVT_CONVENIO_PF PF ON PF.ID_CONVENIO_PF =PB.ID_REGISTRO\r\n"
				+ "INNER JOIN SVC_ESTATUS_CONVENIO_PF ECPF ON ECPF.ID_ESTATUS_CONVENIO_PF = PF.ID_ESTATUS_CONVENIO\r\n"
				+ "INNER JOIN SVC_ESTATUS_ORDEN_SERVICIO EOSP ON EOSP.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO\r\n"
				+ "WHERE\r\n"
				+ "PF.ID_ESTATUS_CONVENIO = '1'\r\n"
				+ "AND PB.ID_FLUJO_PAGOS = '2'\r\n"
				+ ")\r\n"
				+ "UNION ALL\r\n"
				+ "(\r\n"
				+ "SELECT \r\n"
				+ "PB.ID_PAGO_BITACORA AS idPagoBitacora, \r\n"
				+ "PB.FEC_ODS AS fecha, \r\n"
				+ "PB.CVE_FOLIO AS folio, \r\n"
				+ "PB.NOM_CONTRATANTE AS nomContratante, \r\n"
				+ "PB.ID_FLUJO_PAGOS AS idFlujoPagos,\r\n"
				+ "FP.DESC_FLUJO_PAGOS AS tipoPago,\r\n"
				+ "(SELECT \r\n"
				+ "GROUP_CONCAT( MP.DESC_METODO_PAGO SEPARATOR ', ' )\r\n"
				+ "FROM SVC_METODO_PAGO MP\r\n"
				+ "INNER JOIN SVT_PAGO_DETALLE PD ON PD.ID_METODO_PAGO = MP.ID_METODO_PAGO\r\n"
				+ "WHERE PD.ID_PAGO_BITACORA = idPagoBitacora\r\n"
				+ " ) AS metodoPago,\r\n"
				+ "PB.DESC_VALOR AS total, \r\n"
				+ "'vigente' AS estatus, \r\n"
				+ "EOSP.DES_ESTATUS AS estatusPago \r\n"
				+ "FROM SVT_PAGO_BITACORA PB \r\n"
				+ "INNER JOIN SVC_FLUJO_PAGOS FP ON FP.ID_FLUJO_PAGOS = PB.ID_FLUJO_PAGOS \r\n"
				+ "INNER JOIN SVC_ESTATUS_ORDEN_SERVICIO EOSP ON EOSP.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO\r\n"
				+ "INNER JOIN SVT_RENOVACION_CONVENIO_PF RPF ON RPF.ID_RENOVACION_CONVENIO_PF = PB.ID_REGISTRO\r\n"
				+ "WHERE \r\n"
				+ "RPF.IND_ESTATUS = '1'\r\n"
				+ "AND PB.ID_FLUJO_PAGOS = '3'\r\n"
				+ ")\r\n"
				+ ")\r\n"
				+ "T"
				);
		if( validarWhere(filtros) ) {
			
			query.append( "WHERE " );
		
			if( (filtros.getIdVelatorio()!=null && !filtros.getIdVelatorio().isEmpty() )) {
				query.append( "T.ID_VELATORIO = '" + filtros.getIdVelatorio() + "' " );
			}
			
			if( (filtros.getIdFlujoPagos()!=null && !filtros.getIdFlujoPagos().isEmpty() )) {
				query.append( "AND T.ID_FLUJO_PAGOS = '" + filtros.getIdFlujoPagos() +"' " );
			}
			
			if( (filtros.getFolio()!=null && !filtros.getFolio().isEmpty() )) {
				query.append( "AND T.CVE_FOLIO LIKE CONCAT('" + filtros.getFolio() + "', '%') " );
			}
			
			if( (filtros.getNomContratante()!=null && !filtros.getNomContratante().isEmpty() ) ) {
				query.append( "AND T.NOM_CONTRATANTE LIKE CONCAT('"+ filtros.getNomContratante() + "', '%') " );
			}
			
			if( (filtros.getFechaInicio()!=null && !filtros.getFechaInicio().isEmpty() ) ) {
				query.append( "AND T.FEC_ODS BETWEEN '" + filtros.getFechaInicio() + "' AND '" + filtros.getFechaFin() + "' " );
			}
			
		}
		
		return query.toString();
	}
	
	private Boolean validarWhere(FiltroRequest filtros) {
		
		if( (filtros.getIdVelatorio()==null || filtros.getIdVelatorio().isEmpty() )
				&& (filtros.getIdFlujoPagos()==null || filtros.getIdFlujoPagos().isEmpty() ) 
				&& (filtros.getFolio()==null || filtros.getFolio().isEmpty() )
				&& (filtros.getNomContratante()==null || filtros.getNomContratante().isEmpty() )
				&& (filtros.getFechaInicio()==null || filtros.getFechaInicio().isEmpty() )
				&& (filtros.getFechaFin()==null || filtros.getFechaFin().isEmpty() )
				) {
			
			return false;
		
		}
		
		return true;
	}
	
	public String tablaTotales(Integer idFlujo) {
		
		String query = CONSULTA_INI;
		
		switch(idFlujo) {
		case 1: query = query + "INNER JOIN SVC_ORDEN_SERVICIO OS ON OS.ID_ORDEN_SERVICIO = PB.ID_REGISTRO\r\n"
				+ "WHERE\r\n"
				+ "PB.ID_FLUJO_PAGOS = '1'\r\n"
				+ "AND OS.ID_ESTATUS_ORDEN_SERVICIO IN (0,2)\r\n"
				+ ") T";
		break;
		case 2: query = query + "INNER JOIN SVT_CONVENIO_PF PF ON PF.ID_CONVENIO_PF =PB.ID_REGISTRO\r\n"
				+ "INNER JOIN SVC_ESTATUS_CONVENIO_PF ECPF ON ECPF.ID_ESTATUS_CONVENIO_PF = PF.ID_ESTATUS_CONVENIO\r\n"
				+ "WHERE\r\n"
				+ "PB.ID_FLUJO_PAGOS = '2'\r\n"
				+ "AND PF.ID_ESTATUS_CONVENIO = '1'\r\n"
				+ ") T";
		break;
		default:query = query + "INNER JOIN SVT_RENOVACION_CONVENIO_PF RPF ON RPF.ID_RENOVACION_CONVENIO_PF = PB.ID_REGISTRO\r\n"
				+ "WHERE\r\n"
				+ "RPF.IND_ESTATUS = '1'\r\n"
				+ "AND PB.ID_FLUJO_PAGOS = '3'\r\n"
				+ ") T";
		}
		
		return query;
		
	}
	
	public String foliosOds() {
		
		String query = "SELECT\r\n"
				+ "OS.ID_ORDEN_SERVICIO AS id,\r\n"
				+ "OS.CVE_FOLIO AS folio\r\n"
				+ "FROM SVC_ORDEN_SERVICIO OS\r\n"
				+ "INNER JOIN SVC_ESTATUS_ORDEN_SERVICIO EOS ON EOS.ID_ESTATUS_ORDEN_SERVICIO = OS.ID_ESTATUS_ORDEN_SERVICIO\r\n"
				+ "INNER JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = OS.ID_ORDEN_SERVICIO \r\n"
				+ "WHERE\r\n"
				+ "OS.ID_ESTATUS_ORDEN_SERVICIO IN (0,2)\r\n"
				+ "AND PB.ID_FLUJO_PAGOS = '1' \r\n"
				+ "ORDER BY OS.FEC_ALTA ASC";
		return query;
		
	}

	
	public String foliosPf() {
		
		String query = "SELECT\r\n"
				+ "PF.ID_CONVENIO_PF AS id,\r\n"
				+ "PF.DES_FOLIO AS folio\r\n"
				+ "FROM\r\n"
				+ "SVT_CONVENIO_PF PF\r\n"
				+ "INNER JOIN SVC_ESTATUS_CONVENIO_PF EPF ON EPF.ID_ESTATUS_CONVENIO_PF = PF.ID_ESTATUS_CONVENIO\r\n"
				+ "INNER JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO =PF.ID_CONVENIO_PF \r\n"
				+ "WHERE\r\n"
				+ "PF.ID_ESTATUS_CONVENIO = '1'\r\n"
				+ "AND PB.ID_FLUJO_PAGOS = '2' \r\n"
				+ "ORDER BY PF.FEC_ALTA ASC";
		return query;
		
	}
	public String foliosRpf() {
		
		String query = "SELECT\r\n"
				+ "RPF.ID_RENOVACION_CONVENIO_PF AS id,\r\n"
				+ "PF.DES_FOLIO AS folio\r\n"
				+ "FROM\r\n"
				+ "SVT_RENOVACION_CONVENIO_PF RPF\r\n"
				+ "INNER JOIN SVT_CONVENIO_PF PF ON PF.ID_CONVENIO_PF = RPF.ID_CONVENIO_PF\r\n"
				+ "INNER JOIN SVT_PAGO_BITACORA PB ON RPF.ID_RENOVACION_CONVENIO_PF = PB.ID_REGISTRO \r\n"
				+ "WHERE\r\n"
				+ "RPF.IND_ESTATUS = '1'\r\n"
				+ "AND PB.ID_FLUJO_PAGOS = '3' \r\n"
				+ "ORDER BY RPF.FEC_ALTA ASC;";
		return query;
		
	}
	
}
