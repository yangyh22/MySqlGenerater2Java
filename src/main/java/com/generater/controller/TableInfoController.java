package com.generater.controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.generater.dto.GenerateDTO;
import com.generater.entity.TableInfoTree;
import com.generater.service.TableInfoService;
import com.generater.vo.GenerateVO;

import freemarker.template.TemplateException;

/**
 * @Description 表结构信息的conroller
 * @author yangyh
 * @date 2018年4月8日
 * @version V1.0.0
 */
@RestController
@RequestMapping(value = "/tableInfo")
public class TableInfoController {

	@Autowired
	TableInfoService tableInfoService;

	@RequestMapping("/schemaList")
	public List<TableInfoTree> schemaList() throws ClassNotFoundException, SQLException {
		return tableInfoService.querySchemaList();
	}

	@RequestMapping("/tableList")
	public List<TableInfoTree> tableList(String schema) throws ClassNotFoundException, SQLException {
		return tableInfoService.queryTableList(schema);
	}

	@RequestMapping(value = "/generate", method = RequestMethod.POST)
	public GenerateDTO generate(@RequestBody GenerateVO generateVO)
			throws ClassNotFoundException, SQLException, IOException, TemplateException {
		tableInfoService.generate(generateVO);
		GenerateDTO result = new GenerateDTO();
		result.setError_info("success");
		return result;
	}
	
    @RequestMapping(value = "/media", method = RequestMethod.GET)  
    public ResponseEntity<InputStreamResource> downloadFile( Long id)  
            throws IOException {  
        String filePath = "D:\\test\\Test.java";  
        FileSystemResource file = new FileSystemResource(filePath);  
        HttpHeaders headers = new HttpHeaders();  
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");  
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));  
        headers.add("Pragma", "no-cache");  
        headers.add("Expires", "0");  
  
        return ResponseEntity  
                .ok()  
                .headers(headers)  
                .contentLength(file.contentLength())  
                .contentType(MediaType.parseMediaType("application/octet-stream"))  
                .body(new InputStreamResource(file.getInputStream()));  
    }  

}
