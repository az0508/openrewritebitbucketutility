package com.open.rewrite.bitbucket.utility.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FileReader {
	@Value("${git.repos.checkout.path}")
	private String git_repos_checkout_path;

	@Value("${git.command.active}")
	private String git_command_active;

	private Map<String, String> bitbuketReposData = new HashMap<String, String>();

	Logger logger = LoggerFactory.getLogger(this.getClass().getName());

	private Map<?, ?> fetchSourceData(String file) throws IOException {
		FileInputStream sourceFile = new FileInputStream(file);
		logger.info("BitBucket Process Started" + file);
		XSSFWorkbook workbook = new XSSFWorkbook(sourceFile);
		XSSFSheet sheet = workbook.getSheetAt(0);
		Iterator<Row> rowIterator = sheet.iterator();
		while (rowIterator.hasNext()) {
			Row row = rowIterator.next();
			bitbuketReposData.put(row.getCell(0).getStringCellValue(), row.getCell(1).getStringCellValue().replaceAll("\\*/", ""));
		}
		sourceFile.close();
		return bitbuketReposData;
	}

	public void executeGitCli(String file) throws IOException {
		var i = 0;
		fetchSourceData(file);
		logger.info(
				"BitBucket Repos Size : " + bitbuketReposData.size() + " with Git operation : " + git_command_active);
		for (Entry<?,?> kv : bitbuketReposData.entrySet()) {
			try {
				Process process = Runtime.getRuntime().exec("git " + git_command_active + " -b " +kv.getValue()+" "+ kv.getKey() + "", null,
						new File(git_repos_checkout_path));
				logger.info("BitBucket procssing  initited count " + i++ + " " + process.isAlive() + " - " + kv );
				process.waitFor();
				int exitValue = process.exitValue();
				logger.info("process exitValue " + exitValue);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	}

	public String getGit_repos_checkout_path() {
		return git_repos_checkout_path;
	}

	public void setGit_repos_checkout_path(String git_repos_checkout_path) {
		this.git_repos_checkout_path = git_repos_checkout_path;
	}

	public String getGit_command_active() {
		return git_command_active;
	}

	public void setGit_command_active(String git_command_active) {
		this.git_command_active = git_command_active;
	}

}
