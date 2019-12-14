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
        "senderAddress", "receiverAddress",
        "date", "deliverer", "fee",
        "ordered", "pickedUp", "delivering", "received")
    );

    public static int getID(String propertyName) {
        return Types.indexOf(propertyName);
    }
}
