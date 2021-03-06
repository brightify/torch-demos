package org.brightify.torch.demo.todo;

import android.app.Activity;
import android.widget.ListView;
import android.widget.Toast;
import org.brightify.torch.demo.todo.EditDialog_;
import org.brightify.torch.demo.todo.R;
import org.brightify.torch.demo.todo.model.Task;
import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Bean;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.ViewById;

import static org.brightify.torch.TorchService.torch;

/**
 * @author <a href="mailto:tadeas@brightify.org">Tadeas Kriz</a>
 */
@EActivity(R.layout.main_activity)
@OptionsMenu(R.menu.main)
public class MainActivity extends Activity implements EditDialog.OnEditSaveListener, EditDialog.OnEditCancelListener,
        EditDialog.OnEditDeleteListener {

    @ViewById(android.R.id.list)
    ListView tasksList;

    @Bean
    TaskListAdapter adapter;

    @Bean
    AuthManager authManager;

    @AfterViews
    void bindAdapter() {
        tasksList.setAdapter(adapter);
    }

    @OptionsItem(R.id.add)
    void addTask() {
        EditDialog_.builder().build().show(getFragmentManager(), "edit-dialog");
    }

    @ItemClick(android.R.id.list)
    void tasksListItemClicked(Task task) {
        EditDialog.edit(task).show(getFragmentManager(), "edit-dialog");
    }

    @Override
    public void onEditCancel() {
    }

    @Override
    public void onEditSave(Task task) {
        task.setOwnerId(authManager.getLoggedUser().getId());
        torch().save().entity(task);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onEditDelete(Task task) {
        torch().delete().entity(task);
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Task deleted", Toast.LENGTH_SHORT).show();
    }
}
