package servlet.admin.productreview;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Product;
import beans.ProductReview;
import beans.User;
import service.ProductReviewService;
import service.ProductService;
import service.UserService;
import utils.TextUtils;

@WebServlet(name = "ProductReviewDetailServlet", value = "/admin/reviewManager/detail")
public class ProductReviewDetailServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private final ProductReviewService productReviewService = new ProductReviewService();
    private final UserService userService = new UserService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long id;
        try {
            id = Long.parseLong(request.getParameter("id"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/reviewManager");
            return;
        }

        ProductReview productReview = productReviewService.getById(id);
        if (productReview == null) {
            response.sendRedirect(request.getContextPath() + "/admin/reviewManager");
            return;
        }

        /* ===== FORMAT CONTENT ===== */
        productReview.setContent(
                TextUtils.toParagraph(
                        productReview.getContent() == null ? "" : productReview.getContent()
                )
        );

        /* ===== LOAD USER ===== */
        User user = userService.getById(productReview.getUserId());
        if (user != null) {
            productReview.setUser(user);
        }

        /* ===== LOAD PRODUCT ===== */
        Product product = productService.getById(productReview.getProductId());
        if (product != null) {
            productReview.setProduct(product);
        }

        request.setAttribute("productReview", productReview);
        request.getRequestDispatcher("/WEB-INF/views/productReviewDetailView.jsp")
                .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    }
}
