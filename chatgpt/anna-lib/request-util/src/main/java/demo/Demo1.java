package demo;

import model.EnvelopeMessage;
import model.MetaData;
import model.Payload;
import model.TradeType;
import solution.Product;
import solution.ProductFactory;

public class Demo1 {
    public static void main(String[] args) {
        EnvelopeMessage envelopeMessage = Constants.ENVELOPE_MESSAGE_COMMODITY;

        Product product = ProductFactory.createProduct(envelopeMessage);
        String requestBody = product.getRequestBody();
        System.out.println(requestBody);

    }
}
