package com.imss.sivimss.pagos.service.impl;

import java.io.IOException;
import java.util.logging.Level;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.pagos.model.request.BusquedaDto;
import com.imss.sivimss.pagos.util.AppConstantes;
import com.imss.sivimss.pagos.beans.GestionarPagos;
import com.imss.sivimss.pagos.service.GestionarService;
import com.imss.sivimss.pagos.util.DatosRequest;
import com.imss.sivimss.pagos.util.LogUtil;
import com.imss.sivimss.pagos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.pagos.util.Response;

@Service
public class GestionarServiceImpl implements GestionarService {

	@Value("${endpoints.dominio}")
	private String urlDominio;
	
    private static final String PAGINADO = "/paginado";
	
	private static final String CONSULTA = "/consulta";
	
	@Value("${endpoints.generico-reportes}")
	private String urlReportes;
	
	@Value("${formato_fecha}")
	private String formatoFecha;
	
	private static final String INFONOENCONTRADA = "45";
	
	@Autowired
	private LogUtil logUtil;
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
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
		
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		BusquedaDto busqueda = gson.fromJson(datosJson, BusquedaDto.class);
		Response<Object> response = null;
		
		try {
			response = providerRestTemplate.consumirServicio(gestionPagos.consultaPagos(request, busqueda).getDatos(), urlDominio + CONSULTA, authentication);
		} catch (Exception e) {
        	log.error(e.getMessage());
        	logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);
			return null;
        }
		
		return null;
	}

	@Override
	public Response<Object> busqueda(DatosRequest request, Authentication authentication) throws IOException {
		// TODO Auto-generated method stub
		return null;
	}

}
