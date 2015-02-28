package com.example.zf_android.trade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.common.HttpCallback;
import com.example.zf_android.trade.common.Page;
import com.example.zf_android.trade.entity.AfterSaleRecord;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leo on 2015/2/26.
 */
public class AfterSaleListActivity extends Activity {

	public static final int RECORD_MAINTAIN = 0;
	public static final int RECORD_RETURN = 1;
	public static final int RECORD_CANCEL = 2;
	public static final int RECORD_CHANGE = 3;
	public static final int RECORD_UPDATE = 4;
	public static final int RECORD_LEASE = 5;

	public static final String RECORD_TYPE = "record_type";
	public static final String RECORD_ID = "record_id";
	private int mRecordType;

	private ListView mListView;
	private RecordListAdapter mAdapter;
	private List<AfterSaleRecord> mEntities;
	private int currentPage = 1;
	private int total = 0;
	private final int pageSize = 10;

	private LayoutInflater mInflater;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRecordType = getIntent().getIntExtra(RECORD_TYPE, 0);

		setContentView(R.layout.activity_after_sale_list);
		String[] titles = getResources().getStringArray(R.array.title_after_sale_list);
		new TitleMenuUtil(this, titles[mRecordType]).show();

		mInflater = LayoutInflater.from(this);
		mEntities = new ArrayList<AfterSaleRecord>();
		mListView = (ListView) findViewById(R.id.after_sale_list);
		mAdapter = new RecordListAdapter();
		mListView.setAdapter(mAdapter);

		API.getAfterSaleRecordList(this, mRecordType, 16, 1, 10, new HttpCallback<Page<AfterSaleRecord>>(this) {
			@Override
			public void onSuccess(Page<AfterSaleRecord> data) {
				mEntities.addAll(data.getContent());
				currentPage = data.getCurrentPage();
				total = data.getTotal();
				mAdapter.notifyDataSetChanged();
			}

			@Override
			public TypeToken<Page<AfterSaleRecord>> getTypeToken() {
				return new TypeToken<Page<AfterSaleRecord>>() {
				};
			}
		});
	}

	class RecordListAdapter extends BaseAdapter {
		RecordListAdapter() {
		}

		@Override
		public int getCount() {
			return mEntities.size();
		}

		@Override
		public AfterSaleRecord getItem(int position) {
			return mEntities.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.after_sale_record_item, null);
				holder = new ViewHolder();
				holder.tvNumberTitle = (TextView) convertView.findViewById(R.id.after_sale_number_title);
				holder.tvNumber = (TextView) convertView.findViewById(R.id.after_sale_number);
				holder.tvTime = (TextView) convertView.findViewById(R.id.after_sale_time);
				holder.tvTerminal = (TextView) convertView.findViewById(R.id.after_sale_terminal);
				holder.tvStatus = (TextView) convertView.findViewById(R.id.after_sale_status);
				holder.llButtonContainer = (LinearLayout) convertView.findViewById(R.id.after_sale_button_container);
				holder.btnLeft = (Button) convertView.findViewById(R.id.after_sale_button_left);
				holder.btnRight = (Button) convertView.findViewById(R.id.after_sale_button_right);
				holder.btnCenter = (Button) convertView.findViewById(R.id.after_sale_button_center);
				holder.btnCenterBlank = (Button) convertView.findViewById(R.id.after_sale_button_center_blank);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			final AfterSaleRecord data = getItem(position);
			String[] numberTitles = getResources().getStringArray(R.array.after_sale_number);
			holder.tvNumberTitle.setText(numberTitles[mRecordType]);
			holder.tvNumber.setText(data.getApplyNum());
			holder.tvTime.setText(data.getCreateTime());
			holder.tvTerminal.setText(data.getTerminalNum());
			String[] status = getResources().getStringArray(
					mRecordType == RECORD_MAINTAIN ? R.array.maintain_status
							: mRecordType == RECORD_RETURN ? R.array.return_status
							: mRecordType == RECORD_CANCEL ? R.array.cancel_status
							: mRecordType == RECORD_CHANGE ? R.array.change_status
							: mRecordType == RECORD_UPDATE ? R.array.update_status
							: R.array.lease_status
			);
			holder.tvStatus.setText(status[data.getStatus()]);

			switch (mRecordType) {
				case RECORD_MAINTAIN:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.VISIBLE);
						holder.btnRight.setVisibility(View.VISIBLE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.GONE);
						holder.btnLeft.setText(getString(R.string.button_cancel_apply));
						holder.btnRight.setText(getString(R.string.button_pay));
					} else if (data.getStatus() == 2) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.VISIBLE);
						holder.btnCenterBlank.setVisibility(View.GONE);
						holder.btnCenter.setText(getString(R.string.button_submit_flow));
					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
				case RECORD_CANCEL:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);
						holder.btnCenterBlank.setText(R.string.button_cancel_apply);
					} else if (data.getStatus() == 5) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.VISIBLE);
						holder.btnCenterBlank.setVisibility(View.GONE);
						holder.btnCenter.setText(R.string.button_submit_cancel);
					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
				case RECORD_UPDATE:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);
						holder.btnCenterBlank.setText(getString(R.string.button_cancel_apply));
					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
				case RECORD_RETURN:
				case RECORD_CHANGE:
				case RECORD_LEASE:
					if (data.getStatus() == 1) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.GONE);
						holder.btnCenterBlank.setVisibility(View.VISIBLE);
						holder.btnCenterBlank.setText(R.string.button_cancel_apply);
					} else if (data.getStatus() == 2) {
						holder.llButtonContainer.setVisibility(View.VISIBLE);
						holder.btnLeft.setVisibility(View.GONE);
						holder.btnRight.setVisibility(View.GONE);
						holder.btnCenter.setVisibility(View.VISIBLE);
						holder.btnCenterBlank.setVisibility(View.GONE);
						holder.btnCenter.setText(R.string.button_submit_flow);
					} else {
						holder.llButtonContainer.setVisibility(View.GONE);
					}
					break;
			}

			convertView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View view) {
					Intent intent = new Intent(AfterSaleListActivity.this, AfterSaleDetailActivity.class);
					intent.putExtra(RECORD_TYPE, mRecordType);
					intent.putExtra(RECORD_ID, data.getId());
					startActivity(intent);
				}
			});

			return convertView;
		}
	}

	static class ViewHolder {
		public TextView tvNumberTitle;
		public TextView tvNumber;
		public TextView tvTime;
		public TextView tvTerminal;
		public TextView tvStatus;
		public LinearLayout llButtonContainer;
		public Button btnLeft;
		public Button btnRight;
		public Button btnCenter;
		public Button btnCenterBlank;
	}
}
