package org.ht.template;

import java.util.HashMap;
import java.util.Map;

public class Parameters {

    private Map<String, Object> params = new HashMap<>();

    public Parameters() {
    }


    public void put(String key, Object value) {
        params.put(key, value);

    }


    public <T> T get(String key, Class<T> theClass) {
        try {
            return theClass.cast(params.get(key));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
