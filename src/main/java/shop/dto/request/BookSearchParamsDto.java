package shop.dto.request;

public record BookSearchParamsDto(String[] title, String[] author, String[] isbn) {
}
