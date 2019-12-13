package pl.piomin.service.blockchain;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author: HuShili
 * @date: 2019/2/13
 * @description: none
 */
public class PropertyType {

    public static ArrayList<String> Types = new ArrayList<>(
        Arrays.asList(
        "imgQR", "sender", "senderPhone", "receiver", "receiverPhone",
        "date", "deliverer", "fee",
        "ordered", "pickedUp", "delivered", "received")
    );

    public static int getID(String propertyName) {
        return Types.indexOf(propertyName);
    }
}
