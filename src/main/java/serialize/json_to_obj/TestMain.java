package serialize.json_to_obj;

import serialize.json_to_obj.serializer.JsonSerializer;
import serialize.json_to_obj.serializer.Serializer;
import serialize.json_to_obj.serializer.SerializerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author yq
 * @version v1.0 2023-02-13 9:35 PM
 */
public class TestMain {
    public static void main(String[] args) {
        Serializer serializer = SerializerFactory.getSerializer((byte) 1);
        Map<String, String> map = new HashMap<String, String>() {{
            put("k1","v1");
            put("k2","v2");
            put("k3","v3");
        }};

        byte[] bytes = serializer.serialize(map);

        Map<String,String> deserialize = serializer.deserialize(bytes, Map.class);

        System.out.println(deserialize.toString());

    }
}
