package pack.solution;

import pack.solution.model.EnvelopeMessage;

@FunctionalInterface
public interface ParameterValueProvider {
    String getValue(EnvelopeMessage envelopeMessage);
}
