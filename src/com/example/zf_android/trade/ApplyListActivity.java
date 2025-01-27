package com.example.zf_android.trade;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.examlpe.zf_android.util.TitleMenuUtil;
import com.example.zf_android.R;
import com.example.zf_android.trade.common.CommonUtil;
import com.example.zf_android.trade.common.HttpCallback;
import com.example.zf_android.trade.entity.TerminalItem;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

import static com.example.zf_android.trade.Constants.TerminalIntent.REQUEST_DETAIL;
import static com.example.zf_android.trade.Constants.TerminalIntent.TERMINAL_ID;
import static com.example.zf_android.trade.Constants.TerminalIntent.TERMINAL_STATUS;
import static com.example.zf_android.trade.Constants.TerminalStatus.PART_OPENED;
import static com.example.zf_android.trade.Constants.TerminalStatus.UNOPENED;

/**
 * Created by Leo on 2015/3/5.
 */
public class ApplyListActivity extends Activity {

    private LayoutInflater mInflater;
    private ListView mApplyList;
    private List<TerminalItem> mTerminalItems;
    private ApplyListAdapter mAdapter;

    private int page = 0;
    private int total = 0;
    private final int rows = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply_list);
        new TitleMenuUtil(this, getString(R.string.title_apply_open)).show();

        initViews();
        loadData();
    }

    private void initViews() {
        mInflater = LayoutInflater.from(this);
        mApplyList = (ListView) findViewById(R.id.apply_list);
        mTerminalItems = new ArrayList<TerminalItem>();
        mAdapter = new ApplyListAdapter();

        View header = new View(this);
        mApplyList.addHeaderView(header);
        mApplyList.setAdapter(mAdapter);
    }

    private void loadData() {
        API.getApplyList(this, Constants.TEST_CUSTOMER, page+1, rows, new HttpCallback<List<TerminalItem>>(this) {
            @Override
            public void onSuccess(List<TerminalItem> data) {
                mTerminalItems.addAll(data);
                page++;
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public TypeToken<List<TerminalItem>> getTypeToken() {
                return new TypeToken<List<TerminalItem>>() {
                };
            }
        });
    }

    class ApplyListAdapter extends BaseAdapter {
        ApplyListAdapter() {
        }

        @Override
        public int getCount() {
            return mTerminalItems.size();
        }

        @Override
        public TerminalItem getItem(int i) {
            return mTerminalItems.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View convertView, ViewGroup viewGroup) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.apply_list_item, null);
                holder = new ViewHolder();
                holder.tvTerminalNumber = (TextView) convertView.findViewById(R.id.apply_terminal_number);
                holder.tvTerminalStatus = (TextView) convertView.findViewById(R.id.apply_terminal_status);
                holder.btnOpen = (Button) convertView.findViewById(R.id.apply_button_open);
                holder.btnVideo = (Button) convertView.findViewById(R.id.apply_button_video);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            final TerminalItem item = getItem(i);
            holder.tvTerminalNumber.setText(item.getTerminalNumber());
            String[] status = getResources().getStringArray(R.array.terminal_status);
            holder.tvTerminalStatus.setText(status[item.getStatus()]);

            if (item.getStatus() == UNOPENED) {
                holder.btnOpen.setEnabled(true);
                holder.btnOpen.setText(getString(R.string.apply_button_open));
            } else if (item.getStatus() == PART_OPENED) {
                holder.btnOpen.setEnabled(true);
                holder.btnOpen.setText(getString(R.string.apply_button_reopen));
            } else {
                holder.btnOpen.setEnabled(false);
            }
            holder.btnOpen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(ApplyListActivity.this, ApplyDetailActivity.class);
                    intent.putExtra(TERMINAL_ID, item.getId());
                    intent.putExtra(TERMINAL_STATUS, item.getStatus());
                    startActivityForResult(intent, REQUEST_DETAIL);
                }
            });
            holder.btnVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CommonUtil.toastShort(ApplyListActivity.this, "not yet completed...");
                }
            });
            return convertView;
        }
    }

    static class ViewHolder {
        public TextView tvTerminalNumber;
        public TextView tvTerminalStatus;
        public Button btnOpen;
        public Button btnVideo;
    }
}
