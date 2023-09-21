package com.imss.sivimss.pagos.service;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.security.core.Authentication;

import com.imss.sivimss.pagos.util.DatosRequest;
import com.imss.sivimss.pagos.util.Response;

public interface ReporteDetallePagoService {

	Response<?> generarReporteDetPago(DatosRequest request, Authentication authentication) throws IOException, ParseException;

	Response<?> buscarOds(DatosRequest request, Authentication authentication) throws IOException;

}
