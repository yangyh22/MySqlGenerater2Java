package com.generater.vo.base;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Description 返回错误信息的POJO
 * @author yangyh
 * @date 2018年4月12日
 * @version V1.0.0
 */
@Data
@EqualsAndHashCode(callSuper=false)
public class ErrorPOJO extends BasePOJO {
	/**
	*
	*/
	private static final long serialVersionUID = 1L;

	// 当前的错误信息
	private String error_no;

	private String error_info;
	// 错误的值
	private String error_key;
	
	private List<ErrorPOJO> error_list;


}
