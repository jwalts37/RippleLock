package com.nanoha.CropImage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.util.Log;
import java.io.FileDescriptor;
import java.util.Iterator;
import java.util.WeakHashMap;

public class BitmapManager {
    private static final String TAG = "BitmapManager";
    private static BitmapManager sManager = null;
    private final WeakHashMap<Thread, ThreadStatus> mThreadStatus = new WeakHashMap<>();

    private enum State {
        CANCEL,
        ALLOW
    }

    private static class ThreadStatus {
        public BitmapFactory.Options mOptions;
        public State mState;

        private ThreadStatus() {
            this.mState = State.ALLOW;
        }

        /* synthetic */ ThreadStatus(ThreadStatus threadStatus) {
            this();
        }

        public String toString() {
            String s;
            if (this.mState == State.CANCEL) {
                s = "Cancel";
            } else if (this.mState == State.ALLOW) {
                s = "Allow";
            } else {
                s = "?";
            }
            return "thread state = " + s + ", options = " + this.mOptions;
        }
    }

    public static class ThreadSet implements Iterable<Thread> {
        private final WeakHashMap<Thread, Object> mWeakCollection = new WeakHashMap<>();

        public void add(Thread t) {
            this.mWeakCollection.put(t, (Object) null);
        }

        public void remove(Thread t) {
            this.mWeakCollection.remove(t);
        }

        public Iterator<Thread> iterator() {
            return this.mWeakCollection.keySet().iterator();
        }
    }

    private BitmapManager() {
    }

    private synchronized ThreadStatus getOrCreateThreadStatus(Thread t) {
        ThreadStatus status;
        status = this.mThreadStatus.get(t);
        if (status == null) {
            status = new ThreadStatus((ThreadStatus) null);
            this.mThreadStatus.put(t, status);
        }
        return status;
    }

    private synchronized void setDecodingOptions(Thread t, BitmapFactory.Options options) {
        getOrCreateThreadStatus(t).mOptions = options;
    }

    /* access modifiers changed from: package-private */
    public synchronized void removeDecodingOptions(Thread t) {
        this.mThreadStatus.get(t).mOptions = null;
    }

    public synchronized void allowThreadDecoding(ThreadSet threads) {
        Iterator<Thread> it = threads.iterator();
        while (it.hasNext()) {
            allowThreadDecoding(it.next());
        }
    }

    public synchronized void cancelThreadDecoding(ThreadSet threads) {
        Iterator<Thread> it = threads.iterator();
        while (it.hasNext()) {
            cancelThreadDecoding(it.next());
        }
    }

    public synchronized boolean canThreadDecoding(Thread t) {
        boolean result;
        boolean z;
        ThreadStatus status = this.mThreadStatus.get(t);
        if (status == null) {
            z = true;
        } else {
            if (status.mState != State.CANCEL) {
                result = true;
            } else {
                result = false;
            }
            z = result;
        }
        return z;
    }

    public synchronized void allowThreadDecoding(Thread t) {
        getOrCreateThreadStatus(t).mState = State.ALLOW;
    }

    public synchronized void cancelThreadDecoding(Thread t) {
        ThreadStatus status = getOrCreateThreadStatus(t);
        status.mState = State.CANCEL;
        if (status.mOptions != null) {
            status.mOptions.requestCancelDecode();
        }
        notifyAll();
    }

    public static synchronized BitmapManager instance() {
        BitmapManager bitmapManager;
        synchronized (BitmapManager.class) {
            if (sManager == null) {
                sManager = new BitmapManager();
            }
            bitmapManager = sManager;
        }
        return bitmapManager;
    }

    public Bitmap decodeFileDescriptor(FileDescriptor fd, BitmapFactory.Options options) {
        if (options.mCancel) {
            return null;
        }
        Thread thread = Thread.currentThread();
        if (!canThreadDecoding(thread)) {
            Log.d(TAG, "Thread " + thread + " is not allowed to decode.");
            return null;
        }
        setDecodingOptions(thread, options);
        Bitmap b = BitmapFactory.decodeFileDescriptor(fd, (Rect) null, options);
        removeDecodingOptions(thread);
        return b;
    }
}
