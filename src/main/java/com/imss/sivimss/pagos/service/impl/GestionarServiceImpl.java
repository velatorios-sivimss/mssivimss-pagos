package com.imss.sivimss.pagos.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.pagos.model.request.BusquedaDto;
import com.imss.sivimss.pagos.model.request.GestionPagoDto;
import com.imss.sivimss.pagos.model.response.DetPagoResponse;
import com.imss.sivimss.pagos.model.response.ModificaResponse;
import com.imss.sivimss.pagos.util.AppConstantes;
import com.imss.sivimss.pagos.exception.BadRequestException;
import com.imss.sivimss.pagos.util.MensajeResponseUtil;
import com.imss.sivimss.pagos.beans.GestionarPagos;
import com.imss.sivimss.pagos.service.GestionarService;
import com.imss.sivimss.pagos.util.DatosRequest;
import com.imss.sivimss.pagos.util.LogUtil;
import com.imss.sivimss.pagos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.pagos.util.Response;
import com.imss.sivimss.pagos.model.request.UsuarioDto;

@Service
public class GestionarServiceImpl implements GestionarService {

	@Value("${endpoints.dominio}")
	private String urlDominio;
	
    private static final String PAGINADO = "/paginado";
	
	private static final String CONSULTA = "/consulta";
	
	private static final String ACTUALIZAR = "/actualizar";
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	
	@Value("${formato_fecha}")
	private String formatoFecha;
	
	private static final String INFONOENCONTRADA = "45";
	
	private static final String ERROR_DESCARGA = "64";
	
	private static final String MODIFICACION = "modificacion";
	
	private static final String NOMBREPDFREPORTE = "reportes/generales/ReporteGeneraPagos.jrxml";
	
	@Autowired
	private LogUtil logUtil;
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private static final Logger log = LoggerFactory.getLogger(GestionarService.class);
	
	@Override
	public Response<Object> listadoOds(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		GestionarPagos gestionPagos = new GestionarPagos();
		
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		BusquedaDto busqueda = gson.fromJson(datosJson, BusquedaDto.class);
		Response<Object> response = null;
		
		try {
		    response = providerRestTemplate.consumirServicio(gestionPagos.foliosOds(busqueda).getDatos(), urlDominio + CONSULTA, authentication);
		} catch (Exception e) {
			log.error(e.getMessage());
        	logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);
			return null;
        }
		
		return response;
	}

	@Override
	public Response<Object> listadoPf(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		GestionarPagos gestionPagos = new GestionarPagos();
		
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		BusquedaDto busqueda = gson.fromJson(datosJson, BusquedaDto.class);
		Response<Object> response = null;
		
		try {
		    response = providerRestTemplate.consumirServicio(gestionPagos.foliosPf(busqueda).getDatos(), urlDominio + CONSULTA, authentication);
		} catch (Exception e) {
        	log.error(e.getMessage());
        	logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);
			return null;
        }
		
		return response;
	}

	@Override
	public Response<Object> listadoRpf(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		GestionarPagos gestionPagos = new GestionarPagos();
		
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		BusquedaDto busqueda = gson.fromJson(datosJson, BusquedaDto.class);
		Response<Object> response = null;
		
		try {
		    response = providerRestTemplate.consumirServicio(gestionPagos.foliosRpf(busqueda).getDatos(), urlDominio + CONSULTA, authentication);
		} catch (Exception e) {
        	log.error(e.getMessage());
        	logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);
			return null;
        }
		
		return response;
	}

	@Override
	public Response<Object> consultaGeneral(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		GestionarPagos gestionPagos = new GestionarPagos();
		
		String datosJson = String.valueOf(authentication.getPrincipal());
		BusquedaDto busqueda = gson.fromJson(datosJson, BusquedaDto.class);
		Response<Object> response = null;
		
		try {
			response = providerRestTemplate.consumirServicio(gestionPagos.consultaPagos(request, busqueda, formatoFecha).getDatos(), urlDominio + PAGINADO, authentication);
		} catch (Exception e) {
        	log.error(e.getMessage());
        	logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), PAGINADO, authentication);
			return null;
        }
		
		return response;
	}

	@Override
	public Response<Object> busqueda(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		GestionarPagos gestionPagos = new GestionarPagos();
		
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		BusquedaDto busqueda = gson.fromJson(datosJson, BusquedaDto.class);
		Response<Object> response = null;
		
		try {
			response = providerRestTemplate.consumirServicio(gestionPagos.buscaPagos(request, busqueda, formatoFecha).getDatos(), urlDominio + PAGINADO, authentication);
			ArrayList datos1 = (ArrayList) ((LinkedHashMap) response.getDatos()).get("content");
			if (datos1.isEmpty()) {
				response.setMensaje(INFONOENCONTRADA);
		    }
		} catch (Exception e) {
        	log.error(e.getMessage());
        	logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), PAGINADO, authentication);
			return null;
        }
		
		return response;
	}

	@Override
	public Response<Object> detalle(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();

		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		GestionPagoDto gestionPagoDto = gson.fromJson(datosJson, GestionPagoDto.class);
		if (gestionPagoDto.getIdFlujo() == null || gestionPagoDto.getIdPago() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		GestionarPagos gestionPagos = new GestionarPagos(gestionPagoDto.getIdFlujo(), gestionPagoDto.getIdPago());
		
		Response<Object> response;
		List<Map<String, Object>> listaDatos;
		DetPagoResponse detalle = new DetPagoResponse();
		
		try {
		    response = providerRestTemplate.consumirServicio(gestionPagos.detalleGeneral(request, formatoFecha).getDatos(), urlDominio + CONSULTA, authentication);
		    listaDatos = Arrays.asList(modelMapper.map(response.getDatos(), Map[].class));
		    detalle.setFecha(listaDatos.get(0).get("fecha").toString());
		    detalle.setFolio(listaDatos.get(0).get("folio").toString());
		    detalle.setNomContratante(listaDatos.get(0).get("nomContratante").toString());
		    detalle.setIdFlujo((Integer) listaDatos.get(0).get("idFlujo"));
		    detalle.setDesFlujo(listaDatos.get(0).get("desFlujo").toString());
		    detalle.setFecPago(listaDatos.get(0).get("fecPago").toString());
		    detalle.setDesEstatus(listaDatos.get(0).get("desEstatus").toString());
		    detalle.setDesEstatusPago(listaDatos.get(0).get("desEstatusPago").toString());
		    detalle.setIdPagoBitacora((Integer) listaDatos.get(0).get("idPagoBitacora"));
		    
		    response = providerRestTemplate.consumirServicio(gestionPagos.detallePagos(request, formatoFecha, detalle.getIdPagoBitacora()).getDatos(), urlDominio + CONSULTA, authentication);
		    listaDatos = Arrays.asList(modelMapper.map(response.getDatos(), Map[].class));
		    detalle.setMetodosPago(listaDatos);
		    response.setDatos(detalle);
		
		} catch (Exception e) {
			    log.error(e.getMessage());
		        logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);
				return null;
		}
		
		return response;

	}
	
	@Override
	public Response<Object> modifica(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);

		datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		ModificaResponse modificaResponse = gson.fromJson(datosJson, ModificaResponse.class);
		modificaResponse.setIdUsuarioModifica(usuarioDto.getIdUsuario());
		if (modificaResponse.getIdPagoDetalle() == null || modificaResponse.getMotivoModifica() == null) {
			throw new BadRequestException(HttpStatus.BAD_REQUEST, "Informacion incompleta");
		}
		GestionarPagos gestionPagos = new GestionarPagos();
		try {
			return providerRestTemplate.consumirServicio(gestionPagos.modifica(modificaResponse).getDatos(), urlDominio + ACTUALIZAR, authentication);
		} catch (Exception e) {
			log.error(e.getMessage());
			logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), MODIFICACION, authentication);
			return null;
		}
				
	}
	
	@Override
	public Response<Object> descargarDocumento(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		BusquedaDto reporteDto = gson.fromJson(datosJson, BusquedaDto.class);
		
		Map<String, Object> envioDatos = new GestionarPagos().generarReporte(reporteDto, NOMBREPDFREPORTE, formatoFecha);
		Response<Object> response =  providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes, authentication);
				
		return MensajeResponseUtil.mensajeConsultaResponse(response, ERROR_DESCARGA);
	}

}
