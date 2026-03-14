package servlet.admin.category;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Category;
import service.CategoryService;

@WebServlet(name = "CategoryManagerServlet", value = "/admin/categoryManager")
public class CategoryManagerServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final CategoryService categoryService = new CategoryService();

	private static final int CATEGORIES_PER_PAGE = 5;

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// ===== COUNT =====
		int totalCategories;
		try {
			totalCategories = categoryService.count();
		} catch (Exception e) {
			e.printStackTrace();
			totalCategories = 0;
		}

		int totalPages = totalCategories / CATEGORIES_PER_PAGE;
		if (totalCategories % CATEGORIES_PER_PAGE != 0) {
			totalPages++;
		}
		if (totalPages == 0) {
			totalPages = 1;
		}

		// ===== PAGE PARAM =====
		int page = 1;
		String pageParam = request.getParameter("page");
		try {
			if (pageParam != null) {
				page = Integer.parseInt(pageParam);
			}
		} catch (NumberFormatException ignored) {
		}

		if (page < 1)
			page = 1;
		if (page > totalPages)
			page = totalPages;

		// ===== OFFSET =====
		int offset = (page - 1) * CATEGORIES_PER_PAGE;

		// ===== GET DATA =====
		List<Category> categories = new ArrayList<>();
		try {
			categories = categoryService.getOrderedPart(CATEGORIES_PER_PAGE, offset, "id", "DESC");
		} catch (Exception e) {
			e.printStackTrace();
		}

		// ===== SET ATTRIBUTES =====
		request.setAttribute("totalPages", totalPages);
		request.setAttribute("page", page);
		request.setAttribute("categories", categories);

		// ===== FORWARD =====
		request.getRequestDispatcher("/WEB-INF/views/categoryManagerView.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getParameter("action");

		if ("delete".equals(action)) {

			String idParam = request.getParameter("id");
			String pageParam = request.getParameter("page");

			if (pageParam == null || pageParam.isEmpty()) {
				pageParam = "1";
			}

			try {
				int id = Integer.parseInt(idParam);
				categoryService.delete(id);
				request.getSession().setAttribute("successMessage", "Đã xóa thể loại id: " + id);
			} catch (Exception e) {
				request.getSession().setAttribute("errorMessage", "Xóa thất bại!");
			}

			response.sendRedirect(request.getContextPath() + "/admin/categoryManager?page=" + pageParam);
		}
	}

}
