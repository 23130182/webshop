package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.User;

@WebFilter(filterName = "AuthorizationFilter", value = "/admin/*")
public class AuthorizationFilter implements Filter {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws ServletException, IOException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		HttpSession session = request.getSession(false);

		String loginURI = request.getContextPath() + "/admin/signin";
		String admin401URI = request.getContextPath() + "/admin/401";

		// ======= Lấy role user =======
		String role = null;
		if (session != null) {
			User currentUser = (User) session.getAttribute("currentUser");
			if (currentUser != null) {
				role = currentUser.getRole();
			}
		}

		boolean isAdmin = "ADMIN".equals(role);
		boolean isEmployee = "EMPLOYEE".equals(role);
		boolean loginRequest = request.getRequestURI().equals(loginURI);

		// ======= Kiểm tra các đường dẫn hạn chế cho employee =======
		boolean isNotAccessibleForEmployee = false;
		if (isEmployee) {
			String[] restrictedPaths = { "/admin/userManager" };
			for (String path : restrictedPaths) {
				String fullPath = request.getContextPath() + path;
				if (request.getRequestURI().startsWith(fullPath)) {
					isNotAccessibleForEmployee = true;
					break;
				}
			}
		}

		// ======= Quy tắc phân quyền =======
		if (isAdmin || isEmployee || loginRequest) {
			if (isEmployee && isNotAccessibleForEmployee) {
				response.sendRedirect(admin401URI);
			} else {
				chain.doFilter(request, response);
			}
		} else {
			response.sendRedirect(loginURI);
		}
	}
}
