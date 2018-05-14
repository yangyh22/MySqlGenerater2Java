package ${packageName};

<#list attrs.package_name_list as package_name> 
${package_name}
</#list>
import java.io.Serializable;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 *  @author ${author}
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ${className} ${extendClassParam!''}implements Serializable {

	private static final long serialVersionUID = 1L;
	
    <#list attrs.java_field_info_list as attr> 
	/**
	 * ${attr.column_comment}
	 */
	<#if attr_index == 0>
	@javax.persistence.Id
    </#if>
    private ${attr.java_type} ${attr.name};
    
    </#list>
    @Data
	@EqualsAndHashCode(callSuper = false)
	public class List${className}Param extends ${className} {
	
		private static final long serialVersionUID = 1L;
		
	}
}
