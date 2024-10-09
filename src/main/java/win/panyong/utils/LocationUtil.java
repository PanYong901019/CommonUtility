package win.panyong.utils;


import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.TypeReference;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class LocationUtil {
    /**
     * 源坐标类型：
     * 1：GPS设备获取的角度坐标，wgs84坐标;
     * 2：GPS获取的米制坐标、sogou地图所用坐标;
     * 3：google地图、soso地图、aliyun地图、mapabc地图和amap地图所用坐标，国测局（gcj02）坐标;
     * 4：3中列表地图坐标对应的米制坐标;
     * 5：百度地图采用的经纬度坐标;
     * 6：百度地图采用的米制坐标;
     * 7：mapbar地图坐标;
     * 8：51地图坐标
     * 目标坐标类型：
     * 3：国测局（gcj02）坐标;
     * 4：3中对应的米制坐标;
     * 5：bd09ll(百度经纬度坐标);
     * 6：bd09mc(百度米制经纬度坐标)
     *
     * @param longitude
     * @param latitude
     * @return
     * @throws Exception
     */
    public static Map<String, String> changePosition(String longitude, String latitude, String from, String to) throws Exception {
        Map<String, String> result = new HashMap<>();
        JSONObject param = new JSONObject();
        param.put("coords", longitude + "," + latitude);
        param.put("from", from);
        param.put("to", to);
        param.put("ak", "ZSTYdUEWqemORMENNEzBROoEQtx60PLh");
        Map<String, Object> stringObjectMap = ObjectUtil.jsonStringToMap(HttpUtil.doHttpGet("http://api.map.baidu.com/geoconv/v1/", param, null));
        if ((Integer) stringObjectMap.get("status") == 0) {
            Map node = (Map) ((List) stringObjectMap.get("result")).get(0);
            result.put("longitude", ((String) node.get("x"))); //经度
            result.put("latitude", ((String) node.get("y")));  //纬度
        }
        return result;
    }


    /**
     * 0：直线距离
     * 1：驾车导航距离（仅支持国内坐标）会考虑路况，故在不同时间请求返回结果可能不同。
     * 2：公交规划距离（仅支持同城坐标,QPS不可超过1，否则可能导致意外）
     * 3：步行规划距离（仅支持5km之间的距离）
     *
     * @param key
     * @param origins
     * @param destination
     * @param type
     * @return
     * @throws IOException
     */
    public static LinkedHashMap<String, Map<String, String>> getDistance(String key, List<String> origins, String destination, String type) throws Exception {
        LinkedHashMap<String, Map<String, String>> result = new LinkedHashMap<>();
        if (origins != null && origins.size() != 0 && !StringUtil.invalid(destination) && !StringUtil.invalid(type)) {
            StringBuilder sb = new StringBuilder();
            origins.forEach(s -> sb.append(s).append("%7C"));
            sb.substring(0, sb.length() - 1);
            String url = "http://restapi.amap.com/v3/distance";
            JSONObject params = new JSONObject();
            params.put("key", key);
            params.put("origins", sb.toString());
            params.put("destination", destination);
            params.put("type", type);
            String json = HttpUtil.doHttpGet(url, params, null);
            List<Map<String, String>> maps = ObjectUtil.jsonStringToObject(ObjectUtil.objectToJsonString(ObjectUtil.jsonStringToMap(json).get("results")), new TypeReference<List<Map<String, String>>>() {
            });
            maps.stream().sorted((m1, m2) -> {
                if (new Integer(m1.get("distance")) > new Integer(m2.get("distance"))) return 1;
                if (new Integer(m1.get("distance")).equals(new Integer(m2.get("distance")))) return 0;
                return -1;
            }).forEach(map -> {
                result.put(origins.get(new Integer(map.get("origin_id")) - 1), map);
            });
        }
        return result;
    }
}
