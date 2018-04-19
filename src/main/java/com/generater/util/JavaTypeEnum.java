package com.generater.util;

public enum JavaTypeEnum {
	/**
	 *  
	 */
	DEFAULT("default", "default", null),
	
	/**
	 * 
	 */
	INTEGER("int", "Integer", null),
	
	/**
	 *
	 */
	TINYINT("tinyint", "Integer", null),
	
	/**
	 * 
	 */
	STRING("varchar", "String", null),
	
	/**
	 * 
	 */
	DOUBLE("double", "Double", null),
	
	/**
	 * 
	 */
	BIGDECIMAL("decimal", "BigDecimal", "import java.math.BigDecimal;");

	private final String val;

	private final String description;

	private final String packageName;

	private String toString;

	private JavaTypeEnum(String val, String description, String packageName) {
		this.val = val;
		this.description = description;
		this.packageName = packageName;
	}

	public String getPackageName() {
		return packageName;
	}

	public String getVal() {
		return val;
	}

	public String getDescription() {
		return description;
	}

	public static JavaTypeEnum val(String operate) {
		for (JavaTypeEnum s : values()) { // values()方法返回enum实例的数组
			if (operate.equals(s.getVal()))
				return s;
		}
		return DEFAULT;
	}

	@Override
	public String toString() {
		if (null == toString) {
			toString = new StringBuilder().append("JavaTypeEnum[").append(val).append(':').append(description)
					.append(']').toString();
		}
		return toString;
	}

}
