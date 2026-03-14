package servlet.general;

import java.util.List;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import beans.Category;
import dao.CategoryDAO;

@WebListener
public class AppStartupListener implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        CategoryDAO categoryDAO = new CategoryDAO();
        List<Category> categories = categoryDAO.getAll(); // Lấy danh sách category
        // Lưu vào application scope
        sce.getServletContext().setAttribute("categories", categories);
        System.out.println("Categories loaded into application scope: " + categories.size());
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Không cần làm gì
    }
}
