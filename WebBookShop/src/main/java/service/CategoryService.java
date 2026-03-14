package service;

import java.sql.SQLException;
import java.util.List;

import beans.Category;
import dao.CategoryDAO;

public class CategoryService {

    private final CategoryDAO categoryDAO;

    public CategoryService() {
        this.categoryDAO = new CategoryDAO();
    }

    // Thêm mới Category
    public long insert(Category category) throws SQLException {
        return categoryDAO.insert(category);
    }

    // Cập nhật Category
    public void update(Category category) throws SQLException {
        categoryDAO.update(category);
    }

    // Xóa Category theo id
    public void delete(long id) throws SQLException {
        categoryDAO.delete(id);
    }

    // Lấy Category theo id
    public Category getById(long id) {
        return categoryDAO.getById(id);
    }

    // Lấy tất cả Category
    public List<Category> getAll() {
        return categoryDAO.getAll();
    }

    // Lấy Category theo phân trang
    public List<Category> getPart(int limit, int offset) {
        return categoryDAO.getPart(limit, offset);
    }

    // Lấy Category theo phân trang + sắp xếp
    public List<Category> getOrderedPart(int limit, int offset, String orderBy, String orderDir) {
        return categoryDAO.getOrderedPart(limit, offset, orderBy, orderDir);
    }

    // ================== Các phương thức riêng ==================

    // Lấy Category theo productId
    public Category getByProductId(long productId) {
        return categoryDAO.getByProductId(productId);
    }

    // Đếm tổng số Category
    public int count() {
        return categoryDAO.count();
    }
}
