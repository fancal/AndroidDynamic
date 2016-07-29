package com.elianshang.code.reader.asyn;

import android.os.Handler;
import android.os.Looper;

public abstract class UpdateBaseAsyncTask<Progress, Result> extends BaseTaskImpl {
	
	private Handler mHandler = new Handler(Looper.getMainLooper());

	public final void execute() {
		onPreExecute();
		mThreadPool.addNewTask(this);
	}

	protected void onPreExecute() {
	};

	protected abstract Result doInBackground();

	protected void onPostExecute(Result result) {
		isCancel = true;
	}

	protected void onProgressUpdate(Progress... values) {
	};

	protected final void publishProgress(final Progress... values) {
		if (!isCancel) {
			postUI(new Runnable() {

				@Override
				public void run() {
					onProgressUpdate(values);
				}
			});

		}
	}
	
	private void postUI(Runnable runnable){
		if(Thread.currentThread() != Looper.getMainLooper().getThread()){
			mHandler.post(runnable);
		}else{
			runnable.run() ;
		}
	}

	@Override
	public int run() {
		if (!isCancel) {
			final Result result = doInBackground();
			postUI(new Runnable() {

				@Override
				public void run() {
					if (!isCancel) {
						onPostExecute(result);
					}
				}
			});
			
			return SUCCESS ;
		}
		
		return FAIL ;
	}
}
