package com.imss.sivimss.pagos.beans;

import java.util.HashMap;
import java.util.Map;

import com.imss.sivimss.pagos.model.request.ReporteDetallePagoDto;

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
			condition.append(" AND SOS.FEC_ALTA BETWEEN '"+reporte.getFecInicioConsulta()+ " 00:00:01' AND '"+reporte.getFecFinConsulta()+" 23:59:59'");
			envioDatos.put("fecInicio", reporte.getFecha_inicial());
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

}
