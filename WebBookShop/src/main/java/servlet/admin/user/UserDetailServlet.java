package servlet.admin.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.User;
import service.UserService;

@WebServlet(name = "UserDetailServlet", value = "/admin/userManager/detail")
public class UserDetailServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		long id = 0;

		// Lấy id từ request
		try {
			id = Long.parseLong(request.getParameter("id"));
		} catch (Exception e) {
			// Nếu không hợp lệ, chuyển về danh sách user
			response.sendRedirect(request.getContextPath() + "/admin/userManager");
			return;
		}

		// Lấy user từ DB
		User user = null;
		try {
			user = userService.getById(id);
		} catch (Exception e) {
			e.printStackTrace();
		}

		if (user != null) {
			// Đặt user vào request và forward tới view
			request.setAttribute("user", user);
			request.getRequestDispatcher("/WEB-INF/views/userDetailView.jsp").forward(request, response);
		} else {
			// Nếu không tìm thấy user, redirect về danh sách
			response.sendRedirect(request.getContextPath() + "/admin/userManager");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Không cần xử lý POST cho detail
	}
}
