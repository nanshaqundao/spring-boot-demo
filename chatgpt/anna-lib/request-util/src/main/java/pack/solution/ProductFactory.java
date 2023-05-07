package pack.solution;

import pack.solution.model.EnvelopeMessage;

public class ProductFactory {
    public static Product createProduct(EnvelopeMessage envelopeMessage) {
        String productType = envelopeMessage.getMetaData().getTradeType().getTradeTypeName();

        if ("FX".equals(productType)) {
            return new ForeignExchangeProduct(envelopeMessage);
        } else if ("COMMODITY".equals(productType)) {
            return new CommodityProduct(envelopeMessage);
        } else {
            throw new IllegalArgumentException("Unknown product type: " + productType);
        }
    }
}
