package com.appmon.control;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.appmon.control.persistence.ModelPresenterManager;
import com.appmon.control.presenters.AppListPresenter;
import com.appmon.control.presenters.IAppListPresenter;
import com.appmon.control.views.IAppListView;
import com.appmon.shared.entities.PackageInfo;

import java.util.ArrayList;
import java.util.List;

public class AppListActivity extends AppCompatActivity implements IAppListView {

    IAppListPresenter mPresenter;

    List<PackageInfo> mAppList;
    ListView mListView;
    AppListAdapter mListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);
        mListView = (ListView) findViewById(R.id.appList);
        mPresenter = new AppListPresenter(ModelPresenterManager.getInstance().getAppListModel(),
                getIntent().getExtras().getString("DeviceId"));
        mPresenter.attachView(this);
        mAppList = new ArrayList<>();
        mListAdapter = new AppListAdapter();
        mListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
        mListView.setAdapter(mListAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean checked = mListView.isItemChecked(position);
                Integer tag = (Integer) view.getTag();
                if (tag != null) {
                    mPresenter.setAppBlockMode(tag, checked);
                }
            }
        });
        // load cached list immediately
        mPresenter.requestAppList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.detachView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.attachView(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.app_list_menu, menu);
        final MenuItem menuItem =  menu.findItem(R.id.searchBox);
        final SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                menuItem.collapseActionView();
                mPresenter.setFilter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mPresenter.setFilter(newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public void showSyncFailMessage() {
        Toast.makeText(this, R.string.text_app_list_sync_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void updateList(List<PackageInfo> apps) {
        mAppList = apps;
        mListAdapter.notifyDataSetChanged();
    }

    class AppListAdapter extends BaseAdapter {

        private LayoutInflater mInflater;

        AppListAdapter() {
            mInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                view = mInflater.inflate(android.R.layout.simple_list_item_multiple_choice,
                        parent, false);
            }
            if (mAppList != null) {
                PackageInfo app = mAppList.get(position);
                TextView text = (TextView) view;
                text.setText(app.getName());
                view.setTag(position);
                ((ListView)parent).setItemChecked(position, app.isBlocked());
            }
            return view;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public int getCount() {
            if (mAppList == null)
                return 0;
            return mAppList.size();
        }
    }
}
