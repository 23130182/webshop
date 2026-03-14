package dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {

    // Thêm mới, trả về id (nếu có) hoặc -1 nếu thất bại
    long insert(T t) throws SQLException;

    // Cập nhật
    void update(T t) throws SQLException;

    // Xóa theo id
    void delete(long id) throws SQLException;

    // Lấy theo id, trả về null nếu không tìm thấy
    T getById(long id);

    // Lấy tất cả
    List<T> getAll();

    // Lấy 1 phần (phân trang)
    List<T> getPart(int limit, int offset);

    // Lấy 1 phần theo thứ tự
    List<T> getOrderedPart(int limit, int offset, String orderBy, String orderDir);
}
