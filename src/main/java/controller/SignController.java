package controller;

import dao.CodeMapper;
import dao.UserMapper;
import dto.FindPswParam;
import dto.SignInParam;
import dto.SignUpParam;
import dto.response.Response;
import dto.response.UserInfo;
import entity.Code;
import entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Random;

/**
 * Created by p on 2017/7/19.
 */
@RestController
@RequestMapping(value = "/")
@CrossOrigin
public class SignController {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CodeMapper codeMapper;

    /**
     * 登录
     *
     * @param param 用户参数
     * @return 返回信息，以及用户验证信息
     */
    @RequestMapping(value = "/signIn", method = RequestMethod.POST)
    public Response signIn(@RequestBody SignInParam param) {
        User user = userMapper.findByPhone(param.getPhone());
        if (user == null) {
            return Response.NO_USER;
        }
        if (!user.getPsw().equals(param.getPsw())) {
            return Response.PSW_ERROR;
        }
        String token = getATokenStr();
        userMapper.updateToken(param.getPhone(), token);
        user.setToken(token);
        user.setPsw(null);
        return new Response<User>().SUCCESS(user);
    }

    /**
     * 登录
     *
     * @param param 用户参数
     * @return 返回信息，以及用户验证信息
     */
    @RequestMapping(value = "/test", method = RequestMethod.GET)
    public Response test() throws Exception {
        throw new Exception();
    }

    /**
     * 注册
     *
     * @param param 用户参数
     * @return 返回信息，以及用户验证信息
     */
    @RequestMapping(value = "/signUp", method = RequestMethod.POST)
    public Response signUp(@RequestBody SignUpParam param) {
        //验证验证码
        Code code = codeMapper.findByPhone(param.getPhone());
        if (code == null || code.getCode() == null
                || !code.getCode().equals(param.getVercode())
                || isExpired(code.getDate())) {
            return Response.VERCODE_ERROR;
        }
//        System.out.println(param.getName());

        User user = userMapper.findByPhone(param.getPhone());
        if (user != null) {
            return Response.USER_REGISTERED;
        }

        user = new User();
        user.setPhone(param.getPhone());
        user.setName(param.getName());
        user.setPsw(param.getPsw());
        user.setToken(getATokenStr());
        user.setGender(param.getGender());
        userMapper.insert(user);
        user.setPsw(null);
        return new Response<User>().SUCCESS(user);

    }

    /**
     * 找回密码
     *
     * @param param 用户参数
     * @return 返回信息
     */
    @RequestMapping(value = "/findPsw", method = RequestMethod.POST)
    public Response findPsw(@RequestBody FindPswParam param) {
        //验证验证码
        Code code = codeMapper.findByPhone(param.getPhone());
        if (code == null || code.getCode() == null
                || !code.getCode().equals(param.getVerCode())
                || isExpired(code.getDate())) {
            return Response.VERCODE_ERROR;
        }

        User user = userMapper.findByPhone(param.getPhone());
        if (user == null) {
            return Response.NO_USER;
        }
        user.setPsw(param.getPsw());
        userMapper.updatePsw(param.getPhone(), param.getPsw());
        return Response.SUCCESS;
    }

    /**
     * 得到验证码
     *
     * @param phone 用户手机号
     * @return 返回信息
     */
    @RequestMapping(value = "getVerCode", method = RequestMethod.POST)
    public Response getSignUpVerCode(@RequestParam("phone") String phone) {
        return new Response<String>().SUCCESS(getCode(phone).getCode());
    }

    /**
     * 验证token
     *
     * @param token token
     * @return 验证结果
     */
    @RequestMapping(value = "verToken", method = RequestMethod.POST)
    public Response verToken(@RequestParam("token") String token) {
        User user = userMapper.findByToken(token);
        if (user == null) {
            return Response.INVALIED_TOKEN_ERROR;
        }
        UserInfo info = new UserInfo();
        info.setName(user.getName());
        info.setPhone(user.getPhone());
        return new Response<UserInfo>().SUCCESS(info);
    }

    /**
     * 辅助得到验证码
     *
     * @param phone 手机号
     * @return 验证码
     */
    private Code getCode(String phone) {
        Code oldCode = codeMapper.findByPhone(phone);
        Code newCode = new Code(phone);
//        System.out.println(newCode.getPhone());
//        System.out.println(newCode.getCode());
//        System.out.println(newCode.getDate());
        if (oldCode != null) {
            codeMapper.update(newCode);
        } else {
            codeMapper.insert(newCode);
        }
        return newCode;
    }

    private static String getATokenStr(){
        String base = "abcdefghijklmnopqrstuvwxyz0123456789";
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        String token;

        for (int i = 0; i < 16; i++) {
            int number = random.nextInt(base.length());
            sb.append(base.charAt(number));
        }

        token = sb.toString();

        return token;
    }

    public static boolean isExpired(Timestamp date){
        Calendar calendar = Calendar.getInstance();
        java.util.Date utilDate = calendar.getTime();
        //java.util.Date日期转换成转成java.sql.Date格式
        Timestamp nowDate = new Timestamp(utilDate.getTime());
        return nowDate.after(date);
    }

}
