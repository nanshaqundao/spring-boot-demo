package solution;

import model.EnvelopeMessage;

@FunctionalInterface
public interface ParameterValueProvider {
    String getValue(EnvelopeMessage envelopeMessage);
}
