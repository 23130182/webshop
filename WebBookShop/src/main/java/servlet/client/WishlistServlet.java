package servlet.client;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import beans.Product;
import beans.User;
import beans.WishlistItem;
import service.ProductService;
import service.WishlistItemService;

@WebServlet(name = "WishlistServlet", value = "/wishlist")
public class WishlistServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
    private final WishlistItemService wishlistService = new WishlistItemService();
    private final ProductService productService = new ProductService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user != null) {
            try {
                // Lấy danh sách wishlist của user
                List<WishlistItem> wishlist = wishlistService.getByUserId(user.getId());

                // Gắn Product vào từng WishlistItem
                for (WishlistItem item : wishlist) {
                    Product product = productService.getById(item.getProductId());
                    item.setProduct(product);
                }

                request.setAttribute("wishlistItems", wishlist);
            } catch (Exception e) {
                e.printStackTrace();
                request.setAttribute("errorMessage", "Không thể tải wishlist, vui lòng thử lại sau");
            }
        }

        String jspPath = request.getParameter("page") != null 
            ? request.getParameter("page") 
            : "/WEB-INF/views/wishlistView.jsp";
        request.getRequestDispatcher(jspPath).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("currentUser");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/signin");
            return;
        }

        try {
            String action = request.getParameter("action");
            String productIdParam = request.getParameter("productId");
            String idParam = request.getParameter("id");

            if (productIdParam != null) {
                // POST từ nút wishlist trang sản phẩm
                long productId = Long.parseLong(productIdParam);
                boolean exists = wishlistService.countByUserIdAndProductId(user.getId(), productId) > 0;

                if ("add".equals(action) && !exists) {
                    WishlistItem item = new WishlistItem();
                    item.setUserId(user.getId());
                    item.setProductId(productId);
                    wishlistService.insert(item);
                } else if ("delete".equals(action) && exists) {
                    // Xóa item theo productId
                    List<WishlistItem> list = wishlistService.getByUserId(user.getId());
                    for (WishlistItem item : list) {
                        if (item.getProductId() == productId) {
                            wishlistService.delete(item.getId());
                            break;
                        }
                    }
                }
            } else if (idParam != null) {
                // POST từ trang wishlist (xóa theo id)
                long id = Long.parseLong(idParam);
                wishlistService.delete(id);
            }

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("errorMessage", "Thao tác wishlist thất bại, vui lòng thử lại");
        }

        // Redirect về trang trước đó để reload, nút cập nhật màu
        String referer = request.getHeader("referer");
        response.sendRedirect(referer != null ? referer : request.getContextPath() + "/");
    }
}
