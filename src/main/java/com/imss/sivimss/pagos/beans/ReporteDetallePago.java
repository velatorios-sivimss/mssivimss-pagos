package com.imss.sivimss.pagos.beans;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.imss.sivimss.pagos.exception.BadRequestException;
import com.imss.sivimss.pagos.model.request.ReporteDetallePagoDto;
import com.imss.sivimss.pagos.util.AppConstantes;
import com.imss.sivimss.pagos.util.DatosRequest;
import com.imss.sivimss.pagos.util.SelectQueryUtil;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ReporteDetallePago {

	public Map<String, Object> generarReporte(ReporteDetallePagoDto reporte, String reporteDetPago) {
		Map<String, Object> envioDatos = new HashMap<>();
		StringBuilder condition= new StringBuilder();
		if(reporte.getId_delegacion()!=null) {
			condition.append(" AND SVEL.ID_DELEGACION = "+reporte.getId_delegacion()+"");
		}
		if(reporte.getId_velatorio()!=null) {
			condition.append(" AND SVEL.ID_VELATORIO = "+reporte.getId_velatorio()+"");
		}
		if(reporte.getId_ods()!=null) {
			condition.append(" AND SOS.ID_ORDEN_SERVICIO = "+reporte.getId_ods()+"");
		}
		if(reporte.getFecha_inicial()!=null) {
			condition.append(" AND SOS.FEC_ALTA >= '"+reporte.getFecInicioConsulta()+"'");
			envioDatos.put("fecInicio", reporte.getFecha_inicial());
		}
		if(reporte.getFecha_final()!=null) {
			condition.append(" AND SOS.FEC_ALTA <= '"+reporte.getFecFinConsulta()+"'");
			envioDatos.put("fecFin", reporte.getFecha_final());
		}
		log.info("reporte -> "+condition.toString());
		envioDatos.put("condition", condition.toString());
		envioDatos.put("rutaNombreReporte", reporteDetPago);
			envioDatos.put("tipoReporte", reporte.getTipoReporte());
		if(reporte.getTipoReporte().equals("xls")) { 
			envioDatos.put("IS_IGNORE_PAGINATION", true); 
			}
		return envioDatos;
	}

	public DatosRequest catalogoFolios(DatosRequest request, ReporteDetallePagoDto filtros) {
		Map<String, Object> parametros = new HashMap<>();
		SelectQueryUtil queryUtil = new SelectQueryUtil();
		queryUtil.select("ODS.ID_ORDEN_SERVICIO id_ods",
				"ODS.CVE_FOLIO AS folio_ods")
		.from("SVC_ORDEN_SERVICIO ODS")
		.join("SVT_PAGO_BITACORA PAG", "ODS.ID_ORDEN_SERVICIO = PAG.ID_REGISTRO")
		.join("SVC_VELATORIO VEL", "ODS.ID_VELATORIO = VEL.ID_VELATORIO")
		.leftJoin("SVC_FACTURA FAC", "PAG.ID_PAGO_BITACORA = FAC.ID_PAGO");
			queryUtil.where("ODS.ID_ESTATUS_ORDEN_SERVICIO = 4").and("PAG.CVE_ESTATUS_PAGO = 5");
			if(filtros.getId_velatorio()!=null) {
				queryUtil.where("ODS.ID_VELATORIO  ="+filtros.getId_velatorio());
			}
			if(filtros.getId_delegacion()!=null) {
				queryUtil.where("VEL.ID_DELEGACION ="+filtros.getId_delegacion());
			}
			queryUtil.orderBy("ODS.FEC_ALTA ASC");
		String query = obtieneQuery(queryUtil);
		log.info("detalle pago -> "+query);
		String encoded = encodedQuery(query);
	    parametros.put(AppConstantes.QUERY, encoded);
	    request.setDatos(parametros);
		return request;
	}
	
	private static String encodedQuery(String query) {
        return DatatypeConverter.printBase64Binary(query.getBytes(StandardCharsets.UTF_8));
    }
	
	private static String obtieneQuery(SelectQueryUtil queryUtil) {
        return queryUtil.build();
	}

}
