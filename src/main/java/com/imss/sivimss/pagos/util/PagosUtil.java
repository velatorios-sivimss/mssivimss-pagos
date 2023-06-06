package com.imss.sivimss.pagos.util;

import com.imss.sivimss.pagos.model.request.FiltroRequest;

public class PagosUtil {
	
	private static String CONSULTA = "";
	
	
	public String consultaTabla(FiltroRequest filtros) {
		
		StringBuilder query = new StringBuilder("");
		query.append( "SELECT "
				+ "PB.ID_PAGO_BITACORA AS idPagoBitacora, "
				+ "PB.FEC_ODS AS fecha, "
				+ "PB.CVE_FOLIO AS folio, "
				+ "PB.NOM_CONTRATANTE AS nomContratante, "
				+ "PB.ID_FLUJO_PAGOS, "
				+ "CONCAT('Pago de ' , FP.DESC_FLUJO_PAGOS) AS tipoPago, "
				+ "PB.DESC_VALOR AS total, "
				+ "EOS.DES_ESTATUS AS estatus, "
				+ "EOSP.DES_ESTATUS AS estatusPago "
				+ "FROM SVT_PAGO_BITACORA PB "
				+ "INNER JOIN SVC_FLUJO_PAGOS FP ON FP.ID_FLUJO_PAGOS = PB.ID_FLUJO_PAGOS "
				+ "LEFT JOIN SVC_ORDEN_SERVICIO OS ON OS.ID_ORDEN_SERVICIO = PB.ID_REGISTRO "
				+ "LEFT JOIN SVC_ESTATUS_ORDEN_SERVICIO EOS ON EOS.ID_ESTATUS_ORDEN_SERVICIO = OS.ID_ESTATUS_ORDEN_SERVICIO "
				+ "LEFT JOIN SVC_ESTATUS_ORDEN_SERVICIO EOSP ON EOSP.ID_ESTATUS_ORDEN_SERVICIO = PB.CVE_ESTATUS_PAGO "
				);
		if( validarWhere(filtros) ) {
			
			query.append( "WHERE " );
		
			if( (filtros.getIdVelatorio()!=null && !filtros.getIdVelatorio().isEmpty() )) {
				query.append( "OS.ID_VELATORIO = '" + filtros.getIdVelatorio() + "' " );
			}
			
			if( (filtros.getIdFlujoPagos()!=null && !filtros.getIdFlujoPagos().isEmpty() )) {
				query.append( "AND PB.ID_FLUJO_PAGOS = '" + filtros.getIdFlujoPagos() +"' " );
			}
			
			if( (filtros.getFolio()!=null && !filtros.getFolio().isEmpty() )) {
				query.append( "AND PB.CVE_FOLIO LIKE CONCAT('" + filtros.getFolio() + "', '%') " );
			}
			
			if( (filtros.getNomContratante()!=null && !filtros.getNomContratante().isEmpty() ) ) {
				query.append( "AND PB.NOM_CONTRATANTE LIKE CONCAT('"+ filtros.getNomContratante() + "', '%') " );
			}
			
			if( (filtros.getFechaInicio()!=null && !filtros.getFechaInicio().isEmpty() ) ) {
				query.append( "AND PB.FEC_ODS BETWEEN '" + filtros.getFechaInicio() + "' AND '" + filtros.getFechaFin() + "' " );
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

}
