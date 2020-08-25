package no.ion.triplea.stats;

import java.io.IOException;
import java.io.UncheckedIOException;

public class Exceptions {
    @FunctionalInterface
    public interface IOExceptionThrowingSupplier<T> {
        T get() throws IOException;
    }

    public static <T> T uncheckIO(IOExceptionThrowingSupplier<T> supplier) {
        try {
            return supplier.get();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
