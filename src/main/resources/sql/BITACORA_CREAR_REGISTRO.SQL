INSERT INTO SVH_BITACORA (
    ID_TIPO_TRANSACCION,
    DES_TABLA,
    DES_DATO_AFECTADO,
    DES_DATO_ACTUAL,
    TIM_FECHA_HORA,
    ID_USUARIO) 
    
VALUES (
    #{bit.tipo},
    #{bit.tabla},
    #{bit.afectado},
    #{bit.actual},
    NOW(),
    #{bit.idUsuario} )