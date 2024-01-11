package com.imss.sivimss.pagos.service.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

import lombok.extern.slf4j.Slf4j;

import com.imss.sivimss.pagos.model.request.UsuarioDto;
import com.imss.sivimss.pagos.model.request.ValeParitariaRequest;
import com.imss.sivimss.pagos.model.response.DetalleResponse;
import com.imss.sivimss.pagos.model.response.ValeParitariaResponse;
import com.imss.sivimss.pagos.util.MensajeResponseUtil;
import com.imss.sivimss.pagos.util.ProviderServiceRestTemplate;
import com.imss.sivimss.pagos.util.LogUtil;
import com.imss.sivimss.pagos.util.PagosUtil;
import com.imss.sivimss.pagos.beans.GestionarPagos;
import com.imss.sivimss.pagos.model.request.ActualizarMultiRequest;
import com.imss.sivimss.pagos.model.request.AgfDto;
import com.imss.sivimss.pagos.model.request.ContratanteRequest;
import com.imss.sivimss.pagos.model.request.CrearRequest;
import com.imss.sivimss.pagos.model.request.FiltroRequest;
import com.imss.sivimss.pagos.util.AppConstantes;

@Slf4j
@Service
public class PagosServiceImpl implements PagosService {

	@Value("${endpoints.nss}")
	private String urlnss;
	
	@Value("${endpoints.dominio}")
	private String urlDomino;
	
	@Value("${endpoints.ms-reportes}")
	private String urlReportes;
	
	@Value("${endpoints.consulta-siap}")
	private String urlConsultaSiap;
	
	@Value("${formato_fecha}")
	private String formatoFecha;
	
	@Autowired
	private LogUtil logUtil;
	
	@Autowired
	private ProviderServiceRestTemplate providerRestTemplate;
	
	@Autowired
	private ModelMapper modelMapper;
	
	private static final String CONSULTA = "consulta";
	private static final String CONSULTA_PAGINADA = "/paginado";
	private static final String CONSULTA_GENERICA = "/consulta";
	private static final String ACTUALIZAR = "/actualizar";
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

		Gson gson = new Gson();
		CrearRequest crearRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), CrearRequest.class);
		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.tablaTotales(1, formatoFecha, crearRequest.getIdVelatorio());
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		Response<Object> response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_PAGINADA, 
				authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse( response, SIN_INFORMACION );
		
	}

	@Override
	public Response<Object> consultaPf(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		CrearRequest crearRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), CrearRequest.class);
		
		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.tablaTotales(2, formatoFecha, crearRequest.getIdVelatorio());
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		Response<Object> response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_PAGINADA, 
				authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse( response, SIN_INFORMACION );
		
	}

	@Override
	public Response<Object> consultaRpf(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		CrearRequest crearRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), CrearRequest.class);
		
		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.tablaTotales(3, formatoFecha, crearRequest.getIdVelatorio());
		
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
		
		if( crearRequest.getIdMetodoPago().equals("2")) {
			
			query = pagosUtil.detalleAGF( crearRequest.getIdPagoBitacora() );
			
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
					this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
			
			request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
			
			response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
					authentication);
		}
		
		
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
		String query = pagosUtil.registroDetalle(crearRequest.getIdPagoBitacora(), formatoFecha );
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		listadatos = Arrays.asList(modelMapper.map(response.getDatos(), Map[].class));
		
		detalle = gson.fromJson(String.valueOf(listadatos.get(0)), DetalleResponse.class);
		
		query = pagosUtil.pagoDetalle(crearRequest.getIdPagoBitacora(), formatoFecha );
		
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

		Gson gson = new Gson();
		CrearRequest crearRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), CrearRequest.class);
		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.foliosOds(crearRequest.getIdVelatorio());
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		Response<Object> response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse( response, SIN_INFORMACION );
		
	}

	@Override
	public Response<Object> consultaFolPf(DatosRequest request, Authentication authentication) throws IOException {

		Gson gson = new Gson();
		CrearRequest crearRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), CrearRequest.class);
		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.foliosPf(crearRequest.getIdVelatorio());
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
		
		request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
		
		Response<Object> response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
				authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse( response, SIN_INFORMACION );
	
	}

	@Override
	public Response<Object> consultaFolRpf(DatosRequest request, Authentication authentication) throws IOException {

		Gson gson = new Gson();
		CrearRequest crearRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), CrearRequest.class);
		PagosUtil pagosUtil = new PagosUtil();
		String query = pagosUtil.foliosRpf(crearRequest.getIdVelatorio());
		
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

	@SuppressWarnings("unchecked")
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
		
		if(response.getCodigo()==200) {
			
			List<Map<String, Object>> listadatos;
			Double totalPagado;
			String encoded;
			String consulta;
			
			consulta = pagosUtil.obtenerPagoBitacora(crearRequest.getIdPagoDetalle() );
			
			encoded = DatatypeConverter.printBase64Binary(consulta.getBytes("UTF-8"));
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
					this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
			
			request.getDatos().put(AppConstantes.QUERY, encoded);
			
			response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
					authentication);
			
			listadatos = Arrays.asList(modelMapper.map(response.getDatos(), Map[].class));
			crearRequest.setIdPagoBitacora(listadatos.get(0).get("ID_PAGO_BITACORA").toString());
			crearRequest.setImporteRegistro((Double) listadatos.get(0).get("IMP_VALOR"));
			crearRequest.setIdFlujoPago((Integer)listadatos.get(0).get("ID_FLUJO_PAGOS"));
            crearRequest.setIdRegistro(listadatos.get(0).get("ID_REGISTRO").toString());	
			consulta = pagosUtil.totalPagado(crearRequest.getIdPagoBitacora() );
			encoded = DatatypeConverter.printBase64Binary(consulta.getBytes("UTF-8"));
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
					this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
			
			request.getDatos().put(AppConstantes.QUERY, encoded);
			
			response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
					authentication);
			
			listadatos = Arrays.asList(modelMapper.map(response.getDatos(), Map[].class));
			totalPagado = (Double) listadatos.get(0).get("totalPagado");
			
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
					this.getClass().getPackage().toString(), "","Total Pago BD " + totalPagado, authentication);
		
		//	totalPagado += crearRequest.getImportePago();
			
	//		crearRequest.setImportePago(totalPagado);
			
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
					this.getClass().getPackage().toString(), "","Total Pago Actualizado " + totalPagado, authentication);
			
			if( crearRequest.getImportePago() >= crearRequest.getImporteRegistro() ) {
				
				//Actualizamos la OS y el Pago de la Bitacora a Pagado
				if( crearRequest.getIdFlujoPago().equals(1) ) {
					
					query = pagosUtil.actODs( crearRequest.getIdRegistro(), usuarioDto.getIdUsuario() );
					logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
							this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
					encoded = DatatypeConverter.printBase64Binary(query.getBytes("UTF-8"));
					request.getDatos().put(AppConstantes.QUERY, encoded);
					providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + ACTUALIZAR, 
							authentication);
					//querys.add( encoded );
					
				} else if ( crearRequest.getIdFlujoPago().equals(2) ) {
					
					query = pagosUtil.actConPF( crearRequest.getIdRegistro(), usuarioDto.getIdUsuario() );
					logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
							this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
					encoded = DatatypeConverter.printBase64Binary(query.getBytes("UTF-8"));
					request.getDatos().put(AppConstantes.QUERY, encoded);
					providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + ACTUALIZAR, 
							authentication);
					
				} else if ( crearRequest.getIdFlujoPago().equals(3) ) {
					
					query = pagosUtil.actConRenPF( crearRequest.getIdRegistro(), usuarioDto.getIdUsuario() );
					logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
							this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
					encoded = DatatypeConverter.printBase64Binary(query.getBytes("UTF-8"));
					request.getDatos().put(AppConstantes.QUERY, encoded);
					providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + ACTUALIZAR, 
							authentication);
					
				}
				
				query = pagosUtil.actPB( crearRequest.getIdPagoBitacora(), usuarioDto.getIdUsuario(), "4", "0" );
				logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
						this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
				encoded = DatatypeConverter.printBase64Binary(query.getBytes("UTF-8"));
				request.getDatos().put(AppConstantes.QUERY, encoded);
				providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + ACTUALIZAR, 
						authentication);
				/*query = pagosUtil.actualizar(crearRequest, usuarioDto.getIdUsuario() );

				request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes("UTF-8")));
				
				providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, 
						authentication); */
		}
		}
		
	
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
		
		if(filtrosRequest.getTipoReporte().equals("xls")) {
            envioDatos.put("IS_IGNORE_PAGINATION", true);
        }
		
		logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), 
				this.getClass().getPackage().toString(), "",CONSULTA +" " + envioDatos, authentication);
		
		return MensajeResponseUtil.mensajeConsultaResponse(providerRestTemplate.consumirServicioReportes(envioDatos, urlReportes,authentication)
				, ERROR_AL_DESCARGAR_DOCUMENTO);
	}
	
	
	public Response<Object> validarAGF(DatosRequest request, Authentication authentication) throws IOException {

		try {
		Gson gson = new Gson();		
		String datosJson = String.valueOf(request.getDatos().get(AppConstantes.DATOS));
		AgfDto agfDto = gson.fromJson(datosJson, AgfDto.class);
		agfUtilizado(agfDto, authentication);
		if( agfDto.getAgf() == 0) {
			return new Response<>(false, 200, AppConstantes.EXITO, agfDto);		 
		}
		agfDto.setAgf(1);
		obtenerNSS(agfDto, authentication);
		if( agfDto.getNss() == null || agfDto.getNss().equals("")) {
			agfDto.setAgf(0);
			return new Response<>(false, 200, AppConstantes.EXITO, agfDto);		 
		}
		validaNSS(agfDto, authentication);
		return new Response<>(false, 200, AppConstantes.EXITO, agfDto);	
		} catch (Exception e) {
        	logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);
			return null;
        }
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private AgfDto agfUtilizado(AgfDto agfDto, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		GestionarPagos gestionPagos = new GestionarPagos();
		try {
			Response<Object> response = null;
		    response = providerRestTemplate.consumirServicio(gestionPagos.queryUsoAGF(agfDto.getIdODS()).getDatos(), urlDomino + CONSULTA_GENERICA, authentication);
			ArrayList<LinkedHashMap> datos = (ArrayList<LinkedHashMap>) response.getDatos();
		    AgfDto busqueda = gson.fromJson(datos.get(0).toString(), AgfDto.class);
		    agfDto.setAgf(busqueda.getAgf());
		    
		    return agfDto;
		} catch (Exception e) {
        	logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);
			return agfDto;
        }
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private AgfDto obtenerNSS(AgfDto agfDto, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		GestionarPagos gestionPagos = new GestionarPagos();
		try {
			Response<Object> response = null;
		    response = providerRestTemplate.consumirServicio(gestionPagos.queryObtenerNSS(agfDto.getIdODS()).getDatos(), urlDomino + CONSULTA_GENERICA, authentication);
			ArrayList<LinkedHashMap> datos = (ArrayList<LinkedHashMap>) response.getDatos();
		    AgfDto busqueda = gson.fromJson(datos.get(0).toString(), AgfDto.class);
		    agfDto.setIdFinado(busqueda.getIdFinado());
		    agfDto.setNss(busqueda.getNss());
		    return agfDto;
		} catch (Exception e) {
        	logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);
			return agfDto;
        }
	}

	private AgfDto validaNSS(AgfDto agfDto, Authentication authentication) throws IOException {
		try {
			Response<Object>response=providerRestTemplate.consumirServicioExternoGet(urlnss+"/"+agfDto.getNss());
			if(response.getDatos() == null) {
				agfDto.setAgf(0);
				agfDto.setNss(null);
				agfDto.setIdFinado(null);
			}else
				agfDto.setAgf(1);
			
		    return agfDto;
		} catch (Exception e) {
        	logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), e.getMessage(), CONSULTA, authentication);
			return agfDto;
        }
	}
	
	@Override
	public Response<Object> validarValeParitaria(DatosRequest request, Authentication authentication) throws IOException {
		Response<Object> response;
		String consulta = "";
		try {
			ValeParitariaRequest valeParitaria = new Gson().fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), ValeParitariaRequest.class);
			
			String query = new PagosUtil().tipoPagoDetalle(valeParitaria.getIdOds());
			
			logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "",CONSULTA +" " + query, authentication);
			
			request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(query.getBytes(AppConstantes.UTF_8)));
			
			response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, authentication);
			
			if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
				
				List<ValeParitariaResponse> valeParitariaResponse = Arrays.asList(modelMapper.map(response.getDatos(), ValeParitariaResponse[].class));
				
				if( valeParitariaResponse.get(0).getValeP() >= 1) {
					
					response= new Response<>(false, 200, AppConstantes.EXITO,  new ValeParitariaResponse(0));
					
				} else {
					String queryMatricula = new PagosUtil().obtenerMatricula(valeParitaria.getIdOds());
					
					logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "",CONSULTA +" " + queryMatricula, authentication);
					
					request.getDatos().put(AppConstantes.QUERY, DatatypeConverter.printBase64Binary(queryMatricula.getBytes(AppConstantes.UTF_8)));
					
					response = providerRestTemplate.consumirServicio(request.getDatos(), urlDomino + CONSULTA_GENERICA, authentication);
					
					if (response.getCodigo()==200 && !response.getDatos().toString().contains("[]")) {
						
						List<ContratanteRequest> contratante = Arrays.asList(modelMapper.map(response.getDatos(), ContratanteRequest[].class));
						
						if( contratante.get(0).getCveMatricula() == null || Objects.equals(contratante.get(0).getCveMatricula(), AppConstantes.EMPTY_STRING)) {
							
							 response= new Response<>(false, 200, AppConstantes.EXITO,  new ValeParitariaResponse(0));
							
						} else {
							Map<String, Object> resp;
							
							String url = urlConsultaSiap + contratante.get(0).getCveMatricula();
							
							logUtil.crearArchivoLog(Level.INFO.toString(), this.getClass().getSimpleName(), this.getClass().getPackage().toString(), "",CONSULTA +" " + url, authentication);
							
							//Hacemos el consumo para consultar el SIAP
							resp = providerRestTemplate.consumirServicioGet(url);
							
							String status = (String) resp.get("status");
							
							log.info("status: "+ status);
							
							if (status.trim().equalsIgnoreCase("ACTIVO")) {
								
								response= new Response<>(false, 200, AppConstantes.EXITO,  new ValeParitariaResponse(1));
								
							} else {
								
								response= new Response<>(false, 200, AppConstantes.EXITO,  new ValeParitariaResponse(0));
								
							}
							
						}
						
					}
					
				}
				
			}

		} catch (Exception e) {
			String decoded = new String(DatatypeConverter.parseBase64Binary(consulta));
			log.error(AppConstantes.ERROR_AL_EJECUTAR_EL_QUERY + decoded);
			logUtil.crearArchivoLog(Level.SEVERE.toString(), this.getClass().getSimpleName(),this.getClass().getPackage().toString(), AppConstantes.FALLO_AL_EJECUTAR_EL_QUERY + decoded, CONSULTA,
					authentication);
			throw new IOException(AppConstantes.ERROR_INFORMACION, e.getCause());
		}
		return response;
	}
}
