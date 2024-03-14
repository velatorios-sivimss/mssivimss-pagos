package com.imss.sivimss.pagos.util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import lombok.Data;

@Component
@Data
public class SQLLoader {

    private final ResourceLoader resourceLoader;

    private String bitacoraNuevoRegistro;
    private String pagoBitacora;
    private String pagoBitacoraDetalles;
    private String ordenesDeServ;

    public SQLLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() throws IOException {
        Resource resource = resourceLoader.getResource("classpath:sql/BITACORA_CREAR_REGISTRO.SQL");
        byte[] bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        bitacoraNuevoRegistro = new String(bytes, StandardCharsets.UTF_8);

        resource = resourceLoader.getResource("classpath:sql/PAGOS_CONSULTA_REGISTRO.SQL");
        bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        pagoBitacora = new String(bytes, StandardCharsets.UTF_8);

        resource = resourceLoader.getResource("classpath:sql/PAGOS_CONSULTA_DETALLE_CONCEPTOS.SQL");
        bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        pagoBitacoraDetalles = new String(bytes, StandardCharsets.UTF_8);

        resource = resourceLoader.getResource("classpath:sql/PAGOS_CONSULTA_ODS.SQL");
        bytes = FileCopyUtils.copyToByteArray(resource.getInputStream());
        ordenesDeServ = new String(bytes, StandardCharsets.UTF_8);

    }

    
}
