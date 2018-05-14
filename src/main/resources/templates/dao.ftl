package ${packageName};

import java.util.List;
import ${entityPackageName}${"."}${entityName};
import ${entityPackageName}${"."}${entityName}${"."}List${entityName}Param;

/**
 *  @author ${author}
 */
public interface ${className} {
    
    public void save(${entityName} entity);
    
    public ${entityName} get(String key);
    
    public int count(${entityName} entity);
    
    public List<${entityName}> query(${entityName} entity);
    
    public List<${entityName}> list(List${entityName}Param param);
    
    public int countList(List${entityName}Param param);
    
    public int update(${entityName} entity);
    
    public int delete(String key);
    
}