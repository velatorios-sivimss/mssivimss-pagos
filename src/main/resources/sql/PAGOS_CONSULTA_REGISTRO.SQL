SELECT
	ID_PAGO_BITACORA idPagoBitacora,
	ID_REGISTRO idRegistro,
	ID_FLUJO_PAGOS idFlujoPagos,
	ID_VELATORIO idVelatorio, 
	FEC_ODS fechaOds,
	NOM_CONTRATANTE nombreContratante,
	CVE_FOLIO claveFolio,
	IMP_VALOR importeValor,
	CVE_ESTATUS_PAGO claveEstatusPago,
	ID_USUARIO_ALTA idUsuarioAlta,
	FEC_ALTA fechaAlta,
	ID_USUARIO_BAJA idUsuarioBaja,
	FEC_BAJA fechaBaja,
	ID_USUARIO_MODIFICA idUsuarioModifica,
	FEC_ACTUALIZACION fechaActualizacion,
	IND_GEN_PAGARE indGenPagare,
	IND_MOD_PAGO indModPago,
	ID_PLATAFORMA idPlataforma
FROM
	SVT_PAGO_BITACORA
WHERE ID_PAGO_BITACORA = #{idBitacora}
LIMIT 1 ;