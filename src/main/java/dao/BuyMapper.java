package dao;


import entity.BookWithDate;
import entity.Buy;

import java.util.List;

/**
 * Created by p on 2017/7/30.
 */
public interface BuyMapper {
    void insert(Buy buy);
    void delete(Buy buy);
    List<BookWithDate> getByBuyer(String buyer);
}
