
import java.util.List;
import java.util.Arrays;
import org.sql2o.*;

public class Category {
  private int id;
  private String name;


  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public Category(String name) {
    this.name = name;
  }

  public static List<Category> all() {
    String sql = "SELECT id, name FROM Categories";
    try(Connection con = DB.sql2o.open()) {
      return con.createQuery(sql).executeAndFetch(Category.class);
    }
  }

  @Override
  public boolean equals(Object otherCategory){
    if (!(otherCategory instanceof Category)) {
      return false;
    } else {
      Category newCategory = (Category) otherCategory;
      return this.getName().equals(newCategory.getName());
    }
  }

  public void save() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "INSERT INTO Categories(name) VALUES (:name)";
      this.id = (int) con.createQuery(sql, true)
        .addParameter("name", this.name)
        .executeUpdate()
        .getKey();
    }
  }

  public static Category find(int id) {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM Categories where id=:id";
      Category Category = con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetchFirst(Category.class);
      return Category;
    }
  }

  public List<Task> getTasks() {
    try(Connection con = DB.sql2o.open()) {
      String sql = "SELECT * FROM tasks where categoryId=:id";
      return con.createQuery(sql)
        .addParameter("id", id)
        .executeAndFetch(Task.class);
    }
  }

  // public List<Task> sortTasks() {
  //   try(Connection con = DB.sql2o.open()) {
  //     String sql = "SELECT * FROM tasks where categoryId=(:id) ORDER BY dueDate=(:dueDate) [ASC] [NULLS {FIRST}]";
  //     return con.createQuery(sql)
  //       .addParameter("id", id)
  //       .addParameter("dueDate", dueDate)
  //       .executeAndFetch(Task.class);
  //   }
  // }
}
