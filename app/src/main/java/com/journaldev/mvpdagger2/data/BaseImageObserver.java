package com.journaldev.mvpdagger2.data;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.Handler;
import java.util.Timer;
import java.util.TimerTask;

public abstract class BaseImageObserver extends ContentObserver {
    private Context context;
    private Timer waitingTimer;
    private final Handler handler;

    public BaseImageObserver(Handler handler, Context context) {
        super(handler);
        this.context = context;
        this.handler = handler;
    }

    @Override
    public boolean deliverSelfNotifications() {
        return false;
    }

    @Override
    public void onChange(boolean arg0) {
        super.onChange(arg0);
        sendDelayAction();
    }

    private void sendDelayAction() {

        if (waitingTimer != null) {
            waitingTimer.cancel();
            waitingTimer = null;
        }

        waitingTimer = new Timer();

        final TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    Cursor cursor = getCursor(context);
                    loadData(cursor);
                    notifyImage();
                });
            }
        };

        waitingTimer.schedule(timerTask, 250);
    }

    private void runOnUiThread(Runnable r) {
        handler.post(r);
    }

    abstract public void loadData(Cursor cursor);

    abstract public Cursor getCursor(Context context);

    abstract public void notifyImage();
}
