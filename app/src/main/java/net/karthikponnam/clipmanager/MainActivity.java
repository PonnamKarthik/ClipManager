package net.karthikponnam.clipmanager;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListener;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.AdapterView;

import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import net.karthikponnam.clipmanager.Adapter.ListAdapter;
import net.karthikponnam.clipmanager.Database.DataQuery;
import net.karthikponnam.clipmanager.EdiClip.EditClip;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class MainActivity extends AppCompatActivity {

    ArrayList<String> data;
    ArrayList<String> date;
    ArrayList<Integer> _id;

    String TAG = getClass().getName();
    DataQuery dataQuery;
    SwipeMenuListView listView;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        startService(new Intent(MainActivity.this, ClipService.class));

        SharedPreferences sharedPreferences = getSharedPreferences("clip",MODE_PRIVATE);

        Boolean run = sharedPreferences.getBoolean("firstRun", true);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        if(run){
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.warning)
                    .setMessage(R.string.warning_data)
                    .setIcon(R.drawable.warning)
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putBoolean("firstRun", false);
                            editor.commit();
                            dialog.dismiss();
                        }
                    });
            builder.show();

        }

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setAdapter();
                startFabAnim();
            }
        });

        listView = (SwipeMenuListView) findViewById(R.id.listView);

        dataQuery = new DataQuery(MainActivity.this);

        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                SwipeMenuItem copyItem = new SwipeMenuItem(
                        MainActivity.this);
                copyItem.setBackground(R.color.blue);
                copyItem.setWidth(dp2px(90));
                copyItem.setIcon(R.drawable.copy);
                menu.addMenuItem(copyItem);

                SwipeMenuItem shareItem = new SwipeMenuItem(
                        MainActivity.this);
                shareItem.setBackground(R.color.orange_light);
                shareItem.setWidth(dp2px(90));
                shareItem.setIcon(R.drawable.share);
                menu.addMenuItem(shareItem);

                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        MainActivity.this);
                deleteItem.setBackground(R.color.red_light);
                deleteItem.setWidth(dp2px(90));
                deleteItem.setIcon(R.drawable.delete);
                menu.addMenuItem(deleteItem);
            }
        };
        listView.setMenuCreator(creator);
        listView.setSwipeDirection(SwipeMenuListView.DIRECTION_LEFT);

        listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                switch (index) {
                    case 0:
                        ClipboardManager clip = (ClipboardManager) MainActivity.this.getSystemService(CLIPBOARD_SERVICE);
                        ClipData clipData = ClipData.newPlainText("clipmanagerapp", data.get(position));
                        clip.setPrimaryClip(clipData);
                        dataQuery.deleteInsertClip(_id.get(position),data.get(position));
                        Snackbar.make(fab,"Copied Successfully.!", Snackbar.LENGTH_SHORT).show();
                        setAdapter();
                        break;
                    case 1:
                        Intent sharingIntent = new Intent(Intent.ACTION_VIEW);
                        sharingIntent.setType("text/plain");
                        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, data.get(position));
                        startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        break;
                    case 2:
                        dataQuery = new DataQuery(MainActivity.this);
                        dataQuery.deleteClip(_id.get(position));
                        setAdapter();
                        break;
                }
                return false;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(MainActivity.this, EditClip.class);
                i.putExtra("id", _id.get(position));
                startActivity(i);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                alertDialog.setCancelable(true)
                        .setTitle("Clip Data")
                        .setMessage(data.get(position))
                        .setNegativeButton("Close", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("Copy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ClipboardManager clip = (ClipboardManager) MainActivity.this.getSystemService(CLIPBOARD_SERVICE);
                                ClipData clipData = ClipData.newPlainText("clipmanagerapp", data.get(position));
                                clip.setPrimaryClip(clipData);
                                dataQuery.deleteInsertClip(_id.get(position),data.get(position));
                                Snackbar.make(fab,"Copied Successfully.!", Snackbar.LENGTH_SHORT).show();
                                setAdapter();
                            }
                        });
                alertDialog.show();
                return true;
            }
        });

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAdapter();
            }
        },1000);

    }

    public void setAdapter() {
        dataQuery = new DataQuery(MainActivity.this);

        JSONArray jsonArray = dataQuery.getClips();
        data = new ArrayList<String>();
        date = new ArrayList<String>();
        _id = new ArrayList<Integer>();

        for(int i = 0; i < jsonArray.length(); i++) {
            JSONObject jsonObject = jsonArray.optJSONObject(i);
            data.add(jsonObject.optString("data"));
            date.add(jsonObject.optString("date"));
            _id.add(jsonObject.optInt("id"));
        }

        ListAdapter listAdapter = new ListAdapter(MainActivity.this, data, date);
        listView.setAdapter(listAdapter);

    }

    public int dp2px(float dpValue) {
        final float scale = MainActivity.this.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    protected void onResume() {
        super.onResume();
        setAdapter();
    }

    public  void startFabAnim() {
        ViewCompat.animate(fab)
                .rotation(360.0f)
                .withLayer()
                .setDuration(2000)
                .setInterpolator(new OvershootInterpolator(10.0F))
                .setListener(new ViewPropertyAnimatorListener() {
                    @Override
                    public void onAnimationStart(View view) {

                    }

                    @Override
                    public void onAnimationEnd(View view) {
                        view.setRotation(0.0f);
                    }

                    @Override
                    public void onAnimationCancel(View view) {

                    }
                })
                .start();

    }

    @Override
    protected void onDestroy() {
        startService(new Intent(MainActivity.this, ClipService.class));
        super.onDestroy();
    }
}
