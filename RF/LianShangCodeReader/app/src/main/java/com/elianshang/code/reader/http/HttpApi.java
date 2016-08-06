package com.elianshang.code.reader.http;

import android.content.Context;

import com.elianshang.code.reader.BaseApplication;
import com.elianshang.code.reader.bean.Item;
import com.elianshang.code.reader.bean.ReceiptInfo;
import com.elianshang.code.reader.bean.ResponseState;
import com.elianshang.code.reader.bean.Shelve;
import com.elianshang.code.reader.bean.ShelveCreate;
import com.elianshang.code.reader.bean.TakeStockDetail;
import com.elianshang.code.reader.bean.TakeStockList;
import com.elianshang.code.reader.bean.TaskTransfer;
import com.elianshang.code.reader.bean.TaskTransferDetail;
import com.elianshang.code.reader.bean.User;
import com.elianshang.code.reader.parser.ItemParser;
import com.elianshang.code.reader.parser.ReceiptGetOrderInfoParser;
import com.elianshang.code.reader.parser.ResponseStateParser;
import com.elianshang.code.reader.parser.ShelveCreateParser;
import com.elianshang.code.reader.parser.ShelveParser;
import com.elianshang.code.reader.parser.TakeStockDetailParser;
import com.elianshang.code.reader.parser.TakeStockListParser;
import com.elianshang.code.reader.parser.TaskTransferDetailParser;
import com.elianshang.code.reader.parser.TaskTransferParser;
import com.elianshang.code.reader.parser.UserParser;
import com.elianshang.code.reader.tool.AppTool;
import com.elianshang.code.reader.tool.ConfigTool;
import com.xue.http.hook.BaseBean;
import com.xue.http.hook.BaseHttpParameter;
import com.xue.http.hook.BaseKVP;
import com.xue.http.impl.DataHull;
import com.xue.http.impl.DefaultKVPBean;
import com.xue.http.okhttp.OkHttpHandler;
import com.xue.http.parse.BaseParser;

import java.util.ArrayList;
import java.util.List;


public class HttpApi {

    public static String base_url;

    /**
     * 公钥
     */
    public static String secretKey = null;

    static {
        build();
    }

    private static List<BaseKVP> default_headers = null;

    private interface Header {

        /**
         * 用户ID
         */
        String uid = "uid";

        /**
         * 用户token
         */
        String token = "utoken";

        /**
         * app唯一标示传imei
         */
        String app_key = "app-key";

        /**
         * 平台(1.H5  2.Android)
         */
        String platform = "platform";

        /**
         * app版本
         */
        String version = "app-version";

        /**
         * api版本
         */
        String api_version = "api-version";

    }

    /**
     * 公有参数
     */
    private interface PublicParameter {

        String timestampt = "timestamp";
    }

    /**
     * 1.登录
     */
    private interface UserInfoLogin {

        String _function = "v1/user/login";

        /**
         * 用户名
         */
        String username = "userName";

        /**
         * 密码
         */
        String passwd = "passwd";

    }

    /**
     * 2.根据订单号、托盘码和国条码查询接口
     */
    private interface ReceiptGetOrderInfo {

        String _function = "v1/order/po/receipt/getorderinfo";

        /**
         * 物美订单编号(扫描获得)
         */
        String orderOtherId = "orderOtherId";

        /**
         * 托盘码(扫描获得)
         */
        String containerId = "containerId";

        /**
         * 商品国条(扫描获得)
         */
        String barCode = "barCode";

    }

    /**
     * 3.收货单接口
     */
    private interface ReceiptAdd {

        String _function = "v1/order/po/receipt/add";

        /**
         * 物美订单编号
         */
        String orderOtherId = "orderOtherId";

        /**
         * 预约单号
         */
        String bookingNum = "bookingNum";

        /**
         * 托盘码(扫描获得)
         */
        String containerId = "containerId";

        /**
         * 收货码头
         */
        String receiptWharf = "receiptWharf";

        /**
         * 收货明细数组
         */
        String items = "items";

        /**
         * 批次号
         */
        String items_lotNum = "lotNum";

        /**
         * 商品国条码
         */
        String barCode = "barCode";

        /**
         * 实际收货数
         */
        String items_inboundQty = "inboundQty";

        /**
         * 生产日期
         */
        String items_proTime = "proTime";

    }

    /**
     * 4.创建上架任务
     */
    private interface ShelveCreateTask {

        String _function = "v1/inbound/shelve/createTask";

        /**
         * 托盘码
         */
        String containerId = "containerId";

    }

    /**
     * 5.扫托盘领取上架任务（冯坤）
     */
    private interface ShelveScanContainer {

        String _function = "v1/inbound/shelve/scanContainer";

        /**
         * 操作员
         */
        String operator = "operator";

        /**
         * 托盘码
         */
        String containerId = "containerId";

    }

    /**
     * 6.扫货架位完成上架操作（冯坤）
     */
    private interface ShelveScanTargetLocation {

        String _function = "v1/inbound/shelve/scanTargetLocation";

        /**
         * 任务ID
         */
        String taskId = "taskId";

        /**
         * 位置ID
         */
        String locationId = "locationId";

    }

    /**
     * 7.扫描库位获取商品信息（马力）
     */
    private interface StockGetItem {

        String _function = "v1/inhouse/stock/getItem";

        /**
         * 位置ID
         */
        String locationId = "locationId";

        String barCode = "barcode";

    }

    /**
     * 8.转残次（马力））
     */
    private interface InhouseCreateScrap {

        String _function = "v1/inhouse/stock_transfer/createScrap";

        String locationId = "locationId";

        String barCode = "barcode";

        String uomQty = "uomQty";

        String uId = "uId";

    }

    /**
     * 9.转退货（马力）
     */
    private interface InhouseCreateReturn {

        String _function = "v1/inhouse/stock_transfer/createReturn";

        String locationId = "locationId";

        String barCode = "barcode";

        String uomQty = "uomQty";

        String uId = "uId";

    }

    /**
     * 10.领取补货任务（马力）
     */
    private interface ProcurementFetchTask {
        String _function = "v1/inhouse/procurement/fetchTask";

        /**
         * 库位id
         */
        String locationId = "locationId";

        /**
         * 操作员id
         */
        String uId = "uId";
    }

    /**
     * 11.查看补货任务详情（马力）
     */
    private interface ProcurementView {

        String _function = "v1/inhouse/procurement/view";

        /**
         * 任务ID
         */
        String taskId = "taskId";

    }

    /**
     * 12.补货-转出（马力）
     */
    private interface ProcurementScanFromLocation {

        String _function = "v1/inhouse/procurement/scanFromLocation";

        /**
         * 任务id
         */
        String taskId = "taskId";

        /**
         * 库位id
         */
        String locationId = "locationId";

        /**
         * 操作员id
         */
        String uId = "uId";

        /**
         * 数量
         */
        String uomQty = "uomQty";

    }

    /**
     * 13.补货--转入（马力）
     */
    private interface ProcurementScanToLocation {

        String _function = "v1/inhouse/procurement/scanToLocation";

        /**
         * 任务id
         */
        String taskId = "taskId";

        /**
         * 库位id
         */
        String locationId = "locationId";

        /**
         * 操作员id
         */
        String uId = "uId";

        /**
         * 数量
         */
        String uomQty = "uomQty";

    }

    /**
     * 10.领取移库任务（马力）
     */
    private interface StockTransferFetchTask {
        String _function = "v1/inhouse/stock_transfer/fetchTask";

        /**
         * 库位id
         */
        String locationId = "locationId";

        /**
         * 操作员id
         */
        String uId = "uId";
    }

    /**
     * 11.查看任务详情（马力）
     */
    private interface StockTransferView {

        String _function = "v1/inhouse/stock_transfer/view";

        /**
         * 任务ID
         */
        String taskId = "taskId";

    }

    /**
     * 12.转出（马力）
     */
    private interface StockTransferScanFromLocation {

        String _function = "v1/inhouse/stock_transfer/scanFromLocation";

        /**
         * 任务id
         */
        String taskId = "taskId";

        /**
         * 库位id
         */
        String locationId = "locationId";

        /**
         * 操作员id
         */
        String uId = "uId";

        /**
         * 数量
         */
        String uomQty = "uomQty";

    }

    /**
     * 13.转入（马力）
     */
    private interface StockTransferScanToLocation {

        String _function = "v1/inhouse/stock_transfer/scanToLocation";

        /**
         * 任务id
         */
        String taskId = "taskId";

        /**
         * 库位id
         */
        String locationId = "locationId";

        /**
         * 操作员id
         */
        String uId = "uId";

        /**
         * 数量
         */
        String uomQty = "uomQty";

    }


    /**
     * 13.领取盘点任务（吴昊）
     */
    private interface StockTakingAssign {

        String _function = "v1/inhouse/stock_taking/assign";

        String uId = "uId";

    }

    /**
     * 13.盘点任务详情（吴昊）
     */
    private interface StockTakingGetTask {

        String _function = "v1/inhouse/stock_taking/getTask";

        String taskId = "taskId";

        String locationId = "locationId";

    }

    /**
     * 13.完成盘点任务（吴昊）
     */
    private interface StockTakingDoOne {

        String _function = "v1/inhouse/stock_taking/doOne";

        String result = "result";

    }


    private static void build() {
        base_url = ConfigTool.getHttpBaseUrl();
        //TODO
        OkHttpHandler.setOpensslSecret(null);
//        OkHttpHandler.setOpensslSecret(SecretTool.getOpensslSecretKey(BaseApplication.get()));
    }


    private synchronized static void setDefaultHeaders(BaseKVP... kvPairs) {
        if (default_headers == null) {
            default_headers = new ArrayList<>();
            for (int i = 0; i < kvPairs.length; i++) {
                default_headers.add(kvPairs[i]);
            }
        }
    }


    private static void setDefaultHeaders(Context context) {
        setDefaultHeaders(
                new DefaultKVPBean(Header.api_version, AppTool.getAppVersion(context)),
                new DefaultKVPBean(Header.version, AppTool.getAppVersion(context)),
                //new DefaultKVPBean(Header.app_key, BaseApplication.get().getImei()),
                new DefaultKVPBean(Header.app_key, "11111"),
                new DefaultKVPBean(Header.platform, "2")
        );
    }


    /**
     * 获取默认Header
     *
     * @return
     */
    private static List<BaseKVP> getDefaultHeaders() {
        if (default_headers == null) {
            setDefaultHeaders(BaseApplication.get());
        }

        List<BaseKVP> headerList = new ArrayList();
        headerList.addAll(default_headers);
        headerList.add(new DefaultKVPBean(Header.uid, BaseApplication.get().getUserId()));
        headerList.add(new DefaultKVPBean(Header.token, BaseApplication.get().getUserToken()));
        return headerList;
    }

    /**
     * 添加Header
     *
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addHeaders(BaseKVP... kvPairs) {
        List<BaseKVP> headers = new ArrayList<>();
        if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                headers.add(kvPairs[i]);
            }
        }
        return headers;
    }


    /**
     * 添加Header
     *
     * @param headers
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addHeaders(List<BaseKVP> headers, BaseKVP... kvPairs) {
        if (headers == null) {
            headers = addHeaders(kvPairs);

        } else if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                headers.add(kvPairs[i]);
            }
        }
        return headers;
    }


    /**
     * 添加请求参数
     *
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addParams(BaseKVP... kvPairs) {
        List<BaseKVP> params = new ArrayList<>();
        if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                params.add(kvPairs[i]);
            }
        }
        return params;
    }


    /**
     * 添加请求参数
     *
     * @param params
     * @param kvPairs
     * @return
     */
    private static List<BaseKVP> addParams(List<BaseKVP> params, BaseKVP... kvPairs) {
        if (params == null) {
            params = addParams(kvPairs);

        } else if (kvPairs != null) {
            for (int i = 0; i < kvPairs.length; i++) {
                params.add(kvPairs[i]);
            }
        }
        return params;
    }

    /**
     * 根据参数，调起请求
     */
    public static <B extends BaseBean, D, PR extends BaseParser<B, D>> DataHull<B> request(HttpDynamicParameter<PR> httpParameter) {
//        httpParameter.addParameter(new DefaultKVPBean(PublicParameter.timestampt, String.valueOf(TimestampTool.currentTimeMillis() / 1000)));
        OkHttpHandler<B> handler = new OkHttpHandler<>();
        DataHull<B> dataHull = handler.requestData(httpParameter);
        return dataHull;
    }

    /**
     * 登录接口
     *
     * @param username 用户名
     * @param password 密码
     * @return
     */
    public static DataHull<User> userInfoLogin(String username, String password) {
        String url = base_url + UserInfoLogin._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(UserInfoLogin.username, username),
                new DefaultKVPBean(UserInfoLogin.passwd, password)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<UserParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new UserParser(), 0);

        return request(parameter);
    }

    /**
     * 根据订单号、托盘码和国条码查询接口
     */
    public static DataHull<ReceiptInfo> receiptGetOrdeInfo(String orderOtherId, String containerId, String barCode) {
        String url = base_url + ReceiptGetOrderInfo._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(ReceiptGetOrderInfo.orderOtherId, orderOtherId),
                new DefaultKVPBean(ReceiptGetOrderInfo.containerId, containerId),
                new DefaultKVPBean(ReceiptGetOrderInfo.barCode, barCode)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ReceiptGetOrderInfoParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ReceiptGetOrderInfoParser(), 0);

        return request(parameter);
    }

    /**
     * 收货单接口
     */
    public static DataHull<ResponseState> receiptAdd(String orderOtherId, String containerId, String bookingNum, String receiptWharf, String items) {
        String url = base_url + ReceiptAdd._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(ReceiptAdd.orderOtherId, orderOtherId),
                new DefaultKVPBean(ReceiptAdd.containerId, containerId),
                new DefaultKVPBean(ReceiptAdd.bookingNum, bookingNum),
                new DefaultKVPBean(ReceiptAdd.receiptWharf, receiptWharf),
                new DefaultKVPBean(ReceiptAdd.items, items)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ResponseStateParser(), 0);

        return request(parameter);
    }


    /**
     * 创建上架任务(冯坤)
     */
    public static DataHull<ShelveCreate> shelveCreateTask(String containerId) {
        String url = base_url + ShelveCreateTask._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(ShelveCreateTask.containerId, containerId)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ShelveCreateParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ShelveCreateParser(), 0);

        return request(parameter);
    }


    /**
     * 扫托盘领取上架任务（冯坤)
     */
    public static DataHull<Shelve> shelveScanContainer(String operator, String containerId) {
        String url = base_url + ShelveScanContainer._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(ShelveScanContainer.operator, operator),
                new DefaultKVPBean(ShelveScanContainer.containerId, containerId)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ShelveParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ShelveParser(), 0);

        return request(parameter);
    }

    /**
     * 扫货架位完成上架操（冯坤)
     */
    public static DataHull<ResponseState> shelveScanTargetLocation(String taskId, String locationId) {
        String url = base_url + ShelveScanTargetLocation._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(ShelveScanTargetLocation.taskId, taskId),
                new DefaultKVPBean(ShelveScanTargetLocation.locationId, locationId)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ResponseStateParser(), 0);

        return request(parameter);
    }


    /**
     * 扫描库位获取商品信息（马力）
     *
     * @param locationId
     * @return
     */
    public static DataHull<Item> stockGetItem(String locationId, String barCode) {
        String url = base_url + StockGetItem._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(StockGetItem.locationId, locationId),
                new DefaultKVPBean(StockGetItem.barCode, barCode)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ItemParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ItemParser(), 0);

        return request(parameter);
    }


    /**
     * 转残次（马力）
     */
    public static DataHull<ResponseState> createScrap(String locationId, String barCode, String uomQty, String uId) {
        String url = base_url + InhouseCreateScrap._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(InhouseCreateScrap.locationId, locationId),
                new DefaultKVPBean(InhouseCreateScrap.barCode, barCode),
                new DefaultKVPBean(InhouseCreateScrap.uomQty, uomQty),
                new DefaultKVPBean(InhouseCreateScrap.uId, uId)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ResponseStateParser(), 0);

        return request(parameter);
    }

    /**
     * 转退货（马力）
     */
    public static DataHull<ResponseState> createReturn(String locationId, String barCode, String uomQty, String uId) {
        String url = base_url + InhouseCreateReturn._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(InhouseCreateReturn.locationId, locationId),
                new DefaultKVPBean(InhouseCreateReturn.barCode, barCode),
                new DefaultKVPBean(InhouseCreateReturn.uomQty, uomQty),
                new DefaultKVPBean(InhouseCreateReturn.uId, uId)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ResponseStateParser(), 0);

        return request(parameter);
    }

    /**
     * 10.领取补货任务（马力）
     */
    public static DataHull<TaskTransfer> procurementFetchTask(String locationId, String staffId) {
        String url = base_url + ProcurementFetchTask._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(ProcurementFetchTask.locationId, locationId),
                new DefaultKVPBean(ProcurementFetchTask.uId, staffId)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<TaskTransferParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new TaskTransferParser(), 0);

        return request(parameter);
    }


    /**
     * 查看补货任务详情（马力）
     */
    public static DataHull<TaskTransferDetail> procurementView(String taskId) {
        String url = base_url + ProcurementView._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(ProcurementView.taskId, taskId)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<TaskTransferDetailParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new TaskTransferDetailParser(), 0);

        return request(parameter);
    }


    /**
     * 补货-转出（马力）
     */
    public static DataHull<ResponseState> procurementScanFromLocation(String taskId, String locationId, String uId, String uomQty) {
        String url = base_url + ProcurementScanFromLocation._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(ProcurementScanFromLocation.taskId, taskId),
                new DefaultKVPBean(ProcurementScanFromLocation.locationId, locationId),
                new DefaultKVPBean(ProcurementScanFromLocation.uId, uId),
                new DefaultKVPBean(ProcurementScanFromLocation.uomQty, uomQty)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ResponseStateParser(), 0);

        return request(parameter);
    }

    /**
     * 补货-转入（马力）
     */
    public static DataHull<ResponseState> procurementScanToLocation(String taskId, String locationId, String uId, String uomQty) {
        String url = base_url + ProcurementScanToLocation._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(ProcurementScanToLocation.taskId, taskId),
                new DefaultKVPBean(ProcurementScanToLocation.locationId, locationId),
                new DefaultKVPBean(ProcurementScanToLocation.uId, uId),
                new DefaultKVPBean(ProcurementScanToLocation.uomQty, uomQty)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ResponseStateParser(), 0);

        return request(parameter);
    }

    /**
     * 10.领取移库任务（马力）
     */
    public static DataHull<TaskTransfer> stockTransferFetchTask(String locationId, String uId) {
        String url = base_url + StockTransferFetchTask._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(StockTransferFetchTask.locationId, locationId),
                new DefaultKVPBean(StockTransferFetchTask.uId, uId)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<TaskTransferParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new TaskTransferParser(), 0);

        return request(parameter);
    }


    /**
     * 查看移库任务详情（马力）
     */
    public static DataHull<TaskTransferDetail> stockTransferementView(String taskId) {
        String url = base_url + StockTransferView._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(StockTransferView.taskId, taskId)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<TaskTransferDetailParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new TaskTransferDetailParser(), 0);

        return request(parameter);
    }


    /**
     * 移库-出（马力）
     */
    public static DataHull<ResponseState> stockTransferScanFromLocation(String taskId, String locationId, String uId, String uomQty) {
        String url = base_url + StockTransferScanFromLocation._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(StockTransferScanFromLocation.taskId, taskId),
                new DefaultKVPBean(StockTransferScanFromLocation.locationId, locationId),
                new DefaultKVPBean(StockTransferScanFromLocation.uId, uId),
                new DefaultKVPBean(StockTransferScanFromLocation.uomQty, uomQty)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ResponseStateParser(), 0);

        return request(parameter);
    }

    /**
     * 移库-入（马力）
     */
    public static DataHull<ResponseState> stockTransferScanToLocation(String taskId, String locationId, String uId, String uomQty) {
        String url = base_url + StockTransferScanToLocation._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(StockTransferScanToLocation.taskId, taskId),
                new DefaultKVPBean(StockTransferScanToLocation.locationId, locationId),
                new DefaultKVPBean(StockTransferScanToLocation.uId, uId),
                new DefaultKVPBean(StockTransferScanToLocation.uomQty, uomQty)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ResponseStateParser(), 0);

        return request(parameter);
    }


    /**
     * 领取盘点任务(吴昊)
     */
    public static DataHull<TakeStockList> stockTakingAssign(String uid) {
        String url = base_url + StockTakingAssign._function;
        int type = BaseHttpParameter.Type.POST;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(StockTakingAssign.uId, uid)
        );
        HttpDynamicParameter<TakeStockListParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new TakeStockListParser(), 0);

        return request(parameter);
    }

    /**
     * 盘点任务详情(吴昊)
     */
    public static DataHull<TakeStockDetail> stockTakingGetTask(String taskId, String locationId) {
        String url = base_url + StockTakingGetTask._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(StockTakingGetTask.taskId, taskId),
                new DefaultKVPBean(StockTakingGetTask.locationId, locationId)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<TakeStockDetailParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new TakeStockDetailParser(), 0);

        return request(parameter);
    }

    /**
     * 盘点任务详情(吴昊)
     */
    public static DataHull<ResponseState> stockTakingDoOne(String result) {
        String url = base_url + StockTakingDoOne._function;
        List<BaseKVP> params = addParams(
                new DefaultKVPBean(StockTakingDoOne.result, result)
        );
        int type = BaseHttpParameter.Type.POST;
        HttpDynamicParameter<ResponseStateParser> parameter = new HttpDynamicParameter<>(url, getDefaultHeaders(), params, type, new ResponseStateParser(), 0);

        return request(parameter);
    }
}
