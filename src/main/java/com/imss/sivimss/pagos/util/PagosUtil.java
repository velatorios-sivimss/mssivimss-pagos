package com.imss.sivimss.pagos.util;

import java.util.HashMap;
import java.util.Map;

import com.imss.sivimss.pagos.model.request.CrearRequest;
import com.imss.sivimss.pagos.model.request.FiltroRequest;

public class PagosUtil {
	
	private static String CONSULTA_INI = "SELECT \r\n"
			+ "T.*,\r\n"
			+ "(T.total - T.totalPagado) AS diferenciasTotales\r\n"
			+ "FROM \r\n"
			+ "(SELECT\r\n"
			+ "PB.ID_PAGO_BITACORA AS idPagoBitacora,\r\n"
			+ "PB.ID_FLUJO_PAGOS AS idFlujoPago,\r\n"
			+ "PB.ID_REGISTRO AS idRegistro,\r\n"
			+ "PB.FEC_ODS AS fechaPago,\r\n"
			+ "PB.CVE_FOLIO AS folio,\r\n"
			+ "FP.DESC_FLUJO_PAGOS AS tipoPago,\r\n"
			+ "CAST(PB.DESC_VALOR AS double) AS total,\r\n"
			+ "IFNULL( (SELECT SUM(PD.IMP_PAGO)\r\n"
			+ "FROM  SVT_PAGO_DETALLE PD \r\n"
			+ "WHERE \r\n"
			+ "PD.ID_PAGO_BITACORA = idPagoBitacora "
			+ "AND PD.CVE_ESTATUS = '4'), 0) AS totalPagado\r\n"
			+ "FROM SVT_PAGO_BITACORA PB\r\n"
			+ "INNER JOIN SVC_FLUJO_PAGOS FP ON FP.ID_FLUJO_PAGOS = PB.ID_FLUJO_PAGOS\r\n";
	
	private static String CONSULTA_ODS = "SELECT \r\n"
			+ "T.*,\r\n"
			+ "(T.total - T.totalPagado) AS diferenciasTotales\r\n"
			+ "FROM \r\n"
			+ "(SELECT\r\n"
			+ "PB.ID_PAGO_BITACORA AS idPagoBitacora,\r\n"
			+ "PB.ID_FLUJO_PAGOS AS idFlujoPago,\r\n"
			+ "PB.ID_REGISTRO AS idRegistro,\r\n"
			+ "PB.FEC_ODS AS fechaPago,\r\n"
			+ "PB.CVE_FOLIO AS folio,\r\n"
			+ "FP.DESC_FLUJO_PAGOS AS tipoPago,\r\n"
			+ "CAST(PB.DESC_VALOR AS double) AS total,\r\n"
			+ "IFNULL( (SELECT SUM(PD.IMP_PAGO)\r\n"
			+ "FROM  SVT_PAGO_DETALLE PD \r\n"
			+ "WHERE \r\n"
			+ "PD.ID_PAGO_BITACORA = idPagoBitacora AND PD.CVE_ESTATUS = '4'), 0) AS totalPagado,\r\n"
			+ "PB.GEN_PAGARE AS generarPagare,\r\n"
			+ "EP.DES_ESTATUS AS estatusPago,\r\n"
			+ "(SELECT COUNT( PD.ID_PAGO_DETALLE )\r\n"
			+ "FROM SVT_PAGO_BITACORA PB\r\n"
			+ "INNER JOIN SVT_PAGO_DETALLE PD ON PD.ID_PAGO_BITACORA = PB.ID_PAGO_BITACORA\r\n"
			+ "WHERE\r\n"
			+ "PD.ID_METODO_PAGO = 1\r\n"
			+ "AND PD.CVE_ESTATUS = 4\r\n"
			+ "AND PB.ID_REGISTRO = idRegistro\r\n"
			+ ") AS valeP,\r\n"
			+ "(SELECT\r\n"
			+ "PER.CVE_NSS\r\n"
			+ "FROM SVC_ORDEN_SERVICIO OS\r\n"
			+ "INNER JOIN SVC_CONTRATANTE CON ON CON.ID_CONTRATANTE = OS.ID_CONTRATANTE\r\n"
			+ "INNER JOIN SVC_PERSONA PER ON PER.ID_PERSONA = CON.ID_PERSONA\r\n"
			+ "WHERE\r\n"
			+ "OS.ID_ORDEN_SERVICIO = idRegistro ) AS nss,\r\n"
			+ "(SELECT\r\n"
			+ "ID_FINADO\r\n"
			+ "FROM SVC_FINADO\r\n"
			+ "WHERE\r\n"
			+ "ID_ORDEN_SERVICIO = idRegistro \r\n"
			+ "LIMIT 1) AS idFinado\r\n"
			+ "FROM SVT_PAGO_BITACORA PB\r\n"
			+ "INNER JOIN SVC_FLUJO_PAGOS FP ON FP.ID_FLUJO_PAGOS = PB.ID_FLUJO_PAGOS\r\n"
			+ "INNER JOIN SVC_ORDEN_SERVICIO OS ON OS.ID_ORDEN_SERVICIO = PB.ID_REGISTRO\r\n"
			+ "INNER JOIN SVC_ESTATUS_PAGO EP ON EP.ID_ESTATUS_PAGO = PB.CVE_ESTATUS_PAGO\r\n"
			+ "WHERE\r\n"
			+ "PB.ID_FLUJO_PAGOS = '1'\r\n"
			+ "AND OS.ID_ESTATUS_ORDEN_SERVICIO IN (0,2)\r\n"
			+ ") T"; 
	
	private static String CONSULTA_TABLA = "SELECT\r\n"
			+ "T.*\r\n"
			+ "FROM\r\n"
			+ "(\r\n"
			+ "( SELECT \r\n"
			+ "PB.ID_PAGO_BITACORA AS idPagoBitacora, \r\n"
			+ "PB.ID_VELATORIO AS idVelatorio, \r\n"
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
			+ "AND PD.CVE_ESTATUS = '4' \r\n"
			+ " ) AS metodoPago,\r\n"
			+ "PB.DESC_VALOR AS total, \r\n"
			+ "EOS.DES_ESTATUS AS estatus, \r\n"
			+ "EOSP.DES_ESTATUS AS estatusPago,\r\n"
			+ "PB.GEN_PAGARE AS generarPagare\r\n"
			+ "FROM SVT_PAGO_BITACORA PB \r\n"
			+ "INNER JOIN SVC_FLUJO_PAGOS FP ON FP.ID_FLUJO_PAGOS = PB.ID_FLUJO_PAGOS \r\n"
			+ "INNER JOIN SVC_ORDEN_SERVICIO OS ON OS.ID_ORDEN_SERVICIO = PB.ID_REGISTRO \r\n"
			+ "INNER JOIN SVC_ESTATUS_ORDEN_SERVICIO EOS ON EOS.ID_ESTATUS_ORDEN_SERVICIO = OS.ID_ESTATUS_ORDEN_SERVICIO \r\n"
			+ "INNER JOIN SVC_ESTATUS_PAGO EOSP ON EOSP.ID_ESTATUS_PAGO = PB.CVE_ESTATUS_PAGO\r\n"
			+ "WHERE \r\n"
			+ "OS.ID_ESTATUS_ORDEN_SERVICIO IN (0,2,4)\r\n"
			+ "AND PB.ID_FLUJO_PAGOS = '1' )\r\n"
			+ "UNION ALL\r\n"
			+ "(\r\n"
			+ "SELECT \r\n"
			+ "PB.ID_PAGO_BITACORA AS idPagoBitacora, \r\n"
			+ "PB.ID_VELATORIO AS idVelatorio, \r\n"
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
			+ "AND PD.CVE_ESTATUS = '4' \r\n"
			+ " ) AS metodoPago,\r\n"
			+ "PB.DESC_VALOR AS total, \r\n"
			+ "ECPF.DES_ESTATUS AS estatus, \r\n"
			+ "EOSP.DES_ESTATUS AS estatusPago,\r\n"
			+ "0 AS generarPagare\r\n"
			+ "FROM SVT_PAGO_BITACORA PB \r\n"
			+ "INNER JOIN SVC_FLUJO_PAGOS FP ON FP.ID_FLUJO_PAGOS = PB.ID_FLUJO_PAGOS\r\n"
			+ "INNER JOIN SVT_CONVENIO_PF PF ON PF.ID_CONVENIO_PF =PB.ID_REGISTRO\r\n"
			+ "INNER JOIN SVC_ESTATUS_CONVENIO_PF ECPF ON ECPF.ID_ESTATUS_CONVENIO_PF = PF.ID_ESTATUS_CONVENIO\r\n"
			+ "INNER JOIN SVC_ESTATUS_PAGO EOSP ON EOSP.ID_ESTATUS_PAGO = PB.CVE_ESTATUS_PAGO\r\n"
			+ "WHERE\r\n"
			+ "PF.ID_ESTATUS_CONVENIO IN (1,2)\r\n"
			+ "AND PB.ID_FLUJO_PAGOS = '2'\r\n"
			+ ")\r\n"
			+ "UNION ALL\r\n"
			+ "(\r\n"
			+ "SELECT \r\n"
			+ "PB.ID_PAGO_BITACORA AS idPagoBitacora, \r\n"
			+ "PB.ID_VELATORIO AS idVelatorio, \r\n"
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
			+ "AND PD.CVE_ESTATUS = '4' \r\n"
			+ " ) AS metodoPago,\r\n"
			+ "PB.DESC_VALOR AS total, \r\n"
			+ "'Vigente' AS estatus, \r\n"
			+ "EOSP.DES_ESTATUS AS estatusPago,\r\n"
			+ "0 AS generarPagare \r\n"
			+ "FROM SVT_PAGO_BITACORA PB \r\n"
			+ "INNER JOIN SVC_FLUJO_PAGOS FP ON FP.ID_FLUJO_PAGOS = PB.ID_FLUJO_PAGOS \r\n"
			+ "INNER JOIN SVC_ESTATUS_PAGO EOSP ON EOSP.ID_ESTATUS_PAGO = PB.CVE_ESTATUS_PAGO\r\n"
			+ "INNER JOIN SVT_RENOVACION_CONVENIO_PF RPF ON RPF.ID_RENOVACION_CONVENIO_PF = PB.ID_REGISTRO\r\n"
			+ "WHERE \r\n"
			+ "RPF.ID_ESTATUS IN (1,2)\r\n"
			+ "AND PB.ID_FLUJO_PAGOS = '3'\r\n"
			+ ")\r\n"
			+ ")\r\n"
			+ "T";
	
	public String consultaTabla(FiltroRequest filtros) {
		
		StringBuilder query = new StringBuilder(CONSULTA_TABLA);
		
		if( validarWhere(filtros) ) {
			query.append( construyeFiltros(filtros) );
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
		case 1: query = CONSULTA_ODS;
		break;
		case 2: query = query + "INNER JOIN SVT_CONVENIO_PF PF ON PF.ID_CONVENIO_PF =PB.ID_REGISTRO\r\n"
				+ "INNER JOIN SVC_ESTATUS_CONVENIO_PF ECPF ON ECPF.ID_ESTATUS_CONVENIO_PF = PF.ID_ESTATUS_CONVENIO\r\n"
				+ "WHERE\r\n"
				+ "PB.ID_FLUJO_PAGOS = '2'\r\n"
				+ "AND PF.ID_ESTATUS_CONVENIO IN (1,2)\r\n"
				+ ") T";
		break;
		default:query = query + "INNER JOIN SVT_RENOVACION_CONVENIO_PF RPF ON RPF.ID_RENOVACION_CONVENIO_PF = PB.ID_REGISTRO\r\n"
				+ "WHERE\r\n"
				+ "RPF.ID_ESTATUS IN (1,2)\r\n"
				+ "AND PB.ID_FLUJO_PAGOS = '3'\r\n"
				+ ") T";
		}
		
		return query;
		
	}
	
	public String foliosOds() {
		
		String query = "SELECT\r\n"
				+ "OS.ID_ORDEN_SERVICIO AS id,\r\n"
				+ "OS.CVE_FOLIO AS folio,\r\n"
				+ "(SELECT COUNT( PD.ID_PAGO_DETALLE )\r\n"
				+ "FROM SVT_PAGO_BITACORA PB\r\n"
				+ "INNER JOIN SVT_PAGO_DETALLE PD ON PD.ID_PAGO_BITACORA = PB.ID_PAGO_BITACORA\r\n"
				+ "WHERE\r\n"
				+ "PD.ID_METODO_PAGO = 1\r\n"
				+ "AND PD.CVE_ESTATUS = 4\r\n"
				+ "AND PB.ID_REGISTRO = id\r\n"
				+ ") AS valeP,\r\n"
				+ "(SELECT\r\n"
				+ "PER.CVE_NSS\r\n"
				+ "FROM SVC_ORDEN_SERVICIO OS\r\n"
				+ "INNER JOIN SVC_CONTRATANTE CON ON CON.ID_CONTRATANTE = OS.ID_CONTRATANTE\r\n"
				+ "INNER JOIN SVC_PERSONA PER ON PER.ID_PERSONA = CON.ID_PERSONA\r\n"
				+ "WHERE\r\n"
				+ "OS.ID_ORDEN_SERVICIO = id ) AS nss\r\n"
				+ "FROM SVC_ORDEN_SERVICIO OS\r\n"
				+ "INNER JOIN SVC_ESTATUS_ORDEN_SERVICIO EOS ON EOS.ID_ESTATUS_ORDEN_SERVICIO = OS.ID_ESTATUS_ORDEN_SERVICIO\r\n"
				+ "INNER JOIN SVT_PAGO_BITACORA PB ON PB.ID_REGISTRO = OS.ID_ORDEN_SERVICIO \r\n"
				+ "WHERE\r\n"
				+ "OS.ID_ESTATUS_ORDEN_SERVICIO IN (0,2,4)\r\n"
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
				+ "PF.ID_ESTATUS_CONVENIO IN (1,2)\r\n"
				+ "AND PB.ID_FLUJO_PAGOS = '2' \r\n"
				+ "ORDER BY PF.FEC_ALTA ASC";
		return query;
		
	}
	public String foliosRpf() {
		
		String query = "SELECT\r\n"
				+ "RPF.ID_RENOVACION_CONVENIO_PF AS id,\r\n"
				+ "RPF.DES_FOLIO_ADENDA AS folio\r\n"
				+ "FROM\r\n"
				+ "SVT_RENOVACION_CONVENIO_PF RPF\r\n"
				+ "INNER JOIN SVT_CONVENIO_PF PF ON PF.ID_CONVENIO_PF = RPF.ID_CONVENIO_PF\r\n"
				+ "INNER JOIN SVT_PAGO_BITACORA PB ON RPF.ID_RENOVACION_CONVENIO_PF = PB.ID_REGISTRO \r\n"
				+ "WHERE\r\n"
				+ "RPF.ID_ESTATUS IN (1,2)\r\n"
				+ "AND PB.ID_FLUJO_PAGOS = '3' \r\n"
				+ "ORDER BY RPF.FEC_ALTA ASC;";
		return query;
		
	}
	
	public String crearDetalle(CrearRequest datos, Integer idUsuario) {
		
		QueryHelper q = new QueryHelper("INSERT INTO SVT_PAGO_DETALLE");
		q.agregarParametroValues("ID_PAGO_BITACORA", "'" + datos.getIdPagoBitacora() + "'");
		q.agregarParametroValues("ID_METODO_PAGO", "'" + datos.getIdMetodoPago() + "'");
		q.agregarParametroValues("IMP_PAGO", datos.getImportePago().toString());
		
		if( datos.getNumAutorizacion()!= null ) {
			q.agregarParametroValues("NUM_AUTORIZACION", "'" + datos.getNumAutorizacion() + "'");
		}
		
		if( datos.getDescBanco() !=null ) {
			q.agregarParametroValues("REF_BANCO", "'" + datos.getDescBanco() + "'");
		}
		
		if( datos.getFechaPago() != null ) {	
			q.agregarParametroValues("FEC_PAGO", "'" + datos.getFechaPago() + "'" );
		}
		
		if( datos.getFechaValeAGF() != null ) {
			q.agregarParametroValues("FEC_VALE_AGF", "'" + datos.getFechaValeAGF() + "'");
		}
		
		q.agregarParametroValues("CVE_ESTATUS", "4" );
		q.agregarParametroValues("ID_USUARIO_ALTA", idUsuario.toString() );
		
		return q.obtenerQueryInsertar();
		
	}
	
	public String totalPagado(String idPagoBitacora){
		
		StringBuilder query = new StringBuilder("SELECT IFNULL(\r\n"
				+ "(SELECT SUM(PD.IMP_PAGO)\r\n"
				+ "FROM  SVT_PAGO_DETALLE PD\r\n"
				+ "WHERE\r\n"
				+ "PD.ID_PAGO_BITACORA = ");
		query.append(idPagoBitacora);
		query.append(" AND PD.CVE_ESTATUS = '4' ), 0) AS totalPagado");
		
		return query.toString();
	}
	
	public String actODs(String idOds, Integer idUsuario) {
		
		QueryHelper q = new QueryHelper("UPDATE SVC_ORDEN_SERVICIO");
		q.agregarParametroValues("ID_ESTATUS_ORDEN_SERVICIO", "4");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NOW()");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", idUsuario.toString());
		q.addWhere("ID_ORDEN_SERVICIO = " + idOds);
		q.addWhere("AND ID_ESTATUS_ORDEN_SERVICIO IN ('2','3')");
		
		return q.obtenerQueryActualizar();
	}
	
	public String actConPF(String idOds, Integer idUsuario) {
		
		QueryHelper q = new QueryHelper("UPDATE SVT_CONVENIO_PF");
		q.agregarParametroValues("ID_ESTATUS_CONVENIO", "2");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NOW()");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", idUsuario.toString());
		q.addWhere("ID_CONVENIO_PF = " + idOds);
		q.addWhere("AND ID_ESTATUS_CONVENIO IN ('1')");
		
		return q.obtenerQueryActualizar();
	}
	
	public String actConRenPF(String idOds, Integer idUsuario) {
		
		QueryHelper q = new QueryHelper("UPDATE SVT_RENOVACION_CONVENIO_PF");
		q.agregarParametroValues("ID_ESTATUS", "2");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NOW()");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", idUsuario.toString());
		q.addWhere("ID_RENOVACION_CONVENIO_PF = " + idOds);
		q.addWhere("AND ID_ESTATUS IN ('1')");
		
		return q.obtenerQueryActualizar();
	}
	
	public String actPB(String idPb, Integer idUsuario, String idEstatus, String generarPagare) {
		
		QueryHelper q = new QueryHelper("UPDATE SVT_PAGO_BITACORA");
		q.agregarParametroValues("CVE_ESTATUS_PAGO", idEstatus);
		q.agregarParametroValues("FEC_ACTUALIZACION", "NOW()");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", idUsuario.toString());
		q.agregarParametroValues("GEN_PAGARE", generarPagare);
		q.addWhere("ID_PAGO_BITACORA = " + idPb);
	
		return q.obtenerQueryActualizar();
	}
	
	public String registroDetalle(String idPagoBitacora){
		
		StringBuilder query = new StringBuilder("SELECT \r\n"
				+ "T.*,\r\n"
				+ "(T.totalAPagar - T.totalPagado) AS totalPorCubrir\r\n"
				+ "FROM \r\n"
				+ "(SELECT\r\n"
				+ "PB.CVE_FOLIO AS folio,\r\n"
				+ "PB.CVE_ESTATUS_PAGO AS idEstatusPago,\r\n"
				+ "EOS.DES_ESTATUS AS estatusPago,\r\n"
				+ "CAST(PB.DESC_VALOR AS double) AS totalAPagar,\r\n"
				+ "CONCAT('\"', FP.DESC_FLUJO_PAGOS,'\"') AS tipoPago,\r\n"
				+ "IFNULL( (SELECT SUM(PD.IMP_PAGO)\r\n"
				+ "FROM SVT_PAGO_DETALLE PD \r\n"
				+ "WHERE \r\n"
				+ "PD.ID_PAGO_BITACORA = ");
		query.append(idPagoBitacora);
		query.append(" AND PD.CVE_ESTATUS = '4'), 0) AS totalPagado\r\n"
				+ "FROM SVT_PAGO_BITACORA PB\r\n"
				+ "INNER JOIN SVC_ESTATUS_PAGO EOS ON EOS.ID_ESTATUS_PAGO = PB.CVE_ESTATUS_PAGO\r\n"
				+ "INNER JOIN SVC_FLUJO_PAGOS FP ON FP.ID_FLUJO_PAGOS = PB.ID_FLUJO_PAGOS\r\n"
				+ "WHERE\r\n"
				+ "PB.ID_PAGO_BITACORA = ");
		query.append(idPagoBitacora);
		query.append( ") T");
		
		return query.toString();
	}
	
	public String pagoDetalle(String idPagoBitacora){
		
		StringBuilder query = new StringBuilder("SELECT\r\n"
				+ "PD.ID_PAGO_DETALLE AS idPagoDetalle,\r\n"
				+ "MP.DESC_METODO_PAGO AS metodoPago,\r\n"
				+ "PD.ID_METODO_PAGO AS idMetodoPago,\r\n"
				+ "PD.IMP_PAGO AS importe,\r\n"
				+ "PD.NUM_AUTORIZACION AS numAutorizacion,\r\n"
				+ "PD.REF_BANCO AS nomBanco,\r\n"
				+ "PD.FEC_PAGO AS fechaPago,\r\n"
				+ "PD.FEC_VALE_AGF AS fechaValeParAGF,\r\n"
				+ "PD.CVE_ESTATUS AS idEstatusPago,\r\n"
				+ "EOS.DES_ESTATUS AS estatusPago\r\n"
				+ "FROM \r\n"
				+ "SVT_PAGO_DETALLE PD\r\n"
				+ "INNER JOIN SVC_METODO_PAGO MP ON MP.ID_METODO_PAGO = PD.ID_METODO_PAGO\r\n"
				+ "INNER JOIN SVC_ESTATUS_PAGO EOS ON EOS.ID_ESTATUS_PAGO = PD.CVE_ESTATUS\r\n"
				+ "WHERE ID_PAGO_BITACORA = ");
		query.append(idPagoBitacora);
		query.append(" AND PD.CVE_ESTATUS = 4");
		
		return query.toString();
	}
	
	public String eliminar(String idPagoDetalle, Integer idUsuario) {
		
		QueryHelper q = new QueryHelper("UPDATE SVT_PAGO_DETALLE");
		q.agregarParametroValues("CVE_ESTATUS", "0");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NOW()");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", idUsuario.toString());
		q.addWhere("ID_PAGO_DETALLE = " + idPagoDetalle);
	
		return q.obtenerQueryActualizar();
		
	}
	
	public String actualizar(CrearRequest datos, Integer idUsuario) {
		
		QueryHelper q = new QueryHelper("UPDATE SVT_PAGO_DETALLE");
		q.agregarParametroValues("IMP_PAGO", datos.getImportePago().toString());
		q.agregarParametroValues("FEC_ACTUALIZACION", "NOW()");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", idUsuario.toString());
		
		if( datos.getCambioMetPago() ) {
			
			q.agregarParametroValues("ID_METODO_PAGO", "'" + datos.getIdMetodoPago() + "'");
			
			if( datos.getNumAutorizacion()!= null ) {
				q.agregarParametroValues("NUM_AUTORIZACION", "'" + datos.getNumAutorizacion() + "'");
			} else {
				q.agregarParametroValues("NUM_AUTORIZACION", "null");
			}
			
			if( datos.getDescBanco() !=null ) {
				q.agregarParametroValues("REF_BANCO", "'" + datos.getDescBanco() + "'");
			} else {
				q.agregarParametroValues("REF_BANCO", "null");
			}
			
			if( datos.getFechaPago() != null ) {	
				q.agregarParametroValues("FEC_PAGO", "'" + datos.getFechaPago() + "'" );
			} else {
				q.agregarParametroValues("FEC_PAGO", "null" );
			}
			
			if( datos.getFechaValeAGF() != null ) {
				q.agregarParametroValues("FEC_VALE_AGF", "'" + datos.getFechaValeAGF() + "'");
			} else {
				q.agregarParametroValues("FEC_VALE_AGF", "null");
			}
			
		}else {
			
			if( datos.getNumAutorizacion()!= null ) {
				q.agregarParametroValues("NUM_AUTORIZACION", "'" + datos.getNumAutorizacion() + "'");
			}
			
			if( datos.getDescBanco() !=null ) {
				q.agregarParametroValues("REF_BANCO", "'" + datos.getDescBanco() + "'");
			}
			
			if( datos.getFechaPago() != null ) {	
				q.agregarParametroValues("FEC_PAGO", "'" + datos.getFechaPago() + "'" );
			}
			
			if( datos.getFechaValeAGF() != null ) {
				q.agregarParametroValues("FEC_VALE_AGF", "'" + datos.getFechaValeAGF() + "'");
			}
			
		}
		
		q.addWhere("ID_PAGO_DETALLE = " + datos.getIdPagoDetalle());
		return q.obtenerQueryActualizar();
		
	}
	
	public String eliminarTodos(String idPagoBitacora, Integer idUsuario) {
		
		QueryHelper q = new QueryHelper("UPDATE SVT_PAGO_DETALLE");
		q.agregarParametroValues("CVE_ESTATUS", "0");
		q.agregarParametroValues("FEC_ACTUALIZACION", "NOW()");
		q.agregarParametroValues("ID_USUARIO_MODIFICA", idUsuario.toString());
		q.addWhere("ID_PAGO_BITACORA = " + idPagoBitacora);
		q.addWhere("AND CVE_ESTATUS = 4");
	
		return q.obtenerQueryActualizar();
		
	}
	
	public Map<String, Object> generarReportePDF(FiltroRequest filtros, String nombrePdfReportes) {
		Map<String, Object> envioDatos = new HashMap<>();
		StringBuilder condicion = new StringBuilder(" ");
		
		if( validarWhere(filtros) ) {
			condicion.append( construyeFiltros(filtros) );
		}
		
		envioDatos.put("filtros", condicion);
		envioDatos.put("tipoReporte", filtros.getTipoReporte());
		envioDatos.put("rutaNombreReporte", nombrePdfReportes);

		return envioDatos;
	}
	
	private String construyeFiltros(FiltroRequest filtros) {
		StringBuilder query = new StringBuilder("");
		
		query.append( " WHERE " );
		
		int i=0;
		
		
		if( (filtros.getIdVelatorio()!=null && !filtros.getIdVelatorio().isEmpty() )) {
			query.append( "T.idVelatorio = '" + filtros.getIdVelatorio() + "' " );
			i++;
		}
		
		if( (filtros.getIdFlujoPagos()!=null && !filtros.getIdFlujoPagos().isEmpty() )) {
			
			if(i>=1) {
				query.append( "AND ");
			}
			
			query.append( "T.idFlujoPagos = '" + filtros.getIdFlujoPagos() +"' " );
			i++;
		}
		
		if( (filtros.getFolio()!=null && !filtros.getFolio().isEmpty() )) {
			
			if(i>=1) {
				query.append( "AND ");
			}
			
			query.append( "T.folio LIKE CONCAT('" + filtros.getFolio() + "', '%') " );
			i++;
		}
		
		if( (filtros.getNomContratante()!=null && !filtros.getNomContratante().isEmpty() ) ) {
			
			if(i>=1) {
				query.append( "AND ");
			}
			
			query.append( "T.nomContratante LIKE CONCAT('"+ filtros.getNomContratante() + "', '%') " );
			i++;
		}
		
		if( (filtros.getFechaInicio()!=null && !filtros.getFechaInicio().isEmpty() ) ) {
			
			if(i>=1) {
				query.append( "AND ");
			}
			
			query.append( "T.fecha BETWEEN '" + filtros.getFechaInicio() + "' AND '" + filtros.getFechaFin() + "' " );
		}
		
		return query.toString();
	}
	
	public String detalleAGF(String idPagoBitacora){
		StringBuilder query = new StringBuilder("SELECT\r\n"
				+ "ID_PAGO_DETALLE AS idPagoDetalle\r\n"
				+ "FROM\r\n"
				+ "SVT_PAGO_DETALLE\r\n"
				+ "WHERE\r\n"
				+ "ID_METODO_PAGO = 2\r\n"
				+ "AND CVE_ESTATUS = 4\r\n"
				+ "AND ID_PAGO_BITACORA = ");
		
		query.append(idPagoBitacora);
		query.append( " LIMIT 1 ");
		return query.toString();
	}
}
