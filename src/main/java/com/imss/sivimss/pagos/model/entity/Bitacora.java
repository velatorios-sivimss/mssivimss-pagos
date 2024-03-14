package com.imss.sivimss.pagos.model.entity;


public class Bitacora {
    /*#{bit.tipo},
    #{bit.tabla},
    #{bit.afectado},
    #{bit.actual},
    NOW(),
    #{bit.idUsuario}  */

    

    private Integer tipo;
    public Bitacora(Integer tipo, String tabla, String afectado, String actual, Integer idUsuario) {
        this.tipo = tipo;
        this.tabla = tabla;
        this.afectado = afectado;
        this.actual = actual;
        this.idUsuario = idUsuario;
    }

    private String tabla;
    private String afectado;
    private String actual;
    private Integer idUsuario;
}
