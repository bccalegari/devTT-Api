package br.com.devtt.core.application.services;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ComparatorServiceImplUnitTest {
    private final ComparatorServiceImpl comparatorService = new ComparatorServiceImpl();

    @Test
    void shouldReturnTrueWhenObjectsAreDifferent() {
        var newValue = "newValue";
        var oldValue = "oldValue";

        boolean result = comparatorService.hasChanges(newValue, oldValue);

        assertTrue(result);
    }

    @Test
    void shouldReturnFalseWhenObjectsAreEqual() {
        var newValue = "value";
        var oldValue = "value";

        boolean result = comparatorService.hasChanges(newValue, oldValue);

        assertFalse(result);
    }
}