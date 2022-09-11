package com.json4orm.codegen;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.json4orm.model.schema.Entity;
import com.json4orm.model.schema.Property;
import com.json4orm.model.schema.PropertyType;

public class JavaCodeGenerator extends AbstractCodeGenerator {
	public void generateJpaEntity(String packageName, Entity entity) throws IOException {
		StringBuffer sbImport = new StringBuffer();
		StringBuffer sb = new StringBuffer();
		Set<String> imports = new HashSet<>();
		
		sbImport.append("package " + packageName+";");
		sbImport.append(System.lineSeparator());
		sbImport.append("import java.io.Serializable;");
		sbImport.append(System.lineSeparator());
		sbImport.append("import javax.persistence.*;");
		sbImport.append(System.lineSeparator());

		sb.append("@Entity");
		sb.append(System.lineSeparator());
		sb.append("@Table(name=\"" + entity.getTable() + "\")");
		sb.append(System.lineSeparator());
		sb.append("public class " + entity.getName() + " implements Serializable { ");
		sb.append(System.lineSeparator());

		for (Property p : entity.getProperties()) {
			if (p.getType().equalsIgnoreCase(PropertyType.PTY_ID)) {
				sb.append("@Id");
				sb.append(System.lineSeparator());
			}
			final String javaType = getJavaType(p);
			if (javaType != null) {
				sb.append("@Column(name=\"" + p.getColumn() + "\")");
				sb.append(System.lineSeparator());

				sb.append("private " + javaType + " " + p.getName() + ";");
				sb.append(System.lineSeparator());
				
				if(javaType.equalsIgnoreCase("Date")) {
					imports.add("import java.sql.Date;");
				}else if(javaType.equalsIgnoreCase("Timestamp")) {
					imports.add("import java.sql.Timestamp;");
				}else if(javaType.equalsIgnoreCase("Time")) {
					imports.add("import java.sql.Time;");
				}
			}
		}

		sb.append("}");
		sb.append(System.lineSeparator());
		sb.append(System.lineSeparator());
		
		for(String ipt: imports) {
			sbImport.append(ipt);
			sbImport.append(System.lineSeparator());
		}
		sbImport.append(System.lineSeparator());
		sbImport.append(System.lineSeparator());
		final String fileName = entity.getName() + ".java";
		saveFile(fileName, sbImport.toString() + sb.toString());
	}

	public String getJavaType(Property p) {
		switch (p.getType()) {
		case "ID":
			return "Integer";
		case PropertyType.PTY_BOOLEAN:
			return "Boolean";
		case PropertyType.PTY_BYTE:
			return "Byte";
		case PropertyType.PTY_DOUBLE:
			return "Double";
		case PropertyType.PTY_FLOAT:
			return "Float";
		case PropertyType.PTY_ID:
			return "Integer";
		case PropertyType.PTY_INTEGER:
			return "Integer";
		case PropertyType.PTY_LONG:
			return "Long";
		case PropertyType.PTY_SHORT:
			return "Short";
		case PropertyType.PTY_DATE:
			return "Date";
		case PropertyType.PTY_DATETIME:
			return "Timestamp";
		case PropertyType.PTY_TIME:
			return "Time";
		case PropertyType.PTY_TIMESTAMP:
			return "Timestamp";
		case PropertyType.PTY_STRING:
			return "String";
		default:
			return null;
		}
	}

	public void generateJpaEntities(String packageName) throws IOException {
		for (Map.Entry<String, Entity> entry : schema.getEntities().entrySet()) {
			generateJpaEntity(packageName, entry.getValue());
		}
	}
}
