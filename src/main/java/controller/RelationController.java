package controller;

import dao.BookMapper;
import dao.BuyMapper;
import dao.ShopcartMapper;
import entity.BookWithDate;
import dto.response.Response;
import dto.response.Status;
import entity.Book;
import entity.Buy;
import entity.Shopcart;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by p on 2017/7/19.
 */
@RestController
@RequestMapping(value = "/set")
@CrossOrigin
public class RelationController {

    @Autowired
    private BookMapper bookMapper;
    @Autowired
    private BuyMapper buyMapper;
    @Autowired
    private ShopcartMapper shopcartMapper;

    @Transactional
    @PostMapping("/buy")
    public Response buy(int id ,HttpServletRequest request){
        Book book = bookMapper.findById(id);
        if (book.getValid()) {
            Buy buy = new Buy();
            buy.setBuyer(String.valueOf(request.getAttribute("phone")));
            buy.setBookid(id);
            Calendar calendar = Calendar.getInstance();
            java.util.Date utilDate = calendar.getTime();
            //java.util.Date日期转换成转成java.sql.Date格式
            Date nowDate = new Date(utilDate.getTime());
            buy.setDate(nowDate);
            buyMapper.insert(buy);

            bookMapper.changeValid(false, id);
            Shopcart shopcart = new Shopcart();
            shopcart.setBuyer(String.valueOf(request.getAttribute("phone")));
            shopcart.setBookid(id);
            shopcartMapper.delete(shopcart);
            return new Response<Boolean>().SUCCESS(true);
        }
        else {
            return new Response(new Status(0,"该书不能购买"));
        }
    }

    @PostMapping("/getMyBuy")
    public Response getMyBuy(HttpServletRequest request){

        List<BookWithDate> books = buyMapper
                .getByBuyer(String.valueOf(request.getAttribute("phone")));
        return new Response<List>().SUCCESS(books);
    }

    @PostMapping("/deleteMyBuy")
    public Response deleteMyBuy(int id ,HttpServletRequest request){
        Buy buy = new Buy();
        buy.setBookid(id);
        buy.setBuyer(String.valueOf(request.getAttribute("phone")));
        buyMapper.delete(buy);
        return new Response<Boolean>().SUCCESS(true);
    }

    @PostMapping("/addShopcart")
    public Response addShopcart(int id ,HttpServletRequest request){
        Shopcart shopcart = new Shopcart();
        shopcart.setBookid(id);
        shopcart.setBuyer(String.valueOf(request.getAttribute("phone")));
        shopcartMapper.insert(shopcart);
        return new Response<Boolean>().SUCCESS(true);
    }

    @PostMapping("/deleteShopcart")
    public Response deleteShopcart(int id ,HttpServletRequest request){
        Shopcart shopcart = new Shopcart();
        shopcart.setBookid(id);
        shopcart.setBuyer(String.valueOf(request.getAttribute("phone")));
        shopcartMapper.delete(shopcart);
        return new Response<Boolean>().SUCCESS(true);
    }

    @PostMapping("/getMyShopcart")
    public Response getMyShopcart(HttpServletRequest request){
        List<Book> books = shopcartMapper
                .getByBuyer(String.valueOf(request.getAttribute("phone")));
        return new Response<List>().SUCCESS(books);
    }


}
