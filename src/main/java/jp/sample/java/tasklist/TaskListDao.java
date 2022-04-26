package jp.sample.java.tasklist;

import jp.sample.java.tasklist.HomeController.TaskItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TaskListDao {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    TaskListDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(TaskItem taskItem) {
        SqlParameterSource param = new BeanPropertySqlParameterSource(taskItem);
        SimpleJdbcInsert insert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("TASKLIST");
        insert.execute(param);
    }

    public List<TaskItem> findAll() {
        String query = "SELECT * FROM TASKLIST";

        List<Map<String, Object>> result = jdbcTemplate.queryForList(query);
        List<TaskItem> taskItems = result.stream()
                .map((Map<String, Object> row) -> new TaskItem(
                        row.get("ID").toString(),
                        row.get("TASK").toString(),
                        row.get("DEADLINE").toString(),
                        (Boolean) row.get("DONE")
                )).toList();
        return taskItems;
    }

    public int delete(String id) {
        int number = jdbcTemplate.update("DELETE FROM TASKLIST WHERE ID = ?", id);
        return number;
    }

    public int update(TaskItem taskItem) {
        int number = jdbcTemplate.update(
                "UPDATE TASKLIST SET TASK = ?, DEADLINE = ?, DONE = ? WHERE ID = ?",
                taskItem.task(),
                taskItem.deadline(),
                taskItem.done(),
                taskItem.id()
        );
        return number;
    }
}
