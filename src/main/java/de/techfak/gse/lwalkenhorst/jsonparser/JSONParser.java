package de.techfak.gse.lwalkenhorst.jsonparser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * JSONParser to convert object and json.
 * Converts objects to json.
 * Converts json to object.
 * Object that will be converted needs
 * to specify setter and getter methods.
 */
public class JSONParser {

    private ObjectMapper objectMapper;

    public JSONParser() {
        this.objectMapper = new ObjectMapper().findAndRegisterModules();
    }

    /**
     * Creates a json string form given object.
     * @param object to convert.
     * @return a new json string
     * @throws SerialisationException if serialisation fails
     */
    public String toJSON(Object object) throws SerialisationException {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            throw new SerialisationException("Serialisation failed");
        }
    }

    /**
     * Creates a new object from given json string.
     * Need to know the class of the object to convert to
     *
     * @param json the json string representing a class of type T
     * @param clazz the class of the object to create
     * @param <T> the type if object to create
     * @return a new object of type T converted form the json objectMapper
     * @throws SerialisationException if deserialisation fails
     */
    public <T> T parseJSON(String json, Class<T> clazz) throws SerialisationException {
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            throw new SerialisationException("Deserialisation failed");
        }
    }
}
