package utils;

public class TextUtils {

	public static String toParagraph(String description) {
		if (description == null || description.isEmpty()) {
			return "";
		}

		// Tách các đoạn theo \r\n hoặc \n
		String[] paragraphs = description.split("\\r?\\n");

		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < paragraphs.length; i++) {
			String paragraph = paragraphs[i].trim(); // loại bỏ khoảng trắng thừa
			if (!paragraph.isEmpty()) {
				sb.append("<p>").append(paragraph).append("</p>\n");
			}
		}

		return sb.toString();
	}
}
