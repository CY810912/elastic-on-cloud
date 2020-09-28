package com.ithelp.ironman.ecdemo.esdao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ithelp.ironman.ecdemo.bean.es.UserInfo;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

import static com.ithelp.ironman.ecdemo.constant.EsConstant.INDEX;
import static com.ithelp.ironman.ecdemo.constant.EsConstant.TYPE;

@Repository
public class UserInfoDAO {
    @Resource
    private RestHighLevelClient client;
    @Resource
    private ObjectMapper objectMapper;

    public IndexResponse createDoc(UserInfo userInfo) throws IOException {
        IndexRequest indexRequest = new IndexRequest(INDEX,TYPE);
        indexRequest.id(userInfo.getId());
        indexRequest.source(convertProfileDocumentToMap(userInfo));
        return client.index(indexRequest, RequestOptions.DEFAULT);
    }

    private Map<String, Object> convertProfileDocumentToMap(UserInfo userInfo) {
        return objectMapper.convertValue(userInfo, Map.class);
    }
}
