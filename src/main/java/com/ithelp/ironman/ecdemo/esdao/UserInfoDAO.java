package com.ithelp.ironman.ecdemo.esdao;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ithelp.ironman.ecdemo.bean.es.UserInfo;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.ithelp.ironman.ecdemo.constant.EsConstant.INDEX;

@Repository
public class UserInfoDAO {
    @Resource
    private RestHighLevelClient client;
    @Resource
    private ObjectMapper objectMapper;

    public IndexResponse createDoc(UserInfo userInfo) throws IOException {
        IndexRequest indexRequest = new IndexRequest(INDEX);
        indexRequest.id(userInfo.getId());
        indexRequest.source(convertUserInfoToMap(userInfo));
        return client.index(indexRequest, RequestOptions.DEFAULT);
    }

    public String updateDoc(UserInfo userInfo) throws IOException {
        UpdateRequest updateRequest = new UpdateRequest(
                INDEX,
                userInfo.getId());
        updateRequest.doc(convertUserInfoToMap(userInfo));
        UpdateResponse updateResponse = client.update(updateRequest, RequestOptions.DEFAULT);
        return updateResponse.getResult().name();
    }

    public List<UserInfo> findAll() throws IOException {
        SearchRequest searchRequest = buildSearchRequest(INDEX);
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);
        return getSearchResult(client.search(searchRequest, RequestOptions.DEFAULT));
    }

    public List<UserInfo> findByName(String field, String value) throws IOException {
        //來建立個搜尋請求，並告訴他我們要找ES哪個 INDEX
        SearchRequest searchRequest = new SearchRequest(INDEX);
        //建立ES 的資源搜尋類，搜尋起手式
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        //開始拼接ES的搜尋語句，語句爲field match(等於) value
        MatchQueryBuilder matchQueryBuilder = QueryBuilders
                .matchQuery(field, value);
        searchRequest.source(searchSourceBuilder.query(matchQueryBuilder));
        return getSearchResult(client.search(searchRequest, RequestOptions.DEFAULT));
    }

    public UserInfo findById(String id) throws IOException {
        GetRequest getRequest = new GetRequest(INDEX, id);
        GetResponse getResponse = client.get(getRequest, RequestOptions.DEFAULT);
        Map<String, Object> resultMap = getResponse.getSource();
        return convertMapToUserInfo(resultMap);
    }

    private Map<String, Object> convertUserInfoToMap(UserInfo userInfo) {
        return objectMapper.convertValue(userInfo, Map.class);
    }

    private UserInfo convertMapToUserInfo(Map<String, Object> map) {
        return objectMapper.convertValue(map, UserInfo.class);
    }

    private SearchRequest buildSearchRequest(String index) {
        SearchRequest searchRequest = new SearchRequest();
        searchRequest.indices(index);
        return searchRequest;
    }

    private List<UserInfo> getSearchResult(SearchResponse response) {
        SearchHit[] searchHit = response.getHits().getHits();
        List<UserInfo> userInfoList = new ArrayList<>();
        for (SearchHit hit : searchHit) {
            userInfoList.add(objectMapper.convertValue(hit.getSourceAsMap(), UserInfo.class));
        }
        return userInfoList;
    }

    public String deleteDoc(String id) throws IOException {
        DeleteRequest deleteRequest = new DeleteRequest(INDEX, id);
        return client.delete(deleteRequest, RequestOptions.DEFAULT).getResult().name();
    }
}
