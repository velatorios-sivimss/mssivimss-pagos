package com.imss.sivimss.pagos.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.pagos.util.DatosRequest;
import com.imss.sivimss.pagos.util.Response;

public interface PagosService {
	
	Response<Object> buscar(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultaOds(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultaPf(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultaRpf(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> crear(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> obtenerPorId(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultaFolOds(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultaFolPf(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> consultaFolRpf(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> eliminar(DatosRequest request, Authentication authentication) throws IOException;
	Response<Object> actualizar(DatosRequest request, Authentication authentication) throws IOException;
}
