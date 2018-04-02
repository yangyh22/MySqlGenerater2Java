<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="xxx.dao">

	<sql id="fullColumns">
        <![CDATA[
<#list attrs.field_info_list?chunk(5) as list>
			<#list list as attr>${attr.name}<#if attr_has_next>,</#if></#list><#if list_has_next>,</#if>
</#list>
	    ]]>
	</sql>
	
	<sql id="fullColumnsValues">
<#list attrs.field_info_list?chunk(5) as list>
	    <#list list as attr>${"#{"}${attr.name}${"}"}<#if attr_has_next>,</#if></#list><#if list_has_next>,</#if>
</#list>
	</sql>
	
	<sql id="fullSet">
		<set>
		<#list attrs.field_info_list as attr>
			<if test="null != ${attr.name}">${attr.name} = ${"#{"}${attr.name}${"}"},</if>
		</#list>
		</set>
	</sql>
	
	<sql id="fullIf">
		<#list attrs.field_info_list as attr>
		<if test="null != ${attr.name}">AND ${attr.name} =  ${"#{"}${attr.name}${"}"}</if>
		</#list>
	</sql>
	
	<sql id="fullWhere">
		<where>
			<include refid="fullIf" />
		</where>
	</sql>
	
	<sql id="listWhere">
		<where>
			<include refid="fullIf" />
			
		</where>
	</sql>
	
	<sql id="sortAndPage">
		<if test="null != sortMarkers">
			ORDER BY
			<foreach collection="sortMarkers" item="sortMarker"
				separator=",">
				${"#"}{sortMarker.field} ${"#"}{sortMarker.direction}
			</foreach>
		</if>
		<if test="null != page">
			LIMIT ${"#{"}page.offset}, ${"#"}{page.page_size}
		</if>
	</sql>
	
	<insert id="save" useGeneratedKeys="true">
		INSERT INTO ${attrs.table_name} (
		<include refid="fullColumns" />
		) VALUES(
		<include refid="fullColumnsValues" />
		)
	</insert>
	
	<update id="update">
		UPDATE ${attrs.table_name}
		<include refid="fullSet" />
		WHERE ${attrs.pk} = ${"#{"}${attrs.pk}${"}"}
	</update>

	<update id="batchUpdate">
		UPDATE ${attrs.table_name}
		<include refid="fullSet" />
		WHERE ${attrs.pk} IN(
		<foreach collection="ids" item="item" index="index" separator=",">
			${"#"}{item}
		</foreach>
		)
	</update>
	
	<delete id="delete">
		DELETE
		FROM ${attrs.table_name}
		WHERE
		${attrs.pk} = ${"#{"}${attrs.pk}${"}"}
	</delete>
	
	<select id="get" resultType="${packageName}${"."}${className}">
		SELECT
		<include refid="fullColumns" />
		FROM ${attrs.table_name}
		WHERE ${attrs.pk} = ${"#{"}${attrs.pk}${"}"}
	</select>
	
	<select id="query" resultType="${packageName}${"."}${className}">
		SELECT
		<include refid="fullColumns" />
		FROM ${attrs.table_name}
		<include refid="fullWhere" />
		<include refid="sortAndPage" />
	</select>
	
	<select id="count" resultType="int">
		SELECT count(*)
		FROM ${attrs.table_name}
		<include refid="fullWhere" />
	</select>
	
	<select id="list" resultType="${packageName}${"."}${className}${"."}list${className}Param">
		SELECT
		<include refid="fullColumns" />
		FROM ${attrs.table_name}
		<include refid="listWhere" />
		<include refid="sortAndPage" />
	</select>
	
	<select id="countList" resultType="int">
		SELECT count(*)
		FROM ${attrs.table_name}
		<include refid="listWhere" />
	</select>

</mapper>
