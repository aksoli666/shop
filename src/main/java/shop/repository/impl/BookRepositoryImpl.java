package shop.repository.impl;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import shop.entity.Book;
import shop.exception.DataProcessingException;
import shop.repository.BookRepository;

@Repository
@RequiredArgsConstructor
public class BookRepositoryImpl implements BookRepository {
    private final SessionFactory sessionFactory;

    @Override
    public Book createBook(Book book) {
        Session session = null;
        Transaction transaction = null;
        try {
            session = sessionFactory.openSession();
            transaction = session.beginTransaction();
            session.persist(book);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new DataProcessingException("Can`t save book: " + book, e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return book;
    }

    @Override
    public Optional<Book> findBookById(Long id) {
        try (Session session = sessionFactory.openSession()) {
            return Optional.ofNullable(session.get(Book.class, id));
        } catch (Exception e) {
            throw new DataProcessingException("Can`t find book by id: " + id, e);
        }
    }

    @Override
    public List<Book> findAllBooks() {
        List<Book> books = Collections.emptyList();
        try (Session session = sessionFactory.openSession()) {
            books = session.createQuery("FROM Book", Book.class).getResultList();
            return books;
        } catch (Exception e) {
            throw new DataProcessingException("Fetching " + books + " failed", e);
        }
    }
}
