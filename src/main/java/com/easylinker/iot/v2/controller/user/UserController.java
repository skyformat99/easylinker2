package com.easylinker.iot.v2.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.easylinker.iot.v2.constants.FailureMessageEnum;
import com.easylinker.iot.v2.constants.SuccessMessageEnum;
import com.easylinker.iot.v2.model.AppUser;
import com.easylinker.iot.v2.repository.AppUserRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Created by wwhai on 2017/11/15.
 * 用户相关
 */
@Api(value = "用户操作相关", description = "用户的增删改查操作")
@RestController
public class UserController {
    String username;
    String password;
    String email;
    String phone;
    @Autowired
    AppUserRepository appUserRepository;


    @ApiOperation(value = "增加一个用户", notes = "增加一个用户", httpMethod = "POST")
    @RequestMapping(value = "/user", method = RequestMethod.POST)
    public JSONObject addUser(@RequestBody(required = false) Map<String, String> loginParamMap) {
        JSONObject resultJson = new JSONObject();
        /**
         * Map 提取参数的时候可能会抛出异常，所以进行异常捕获
         */
        try {
            username = loginParamMap.get("username");
            password = loginParamMap.get("password");
            email = loginParamMap.get("email");
            phone = loginParamMap.get("phone");
            /**
             * 排除非空
             */
            if (username.equals("") || password.equals("") || email.equals("") || phone.equals("")) {
                resultJson.put("state", 0);
                resultJson.put("message", FailureMessageEnum.INVALID_PARAM);
            } else {
                /**
                 *  判断用户是否存在
                 */

                if (appUserRepository.findTop1ByUsernameOrEmailOrPhone(username, email, phone) != null) {
                    resultJson.put("state", 0);
                    resultJson.put("message", FailureMessageEnum.USER_EXIST);

                    /**
                     * 所有的非法条件过滤以后，进行增加用户
                     */
                } else {
                    AppUser appUser = new AppUser();
                    appUser.setUsername(username);
                    appUser.setPassword(password);
                    appUser.setPhone(phone);
                    appUser.setEmail(email);
                    appUserRepository.save(appUser);
                    resultJson.put("state", 1);
                    resultJson.put("data", appUser);
                    resultJson.put("message", SuccessMessageEnum.REGISTER_SUCCESS);
                }

            }
        } catch (Exception e) {
            /**
             * 抛出空指针的时候，就是参数没有传递
             */
            if (e instanceof NullPointerException) {
                resultJson.put("state", 0);
                resultJson.put("message", FailureMessageEnum.INVALID_PARAM);
            }
        }

        return resultJson;
    }

//    @ApiOperation(value = "删除一个用户", notes = "删除一个用户", httpMethod = "DELETE")
//    @RequestMapping(value = "/user", method = RequestMethod.DELETE)
//    public JSONObject deleteUser() {
//
//        return resultJson;
//    }

    @ApiOperation(value = "更新一个用户", notes = "更新一个用户", httpMethod = "PUT")
    @RequestMapping(value = "/user", method = RequestMethod.PUT)
    public JSONObject updateUser() {
        JSONObject resultJson = new JSONObject();

        return resultJson;
    }

    @ApiOperation(value = "查找一个用户", notes = "查询一个用户", httpMethod = "GET")
    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public JSONObject findUser(@PathVariable String userId) {
        JSONObject resultJson = new JSONObject();
        if (userId != null) {
            AppUser appUser = appUserRepository.findOne(userId);
            if (appUser != null) {
                resultJson.put("state", 1);
                resultJson.put("data", appUser);
                resultJson.put("message", SuccessMessageEnum.OPERATE_SUCCESS);
            }
        } else {
            resultJson.put("state", 0);
            resultJson.put("message", FailureMessageEnum.OPERATE_FAILED);
        }


        return resultJson;
    }

    @ApiOperation(value = "测试", notes = "测试", httpMethod = "GET")
    @RequestMapping(value = "/user/test", method = RequestMethod.GET)
    public JSONObject test() {
        JSONObject resultJson = new JSONObject();
        resultJson.put("state", "0");
        resultJson.put("message", "testOk");
        return resultJson;
    }

//    public static void main(String[] args) {
//        String username = "";
//        String password = "password";
//        Assert.hasLength(username, "error");
//        Assert.hasLength(password, "error");
//
//        if (username.equals("") || password.equals("")) {
//            System.out.println("sssss");
//        } else {
//            System.out.println("bbbbb");
//        }
//
//
//    }
}
