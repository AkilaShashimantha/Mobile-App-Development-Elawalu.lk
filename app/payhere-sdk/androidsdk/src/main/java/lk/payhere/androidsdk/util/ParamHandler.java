package lk.payhere.androidsdk.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lk.payhere.androidsdk.PHConstants;
import lk.payhere.androidsdk.model.InitRequest;
import lk.payhere.androidsdk.model.Item;

/**
 * Created by chamika on 9/19/16.
 */

public class ParamHandler {

    public static Map<String, String> createParams(InitRequest req) throws UnsupportedEncodingException {
        Map<String, String> map = new HashMap<>();
        putToMap(map, "merchant_id", req.getMerchantId());
        putToMap(map, "return_url", PHConstants.dummyUrl);
        putToMap(map, "cancel_url", PHConstants.dummyUrl);
        putToMap(map, "notify_url", PHConstants.dummyUrl);
        putToMap(map, "first_name", req.getCustomer().getFirstName());
        putToMap(map, "last_name", req.getCustomer().getLastName());
        putToMap(map, "email", req.getCustomer().getEmail());
        putToMap(map, "phone", req.getCustomer().getPhone());
        putToMap(map, "address", req.getCustomer().getAddress().getAddress());
        putToMap(map, "city", req.getCustomer().getAddress().getCity());
        putToMap(map, "country", req.getCustomer().getAddress().getCountry());
        putToMap(map, "order_id", req.getOrderId());
        putToMap(map, "items", req.getItemsDescription());
        putToMap(map, "currency", req.getCurrency());
        putToMap(map, "amount", convertAmount(req.getAmount()));
        putToMap(map, "delivery_address", req.getCustomer().getDeliveryAddress().getAddress());
        putToMap(map, "delivery_city", req.getCustomer().getDeliveryAddress().getCity());
        putToMap(map, "delivery_country", req.getCustomer().getDeliveryAddress().getCountry());
        putToMap(map, "internal_checkout", "false");
        putToMap(map, "platform", PHConstants.PLATFORM);
        putToMap(map, "custom_1", req.getCustom1());
        putToMap(map, "custom_2", req.getCustom2());
        List<Item> items = req.getItems();
        if (!items.isEmpty()) {
            for (int i = 0; i < items.size(); i++) {
                Item item = items.get(i);
                int itemNo = i + 1;
                putToMap(map, "item_number_" + itemNo, item.getId());
                putToMap(map, "item_name_" + itemNo, item.getName());
                putToMap(map, "quantity_" + itemNo, String.valueOf(item.getQuantity()));
                putToMap(map, "amount_" + itemNo, String.valueOf(item.getAmount()));
            }
        }
        return map;
    }

    private static void putToMap(Map<String, String> map, String key, String value) throws UnsupportedEncodingException {
//        map.put(key, URLEncoder.encode(value,"UTF-8"));
        if (key != null && value != null) {
            map.put(key, value);
        }
    }

    public static String convertAmount(double amount) {
        return String.format(PHConstants.DEFAULT_LOCALE, "%.2f", amount);
    }
}
