package com.td.innovate.swipetodismiss;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    SwipeToDeleteListenerCallbackHelper cbh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final ListView lv = (ListView) findViewById(R.id.listView);
        final MyAdapter mAdapter = new MyAdapter();
        lv.setAdapter(mAdapter);

        cbh = new SwipeToDeleteListenerCallbackHelper();

        SwipeToDeleteListener swipeToDeleteListener = new SwipeToDeleteListener(lv, new SwipeToDeleteListener.SwipeCallbacks() {
            @Override
            public void onSwipe(int position) {
                //todo
            }

            @Override
            public void onOpen(View view, final int position) {
                TextView deleteText = (TextView) view.findViewById(R.id.txt_delete);
                deleteText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        deleteItem(MainActivity.this, position);
                    }
                });

                TextView cancelText = (TextView) view.findViewById(R.id.txt_cancel);
                cancelText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        cbh.getSwipeToDeleteListener().cancelPendingRow(null, 0, false);
                    }
                });

            }

            @Override
            public void onClose(View view, int position) {
                //todo
            }

            @Override
            public void onDelete(View view, int position) {
                mAdapter.remove(position);
            }
        });

        cbh.setSwipeToDeleteListener(swipeToDeleteListener);

        lv.setOnTouchListener(swipeToDeleteListener);
        swipeToDeleteListener.makeScrollListener();
    }

    public void deleteItem(Context context, final int position) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

//        // set title
//        alertDialogBuilder.setTitle("Delete item");

        // set dialog message
        alertDialogBuilder
                .setMessage("Do you want to delete this item?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, close
                        // current activity
                        cbh.getSwipeToDeleteListener().deletePendingRow(position);
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // if this button is clicked, just close
                        // the dialog box and do nothing
                        cbh.getSwipeToDeleteListener().cancelPendingRow(null, 0, false);
                        dialog.cancel();
                    }
                });

        alertDialogBuilder.setCancelable(true);

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static class MyAdapter extends BaseAdapter {
        private static final int SIZE = 100;

        private final List<String> mDataSet = new ArrayList<>();

        MyAdapter() {
            for (int i = 0; i < SIZE; i++) {
                mDataSet.add(i, "This is row number " + i);
            }
        }

        @Override
        public int getCount() {
            return mDataSet.size();
        }

        @Override
        public String getItem(int position) {
            return mDataSet.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void remove(int position) {
            mDataSet.remove(position);
            notifyDataSetChanged();
        }

        static class ViewHolder {
            TextView dataTextView;

            ViewHolder(View view) {
                dataTextView = ((TextView) view.findViewById(R.id.txt_data));
                view.setTag(this);
            }
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = convertView == null
                    ? new ViewHolder(convertView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item, parent, false))
                    : (ViewHolder) convertView.getTag();

            viewHolder.dataTextView.setText(mDataSet.get(position));
            return convertView;
        }
    }


    public class SwipeToDeleteListenerCallbackHelper {
        private SwipeToDeleteListener swipeToDeleteListener;

        public void setSwipeToDeleteListener(SwipeToDeleteListener swipeToDeleteListener) {
            this.swipeToDeleteListener = swipeToDeleteListener;
        }

        public SwipeToDeleteListener getSwipeToDeleteListener() {
            return swipeToDeleteListener;
        }
    }
    /* use this class to setup swipetodelete listener */
}
