package servlet.admin.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.UserService;

@WebServlet(name = "DeleteUserServlet", value = "/admin/userManager/delete")
public class DeleteUserServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private final UserService userService = new UserService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long id;

        // 1. Parse ID
        try {
            id = Long.parseLong(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendRedirect(request.getContextPath() + "/admin/userManager");
            return;
        }

        try {
            // 2. Xóa trực tiếp
            userService.delete(id);

            // 3. Thành công
            request.getSession().setAttribute(
                "successMessage",
                "Xóa người dùng #" + id + " thành công!"
            );

        } catch (Exception e) {

            e.printStackTrace(); // log cho dev

            String errorMessage = "Xóa người dùng #" + id + " thất bại!";

            // 4. Phân tích lỗi DB (nâng cao)
            String dbMessage = e.getMessage().toLowerCase();

            if (dbMessage.contains("foreign key")) {
                errorMessage = "Không thể xóa người dùng id: " + id + " vì đang được sử dụng";
            } else if (dbMessage.contains("no rows affected")) {
                errorMessage = "Người dùng " + id + " không tồn tại";
            }

            request.getSession().setAttribute("errorMessage", errorMessage);
        }

        // 5. Redirect
        response.sendRedirect(request.getContextPath() + "/admin/userManager");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.sendRedirect(request.getContextPath() + "/admin/userManager");
    }
}