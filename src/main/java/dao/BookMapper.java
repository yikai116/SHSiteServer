package dao;

import entity.Book;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by p on 2017/7/30.
 */
public interface BookMapper {
    Book findById(int id);
    void insertBook(Book book);
    List<Book> getBooksByOwner(String phone);
    List<Book> getLastBook(String type);
    List<Book> getBookByType(String type, int start, int end);
    List<Book> getMyLike();
    List<Book> search(String[] labels);
    void deleteById(int id);
    void changeValid(boolean valid,int id);
}
