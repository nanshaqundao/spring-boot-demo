package pack.solution;

import com.google.gson.JsonObject;
import pack.solution.model.EnvelopeMessage;

import java.util.Map;

public class CommodityProduct extends Product {
    public CommodityProduct(EnvelopeMessage envelopeMessage) {
        super(envelopeMessage);
        valueProviders.put("referenceRate", this::getReferenceRate);
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
        String referenceRate = getParameterValue("referenceRate");
        String currency1 = envelopeMessage.getPayload().getNotionalCurrencyOne();
        String currency2 = envelopeMessage.getPayload().getNotionalCurrencyTwo();
        String expiryDate = envelopeMessage.getPayload().getSpecialCode();

        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("referenceRate", referenceRate);
        requestBody.addProperty("currency1", currency1);
        requestBody.addProperty("currency2", currency2);
        requestBody.addProperty("ExpiryDate", expiryDate);

        return requestBody.toString();
    }

    // Implement getRequestUrl, getRequestHeaders and getRequestBody

    private String getReferenceRate(EnvelopeMessage envelopeMessage) {
        return "1.0";
    }
}