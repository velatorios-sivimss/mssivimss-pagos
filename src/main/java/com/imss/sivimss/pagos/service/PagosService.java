package com.imss.sivimss.pagos.service;

import java.io.IOException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.pagos.util.DatosRequest;
import com.imss.sivimss.pagos.util.Response;

public interface PagosService {
	
	Response<Object> buscar(DatosRequest request, Authentication authentication) throws IOException;

}
