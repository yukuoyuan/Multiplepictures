package cn.yuan.mutiph;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by yukuoyuan on 2017/3/29.
 * 这是一个展示图片列表的适配器
 */

public class ImgsAdapter extends RecyclerView.Adapter<ImgsAdapter.ImgsHolder> {
    private List<String> list;
    private Context context;
    private OnDeleteListener onDeleteListener;
    private int number;

    public ImgsAdapter(List<String> list, Context context, int number) {
        this.list = list;
        this.context = context;
        this.number = number;
    }

    public void setOnDeleteListener(OnDeleteListener onDeleteListener) {
        this.onDeleteListener = onDeleteListener;
    }

    /**
     * 这是一个添加数据的方法
     *
     * @param imgs
     */
    public void addData(List<String> imgs) {
        if (list.size() <= number) {
            list.remove(list.size() - 1);
        }
        for (String dates : imgs) {
            list.add(dates);
        }
        if (list.size() == number) {
        } else if (list.size() < number) {
            list.add("http://7xp26r.com1.z0.glb.clouddn.com/ic_add.png");
        }
        notifyDataSetChanged();
    }

    /**
     * 这是一个删除一个图片的方法
     *
     * @param position
     */
    public void remove(int position) {
        boolean isAdd = true;
        for (String dates : list) {
            if (dates.equals("http://7xp26r.com1.z0.glb.clouddn.com/ic_add.png")) {
                isAdd = false;
            }
        }
        list.remove(position);
        if (isAdd) {
            list.add("http://7xp26r.com1.z0.glb.clouddn.com/ic_add.png");
        }
        notifyDataSetChanged();
    }

    public interface OnDeleteListener {
        void onDelte(int position);

        void onAdd();
    }

    @Override
    public ImgsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.item_imgs, null);
        return new ImgsHolder(view);
    }

    @Override
    public void onBindViewHolder(ImgsHolder holder, final int position) {
        Glide.with(context).load(list.get(position)).into(holder.iv_img_content);
        if (list.get(position).equals("http://7xp26r.com1.z0.glb.clouddn.com/ic_add.png")) {
            holder.iv_img_delete.setVisibility(View.GONE);

        } else {
            holder.iv_img_delete.setVisibility(View.VISIBLE);
        }
        holder.iv_img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteListener != null) {
                    onDeleteListener.onDelte(position);
                }
            }
        });
        holder.iv_img_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.get(position).equals("http://7xp26r.com1.z0.glb.clouddn.com/ic_add.png")) {
                    if (onDeleteListener != null) {
                        onDeleteListener.onAdd();
                    }
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ImgsHolder extends RecyclerView.ViewHolder {

        private final ImageView iv_img_content;
        private final ImageView iv_img_delete;

        public ImgsHolder(View itemView) {
            super(itemView);
            iv_img_content = (ImageView) itemView.findViewById(R.id.iv_img_content);
            iv_img_delete = (ImageView) itemView.findViewById(R.id.iv_img_delete);
        }
    }
}
