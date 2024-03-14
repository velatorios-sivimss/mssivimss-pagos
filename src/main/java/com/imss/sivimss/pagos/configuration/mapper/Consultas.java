package com.imss.sivimss.pagos.configuration.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import com.imss.sivimss.pagos.model.entity.Bitacora;
import com.imss.sivimss.pagos.model.entity.PagoBitacora;

@Repository
public interface Consultas {
	static class PureSqlProvider{
        public String sql(String sql) {
            return sql;
        }
 
        public String count(String from) {
            return "SELECT count(*) FROM " + from;
        }
    }
    @SelectProvider(type = PureSqlProvider.class, method = "sql")
	public List<Map<String, Object>> selectNativeQuery(String sql);

    @SelectProvider(type = PureSqlProvider.class, method = "sql")
	public PagoBitacora consultaPagosBitacora(String sql);
 
    @Insert("${sqlQuery}")
    // @Options(useGeneratedKeys = true,keyProperty = "bit.idPersona", keyColumn="id")
    int insertData(@Param("sqlQuery") String sqlQuery, @Param("bit")Bitacora bitacora);
}
