package com.generater.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ClassUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.generater.dto.GenerateDTO;
import com.generater.entity.TableInfoTree;
import com.generater.service.TableInfoService;
import com.generater.util.ZipUtils;
import com.generater.vo.GenerateVO;

import freemarker.template.TemplateException;

/**
 * @author yangyh
 * @version V1.0.0
 * @Description 表结构信息的conroller
 * @date 2018年4月8日
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
		String tempFileUUId = UUID.randomUUID().toString().replaceAll("-", "");
		if ("1".equals(generateVO.getDownLoadMode())) {
			String tempFilePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + tempFileUUId;
			generateVO.setTempFilePath(tempFilePath);
		}
		tableInfoService.generate(generateVO);
		GenerateDTO result = new GenerateDTO();
		result.setError_info("success");
		String zipFileUUID = UUID.randomUUID().toString().replaceAll("-", "");
		if ("1".equals(generateVO.getDownLoadMode())) {
			String zipFilePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + zipFileUUID;
			new File(zipFilePath).mkdir();

			FileOutputStream fos1 = new FileOutputStream(new File(zipFilePath + "/t.zip"));
			ZipUtils.toZip(generateVO.getTempFilePath(), fos1, true);
			System.out.println(zipFilePath + "/t.zip");
			result.setZipFilePath(zipFileUUID + "/t.zip");
		}

		return result;
	}

	@RequestMapping(value = "/generateDownloadMode", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> generateDownloadMode(GenerateVO generateVO)
			throws ClassNotFoundException, SQLException, IOException, TemplateException {
		String tempFilePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + UUID.randomUUID().toString().replaceAll("-", "");
		String zipFilePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + UUID.randomUUID().toString().replaceAll("-", "");
		generateVO.setTempFilePath(tempFilePath);
		tableInfoService.generate(generateVO);

		new File(zipFilePath).mkdir();
		// tempFilePath += "/t.zip";
		FileSystemResource file = new FileSystemResource(zipFilePath + "/t.zip");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
		headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getFilename()));
		headers.add("Pragma", "no-cache");
		headers.add("Expires", "0");

		// List<File> fileList = new ArrayList<>();
		// fileList.add(new File("D:/test/Test.java"));
		// fileList.add(new File("D:/test/Person.java"));
		// FileOutputStream fos2 = new FileOutputStream(new File(tempFilePath));
		// ZipUtils.toZip(fileList, fos2);

		FileOutputStream fos1 = new FileOutputStream(new File(zipFilePath + "/t.zip"));
		ZipUtils.toZip(tempFilePath, fos1, true);
		System.out.println(zipFilePath + "/t.zip");
		// return  null;
		return ResponseEntity
				.ok()
				.headers(headers)
				.contentLength(file.contentLength())
				.contentType(MediaType.parseMediaType("application/octet-stream"))
				.body(new InputStreamResource(new FileSystemResource(zipFilePath + "/t.zip").getInputStream()));
	}

	@RequestMapping(value = "/downloadZip", method = RequestMethod.GET)
	public ResponseEntity<InputStreamResource> downloadFile(String zipFilePath)
			throws IOException {
		zipFilePath = ClassUtils.getDefaultClassLoader().getResource("").getPath() + zipFilePath;
		FileSystemResource file = new FileSystemResource(zipFilePath);
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
				.body(new InputStreamResource(new FileSystemResource(zipFilePath).getInputStream()));

	}

}
