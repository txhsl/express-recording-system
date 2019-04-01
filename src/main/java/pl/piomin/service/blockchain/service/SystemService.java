package pl.piomin.service.blockchain.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.FileCopyUtils;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.contract.System_sol_System;

import java.util.HashMap;
import java.util.Map;

import static org.web3j.tx.gas.DefaultGasProvider.GAS_LIMIT;
import static org.web3j.tx.gas.DefaultGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/2/17
 * @description: none
 */
@Service
public class SystemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemService.class);
    private String sysAddress;

    private final Web3j web3j;

    public SystemService(Web3j web3j) {
        this.web3j = web3j;

        try {
            LOGGER.info("Blockchain height: " + web3j.ethBlockNumber().send().getBlockNumber() + ". Connected!");
        } catch (Exception e) {
            LOGGER.error("Connection failed. " + e.getMessage());
        }

        sysAddress = recover();
    }

    private String recover() {
        try {
            Resource resource = new ClassPathResource("system.json");
            JSONObject json = new JSONObject(new String(FileCopyUtils.copyToByteArray(resource.getFile())));
            return json.getString("address");
        } catch (Exception e) {
            LOGGER.error("Address of System Contract not found. " + e.getMessage());
        }
        return null;
    }

    public void reset(Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.deploy(web3j, credentials, GAS_PRICE, GAS_LIMIT).send();
        LOGGER.info("System Contract deployed: " + system.getContractAddress());

        this.sysAddress = system.getContractAddress();
    }

    public TransactionReceipt addRC(String name, Address rcAddr, Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = system.setIndex(new Utf8String(name), rcAddr).send();

        LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public TransactionReceipt setRC(String userAddr, String roleName, Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        TransactionReceipt transactionReceipt = system.register(new Address(userAddr), new Utf8String(roleName)).send();

        LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        return transactionReceipt;
    }

    public Address getRC(Credentials credentials) throws Exception {
        return getRC(credentials.getAddress(), credentials);
    }

    public Address getRC(String userAddr, Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        String rcAddr = system.getRC(new Address(userAddr)).send().getValue();

        LOGGER.info("Read succeed: " + rcAddr);
        return new Address(rcAddr);
    }

    public Address getIndex(String roleName, Credentials credentials) throws Exception {
        System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
        String rcAddr = system.getIndex(new Utf8String(roleName)).send().getValue();

        LOGGER.info("Read succeed: " + rcAddr);
        return new Address(rcAddr);
    }

    public Map<String, String> getRoleAll(Credentials credentials) throws Exception {
        Map<String, String> result = new HashMap<>();
        System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);

        for (String role : RoleType.Types) {
            String rcAddr = system.getIndex(new Utf8String(role)).send().getValue();
            if (!rcAddr.equals("")) {
                result.putIfAbsent(role, rcAddr);
            }
        }

        return result;
    }

    public String getSysAddress() {
        return sysAddress;
    }
}
