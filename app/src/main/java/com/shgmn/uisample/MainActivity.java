package com.shgmn.uisample;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.logging.Logger;

public class MainActivity extends AppCompatActivity implements OnRecyclerListener {
    MyRecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mRecyclerView = (MyRecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        // 適当にデータ作成
        ArrayList<String> array = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            array.add("" + i);
        }
        MyRecyclerViewAdapter adapter = new MyRecyclerViewAdapter(this, array, this);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnItemTouchListener(new MyItemTouchListener());
    }


    @Override
    public void onRecyclerClicked(View v, int position) {

    }
}

class MyItemTouchListener implements RecyclerView.OnItemTouchListener {
    float mStartPosY;

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        if (e.getAction() == MotionEvent.ACTION_DOWN) {
            mStartPosY = e.getY();
        } else if (e.getAction() == MotionEvent.ACTION_UP) {
            MyRecyclerView mrv = (MyRecyclerView) rv;
            float endPosY = e.getY();
            int newPage = mrv.getCurrentPage();
            if (mStartPosY > endPosY) {
                Log.d("MyItemTouchListener", "next");
                newPage++;
            } else {
                Log.d("MyItemTouchListener", "previous");
                newPage--;
            }
            if (newPage < 0) {
                newPage = 0;
            }
            Log.d("MyItemTouchListener", "newPage is " + newPage);
            mrv.smoothScrollToPosition(newPage);
            mrv.setCurrentPage(newPage);
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {

    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
}

class MyRecyclerViewHolder extends RecyclerView.ViewHolder {
    TextView textView;

    public MyRecyclerViewHolder(View itemView) {
        super(itemView);
        textView = (TextView) itemView.findViewById(R.id.list_item_text);
    }
}

interface OnRecyclerListener {
    void onRecyclerClicked(View v, int position);
}

class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewHolder> {

    private LayoutInflater mInflater;
    private ArrayList<String> mData;
    private Context mContext;
    private OnRecyclerListener mListener;

    public MyRecyclerViewAdapter(Context context, ArrayList<String> data, OnRecyclerListener listener) {
        mInflater = LayoutInflater.from(context);
        mContext = context;
        mData = data;
        mListener = listener;
    }

    @Override
    public MyRecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("MyRecyclerViewAdapter", "onCreateViewHolder");
        return new MyRecyclerViewHolder(mInflater.inflate(R.layout.list_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyRecyclerViewHolder holder, final int position) {
        Log.d("MyRecyclerViewAdapter", "onBindViewHolder " + position);
        // データ表示
        final int tempPosition = holder.getAdapterPosition();
        if (mData != null && mData.size() > tempPosition && mData.get(tempPosition) != null) {
            holder.textView.setText(mData.get(tempPosition));
        }

        // クリック処理
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onRecyclerClicked(v, tempPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        Log.d("MyRecyclerViewAdapter", "getItemCount");
        if (mData != null) {
            return mData.size();
        } else {
            return 0;
        }
    }
}