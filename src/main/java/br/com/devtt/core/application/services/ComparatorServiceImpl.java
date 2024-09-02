package br.com.devtt.core.application.services;

import br.com.devtt.core.abstractions.application.services.ComparatorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("ComparatorServiceImpl")
public class ComparatorServiceImpl implements ComparatorService {
    public <T> boolean hasChanges(T newValue, T oldValue) {
        if (newValue.getClass() != oldValue.getClass()) {
            throw new IllegalArgumentException("The objects must be of the same type.");
        }

        return !newValue.equals(oldValue);
    }
}