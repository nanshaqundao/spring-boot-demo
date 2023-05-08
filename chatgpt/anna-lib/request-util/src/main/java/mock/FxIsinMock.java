package mock;

import pack.solution3.model3.CommonRequestBodyUnit;
import pack.solution3.model3.CommonRequestUnit;

public class FxIsinMock {
    public static final String MOCK_REQUEST = "{\n" +
            "    \"url\": \"localhost\",\n" +
            "    \"httpMethod\": \"GET\",\n" +
            "    \"requestBody\": {\n" +
            "        \"NotionalCurrency1\": \"USD\",\n" +
            "        \"NotionalCurrency2\": \"EUR\",\n" +
            "        \"ExpiryDate\": \"2023-05-15\"\n" +
            "    }\n" +
            "}";

    public static final String MOCK_REQUEST_BODY = "{\n" +
            "    \"NotionalCurrency1\": \"USD\",\n" +
            "    \"NotionalCurrency2\": \"EUR\",\n" +
            "    \"ExpiryDate\": \"2023-05-15\"\n" +
            "}";

    public static CommonRequestUnit getMockRequest() {
        CommonRequestUnit request = new CommonRequestUnit();
        request.setUrl("localhost");
        request.setHttpMethod("GET");
        request.setRequestBody(FxIsinMock.getMockRequestBody());
        return request;
    }

    public static CommonRequestBodyUnit getMockRequestBody() {
        CommonRequestBodyUnit requestBody = new CommonRequestBodyUnit();
        requestBody.getStringPropertiesMap().put("NotionalCurrency1", "USD");
        requestBody.getStringPropertiesMap().put("NotionalCurrency2", "EUR");
        requestBody.getStringPropertiesMap().put("ExpiryDate", "2023-05-15");
        return requestBody;
    }

}
