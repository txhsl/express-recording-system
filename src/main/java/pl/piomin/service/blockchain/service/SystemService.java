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
import org.web3j.crypto.CipherException;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.contract.Role_sol_Role;
import pl.piomin.service.blockchain.contract.System_sol_System;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import static pl.piomin.service.blockchain.model.CustomGasProvider.GAS_LIMIT;
import static pl.piomin.service.blockchain.model.CustomGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/2/17
 * @description: none
 */
@Service
public class SystemService {

    private static final Logger LOGGER = LoggerFactory.getLogger(SystemService.class);
    private String sysAddress;

    private final int REQUEST_LIMIT = 10;
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

    public boolean reset(Credentials credentials) throws Exception {
        //System
        System_sol_System system = System_sol_System.deploy(web3j, credentials, GAS_PRICE, GAS_LIMIT).send();
        LOGGER.info("System Contract deployed: " + system.getContractAddress());
        this.sysAddress = system.getContractAddress();

        //Roles
        for (int i = 0; i < 3; i++) {
            TransactionReceipt transactionReceipt = system.addRC(new Utf8String(RoleType.Types.get(i)), new Address(UserService.accounts[0])).send();
            LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        }

        //Properties
        for (int j = 0; j < PropertyType.Types.size(); j++) {
            TransactionReceipt transactionReceipt = system.addDC(new Utf8String(PropertyType.Types.get(j)), new Address(UserService.accounts[0])).send();
            LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        }

        TransactionReceipt receipt = register(new Address(UserService.accounts[0]), RoleType.Types.get(0), credentials);
        LOGGER.info("Transaction succeed: " + receipt.toString());
        for (int i = 1; i < 9; i++) {
            //register
            TransactionReceipt transactionReceipt = register(new Address(UserService.accounts[i]), RoleType.Types.get((i+1)%2+1), credentials);
            LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
        }
        return true;
    }

    public TransactionReceipt addRC(String name, Address rcAddr, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                Role_sol_Role rc = Role_sol_Role.load(rcAddr.toString(), web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.addRC(new Utf8String(name), new Address(rc.getAdmin().send().getValue())).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> addRCAsync(String name, Address admin , Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                return system.addRC(new Utf8String(name), admin).sendAsync();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt register(Address user, String roleName, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.register(user, new Utf8String(roleName)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> registerAsync(Address user, String roleName, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                return system.register(user, new Utf8String(roleName)).sendAsync();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt addDC(String name, Address admin, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.addDC(new Utf8String(name), admin).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> addDCAsync(String name, Address admin, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                return system.addDC(new Utf8String(name), admin).sendAsync();
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public String getRole(String user, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                String roleName = system.getRole(new Address(user)).send().getValue();

                LOGGER.info("Read succeed: " + roleName);
                return roleName;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public String getRole(Credentials credentials) throws Exception {
        return getRole(credentials.getAddress(), credentials);
    }

    public String getRC(String roleName, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                String rcAddr = system.getRC(new Utf8String(roleName)).send().getValue();

                LOGGER.info("Read succeed: " + rcAddr);
                return rcAddr;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public String getDC(String propertyName, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                String scAddr = system.getDC(new Utf8String(propertyName)).send().getValue();

                LOGGER.info("Read succeed: " + scAddr);
                return scAddr;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public Map<String, String> getRoleAll(Credentials credentials) throws Exception {
        Map<String, String> result = new HashMap<>();
        System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);

        for (String role : RoleType.Types) {
            boolean flag = false;
            int count = 0;

            while(!flag && count < REQUEST_LIMIT) {
                try {
                    String rcAddr = system.getRC(new Utf8String(role)).send().getValue();
                    if (!rcAddr.equals("")) {
                        result.putIfAbsent(role, rcAddr);
                    }
                    flag = true;
                } catch (NullPointerException e) {
                    LOGGER.error(e.toString());
                    count++;
                }
            }
        }

        return result;
}

    public Map<String, String> getPropertyAll(Credentials credentials) throws Exception {
        Map<String, String> result = new HashMap<>();
        System_sol_System system = System_sol_System.load(this.sysAddress, web3j, credentials, GAS_PRICE, GAS_LIMIT);

        for (String property : PropertyType.Types) {
            boolean flag = false;
            int count = 0;

            while(!flag && count < REQUEST_LIMIT) {
                try {
                    String scAddr = system.getDC(new Utf8String(property)).send().getValue();
                    if (!scAddr.equals("")) {
                        result.putIfAbsent(property, scAddr);
                    }
                    flag = true;
                } catch (NullPointerException e) {
                    LOGGER.error(e.toString());
                    count++;
                }
            }
        }

        return result;
    }

    public String getSysAddress() {
        return sysAddress;
    }

    private Credentials signIn(String address, String password) throws IOException, CipherException {
        Resource resource = new ClassPathResource(address);
        File file = resource.getFile();
        return  WalletUtils.loadCredentials(
                password,
                file.getAbsolutePath());
    }
}
