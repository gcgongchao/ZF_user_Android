package com.example.zf_android.trade;

import android.content.Context;
import android.util.Log;

import com.example.zf_android.R;
import com.example.zf_android.trade.common.HttpCallback;
import com.example.zf_android.trade.common.HttpRequest;

import org.apache.http.HttpEntity;
import org.apache.http.entity.StringEntity;
import org.json.JSONObject;

/**
 * Created by Leo on 2015/2/11.
 */
public class API {

	public static final String SCHEMA = "http://";
	public static final String HOST = "114.215.149.242:18080";

	public static final String TERMINAL_LIST = SCHEMA + HOST + "/ZFMerchant/api/trade/record/getTerminals/%d";
	public static final String TRADE_RECORD_LIST = SCHEMA + HOST + "/ZFMerchant/api/trade/record/getTradeRecords/%d/%s/%s/%s/%d/%d";
	public static final String TRADE_RECORD_STATISTIC = SCHEMA + HOST + "/ZFMerchant/api/trade/record/getTradeRecordTotal/%d/%s/%s/%s";
	public static final String TRADE_RECORD_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/trade/record/getTradeRecord/%d";

	public static final String AFTER_SALE_MAINTAIN_LIST = SCHEMA + HOST + "/ZFMerchant/api/cs/repair/getAll";
	public static final String AFTER_SALE_RETURN_LIST = SCHEMA + HOST + "/ZFMerchant/api/return/getAll";
	public static final String AFTER_SALE_CANCEL_LIST = SCHEMA + HOST + "/ZFMerchant/api/cs/cancels/getAll";
	public static final String AFTER_SALE_CHANGE_LIST = SCHEMA + HOST + "/ZFMerchant/api/cs/change/getAll";
	public static final String AFTER_SALE_UPDATE_LIST = SCHEMA + HOST + "/ZFMerchant/api/update/info/getAll";
	public static final String AFTER_SALE_LEASE_LIST = SCHEMA + HOST + "/ZFMerchant/api/cs/lease/returns/getAll";

	public static final String AFTER_SALE_MAINTAIN_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/cs/repair/getRepairById";
	public static final String AFTER_SALE_RETURN_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/return/getReturnById";
	public static final String AFTER_SALE_CANCEL_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/cs/cancels/getCanCelById";
	public static final String AFTER_SALE_CHANGE_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/cs/change/getChangeById";
	public static final String AFTER_SALE_UPDATE_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/update/info/getInfoById";
	public static final String AFTER_SALE_LEASE_DETAIL = SCHEMA + HOST + "/ZFMerchant/api/cs/lease/returns/getById";

	public static void getTerminalList(
			Context context,
			int customerId,
			HttpCallback callback) {
		new HttpRequest(context, callback).get(String.format(TERMINAL_LIST, customerId));
	}

	public static void getTradeRecordList(
			Context context,
			int tradeTypeId,
			String terminalNumber,
			String startTime,
			String endTime,
			int page,
			int rows,
			HttpCallback callback) {
		new HttpRequest(context, callback).get(String.format(TRADE_RECORD_LIST,
				tradeTypeId, terminalNumber, startTime, endTime, page, rows));
	}

	public static void getTradeRecordStatistic(
			Context context,
			int tradeTypeId,
			String terminalNumber,
			String startTime,
			String endTime,
			HttpCallback callback) {
		new HttpRequest(context, callback).get(String.format(TRADE_RECORD_STATISTIC,
				tradeTypeId, terminalNumber, startTime, endTime));
	}

	public static void getTradeRecordDetail(
			Context context,
			int recordId,
			HttpCallback callback) {
		new HttpRequest(context, callback).get(String.format(TRADE_RECORD_DETAIL, recordId));
	}

	public static void getAfterSaleRecordList(
			Context context,
			int recordType,
			int customId,
			int page,
			int pageSize,
			HttpCallback callback) {
		String url;
		switch (recordType) {
			case AfterSaleListActivity.RECORD_MAINTAIN:
				url = AFTER_SALE_MAINTAIN_LIST;
				break;
			case AfterSaleListActivity.RECORD_RETURN:
				url = AFTER_SALE_RETURN_LIST;
				break;
			case AfterSaleListActivity.RECORD_CANCEL:
				url = AFTER_SALE_CANCEL_LIST;
				break;
			case AfterSaleListActivity.RECORD_CHANGE:
				url = AFTER_SALE_CHANGE_LIST;
				break;
			case AfterSaleListActivity.RECORD_UPDATE:
				url = AFTER_SALE_UPDATE_LIST;
				break;
			case AfterSaleListActivity.RECORD_LEASE:
				url = AFTER_SALE_LEASE_LIST;
				break;
			default:
				throw new IllegalArgumentException("illegal argument [recordType], within [0-5]");
		}

		HttpEntity entity = null;
		JSONObject params = new JSONObject();
		try {
			params.put("customer_id", customId);
			params.put("page", page);
			params.put("pageSize", pageSize);
			entity = new StringEntity(params.toString());
		} catch (Exception e) {
			callback.onFailure(context.getString(R.string.load_data_failed));
		}
		new HttpRequest(context, callback).post(url, entity);
	}

	public static void getAfterSaleRecordDetail(
			Context context,
			int recordType,
			int recordId,
			HttpCallback callback) {
		Log.e("", recordType + " <> " + recordId);
		String url;
		switch (recordType) {
			case AfterSaleListActivity.RECORD_MAINTAIN:
				url = AFTER_SALE_MAINTAIN_DETAIL;
				break;
			case AfterSaleListActivity.RECORD_RETURN:
				url = AFTER_SALE_RETURN_DETAIL;
				break;
			case AfterSaleListActivity.RECORD_CANCEL:
				url = AFTER_SALE_CANCEL_DETAIL;
				break;
			case AfterSaleListActivity.RECORD_CHANGE:
				url = AFTER_SALE_CHANGE_DETAIL;
				break;
			case AfterSaleListActivity.RECORD_UPDATE:
				url = AFTER_SALE_UPDATE_DETAIL;
				break;
			case AfterSaleListActivity.RECORD_LEASE:
				url = AFTER_SALE_LEASE_DETAIL;
				break;
			default:
				throw new IllegalArgumentException("illegal argument [recordType], within [0-5]");
		}
		HttpEntity entity = null;
		JSONObject params = new JSONObject();
		try {
			params.put("id", recordId);
			entity = new StringEntity(params.toString());
		} catch (Exception e) {
			callback.onFailure(context.getString(R.string.load_data_failed));
		}
		new HttpRequest(context, callback).post(url, entity);
	}
}
