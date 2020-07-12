package com.json4orm.codegen;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.json4orm.model.schema.Entity;
import com.json4orm.model.schema.Property;
import com.json4orm.model.schema.PropertyType;
import com.json4orm.model.schema.Schema;

public class AngularCodeGenerator {
	Schema schema;
	String saveToFolder;
	private static final String TAB = "   ";

	public void generateModels() throws IOException {
		StringBuffer sb = new StringBuffer();

		for (Map.Entry<String, Entity> entry : schema.getEntities().entrySet()) {
			sb.append("export interface " + entry.getValue().getName() + " { ");
			sb.append(System.lineSeparator());
			Entity entity = entry.getValue();
			for (Property p : entity.getProperties()) {
				sb.append(TAB);
				sb.append(p.getName() + ": ");
				switch (p.getType()) {
				case PropertyType.PTY_BOOLEAN:
					sb.append(PropertyType.PTY_BOOLEAN);
					break;
				case PropertyType.PTY_BYTE:
				case PropertyType.PTY_DOUBLE:
				case PropertyType.PTY_FLOAT:
				case PropertyType.PTY_ID:
				case PropertyType.PTY_INTEGER:
				case PropertyType.PTY_LONG:
				case PropertyType.PTY_SHORT:
				case "ID":
					sb.append("number");
					break;
				case PropertyType.PTY_DATE:
				case PropertyType.PTY_DATETIME:
				case PropertyType.PTY_TIME:
				case PropertyType.PTY_TIMESTAMP:
					sb.append("string");
					break;
				case PropertyType.PTY_STRING:
					sb.append("string");
					break;
				case PropertyType.PTY_LIST:
					sb.append(p.getItemType() + "[]");
					break;
				default:
					Entity ent = schema.getEntity(p.getType());
					if (ent != null) {
						sb.append(ent.getName());
					} else {
						throw new IllegalArgumentException("No entity found for name: " + p.getType());
					}
				}

				sb.append(";");
				sb.append(System.lineSeparator());
			}

			sb.append("}");
			sb.append(System.lineSeparator());
			sb.append(System.lineSeparator());
		}

		final String fileName = "models.ts";
		saveFile(fileName, sb.toString());
	}

	public void generateCreateForm(Entity entity) throws IOException {
		StringBuffer sbHtml = new StringBuffer();
		StringBuffer sbForm = new StringBuffer();
		String formGroupName = entity.getName().toLowerCase() + "FormGroup";
		sbHtml.append("<div class=\"form-container\">");
		sbHtml.append(System.lineSeparator());

		sbForm.append(formGroupName + " = new FormGroup ({");

		boolean firstOne = true;

		sbHtml.append("<form [formGroup]=\"" + formGroupName + "\" (ngSubmit)=\"submitForm()\">");
		for (Property p : entity.getProperties()) {
			if (PropertyType.PTY_LIST.equalsIgnoreCase(p.getType())
					|| PropertyType.PTY_ID.equalsIgnoreCase(p.getType())) {
				continue;
			}

			sbHtml.append(System.lineSeparator());

			String inputType = getInputType(p);
			if (StringUtils.isBlank(inputType)) {
				continue;
			}

			addField(p, sbHtml);
			if (!firstOne) {
				sbForm.append(",");
			}
			addFormControl(p, sbForm);
			firstOne = false;

		}

		sbHtml.append(System.lineSeparator());
		// add submit and clear buttons
		sbHtml.append(TAB + "<div class=\"form-button-group\">");
		sbHtml.append(System.lineSeparator());
		sbHtml.append(TAB + "<button type=\"submit\" [disabled]=\"!" + formGroupName + ".valid\">Submit</button>");
		sbHtml.append(System.lineSeparator());
		sbHtml.append(TAB + "<button type=\"reset\" (click)=\"resetForm()\">Clear</button>");
		sbHtml.append(System.lineSeparator());
		sbHtml.append(TAB + "<button (click)=\"cancelForm()\">Cancel</button>");
		sbHtml.append(TAB + "</div>");
		sbHtml.append(System.lineSeparator());
		sbHtml.append(TAB + "</form>");
		sbHtml.append(System.lineSeparator());
		sbHtml.append(TAB + "</div>");

		saveFile(entity.getName() + "_form.html", sbHtml.toString());

		sbForm.append(System.lineSeparator());
		sbForm.append(TAB + "});");
		sbForm.append(System.lineSeparator());
		sbForm.append(System.lineSeparator());
		sbForm.append(System.lineSeparator());
		sbForm.append(createSubmitFormFunction(entity));
		sbForm.append(System.lineSeparator());
		sbForm.append(System.lineSeparator());
		sbForm.append(createProcessDataFunction(entity));
		sbForm.append(System.lineSeparator());
		sbForm.append(System.lineSeparator());
		sbForm.append(TAB + "resetForm() {");
		sbForm.append(System.lineSeparator());
		sbForm.append(TAB + "this." + formGroupName + ".reset();");
		sbForm.append(System.lineSeparator());
		sbForm.append(TAB + "}");
		sbForm.append(System.lineSeparator());
		sbForm.append(System.lineSeparator());
		sbForm.append(TAB + "cancelForm() {");
		sbForm.append(System.lineSeparator());
		sbForm.append(TAB + "}");
		sbForm.append(System.lineSeparator());
		sbForm.append(System.lineSeparator());
		saveFile(entity.getName() + "_form.ts", sbForm.toString());

	}

	private String createSubmitFormFunction(Entity entity) {
		StringBuffer sb = new StringBuffer();
		sb.append(TAB + "submitForm() {");
		sb.append(System.lineSeparator());
		String varName = entity.getName().toLowerCase();
		sb.append(TAB + "let " + varName + " = <" + entity.getName() + ">{}");
		sb.append(System.lineSeparator());
		for (Property p : entity.getProperties()) {
			if (PropertyType.PTY_LIST.equalsIgnoreCase(p.getType())
					|| PropertyType.PTY_ID.equalsIgnoreCase(p.getType())) {
				continue;
			}
			sb.append(TAB + varName + "." + p.getName() + " = this." + varName + "FormGroup.get('" + p.getName()
					+ "').value;");
			sb.append(System.lineSeparator());
		}
		sb.append(System.lineSeparator());
		sb.append(TAB + "this." + varName + "Service.add" + entity.getName() + "(" + varName
				+ ").subscribe((data: QueryResponse<" + entity.getName() + ">) => this.processData(data));");
		sb.append(System.lineSeparator());
		sb.append(TAB + "}");

		return sb.toString();
	}

	private String createProcessDataFunction(Entity entity) {
		StringBuffer sb = new StringBuffer();
		sb.append(TAB + "processData(data: QueryResponse<" + entity.getName() + ">) : void {");
		sb.append(System.lineSeparator());
		sb.append(TAB + TAB + "if(data.status === 'SUCCESS'){");
		sb.append(System.lineSeparator());
		sb.append(TAB + TAB + TAB + "console.log('Successfully registered.');");
		sb.append(System.lineSeparator());
		sb.append(TAB + TAB + "    }else{");
		sb.append(System.lineSeparator());
		sb.append(TAB + TAB + TAB + "console.log('Failed with error: ' + data.error);");
		sb.append(System.lineSeparator());
		sb.append(TAB + TAB + "}");
		sb.append(System.lineSeparator());
		sb.append(TAB + "}");
		return sb.toString();
	}

	private void addFormControl(Property property, StringBuffer sbForm) {
		sbForm.append(System.lineSeparator());
		sbForm.append(TAB + property.getName() + ": new FormControl(''");

		if (property.isRequired()) {
			sbForm.append(", Validators.required)");
		} else {
			sbForm.append(")");
		}
	}

	private void addField(Property property, StringBuffer sbHtml) {
		String inputType = getInputType(property);
		if (inputType == null) {
			return;
		}
		sbHtml.append(TAB + "<div class=\"form-field" + (property.isRequired() ? " required" : "") + "\">");
		sbHtml.append(System.lineSeparator());
		sbHtml.append(
				TAB + "<label for=\"" + property.getName() + "\">" + decamelcase(property.getName()) + ": </label>");
		sbHtml.append(System.lineSeparator());
		if (inputType.equalsIgnoreCase("radio")) {
			sbHtml.append(TAB + "<input type=\"radio\" name=\"" + property.getName() + "\" formControlName=\""
					+ property.getName() + "\" [value]=\"true\" />Yes");
			sbHtml.append(System.lineSeparator());
			sbHtml.append(TAB + "<input type=\"radio\" name=\"" + property.getName() + "\" formControlName=\""
					+ property.getName() + "\" [value]=\"false\" />No");
		} else {
			sbHtml.append(
					TAB + "<input type=\"" + inputType + "\" name=\"" + property.getName() + "\" formControlName=\""
							+ property.getName() + "\"" + (property.isRequired() ? " required" : "") + " />");
		}
		sbHtml.append(System.lineSeparator());
		sbHtml.append(TAB + "</div>");
	}

	public static String decamelcase(String s) {
		List<String> result = new ArrayList<>();
		for (String w : s.split("(?<!(^|[A-Z]))(?=[A-Z])|(?<!^)(?=[A-Z][a-z])")) {
			result.add(w);
		}
		return StringUtils.join(result, " ");
	}

	private String getInputType(Property property) {
		switch (property.getType()) {
		case PropertyType.PTY_BOOLEAN:
			return "radio";
		case PropertyType.PTY_BYTE:
		case PropertyType.PTY_DOUBLE:
		case PropertyType.PTY_FLOAT:
		case PropertyType.PTY_INTEGER:
		case PropertyType.PTY_LONG:
		case PropertyType.PTY_SHORT:
		case PropertyType.PTY_STRING:
			return "text";
		case PropertyType.PTY_DATE:
			return "date";

		case PropertyType.PTY_DATETIME:
		case PropertyType.PTY_TIMESTAMP:
			return "datetime-local";

		case PropertyType.PTY_TIME:
			return "time";

		default:
			return null;
		}
	}

	public void generateCreateForms() throws IOException {
		for (Map.Entry<String, Entity> entry : schema.getEntities().entrySet()) {
			generateCreateForm(entry.getValue());
		}
	}

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

	private void saveFile(String fileName, String fileContent) throws IOException {
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
