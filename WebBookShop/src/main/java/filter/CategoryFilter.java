package filter;

import java.io.IOException;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;

import beans.Category;
import dao.CategoryDAO;

@WebFilter("/*") // Áp dụng cho tất cả URL
public class CategoryFilter implements Filter {

    private CategoryDAO categoryDAO;

    @Override
    public void init(FilterConfig filterConfig) {
        categoryDAO = new CategoryDAO();
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        // Load danh sách category từ database
        List<Category> categories = categoryDAO.getAll();

        // Đưa vào request scope để JSP dùng
        request.setAttribute("categories", categories);

        // Tiếp tục chuỗi filter
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
}
