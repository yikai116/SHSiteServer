package controller;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.org.apache.xpath.internal.operations.Bool;
import dao.BookMapper;
import dto.BookInsertParam;
import dto.response.Response;
import entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by p on 2017/7/19.
 */
@RestController
@RequestMapping(value = "/set")
@CrossOrigin
public class BookController {

    @Autowired
    private BookMapper bookMapper;

    @RequestMapping(value = "/insertBook", method = RequestMethod.POST)
    public Response insertBook(@RequestBody BookInsertParam param, HttpServletRequest request) throws Exception{
        Book book = new Book();
        book.setName(param.getName());
        book.setAuthor(param.getAuthor());
        book.setLevel(param.getLevel());
        book.setDescription(param.getDescription());
        book.setPrice(param.getPrice());
        book.setOwner(String.valueOf(request.getAttribute("phone")));
        book.setValid(true);
        book.setType(new Gson().toJson(param.getType()));
        book.setPic(new Gson().toJson(param.getPic()));
        bookMapper.insertBook(book);
        return new Response<Boolean>().SUCCESS(true);
    }

    @RequestMapping(value = "/getMyBooks", method = RequestMethod.POST)
    public Response getMyBooks(HttpServletRequest request) throws Exception{

        return new Response<List>().SUCCESS(
                bookMapper.getBooksByOwner(
                        String.valueOf(request.getAttribute("phone"))));

    }

    @RequestMapping(value = "/getBookById", method = RequestMethod.POST)
    public Response getBookById(int id) throws Exception{

        return new Response<Book>().SUCCESS(
                bookMapper.findById(id));

    }

    @RequestMapping(value = "/deleteBook", method = RequestMethod.POST)
    public Response deleteBook(int id) throws Exception{

        bookMapper.deleteById(id);
        return new Response<Boolean>().SUCCESS(true);

    }

    @RequestMapping(value = "/getLastBook", method = RequestMethod.POST)
    public Response getLastBook() throws Exception{
        ArrayList<ArrayList<Book>> books = new ArrayList<ArrayList<Book>>();
        books.add((ArrayList<Book>) bookMapper.getLastBook("%小说%"));
        books.add((ArrayList<Book>) bookMapper.getLastBook("%教科书%"));
        books.add((ArrayList<Book>) bookMapper.getLastBook("%漫画%"));
        books.add((ArrayList<Book>) bookMapper.getLastBook("%杂志%"));
        books.add((ArrayList<Book>) bookMapper.getLastBook("%其他%"));

        return new Response<ArrayList<ArrayList<Book>>>().SUCCESS(books);
    }

    @RequestMapping(value = "/getMyLike", method = RequestMethod.POST)
    public Response getMyLike() throws Exception{
        return new Response<List>().SUCCESS(bookMapper.getMyLike());
    }

    @PostMapping("/search")
    public Response search(@RequestParam String labels) throws UnsupportedEncodingException {

        String[] ss = labels.split("\\s");
        for (int i = 0; i < ss.length; i++) {
            ss[i] = "%" + ss[i] + "%";
        }
//        System.out.println(labels);
        return new Response<List>().SUCCESS(bookMapper.search(ss));
    }

    @PostMapping("/getMoreByType")
    public Response getMoreByType(@RequestParam String type,@RequestParam int start,@RequestParam int end) throws UnsupportedEncodingException {

        type = "%" + type + "%";
        return new Response<List>().SUCCESS(bookMapper.getBookByType(type,start,end));
    }

    @RequestMapping(value = "/uploadPic", method = RequestMethod.POST)
    public Response uploadPic(@RequestParam MultipartFile pic, HttpServletRequest request) throws Exception {
        // 保存相对路径到数据库 图片写入服务器
        if (pic != null && !pic.isEmpty()) {
            // 获取图片的文件名
            String fileName = pic.getOriginalFilename();
            // 获取图片的扩展名
            String extensionName = fileName
                    .substring(fileName.lastIndexOf(".") + 1);
            // 新的图片文件名 = 获取时间戳+"."图片扩展名
            String newFileName = System.currentTimeMillis()
                    + "." + extensionName;
            File file = new File(
                    request.getSession().getServletContext()
                            .getRealPath("/avatar"),
                    newFileName);
            if (!file.exists()) {
                file.mkdirs();
            }
            pic.transferTo(file);
            String host="http://" + request.getServerName() //服务器地址
                    + ":"
                    + request.getServerPort();
            String url = host + "/SHSS/avatar/" + newFileName;
            return new Response<String>().SUCCESS(url);
        } else {
            return Response.NO_FILE;
        }
    }
}
