import java.util.HashMap;
import java.util.ArrayList;
import spark.ModelAndView;
import spark.template.velocity.VelocityTemplateEngine;
import static spark.Spark.*;

public class App {
  public static void main(String[] args) {
    String layout = "views/layout.vtl";

    get("/", (request, response) -> {
          HashMap<String, Object> model = new HashMap<String, Object>();

          model.put("template", "views/index.vtl");
          return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

    post("/tasks", (request, response) -> {
          HashMap<String, Object> model = new HashMap<String, Object>();

          String description = request.queryParams("description");
          String dueDate = request.queryParams("dueDate");

          Task task = new Task(description, dueDate);

          model.put("template", "views/index.vtl");
          return new ModelAndView(model, layout);
        }, new VelocityTemplateEngine());

    post("/categories", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();

        String name = request.queryParams("name");
        Category category = new Category(name);

        model.put("categories", category);
        model.put("template", "views/index.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());

    post("/categories/:id", (request, response) -> {
        HashMap<String, Object> model = new HashMap<String, Object>();

         model.put("category", Category.find(Integer.parseInt(request.params(":id"))));

        model.put("template", "views/category.vtl");
        return new ModelAndView(model, layout);
      }, new VelocityTemplateEngine());
}
}
