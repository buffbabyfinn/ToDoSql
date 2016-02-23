import java.util.List;
import org.sql2o.*;

public class Task {
  private int id;
  private int categoryId;
  private String description;
  private String dueDate;

  @Override
  public boolean equals(Object otherTask){
    if (!(otherTask instanceof Task)) {
      return false;
    } else {
      Task newTask = (Task) otherTask;
      return this.getDescription().equals(newTask.getDescription()) && this.getId() == newTask.getId() && this.getDueDate() == newTask.getDueDate();
    }
  }

  public int getId() {
    return id;
  }

  public int getCategoryId() {
    return categoryId;
  }

  public Task(String description, String dueDate) {
    this.description = description;
    this.dueDate = dueDate;
  }

  public String getDueDate() {
    return dueDate;
  }

  public String getDescription() {
    return description;
  }

  public static List<Task> all() {
    String sql = "SELECT id, description FROM Tasks";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Task.class);
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO Tasks (description, categoryId, dueDate) VALUES (:description, :categoryId, :dueDate)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("description",  this.description)
        .addParameter("categoryId", categoryId)
        .addParameter("dueDate", dueDate)
        .executeUpdate()
        .getKey();
    }
  }

  public static Task find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM Tasks where id=:id";
      Task task = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Task.class);
    return task;
    }
  }

  public void update(String description) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE tasks SET description = (:description) WHERE id = (:id)";
      con.createQuery(sql)
        .addParameter("description", description)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void updateDueDate(String dueDate) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "UPDATE tasks SET dueDate = (:dueDate) WHERE id = (:id)";
      con.createQuery(sql)
        .addParameter("dueDate", dueDate)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public void delete() {
    try(Connection con = DB.sql2o.open()) {
    String sql = "DELETE FROM tasks WHERE id = (:id)";
      con.createQuery(sql)
        .addParameter("id", id)
        .executeUpdate();
    }
  }

  public List<Task> sortTasks() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks where categoryId=(:id) ORDER BY dueDate=(:dueDate) [ASC] [NULLS {FIRST}]";
      return con.createQuery(sql)
        .addParameter("id", id)
        .addParameter("dueDate", dueDate)
        .executeAndFetch(Task.class);
    }
  }
}
