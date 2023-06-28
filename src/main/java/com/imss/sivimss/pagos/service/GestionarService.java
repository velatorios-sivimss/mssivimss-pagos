package com.imss.sivimss.pagos.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.pagos.util.DatosRequest;
import com.imss.sivimss.pagos.util.Response;

public interface GestionarService {

	Response<Object> listadoOds(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> listadoPf(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> listadoRpf(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> consultaGeneral(DatosRequest request, Authentication authentication) throws IOException;
	
	Response<Object> busqueda(DatosRequest request, Authentication authentication) throws IOException;
	
}
