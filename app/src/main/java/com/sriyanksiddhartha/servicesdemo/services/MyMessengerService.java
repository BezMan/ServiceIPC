package com.sriyanksiddhartha.servicesdemo.services;


import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.widget.Toast;

public class MyMessengerService extends Service {

	private class IncomingHandler extends Handler {

		@Override
		public void handleMessage(Message msgFromActivity) {

			switch (msgFromActivity.what) {
				case 43:

					Bundle bundle = msgFromActivity.getData();
					int numOne = bundle.getInt("numOne", 0);
					int numTwo = bundle.getInt("numTwo", 0);

					int result = addNumbers(numOne, numTwo);

					Toast.makeText(getApplicationContext(), "Result : " + result, Toast.LENGTH_SHORT).show();

					// Send data back to the Activity
					Messenger incomingMessenger = msgFromActivity.replyTo;
					Message msgToActivity = Message.obtain(null, 87);

					Bundle bundleToActivity = new Bundle();
					bundleToActivity.putInt("result", result);

					msgToActivity.setData(bundleToActivity);

					try {
						incomingMessenger.send(msgToActivity);
					} catch (RemoteException e) {
						e.printStackTrace();
					}

					break;

				default:
					super.handleMessage(msgFromActivity);
			}
		}
	}

	Messenger mMessenger = new Messenger(new IncomingHandler());

	@Nullable
	@Override
	public IBinder onBind(Intent intent) {
		return mMessenger.getBinder();
	}

	public int addNumbers(int a, int b) {
		return a + b;
	}
}
