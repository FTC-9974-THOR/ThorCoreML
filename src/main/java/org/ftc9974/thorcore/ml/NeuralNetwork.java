package org.ftc9974.thorcore.ml;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;

import com.qualcomm.robotcore.util.RobotLog;

import org.ftc9974.thorcore.cv.OpenCVLoader;
import org.ftc9974.thorcore.ml.classifier.ImageClassifier;
import org.ftc9974.thorcore.ml.classifier.ImageClassifierQuantizedMobileNet;

import java.io.IOException;

public class NeuralNetwork {

    private static final String TAG = "org.ftc9974.thorcore.ml.NeuralNetwork";

    private static final String HANDLER_THREAD_NAME = "NeuralNetworkHandler";

    private ImageClassifier classifier;
    private HandlerThread classifierHandlerThread;
    private Handler classifierHandler;
    private final Object lock;
    private boolean runClassifier;

    private Runnable classifyRunnable = new Runnable() {
        @Override
        public void run() {
            synchronized (lock) {
                if (runClassifier) {
                    classify();
                }
            }
            classifierHandler.post(classifyRunnable);
        }
    };

    public NeuralNetwork(Context context) {
        OpenCVLoader.loadIfNotAlready();
        try {
            classifier = new ImageClassifierQuantizedMobileNet((Activity) context);
        } catch (IOException e) {
            RobotLog.ee(TAG, e, "Model file not found");
        }
        lock = new Object();

        synchronized (lock) {
            runClassifier = false;
        }
    }

    private void classify() {

    }

    public void activate() {
        classifierHandlerThread = new HandlerThread(HANDLER_THREAD_NAME);
        classifierHandlerThread.start();
        classifierHandler = new Handler(classifierHandlerThread.getLooper());
        synchronized (lock) {
            runClassifier = true;
        }
        classifierHandler.post(classifyRunnable);
    }

    public void deactivate() {
        classifierHandlerThread.quitSafely();
        try {
            classifierHandlerThread.join();
            classifierHandlerThread = null;
            classifierHandler = null;
            synchronized (lock) {
                runClassifier = false;
            }
        } catch (InterruptedException e) {
            RobotLog.ee(TAG, e, "Interrupted while stopping classification thread");
        }
    }

    public void close() {
        classifier.close();
    }
}
