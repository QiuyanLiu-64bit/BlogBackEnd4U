package com.cqucs.blogbackend.test;

import cn.hutool.core.convert.ConvertException;
import cn.hutool.http.HttpException;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.cqucs.blogbackend.util.JsonUtils;
import lombok.experimental.UtilityClass;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@UtilityClass
public class ChatController {
    /**
     * 聊天端点
     */
    String chatEndpoint = "https://api.openai.com/v1/chat/completions";
    /**
     * api密匙
     */
    String apiKey = "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";

    /**
     * 发送消息
     *
     * @param txt 内容
     * @return {@link String}
     */
    public String chat(String txt) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("model", "gpt-3.5-turbo-0613");
        List<Map<String, String>> dataList = new ArrayList<>();
        dataList.add(new HashMap<String, String>(){{
            put("role", "user");
            put("content", txt);
        }});
        paramMap.put("messages", dataList);
        JSONObject message = null;
        try {
            // 创建代理对象
            Proxy proxy = new Proxy(Proxy.Type.SOCKS, new InetSocketAddress("localhost", 7890));
            // 创建HttpRequest对象
            HttpRequest httpRequest = HttpRequest.post(chatEndpoint);
            // 为HttpRequest对象设置代理对象
            httpRequest.setProxy(proxy);
            /*String body = HttpRequest.post(chatEndpoint)
                    .header("Authorization", apiKey)
                    .header("Content-Type", "application/json")
                    .body(JsonUtils.toJson(paramMap))
                    .execute()
                    .body();*/
            // 为HttpRequest对象设置请求头和请求体
            httpRequest.header("Authorization", apiKey)
                    .header("Content-Type", "application/json")
                    .body(JsonUtils.toJson(paramMap));
            // 发送和执行HttpRequest对象，并返回一个HttpResponse对象
            HttpResponse httpResponse = httpRequest.execute();
            // 获取HttpResponse对象的响应体，并返回一个字符串
            String body = httpResponse.body();
            JSONObject jsonObject = JSONUtil.parseObj(body);
            JSONArray choices = jsonObject.getJSONArray("choices");
            JSONObject result = choices.get(0, JSONObject.class, Boolean.TRUE);
            message = result.getJSONObject("message");
        } catch (HttpException e) {
            e.printStackTrace();
            return "出现了异常";
        } catch (ConvertException e) {
            e.printStackTrace();
            return "出现了异常";
        }
        return message.getStr("content");
    }

    public static void main(String[] args) {
        System.out.println(chat("Hello，一个小浪吴啊"));
    }
}
