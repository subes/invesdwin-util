package de.invesdwin.util.lang.finalizer;

import javax.annotation.concurrent.ThreadSafe;

import de.invesdwin.util.lang.Reflections;
import de.invesdwin.util.lang.finalizer.internal.FallbackFinalizerManagerProvider;
import de.invesdwin.util.lang.finalizer.internal.IFinalizerManagerProvider;
import de.invesdwin.util.lang.finalizer.internal.JavaFinalizerManagerProvider;

@ThreadSafe
public final class FinalizerManager {

    private static final IFinalizerManagerProvider PROVIDER;

    static {
        if (Reflections.classExists(IFinalizerManagerProvider.JAVA_CLEANER_CLASS)) {
            PROVIDER = new JavaFinalizerManagerProvider();
        } else {
            PROVIDER = new FallbackFinalizerManagerProvider();
        }
    }

    private FinalizerManager() {}

    public static IFinalizerReference register(final Object obj, final AFinalizer finalizer) {
        return PROVIDER.register(obj, finalizer);
    }

}
