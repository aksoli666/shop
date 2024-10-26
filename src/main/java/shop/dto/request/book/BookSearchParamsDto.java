package shop.dto.request.book;

public record BookSearchParamsDto(String[] title, String[] author, String[] isbn) {
}
