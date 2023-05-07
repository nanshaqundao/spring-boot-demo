package pack.solution;

import com.google.gson.JsonObject;
import pack.solution.model.EnvelopeMessage;

import java.util.Map;

public class ForeignExchangeProduct extends Product {
    public ForeignExchangeProduct(EnvelopeMessage envelopeMessage) {
        super(envelopeMessage);
    }

    @Override
    public String getRequestURL() {
        return null;
    }


    @Override
    public Map<String, String> getRequestHeaders() {
        return null;
    }

    @Override
    public String getRequestBody() {
        String currency1 = envelopeMessage.getPayload().getNotionalCurrencyOne();
        String currency2 = envelopeMessage.getPayload().getNotionalCurrencyTwo();
        String expiryDate = envelopeMessage.getPayload().getSpecialCode();

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("currency1", currency1);
        requestBody.addProperty("currency2", currency2);
        requestBody.addProperty("ExpiryDate", expiryDate);

        return requestBody.toString();
    }

    // Implement getRequestUrl, getRequestHeaders and getRequestBody
}