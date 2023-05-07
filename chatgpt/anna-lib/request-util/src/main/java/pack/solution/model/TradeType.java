package pack.solution.model;

public enum TradeType {
    UNKNOWN("Unknown"), FX("FX"), COMMODITY("COMMODITY"), IR_SWAP("IR_SWAP");

    private final String tradeTypeName;

    TradeType(String tradeTypeName) {
        this.tradeTypeName = tradeTypeName;
    }

    public String getTradeTypeName() {
        return tradeTypeName;
    }

    public static TradeType fromString(String text) {
        for (TradeType tradeType : TradeType.values()) {
            if (tradeType.tradeTypeName.equalsIgnoreCase(text)) {
                return tradeType;
            }
        }
        return UNKNOWN;
    }
}
