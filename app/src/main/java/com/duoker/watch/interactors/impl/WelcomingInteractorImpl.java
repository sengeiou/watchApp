package com.duoker.watch.interactors.impl;

import com.duoker.watch.executor.Executor;
import com.duoker.watch.executor.MainThread;
import com.duoker.watch.interactors.WelcomingInteractor;
import com.duoker.watch.interactors.base.AbstractInteractor;
import com.duoker.watch.repository.MessageRepository;

/**
 * Created by chengang on 4/25/2017.
 */

public class WelcomingInteractorImpl extends AbstractInteractor implements WelcomingInteractor {

    private WelcomingInteractor.Callback mCallback;
    private MessageRepository mMessageRepository;

    public WelcomingInteractorImpl(Executor threadExecutor,
                                   MainThread mainThread,
                                   Callback callback, MessageRepository messageRepository) {
        super(threadExecutor, mainThread);
        mCallback = callback;
        mMessageRepository = messageRepository;
    }
    public WelcomingInteractorImpl(  Callback callback, MessageRepository messageRepository) {
        super( );
        mCallback = callback;
        mMessageRepository = messageRepository;
    }
    private void notifyError() {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onRetrievalFailed("Nothing to welcome you with :(");
            }
        });
    }

    private void postMessage(final String msg) {
        mMainThread.post(new Runnable() {
            @Override
            public void run() {
                mCallback.onMessageRetrieved(msg);
            }
        });
    }

    @Override
    public void run() {

        // retrieve the message
        final String message = mMessageRepository.getWelcomeMessage();

        // check if we have failed to retrieve our message
        if (message == null || message.length() == 0) {
            // notify the failure on the main thread
            notifyError();
            return;
        }

        // we have retrieved our message, notify the UI on the main thread
        postMessage(message);
    }
}