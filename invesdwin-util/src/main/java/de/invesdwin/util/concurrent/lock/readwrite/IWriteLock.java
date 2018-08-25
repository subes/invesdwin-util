package de.invesdwin.util.concurrent.lock.readwrite;

import de.invesdwin.util.concurrent.lock.ILock;

public interface IWriteLock extends ILock {

    boolean isHeldByCurrentThread();

    int getHoldCount();

}
