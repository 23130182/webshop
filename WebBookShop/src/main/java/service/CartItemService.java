package service;

import java.sql.SQLException;
import java.util.List;

import beans.CartItem;
import dao.CartItemDAO;

public class CartItemService {

    private final CartItemDAO cartItemDAO;

    public CartItemService() {
        this.cartItemDAO = new CartItemDAO();
    }

    // Thêm mới CartItem
    public long insert(CartItem cartItem) throws SQLException {
        return cartItemDAO.insert(cartItem);
    }

    // Cập nhật CartItem
    public void update(CartItem cartItem) throws SQLException {
        cartItemDAO.update(cartItem);
    }

    // Xóa CartItem theo id
    public void delete(long id) throws SQLException {
        cartItemDAO.delete(id);
    }

    // Lấy CartItem theo id
    public CartItem getById(long id) {
        return cartItemDAO.getById(id);
    }

    // Lấy tất cả CartItem
    public List<CartItem> getAll() {
        return cartItemDAO.getAll();
    }

    // Lấy CartItem theo phân trang
    public List<CartItem> getPart(int limit, int offset) {
        return cartItemDAO.getPart(limit, offset);
    }

    // Lấy CartItem theo phân trang + sắp xếp
    public List<CartItem> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return cartItemDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }

    // ================== Các phương thức riêng ==================

    // Lấy danh sách CartItem theo cartId
    public List<CartItem> getByCartId(long cartId) {
        return cartItemDAO.getByCartId(cartId);
    }

    // Lấy CartItem theo cartId và productId (trả về null nếu không tìm thấy)
    public CartItem getByCartIdAndProductId(long cartId, long productId) {
        return cartItemDAO.getByCartIdAndProductId(cartId, productId);
    }

    // Tổng số lượng sản phẩm trong giỏ của user
    public int sumQuantityByUserId(long userId) {
        return cartItemDAO.sumQuantityByUserId(userId);
    }
}
