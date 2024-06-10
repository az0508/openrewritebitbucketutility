package com.open.rewrite.bitbucket.utility;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.open.rewrite.bitbucket.utility.helper.FileReader;


@SpringBootApplication
public class OpenrewritebitbucketutlilyApplication implements CommandLineRunner {

	@Value("${git.repos.source.excel.file.path}")
	private String reposSourcePath;

	@Autowired
	private FileReader fileReader;

	public static void main(String[] args) {
		SpringApplication.run(OpenrewritebitbucketutlilyApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		fileReader.executeGitCli(reposSourcePath);

	}

	public String getReposSourcePath() {
		return reposSourcePath;
	}

	public void setReposSourcePath(String reposSourcePath) {
		this.reposSourcePath = reposSourcePath;
	}

	public FileReader getFileReader() {
		return fileReader;
	}

	public void setFileReader(FileReader fileReader) {
		this.fileReader = fileReader;
	}

}
