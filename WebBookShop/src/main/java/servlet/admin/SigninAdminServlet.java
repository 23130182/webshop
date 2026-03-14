package servlet.admin;

import java.io.IOException;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

import beans.User;
import service.UserService;
import utils.HashingUtils;

@WebServlet(name = "SigninAdminServlet", value = "/admin/signin")
public class SigninAdminServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final UserService userService = new UserService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.getRequestDispatcher("/WEB-INF/views/signinAdminView.jsp").forward(request, response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String username = request.getParameter("username");
		String password = request.getParameter("password");

		Map<String, String> values = new HashMap<>();
		values.put("username", username);
		values.put("password", password);

		Map<String, List<String>> violations = new HashMap<>();
		violations.put("usernameViolations", new ArrayList<>());
		violations.put("passwordViolations", new ArrayList<>());

		// ================= VALIDATE USERNAME =================
		if (username == null || username.trim().isEmpty()) {
			violations.get("usernameViolations").add("Tên đăng nhập không được để trống");
		} else {
			if (!username.equals(username.trim())) {
				violations.get("usernameViolations").add("Tên đăng nhập không có dấu cách ở hai đầu");
			}
			if (username.length() > 25) {
				violations.get("usernameViolations").add("Tên đăng nhập tối đa 25 ký tự");
			}
		}

		// ================= VALIDATE PASSWORD =================
		if (password == null || password.trim().isEmpty()) {
			violations.get("passwordViolations").add("Mật khẩu không được để trống");
		} else {
			if (!password.equals(password.trim())) {
				violations.get("passwordViolations").add("Mật khẩu không có dấu cách ở hai đầu");
			}
			if (password.length() > 32) {
				violations.get("passwordViolations").add("Mật khẩu tối đa 32 ký tự");
			}
		}

		// ================= CHECK USER FROM DB =================
		User userFromServer = null;
		if (violations.get("usernameViolations").isEmpty()) {
			try {
				userFromServer = userService.getByUsername(username);
				if (userFromServer == null) {
					violations.get("usernameViolations").add("Tên đăng nhập không tồn tại");
				}
			} catch (Exception e) {
				e.printStackTrace();
				request.setAttribute("errorMessage", "Lỗi hệ thống, vui lòng thử lại sau");
			}
		}

		// ================= CHECK PASSWORD =================
		if (userFromServer != null && violations.get("passwordViolations").isEmpty()) {
			String hashedInputPassword = HashingUtils.hash(password);
			if (!hashedInputPassword.equals(userFromServer.getPassword())) {
				violations.get("passwordViolations").add("Mật khẩu không đúng");
			}
		}

		// ================= HANDLE RESULT =================
		int totalViolations = violations.values().stream().mapToInt(List::size).sum();

		if (totalViolations == 0 && userFromServer != null) {
			if ("ADMIN".equals(userFromServer.getRole()) || "EMPLOYEE".equals(userFromServer.getRole())) {

				request.getSession().setAttribute("currentUser", userFromServer);
				response.sendRedirect(request.getContextPath() + "/admin");
				return;
			} else {
				request.setAttribute("errorMessage", "Người dùng không có quyền đăng nhập Admin");
			}
		}

		// ================= RETURN FORM =================
		request.setAttribute("values", values);
		request.setAttribute("violations", violations);
		request.getRequestDispatcher("/WEB-INF/views/signinAdminView.jsp").forward(request, response);
	}
}
