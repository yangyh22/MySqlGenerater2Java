package ${packageName};

import java.math.BigDecimal;

import lombok.Data;
import lombok.EqualsAndHashCode;
/**
 *  @author ${author}
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class ${className}   {

	private static final long serialVersionUID = 1L;
	
    <#list attrs.field_info_list as attr> 
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
	public class list${className}Param extends ${className} {
	
		private static final long serialVersionUID = 1L;
		
	}
}
