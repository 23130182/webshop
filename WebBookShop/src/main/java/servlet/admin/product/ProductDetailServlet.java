package servlet.admin.product;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import beans.Category;
import beans.Product;
import service.CategoryService;
import service.ProductService;
import utils.TextUtils;

@WebServlet(name = "ProductDetailServlet", value = "/admin/productManager/detail")
public class ProductDetailServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private final ProductService productService = new ProductService();
    private final CategoryService categoryService = new CategoryService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        long id = 0;
        try {
            id = Long.parseLong(request.getParameter("id"));
        } catch (Exception e) {
            response.sendRedirect(request.getContextPath() + "/admin/productManager");
            return;
        }

        Product product = productService.getById(id);
        if (product == null) {
            response.sendRedirect(request.getContextPath() + "/admin/productManager");
            return;
        }

        /* ===== FORMAT DESCRIPTION ===== */
        String description = product.getDescription();
        if (description == null) {
            description = "";
        }
        product.setDescription(TextUtils.toParagraph(description));

        /* ===== GET CATEGORY ===== */
        Category category = categoryService.getByProductId(id);
        if (category == null) {
            category = new Category();
        }

        request.setAttribute("product", product);
        request.setAttribute("category", category);
        request.getRequestDispatcher("/WEB-INF/views/productDetailView.jsp")
               .forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Không dùng
    }
}
