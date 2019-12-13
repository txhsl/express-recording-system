package pl.piomin.service.blockchain;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * @author: HuShili
 * @date: 2019/2/18
 * @description: none
 */
public class RoleType {

    public static ArrayList<String> Types = new ArrayList<>(
        Arrays.asList("Admin", "User", "Deliverer")
    );

    public static int getID(String roleName) {
        return Types.indexOf(roleName);
    }
}
