package dao;


import entity.Book;
import entity.Shopcart;

import java.util.List;

/**
 * Created by p on 2017/7/30.
 */
public interface ShopcartMapper {
    void insert(Shopcart shopcart);
    void delete(Shopcart shopcart);
    List<Book> getByBuyer(String buyer);
}
