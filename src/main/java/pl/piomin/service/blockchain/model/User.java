package pl.piomin.service.blockchain.model;

import org.web3j.abi.datatypes.Address;

/**
 * @author: HuShili
 * @date: 2019/2/11
 * @description: none
 */
public class User {

    private String address;
    private String password;

    public User() {

    }

    public User(String address, String password) {
        this.address = address;
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
