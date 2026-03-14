package service;

import java.sql.SQLException;
import java.util.List;

import beans.Cart;
import dao.CartDAO;

public class CartService {

    private final CartDAO cartDAO;

    public CartService() {
        this.cartDAO = new CartDAO();
    }

    // Thêm mới Cart
    public long insert(Cart cart) throws SQLException {
        return cartDAO.insert(cart);
    }

    // Cập nhật Cart
    public void update(Cart cart) throws SQLException {
        cartDAO.update(cart);
    }

    // Xóa Cart theo id
    public void delete(long id) throws SQLException {
        cartDAO.delete(id);
    }

    // Lấy Cart theo id
    public Cart getById(long id) {
        return cartDAO.getById(id);
    }

    // Lấy tất cả Cart
    public List<Cart> getAll() {
        return cartDAO.getAll();
    }

    // Lấy Cart theo phân trang
    public List<Cart> getPart(int limit, int offset) {
        return cartDAO.getPart(limit, offset);
    }

    // Lấy Cart theo phân trang + sắp xếp
    public List<Cart> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return cartDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }

    // ================== Các phương thức riêng ==================

    // Lấy Cart theo userId
    public Cart getByUserId(long userId) {
        return cartDAO.getByUserId(userId);
    }

    // Tổng số lượng sản phẩm trong giỏ của user
    public int countCartItemQuantityByUserId(long userId) {
        return cartDAO.countCartItemQuantityByUserId(userId);
    }

    // Tổng số đơn hàng của user
    public int countOrderByUserId(long userId) {
        return cartDAO.countOrderByUserId(userId);
    }

    // Số đơn hàng đang giao
    public int countOrderDeliverByUserId(long userId) {
        return cartDAO.countOrderDeliverByUserId(userId);
    }

    // Số đơn hàng đã nhận
    public int countOrderReceivedByUserId(long userId) {
        return cartDAO.countOrderReceivedByUserId(userId);
    }
}
