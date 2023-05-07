package demo;

import pack.solution.model.EnvelopeMessage;
import pack.solution.Product;
import pack.solution.ProductFactory;

public class Demo1 {
    public static void main(String[] args) {
        EnvelopeMessage envelopeMessage = Constants.ENVELOPE_MESSAGE_COMMODITY;

        Product product = ProductFactory.createProduct(envelopeMessage);
        String requestBody = product.getRequestBody();
        System.out.println(requestBody);

    }
}
