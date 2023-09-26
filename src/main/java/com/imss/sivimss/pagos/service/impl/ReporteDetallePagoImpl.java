package com.imss.sivimss.pagos.service.impl;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.pagos.beans.ReporteDetallePago;
import com.imss.sivimss.pagos.exception.BadRequestException;
import com.imss.sivimss.pagos.model.request.ReporteDetallePagoDto;
import com.imss.sivimss.pagos.service.ReporteDetallePagoService;
import com.imss.sivimss.pagos.util.AppConstantes;
import com.imss.sivimss.pagos.util.DatosRequest;
import com.imss.sivimss.pagos.util.LogUtil;
import com.imss.sivimss.pagos.util.MensajeResponseUtil;
import com.imss.sivimss.pagos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.pagos.util.Response;

@Service
public class ReporteDetallePagoImpl implements ReporteDetallePagoService{
	
	@Autowired
	private LogUtil logUtil;

	@Value("${endpoints.dominio}")
	private String urlConsulta;
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	@Value("${modulo.reporte-detalle-pago}")
	private String reporteDetPago;
	
	private static final String CONSULTA = "consulta";
	private static final String INFORMACION_INCOMPLETA = "Informacion incompleta";
	private static final String EXITO = "EXITO";
	private static final String IMPRIMIR = "IMPRIMIR";
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;

	ReporteDetallePago detallePago = new ReporteDetallePago();
	
	Gson gson = new Gson();

	@Override
	public Response<?> generarReporteDetPago(DatosRequest request, Authentication authentication)
			throws IOException, ParseException {
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ReporteDetallePagoDto reporte= gson.fromJson(datosJson, ReporteDetallePagoDto .class);
		if(reporte.getFecha_inicial()!=null) {
			reporte.setFecInicioConsulta(formatFecha(reporte.getFecha_inicial()));
		}
		if(reporte.getFecha_final()!=null) {
			reporte.setFecFinConsulta(formatFecha(reporte.getFecha_final()));
		}
		if(reporte.getTipoReporte()==null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, INFORMACION_INCOMPLETA);
		}
		Map<String, Object> envioDatos = detallePago.generarReporte(reporte, reporteDetPago);
		Response<?> response = providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes,
				authentication);
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"SE GENERO CORRECTAMENTE EL REPORTE SERVICIOS VELATORIOS", IMPRIMIR, authentication);
		return response;
	}

	@Override
	public Response<?> buscarOds(DatosRequest request, Authentication authentication) throws IOException {
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		Response<?> response;
		ReporteDetallePagoDto  filtros = gson.fromJson(datosJson, ReporteDetallePagoDto .class);
		 response = MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicio(detallePago.catalogoFolios(request, filtros).getDatos(), urlConsulta+"/consulta",
					authentication), EXITO);
		    	   logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(),"CONSULTA ARTICULOS CONSIGNADOS OK", CONSULTA, authentication);   	
	    	   return response;
	}
	
	 public String formatFecha(String fecha) throws ParseException {
			Date dateF = new SimpleDateFormat("dd/MM/yyyy").parse(fecha);
			DateFormat fecForma = new SimpleDateFormat("yyyy-MM-dd", new Locale("es", "MX"));
			return fecForma.format(dateF);       
		}


}
