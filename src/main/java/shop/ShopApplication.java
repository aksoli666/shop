package shop;

import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import shop.entity.Book;
import shop.service.BookService;

@SpringBootApplication
public class ShopApplication {
    @Autowired
    private BookService bookService;

    public static void main(String[] args) {
        SpringApplication.run(ShopApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            Book book = new Book();
            book.setTitle("First Book");
            book.setAuthor("First Author");
            book.setIsbn("1");
            book.setPrice(BigDecimal.ONE);
            book.setDescription("First Description");
            book.setCoverImage("First Cover Image");
            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }
}
