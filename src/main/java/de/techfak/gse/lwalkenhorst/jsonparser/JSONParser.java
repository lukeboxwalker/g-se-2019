package de.techfak.gse.lwalkenhorst.jsonparser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONParser<T> {

    private ObjectMapper objectMapper;
    private Class<T> clazz;

    public JSONParser(Class<T> clazz) {
        this.clazz = clazz;
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    public String toJSON(T object) throws SerialisationException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new SerialisationException("Serialisierung fehlgeschlagen");
        }
    }

    public T parseJSON(String json) throws SerialisationException {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new SerialisationException("Deserialisierung fehlgeschlagen");
        }
    }
}
