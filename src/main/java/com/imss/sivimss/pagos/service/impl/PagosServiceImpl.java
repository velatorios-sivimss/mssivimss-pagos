package com.imss.sivimss.pagos.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import javax.xml.bind.DatatypeConverter;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.pagos.service.PagosService;
import com.imss.sivimss.pagos.util.DatosRequest;
import com.imss.sivimss.pagos.util.Response;
import com.imss.sivimss.pagos.model.request.UsuarioDto;
import com.imss.sivimss.pagos.model.response.DetalleResponse;
import com.imss.sivimss.pagos.util.MensajeResponseUtil;
import com.imss.sivimss.pagos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.pagos.util.LogUtil;
import com.imss.sivimss.pagos.util.PagosUtil;
import com.imss.sivimss.pagos.model.request.ActualizarMultiRequest;
import com.imss.sivimss.pagos.model.request.CrearRequest;
import com.imss.sivimss.pagos.model.request.FiltroRequest;
import com.imss.sivimss.pagos.util.AppConstantes;

@Service
public class PagosServiceImpl implements PagosService {

	@Value("${endpoints.dominio}")
	private String urlDomino;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	
	@Autowired
	private LogUtil logUtil;
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private static final String CONSULTA = "consulta";
	private static final String CONSULTA_PAGINADA = "/paginado";
	private static final String CONSULTA_GENERICA = "/consulta";
	private static final String ACTUALIZAR_MULTIPLES = "/actualizar/multiples";
	private static final String SIN_INFORMACION = "45";
	private static final String NOM_REPORTE = "reportes/generales/ReporteTablaPagos.jrxml";
	private static final String ERROR_AL_DESCARGAR_DOCUMENTO= "64"; // Error en la descarga del documento.Intenta nuevamente.
	@Override
	public Response<Object> buscar(DatosRequest request, Authentication authentication) throws IOException {
		
		Gson gson = new Gson();
		FiltroRequest filtrosRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), FiltroRequest.class);
		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.consultaTabla(filtrosRequest);
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		Response<Object> response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_PAGINADA, 
				authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse( response, SIN_INFORMACION );
	}

	@Override
	public Response<Object> consultaOds(DatosRequest request, Authentication authentication) throws IOException {
		
		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.tablaTotales(1);
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		Response<Object> response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_PAGINADA, 
				authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse( response, SIN_INFORMACION );
		
	}

	@Override
	public Response<Object> consultaPf(DatosRequest request, Authentication authentication) throws IOException {

		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.tablaTotales(2);
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		Response<Object> response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_PAGINADA, 
				authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse( response, SIN_INFORMACION );
		
	}

	@Override
	public Response<Object> consultaRpf(DatosRequest request, Authentication authentication) throws IOException {

		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.tablaTotales(3);
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		Response<Object> response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_PAGINADA, 
				authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse( response, SIN_INFORMACION );
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> crear(DatosRequest request, Authentication authentication) throws IOException {
		
		Gson gson = new Gson();
		CrearRequest crearRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), CrearRequest.class);
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		ArrayList<String> querys = new ArrayList<>();
		PagosUtil pagosUtil = new PagosUtil();
		List<Map<String, Object>> listadatos;
		Double totalPagado;
		String encoded;
		Response<Object> response;
		ActualizarMultiRequest actualizarMultiRequest = new ActualizarMultiRequest();
		String query = pagosUtil.totalPagado(crearRequest.getIdPagoBitacora() );
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		listadatos = Arrays.asList(modelMapper.map(response.getDatos(), Map[].class));
		totalPagado = (Double) listadatos.get(0).get("totalPagado");
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "","Total Pago BD " + totalPagado, authentication);
	
		totalPagado += crearRequest.getImportePago();
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "","Total Pago Actualizado " + totalPagado, authentication);
		
		if( totalPagado >= crearRequest.getImporteRegistro() ) {
			
			//Actualizamos la OS y el Pago de la Bitacora a Pagado
			if( crearRequest.getIdFlujoPago().equals(1) ) {
				
				query = pagosUtil.actODs( crearRequest.getIdRegistro(), usuarioDto.getIdUsuario() );
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
						this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
				encoded = DatatypeConverter.printBase64Binary(query.getBytes());
				querys.add( encoded );
				
			} else if ( crearRequest.getIdFlujoPago().equals(2) ) {
				
				query = pagosUtil.actConPF( crearRequest.getIdRegistro(), usuarioDto.getIdUsuario() );
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
						this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
				encoded = DatatypeConverter.printBase64Binary(query.getBytes());
				querys.add( encoded );
				
			} else if ( crearRequest.getIdFlujoPago().equals(3) ) {
				
				query = pagosUtil.actConRenPF( crearRequest.getIdRegistro(), usuarioDto.getIdUsuario() );
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
						this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
				encoded = DatatypeConverter.printBase64Binary(query.getBytes());
				querys.add( encoded );
				
			}
			
			query = pagosUtil.actPB( crearRequest.getIdPagoBitacora(), usuarioDto.getIdUsuario(), "4", "0" );
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
					this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
			encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			querys.add( encoded );
			
		}else {
			if( crearRequest.getIdFlujoPago().equals(1) ) {
				query = pagosUtil.actPB( crearRequest.getIdPagoBitacora(), usuarioDto.getIdUsuario(), "8", "1" );
			}else {
				query = pagosUtil.actPB( crearRequest.getIdPagoBitacora(), usuarioDto.getIdUsuario(), "8", "0" );
			}
			
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
					this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
			encoded = DatatypeConverter.printBase64Binary(query.getBytes());
			querys.add( encoded );
			
		}
		
		query = pagosUtil.crearDetalle(crearRequest, usuarioDto.getIdUsuario());
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		encoded = DatatypeConverter.printBase64Binary(query.getBytes());
		querys.add( encoded );
		
		actualizarMultiRequest.setUpdates(querys);
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + actualizarMultiRequest, authentication);
		
		response = providerRestTemplate.consumirServicioActMult(actualizarMultiRequest, urlDomino + ACTUALIZAR_MULTIPLES, 
				authentication);
		
		return response;
	
	}

	@SuppressWarnings("unchecked")
	@Override
	public Response<Object> obtenerPorId(DatosRequest request, Authentication authentication) throws IOException {
		
		Gson gson = new Gson();
		CrearRequest crearRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), CrearRequest.class);
		PagosUtil pagosUtil = new PagosUtil();
		Response<Object> response;
		List<Map<String, Object>> listadatos;
		DetalleResponse detalle;
		String query = pagosUtil.registroDetalle(crearRequest.getIdPagoBitacora() );
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		listadatos = Arrays.asList(modelMapper.map(response.getDatos(), Map[].class));
		
		detalle = gson.fromJson(String.valueOf(listadatos.get(0)), DetalleResponse.class);
		
		query = pagosUtil.pagoDetalle(crearRequest.getIdPagoBitacora() );
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		listadatos = Arrays.asList(modelMapper.map(response.getDatos(), Map[].class));
		
		detalle.setMetodosPago(listadatos);
		
		response.setDatos(detalle);
		return response;
	}

	@Override
	public Response<Object> consultaFolOds(DatosRequest request, Authentication authentication) throws IOException {
		
		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.foliosOds();
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		Response<Object> response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse( response, SIN_INFORMACION );
		
	}

	@Override
	public Response<Object> consultaFolPf(DatosRequest request, Authentication authentication) throws IOException {
		
		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.foliosPf();
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		Response<Object> response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse( response, SIN_INFORMACION );
	
	}

	@Override
	public Response<Object> consultaFolRpf(DatosRequest request, Authentication authentication) throws IOException {
		
		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.foliosRpf();
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		Response<Object> response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse( response, SIN_INFORMACION );
	
		
	}

	@Override
	public Response<Object> eliminar(DatosRequest request, Authentication authentication) throws IOException {
		
		Gson gson = new Gson();
		CrearRequest crearRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), CrearRequest.class);
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		PagosUtil pagosUtil = new PagosUtil();
		Response<Object> response;
		String query = pagosUtil.eliminar(crearRequest.getIdPagoDetalle(), usuarioDto.getIdUsuario() );

		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		return response;
	}

	@Override
	public Response<Object> actualizar(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		CrearRequest crearRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), CrearRequest.class);
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		PagosUtil pagosUtil = new PagosUtil();
		Response<Object> response;
		String query = pagosUtil.actualizar(crearRequest, usuarioDto.getIdUsuario() );

		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		return response;
	}

	@Override
	public Response<Object> eliminarTodos(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		CrearRequest crearRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), CrearRequest.class);
		UsuarioDto usuarioDto = gson.fromJson((String) authentication.getPrincipal(), UsuarioDto.class);
		PagosUtil pagosUtil = new PagosUtil();
		Response<Object> response;
		String query = pagosUtil.eliminarTodos(crearRequest.getIdPagoBitacora(), usuarioDto.getIdUsuario() );
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		return response;
	}

	@Override
	public Response<Object> generartablaPDF(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		FiltroRequest filtrosRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), FiltroRequest.class);
		PagosUtil pagosUtil = new PagosUtil();
		Map<String, Object> envioDatos = pagosUtil.generarReportePDF(filtrosRequest, NOM_REPORTE);
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + envioDatos, authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes,authentication)
				, ERROR_AL_DESCARGAR_DOCUMENTO);
	}

}
