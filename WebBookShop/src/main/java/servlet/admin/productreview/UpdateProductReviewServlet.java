package servlet.admin.productreview;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import service.ProductReviewService;

@WebServlet(name = "UpdateProductReviewServlet", value = "/admin/reviewManager/update")
public class UpdateProductReviewServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	private final ProductReviewService productReviewService = new ProductReviewService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// không dùng
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String errorMessage = "Đã có lỗi truy vấn!";

		long id;
		try {
			id = Long.parseLong(request.getParameter("id"));
		} catch (Exception e) {
			request.getSession().setAttribute("errorMessage", errorMessage);
			response.sendRedirect(request.getContextPath() + "/admin/reviewManager");
			return;
		}

		String action = request.getParameter("action");

		try {
			if ("HIDE".equals(action)) {
				productReviewService.hide(id);
				String successMessage = String.format("Đã ẩn đánh giá #%s thành công!", id);
				request.getSession().setAttribute("successMessage", successMessage);
			}

			if ("SHOW".equals(action)) {
				productReviewService.show(id);
				String successMessage = String.format("Đã hiện đánh giá #%s thành công!", id);
				request.getSession().setAttribute("successMessage", successMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			request.getSession().setAttribute("errorMessage", errorMessage);
		}

		response.sendRedirect(request.getContextPath() + "/admin/reviewManager");
	}
}
