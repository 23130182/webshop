package utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ImageUtils {

    private static final Path IMAGE_DIR = Paths.get(ConstantUtils.IMAGE_PATH);

    /**
     * Upload image từ request
     * @return tên file ảnh nếu upload thành công, null nếu không có ảnh
     */
    public static String upload(HttpServletRequest request) {

        try {
            Part filePart = request.getPart("image");

            if (filePart == null || filePart.getSize() == 0) {
                return null;
            }

            String contentType = filePart.getContentType();
            if (contentType == null || !contentType.startsWith("image")) {
                return null;
            }

            // tạo thư mục nếu chưa tồn tại
            if (!Files.exists(IMAGE_DIR)) {
                Files.createDirectories(IMAGE_DIR);
            }

            // ===== LẤY TÊN FILE GỐC =====
            String fileName = Paths.get(filePart.getSubmittedFileName())
                    .getFileName().toString();

            Path targetLocation = IMAGE_DIR.resolve(fileName);

            // ghi file vào IMAGE_DIR
            try (InputStream fileContent = filePart.getInputStream()) {
                Files.copy(fileContent, targetLocation, StandardCopyOption.REPLACE_EXISTING);
            }

            /* ===================== COPY SANG WEBAPP ===================== */

            String webImagePath = request.getServletContext().getRealPath("/image");
            Path webImageDir = Paths.get(webImagePath);

            if (!Files.exists(webImageDir)) {
                Files.createDirectories(webImageDir);
            }

            Path webImageFile = webImageDir.resolve(fileName);
            Files.copy(targetLocation, webImageFile, StandardCopyOption.REPLACE_EXISTING);

            /* ============================================================= */

            return fileName; // lưu DB

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * Xóa ảnh theo tên file
     */
    public static void delete(String imageName) {

        if (imageName == null || imageName.trim().isEmpty()) {
            return;
        }

        Path imagePath = IMAGE_DIR.resolve(imageName).normalize();

        try {
            Files.deleteIfExists(imagePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
