package servlet.client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.User;
import service.UserService;
import utils.HashingUtils;

@WebServlet(name = "SigninServlet", value = "/signin")
public class SigninServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/signinView.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Map<String, String> values = new HashMap<>();
        values.put("username", username);
        values.put("password", password);

        Map<String, List<String>> violations = new HashMap<>();
        violations.put("usernameViolations", new ArrayList<String>());
        violations.put("passwordViolations", new ArrayList<String>());

        // ---- VALIDATE USERNAME ----
        if (username == null || username.trim().isEmpty()) {
            violations.get("usernameViolations").add("Tên đăng nhập không được để trống");
        } else if (!username.equals(username.trim())) {
            violations.get("usernameViolations").add("Tên đăng nhập không có dấu cách ở hai đầu");
        } else if (username.length() > 25) {
            violations.get("usernameViolations").add("Tên đăng nhập chỉ được tối đa 25 ký tự");
        }

        // ---- VALIDATE PASSWORD ----
        if (password == null || password.trim().isEmpty()) {
            violations.get("passwordViolations").add("Mật khẩu không được để trống");
        } else if (!password.equals(password.trim())) {
            violations.get("passwordViolations").add("Mật khẩu không có dấu cách ở hai đầu");
        } else if (password.length() > 32) {
            violations.get("passwordViolations").add("Mật khẩu chỉ được tối đa 32 ký tự");
        }

        // ---- CHECK CREDENTIALS ----
        User userFromServer = null;
        try {
            userFromServer = userService.getByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (userFromServer == null) {
            violations.get("usernameViolations").add("Tên đăng nhập không tồn tại");
        } else {
            String hashedPassword = HashingUtils.hash(password);
            if (!hashedPassword.equals(userFromServer.getPassword())) {
                violations.get("passwordViolations").add("Mật khẩu không đúng");
            }
        }

        // ---- CHECK IF THERE ARE ERRORS ----
        boolean hasErrors = false;
        for (List<String> list : violations.values()) {
            if (!list.isEmpty()) {
                hasErrors = true;
                break;
            }
        }

        if (!hasErrors) {
            // Login success
            request.getSession().setAttribute("currentUser", userFromServer);
            response.sendRedirect(request.getContextPath() + "/");
        } else {
            // Return form with errors
            request.setAttribute("values", values);
            request.setAttribute("violations", violations);
            request.getRequestDispatcher("/WEB-INF/views/signinView.jsp").forward(request, response);
        }
    }
}
