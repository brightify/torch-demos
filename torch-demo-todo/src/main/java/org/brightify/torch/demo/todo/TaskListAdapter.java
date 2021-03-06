package org.brightify.torch.demo.todo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import com.googlecode.androidannotations.annotations.AfterInject;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EBean;
import com.googlecode.androidannotations.annotations.RootContext;
import org.brightify.torch.demo.todo.model.Task;
import org.brightify.torch.demo.todo.model.Task$;
import org.brightify.torch.demo.todo.view.TaskItemView;
import org.brightify.torch.demo.todo.view.TaskItemView_;
import org.brightify.torch.util.async.Callback;

import java.util.List;

import static org.brightify.torch.TorchService.torch;

/**
 * @author <a href="mailto:tadeas@brightify.org">Tadeas Kriz</a>
 */
@EBean
public class TaskListAdapter extends BaseAdapter {

    List<Task> tasks;

    @RootContext
    Context context;

    @Bean
    AuthManager authManager;

    @AfterInject
    void initAdapter() {
        loadData();
    }

    @Override
    public void notifyDataSetChanged() {
        loadData();
    }

    private void loadData() {
        torch().load().type(Task.class).filter(Task$.ownerId.equalTo(authManager.getLoggedUser().getId())).list(
                new Callback<List<Task>>() {
                    @Override
                    public void onSuccess(List<Task> data) {
                        tasks = data;
                        TaskListAdapter.super.notifyDataSetChanged();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Override
    public int getCount() {
        return tasks != null ? tasks.size() : 0;
    }

    @Override
    public Task getItem(int position) {
        return tasks != null ? tasks.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TaskItemView itemView;
        if (convertView == null) {
            itemView = TaskItemView_.build(context);
        } else {
            itemView = (TaskItemView) convertView;
        }

        itemView.bind(getItem(position));

        return itemView;
    }
}
