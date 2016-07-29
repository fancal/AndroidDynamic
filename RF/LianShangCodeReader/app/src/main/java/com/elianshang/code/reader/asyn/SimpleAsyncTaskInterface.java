package com.elianshang.code.reader.asyn;

/**
 * 异步任务接口
 * */
public interface SimpleAsyncTaskInterface <D>{

	/**
	 * 异步任务开始前
	 * */
	boolean onPreExecute();
	
	/**
	 * 异步任务执行
	 * */
	D doInBackground();
	
	/**
	 * 异步任务完成
	 * */
	void onPostExecute(D result);
	
}
