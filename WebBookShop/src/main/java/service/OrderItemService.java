package service;

import java.sql.SQLException;
import java.util.List;

import beans.OrderItem;
import dao.OrderItemDAO;

public class OrderItemService {

    private final OrderItemDAO orderItemDAO;

    public OrderItemService() {
        this.orderItemDAO = new OrderItemDAO();
    }

    // Thêm 1 OrderItem
    public long insert(OrderItem orderItem) throws SQLException {
        return orderItemDAO.insert(orderItem);
    }

    // Cập nhật OrderItem
    public void update(OrderItem orderItem) throws SQLException {
        orderItemDAO.update(orderItem);
    }

    // Xóa OrderItem theo id
    public void delete(long id) throws SQLException {
        orderItemDAO.delete(id);
    }

    // Lấy theo id
    public OrderItem getById(long id) {
        return orderItemDAO.getById(id);
    }

    // Lấy tất cả
    public List<OrderItem> getAll() {
        return orderItemDAO.getAll();
    }

    // Lấy theo phân trang
    public List<OrderItem> getPart(int limit, int offset) {
        return orderItemDAO.getPart(limit, offset);
    }

    // Lấy theo phân trang + sắp xếp
    public List<OrderItem> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return orderItemDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }

    // ================== Các phương thức riêng ==================

    // Thêm nhiều OrderItem cùng lúc
    public void bulkInsert(List<OrderItem> orderItems) {
        orderItemDAO.bulkInsert(orderItems);
    }

    // Lấy tên sản phẩm theo orderId
    public List<String> getProductNamesByOrderId(long orderId) {
        return orderItemDAO.getProductNamesByOrderId(orderId);
    }

    // Lấy tất cả OrderItem theo orderId
    public List<OrderItem> getByOrderId(long orderId) {
        return orderItemDAO.getByOrderId(orderId);
    }
}
