package br.com.devtt.enterprise.application.services;

import br.com.devtt.enterprise.abstractions.application.services.ComparatorService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier("ComparatorServiceImpl")
public class ComparatorServiceImpl implements ComparatorService {
    public <T> boolean hasChanges(T newValue, T oldValue) {
        if (newValue == null) {
            return false;
        }

        if (newValue.getClass() != oldValue.getClass()) {
            throw new IllegalArgumentException("The objects must be of the same type.");
        }

        return !newValue.equals(oldValue);
    }
}