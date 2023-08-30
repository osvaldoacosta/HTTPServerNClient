import java.util.HashMap;
import java.util.Map;

public class  HandlerJSON {


    public static Map<String, Object> parseJson(String jsonString) {
        Map<String, Object> jsonMap = new HashMap<>();

        // Remove curly braces and split by commas to get key-value pairs
        String[] keyValuePairs = jsonString.substring(1, jsonString.length() - 1).split(",");

        for (String keyValue : keyValuePairs) {
            String[] parts = keyValue.split(":");
            String key = parts[0].trim().replaceAll("\"", "");

            // Trim whitespace and remove double quotes around value
            String valueString = parts[1].trim().replaceAll("\"", "");

            // Remove newline characters if present
            valueString = valueString.replaceAll("\r", "").replaceAll("\n", "");

            // Convert value to appropriate data type
            Object value;
            if (valueString.equals("true") || valueString.equals("false")) {
                value = Boolean.parseBoolean(valueString);
            } else if (valueString.matches("\\d+")) {
                value = Integer.parseInt(valueString);
            } else {
                value = valueString;
            }

            //Gambiarra :D
            if (value.toString().endsWith("}")) {
                value = value.toString().substring(0, value.toString().length() - 1);
            }
            jsonMap.put(key, value);
        }

        return jsonMap;
    }



}
