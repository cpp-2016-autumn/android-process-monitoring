package com.pacmancoder.applist;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final ListView appListView = (ListView) findViewById(R.id.AppListView);
        // Building app list
        PackageManager pm = getPackageManager();
        final List<PackageInfo> ai = pm.getInstalledPackages(0);
        final List<String> apps = new ArrayList<>();
        for (PackageInfo i : ai) {
            if (i.versionName != null) {
                apps.add(i.applicationInfo.loadLabel(pm).toString());
            }
        }
        // Set new adapter
        ArrayAdapter<String> values = new ArrayAdapter<>(this, R.layout.list_item, apps);
        appListView.setAdapter(values);
        // Add onClick callback for toast info
        appListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(
                        MainActivity.this,
                        ai.get(position).packageName,
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
