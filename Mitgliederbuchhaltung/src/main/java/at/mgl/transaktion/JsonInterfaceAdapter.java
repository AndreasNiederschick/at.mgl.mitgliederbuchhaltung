package at.mgl.transaktion;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class JsonInterfaceAdapter implements JsonSerializer, JsonDeserializer {
	
	private static final String CLASSNAME = "CLASSNAME";
	private static final String DATA = "DATA";

	@Override
	public Object deserialize(JsonElement jsonElement, Type typeOfT, JsonDeserializationContext context)
			throws JsonParseException {
		
		JsonObject jsonObject = jsonElement.getAsJsonObject();
		JsonPrimitive prim = (JsonPrimitive)jsonObject.get(CLASSNAME);
		String className = prim.getAsString();
		Class cls = getObjectClass(className);
		
		return context.deserialize(jsonObject.get(DATA),  cls);
	}

	@Override
	public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
		JsonObject jsonObject = new JsonObject();
		jsonObject.addProperty(CLASSNAME, src.getClass().getName());
		jsonObject.add(DATA, context.serialize(src));
		return jsonObject;
	}
	
	public Class getObjectClass(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			//e.printStackrace();
			throw new JsonParseException(e.getMessage());
		}
	}

}
