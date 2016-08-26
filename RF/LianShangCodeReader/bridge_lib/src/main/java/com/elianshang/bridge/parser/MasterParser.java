package com.elianshang.bridge.parser;

import com.elianshang.bridge.tool.TimestampTool;
import com.elianshang.tools.DateTool;
import com.xue.http.hook.BaseBean;
import com.xue.http.parse.MainParser;

import org.json.JSONException;
import org.json.JSONObject;


/**
 * 移动端接口，解析器父类 ｛ header:{status:"x"}, body:{...} ｝ 针对返回模式这样的解析
 */
public abstract class MasterParser<T extends BaseBean> extends MainParser<T, JSONObject> {

    /**
     * 接口返回头
     */
    protected final String HEAD = "head";

    /**
     * 接口返回状态 1为正确 , 2 参数异常 , 3 业务异常 , 4 系统异常
     */
    protected final String STATUS = "status";

    /**
     * 接口返回的信息
     */
    protected final String MSG = "message";

    /**
     * 接口数据key
     */
    protected final String DATA_KEY = "datakey";

    /**
     * 接口返回数据节点
     */
    protected final String BODY = "body";

    /**
     * 时间戳
     */
    protected final String TIMESTAMP = "timestamp";

    /**
     * 接口状态类型
     */
    public interface STATE {

        /**
         * 接口正常返回
         */
        int NORMAL = 1;

        /**
         * 参数错误
         */
        int PARAMETERSERR = 2;

        /**
         * 业务异常
         */
        int DATAEXCEPTION = 3;

        /**
         * 系统异常
         */
        int SYSTEMEXCEPTION = 4;

        /**
         * token非法
         */
        int TOKENILLEGAL = 5;
    }

    public MasterParser() {
        super();
    }

    @Override
    protected JSONObject initData(String data) throws Exception {
        JSONObject jsonObject = new JSONObject(data);
        return jsonObject;
    }

    @Override
    protected final boolean canParse(JSONObject data) {
        try {
            if (!data.has(HEAD)) {
                return false;
            }

            JSONObject head = optJSONObject(data, HEAD);

            setStatus(getInt(head, STATUS));

            setMessage(getString(head, MSG));

            if (has(head, TIMESTAMP)) {
                long timestamp = getLong(head, TIMESTAMP);
                long cur = DateTool.getDateLong();
                TimestampTool.timeOffset = timestamp * 1000 - cur;
            }

            if (isNormalData()) {
                if (has(data, DATA_KEY)) {
                    setDataKey(getString(data, DATA_KEY));
                }
                return true;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    protected JSONObject getData(String data) throws JSONException {
        JSONObject object = null;
        if (getStatus() == STATE.NORMAL) {
            object = new JSONObject(data);
            object = optJSONObject(object, BODY);
        }

        return object;
    }


    @Override
    public boolean hasUpdate() {
        return true;
    }

    /**
     * 是否是正常
     */
    public boolean isNormalData() {
        return getStatus() == STATE.NORMAL;
    }
}
