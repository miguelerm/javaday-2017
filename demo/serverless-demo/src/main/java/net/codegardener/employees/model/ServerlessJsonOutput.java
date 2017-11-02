package net.codegardener.employees.model;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;

public class ServerlessJsonOutput extends ServerlessOutput {

	public ServerlessJsonOutput(Object data) {
		this(200, data);
	}

	public ServerlessJsonOutput(int statusCode, Object data) {
		setStatusCode(200);
		setJsonHeaders();
		setJsonBody(data);
	}

	private void setJsonHeaders() {
		Map<String, String> headers = new HashMap<String, String>();
		headers.put("Content-Type", "application/json");
	}

	private void setJsonBody(Object data) {
		Gson gson = new Gson();
		setBody(gson.toJson(data));
	}

}
