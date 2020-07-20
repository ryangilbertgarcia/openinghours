package com.wolt.openinghours.rest;

import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javax.enterprise.context.RequestScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.wolt.openinghours.model.BusinessHour;
import com.wolt.openinghours.model.DisplayableWeeklySchedule;

@Path("hours")
@RequestScoped
public class OpeningHoursResource {
 
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@Produces(MediaType.TEXT_PLAIN)
	public Response getMessage(InputStream jsonInput) {
		JsonObject object = Json.createReader(jsonInput).readObject();

		List<JsonObject> flatJsonArray = object.entrySet().stream().flatMap(entry -> flattenMapEntry(entry).stream())
				.collect(Collectors.toList());

		List<BusinessHour> businessHours = flatJsonArray.stream()
				.map(json -> new BusinessHour(json.getString("day"), json.getInt("value"), json.getString("type")))
				.collect(Collectors.toList());

		return Response.ok().entity(new DisplayableWeeklySchedule(businessHours).toString()).build();
	}

	private List<JsonObject> flattenMapEntry(Entry<String, JsonValue> mapEntry) {
		List<JsonObject> result = mapEntry
				.getValue().asJsonArray().stream().map(currentHour -> Json
						.createObjectBuilder(currentHour.asJsonObject()).add("day", mapEntry.getKey()).build())
				.collect(Collectors.toList());
		return result;
	}
}
