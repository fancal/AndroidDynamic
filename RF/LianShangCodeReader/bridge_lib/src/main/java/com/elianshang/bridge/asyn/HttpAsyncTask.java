package com.elianshang.bridge.asyn;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;

import com.elianshang.bridge.tool.DialogTools;
import com.elianshang.tools.NetWorkTool;
import com.elianshang.tools.ToastTool;
import com.elianshang.tools.WeakReferenceHandler;
import com.xue.http.hook.BaseBean;
import com.xue.http.impl.DataHull;

/**
 * 网络请求的异步任务
 */
public abstract class HttpAsyncTask<T extends BaseBean> extends BaseTaskImpl<DataHull<T>, T> {

    /**
     * 上下文对象
     */
    protected Context context;

    /**
     * 错误信息
     */
    private String message;

    /**
     * 状态
     */
    private int status;

    /**
     * 是否显示toast
     */
    private boolean showToast = false;
    /**
     * 是否显示加载dialog
     */
    private boolean showLoading = false;

    private Dialog loadingDialog;

    protected boolean hasNet = true;

    private boolean cancelable = true;

    public HttpAsyncTask(Context context) {
        this.context = context;
    }

    public HttpAsyncTask(Context context, boolean showToast) {
        this(context);
        this.showToast = showToast;
    }

    public HttpAsyncTask(Context context, boolean showToast, boolean showLoading) {
        this(context, showToast);
        this.showLoading = showLoading;
    }

    public HttpAsyncTask(Context context, boolean showToast, boolean showLoading, boolean cancelable) {
        this(context, showToast);
        this.showLoading = showLoading;
        this.cancelable = cancelable;
    }

    @Override
    public final T run() {
        try {
            hasNet = NetWorkTool.isNetAvailable(context);

            if (!hasNet) {// 判断网络
                if (showLoading && context instanceof Activity) {
                    postUI(this, new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                        @Override
                        public void run(HttpAsyncTask httpAsyncTask) {
                            DialogTools.showTwoButtonDialog((Activity) context, "没有网络,请检查网络后,是否重试", "取消", "重试", null, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    HttpAsyncTask.this.start();
                                }
                            }, true);
                        }
                    });
                }

                postUI(this, new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                    @Override
                    public void run(HttpAsyncTask httpAsyncTask) {
                        try {
                            httpAsyncTask.netNull();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
                return null;
            }

            if (!isCancel()) {// 加载网络数据
                showLoadingDialog();
                DataHull<T> dh = doInBackground();

                dissmissLoadingDialog();

                final DataHull<T> dataHull = dh;

                if (dataHull == null) {
                    postUI(this, new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                        @Override
                        public void run(HttpAsyncTask httpAsyncTask) {
                            try {
                                httpAsyncTask.netErr(null);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
                    return null;
                } else {
                    this.message = dataHull.getMessage();
                    this.status = dataHull.getStatus();
                    if (dataHull.getDataType() == DataHull.DataType.DATA_IS_INTEGRITY) {
                        T t = dataHull.getDataEntity();
                        if (t == null) {
                            postUI(this, new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                                @Override
                                public void run(HttpAsyncTask httpAsyncTask) {
                                    try {
                                        httpAsyncTask.dataNull(message);
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                            });

                            return null;
                        } else {
                            return t;
                        }
                    } else {
                        if (dataHull.getDataType() == DataHull.DataType.DATA_CAN_NOT_PARSE) {

                        } else if (dataHull.getDataType() == DataHull.DataType.CONNECTION_FAIL
                                || dataHull.getDataType() == DataHull.DataType.RESPONSE_CODE_ERR
                                || dataHull.getDataType() == DataHull.DataType.DATA_CAN_NOT_PARSE
                                || dataHull.getDataType() == DataHull.DataType.DATA_IS_NULL) {
                            if (showLoading && context instanceof Activity) {
                                postUI(this, new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                                    @Override
                                    public void run(HttpAsyncTask httpAsyncTask) {
                                        DialogTools.showTwoButtonDialog((Activity) context, "网络异常,是否重试", "取消", "重试", null, new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                HttpAsyncTask.this.start();
                                            }
                                        }, true);
                                    }
                                });
                            }
                        }

                        postUI(this, new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                            @Override
                            public void run(HttpAsyncTask httpAsyncTask) {
                                try {
                                    httpAsyncTask.netErr(message);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                        return null;
                    }
                }
            } else {
                ToastTool.show(context, "任务取消了");
            }
        } catch (Exception e) {
            // 线程异常
            e.printStackTrace();
            dissmissLoadingDialog();
            postUI(this, new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                @Override
                public void run(HttpAsyncTask httpAsyncTask) {
                    try {
                        httpAsyncTask.netErr(null);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        return null;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void onPreExecute() {
    }

    /**
     * 没有网络，回调
     */
    public void netNull() {
        if (showToast && null != context) {
            ToastTool.show(context, "请检查网络");
        }
    }

    /**
     * 网络异常和数据错误，回调
     */
    public void netErr(String errMsg) {
        if (showToast && null != context) {
            if (!TextUtils.isEmpty(errMsg)) {
                ToastTool.show(context, errMsg);
            } else {
                ToastTool.show(context, "网络异常");
            }
        }
    }

    /**
     * 数据为空，回调
     */
    public void dataNull(String errMsg) {
        if (showToast && null != context && !TextUtils.isEmpty(errMsg)) {
            ToastTool.show(context, errMsg);
        }
    }

    private void showLoadingDialog() {
        if (showLoading && null != context) {
            postUI(this, new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                @Override
                public void run(HttpAsyncTask httpAsyncTask) {
                    try {
                        loadingDialog = DialogTools.showLoadingDialog(context);
                        loadingDialog.setCancelable(cancelable);
                        loadingDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialog) {
                                cancel();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void dissmissLoadingDialog() {
        if (showLoading && null != context) {
            postUI(this, new WeakReferenceHandler.WeakReferenceHandlerRunnalbe<HttpAsyncTask>() {
                @Override
                public void run(HttpAsyncTask httpAsyncTask) {
                    try {
                        if (null != loadingDialog && loadingDialog.isShowing()) {
                            loadingDialog.dismiss();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }
}