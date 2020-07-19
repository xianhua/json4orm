package com.json4orm.codegen;

import java.io.FileWriter;
import java.io.IOException;

import com.json4orm.model.schema.Schema;

public class AbstractCodeGenerator {
	Schema schema;
	String saveToFolder;
	protected static final String TAB = "   ";
	
	public Schema getSchema() {
		return schema;
	}
	public void setSchema(Schema schema) {
		this.schema = schema;
	}
	public String getSaveToFolder() {
		return saveToFolder;
	}
	public void setSaveToFolder(String saveToFolder) {
		this.saveToFolder = saveToFolder;
	}
	
	protected void saveFile(String fileName, String fileContent) throws IOException {
		FileWriter fw = null;
		try {
			fw = new FileWriter(saveToFolder + fileName);
			fw.write(fileContent);

		} finally {
			if (fw != null) {
				fw.close();
			}
		}
	}
}
