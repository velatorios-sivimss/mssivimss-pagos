package com.imss.sivimss.pagos.service.impl;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.imss.sivimss.pagos.service.PagosService;
import com.imss.sivimss.pagos.util.DatosRequest;
import com.imss.sivimss.pagos.util.Response;
import com.imss.sivimss.pagos.model.request.FiltroRequest;
import com.imss.sivimss.pagos.util.AppConstantes;

@Service
public class PagosServiceImpl implements PagosService {

	@Value("${endpoints.dominio}")
	private String urlDomino;
	
	@Override
	public Response<Object> buscar(DatosRequest request, Authentication authentication) throws IOException {
		Gson gson = new Gson();
		
		FiltroRequest filtrosRequest = gson.fromJson(String.valueOf(request.getDatos().get(AppConstantes.DATOS)), FiltroRequest.class);
		
		return null;
	}

}
