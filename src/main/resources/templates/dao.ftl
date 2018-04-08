package ${packageName};

import java.util.List;
import ${entityPackageName}${"."}${entityName};
import ${entityPackageName}${"."}${entityName}${"."}list${entityName}Param;

/**
 *  @author ${author}
 */
public interface ${className} {
    
    public void save(${entityName} entity);
    
    public ${entityName} get(String key);
    
    public int count(${entityName} entity);
    
    public List<${entityName}> query(${entityName} entity);
    
    public List<${entityName}> list(list${entityName}Param param);
    
    public int countList(list${entityName}Param param);
    
    public int update(${entityName} entity);
    
    public int delete(String key);
    
}