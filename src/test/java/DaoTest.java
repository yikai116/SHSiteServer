import dao.BookMapper;
import dao.BuyMapper;
import entity.Book;
import entity.BookWithDate;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;

/**
 * Created by p on 2017/7/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath*:spring-mybaits.xml"})
public class DaoTest {

    @Autowired
    BookMapper bookMapper;
    @Autowired
    BuyMapper buyMapper;
    @Test
    public void name() throws Exception {
        ArrayList<BookWithDate> dates = (ArrayList<BookWithDate>) buyMapper.getByBuyer("15196673448");
        System.out.println(dates.get(0).getName());
    }
}
