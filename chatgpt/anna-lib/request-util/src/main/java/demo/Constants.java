package demo;

import model.EnvelopeMessage;
import model.MetaData;
import model.Payload;
import model.TradeType;

public class Constants {
    public static final EnvelopeMessage ENVELOPE_MESSAGE_COMMODITY = new EnvelopeMessage() {
        {
            setDocumentId("123");
            setMetaData(null);
            setPayload(null);
            Payload payload = new Payload();
            payload.setNotionalCurrencyOne("USD");
            payload.setNotionalCurrencyTwo("EUR");
            payload.setSpecialCode("123");
            setPayload(payload);
            MetaData metaData = new MetaData();
            metaData.setDocumentId("123");
            metaData.setEnvelopeId("456");
            metaData.setTradeType(TradeType.COMMODITY);
            setMetaData(metaData);
        }
    };

    ;

    public static final EnvelopeMessage ENVELOPE_MESSAGE_FX = new EnvelopeMessage() {
        {
            setDocumentId("123");
            setMetaData(null);
            setPayload(null);
            Payload payload = new Payload();
            payload.setNotionalCurrencyOne("USD");
            payload.setNotionalCurrencyTwo("EUR");
            payload.setSpecialCode("123");
            setPayload(payload);
            MetaData metaData = new MetaData();
            metaData.setDocumentId("123");
            metaData.setEnvelopeId("456");
            metaData.setTradeType(TradeType.FX);
            setMetaData(metaData);

        }


    };
}
