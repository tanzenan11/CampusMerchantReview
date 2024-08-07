package com.cmr;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.cmr.dto.UserDTO;
import com.cmr.entity.User;
import com.cmr.service.IUserService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import javax.annotation.Resource;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static com.cmr.utils.RedisConstants.LOGIN_USER_KEY;
import static com.cmr.utils.RedisConstants.LOGIN_USER_TTL;

// P69创建 tokens.txt文件
@SpringBootTest
@AutoConfigureMockMvc
class VoucherOrderControllerTest {

    @Resource
    private MockMvc mockMvc;

    @Resource
    private IUserService userService;

    @Resource
    private ObjectMapper mapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    /**
     * 向redis写入token数据
     * @throws IOException
     */
    @Test
    public void createToken() throws IOException {
        List<User> list = userService.list();
        PrintWriter printWriter = new PrintWriter(new FileWriter("E:\\token.txt"));
        for(User user: list){
            String token = UUID.randomUUID().toString(true);
            UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
            Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                    CopyOptions.create()
                            .setIgnoreNullValue(true)
                            .setFieldValueEditor((fieldName, fieldValue)->fieldValue.toString()));
            String tokenKey = LOGIN_USER_KEY + token;
            stringRedisTemplate.opsForHash().putAll(tokenKey, userMap);
            stringRedisTemplate.expire(tokenKey, LOGIN_USER_TTL, TimeUnit.MINUTES);
            printWriter.print(token + "\n");
            printWriter.flush();
        }
    }
}