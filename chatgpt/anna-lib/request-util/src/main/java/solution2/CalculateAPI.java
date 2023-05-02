package solution2;

public class CalculateAPI {

    public String call(ApiRequestBody requestBody) {
        // Make the actual API call using the requestBody
        // This is just a placeholder
        return "API result for " + requestBody.getName();
    }

    public String getOuterValue() {
        // Make the actual API call to get the outerValue
        return "outerValueResult";
    }

    public String getCrossRate() {
        // Make the actual API call to get the crossRate
        return "crossRateResult";
    }
}
