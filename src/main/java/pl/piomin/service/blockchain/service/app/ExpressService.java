package pl.piomin.service.blockchain.service.app;

import io.ipfs.api.IPFS;
import io.ipfs.api.MerkleNode;
import io.ipfs.api.NamedStreamable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.utils.Convert;
import pl.piomin.service.blockchain.contract.System_sol_System;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;

import static pl.piomin.service.blockchain.model.CustomGasProvider.GAS_LIMIT;
import static pl.piomin.service.blockchain.model.CustomGasProvider.GAS_PRICE;

/**
 * @author: HuShili
 * @date: 2019/2/17
 * @description: none
 */
@Service
public class ExpressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExpressService.class);
    private ArrayList<String> orderIds = new ArrayList<>();

    private final int REQUEST_LIMIT = 10;
    private final Web3j web3j;

    private static final String IPFS_URL = "/ip4/127.0.0.1/tcp/5001";

    private final IPFS ipfs;
    private final File folder;

    public ExpressService(Web3j web3j) {
        ipfs = new IPFS(IPFS_URL);
        folder = new File("img");

        try {
            LOGGER.info("IPFS connected, version: " + ipfs.version());
        } catch (IOException e){
            LOGGER.error("Connection failed. " + e.getMessage());
        }

        if (!folder.exists()) {
            folder.mkdir();
            LOGGER.info("Data folder created, path: " + folder.getAbsolutePath());
        }
        this.web3j = web3j;
    }

    public ArrayList<String> getOrderIds() {
        return orderIds;
    }

    public String upload(MultipartFile img) throws IOException {
        byte[] data = img.getBytes();

        String name = String.valueOf(System.currentTimeMillis());
        File target = new File(folder.getPath() + '\\' + name);
        FileOutputStream fos = new FileOutputStream(target);
        fos.write(data, 0, data.length);
        fos.flush();
        fos.close();
        LOGGER.info("Image uploaded " + name + ", path: " + target.getPath());

        NamedStreamable.FileWrapper file = new NamedStreamable.FileWrapper(target);
        MerkleNode addResult = ipfs.add(file).get(0);
        String hash = addResult.hash.toString();

        //Rename the file
        if (target.renameTo(new File(folder.getPath() + '\\' + hash))) {
            LOGGER.info("File uploaded, hash: " + hash);
        } else {
            LOGGER.error("File upload failed, already existed, hash: " + hash);
        }
        return hash;
    }

    public void download(String hash, HttpServletResponse response) throws IOException {
        File target = new File(folder.getPath() + '\\' + hash);
        FileInputStream fis = new FileInputStream(target);
        byte[] data = new byte[fis.available()];
        fis.read(data);
        fis.close();
        response.setContentType("image/jpeg");
        OutputStream os = response.getOutputStream();
        os.write(data);
        os.close();
    }

    public CompletableFuture<TransactionReceipt> addOrder(String sysAddr, String id, String sender, String receiver, double value, Credentials credentials) throws Exception {
        int count = 0;
        BigInteger wei = Convert.toWei(BigDecimal.valueOf(value), Convert.Unit.ETHER).toBigInteger();

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                CompletableFuture<TransactionReceipt> future = system.addOrder(new Utf8String(id), new Address(sender), new Address(receiver), new Uint256(wei)).sendAsync();

                orderIds.add(id);
                LOGGER.info("Order created: " + id);
                return future;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> pickOrder(String sysAddr, String id, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                CompletableFuture<TransactionReceipt> future = system.pickOrder(new Utf8String(id)).sendAsync();

                LOGGER.info("Order picked: " + id);
                return future;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> deliver(String sysAddr, String id, double fee, Credentials credentials) throws Exception {
        int count = 0;
        BigInteger wei = Convert.toWei(BigDecimal.valueOf(fee), Convert.Unit.ETHER).toBigInteger();

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                CompletableFuture<TransactionReceipt> future = system.deliver(new Utf8String(id), new Uint256(wei)).sendAsync();

                LOGGER.info("Order delivering: " + id);
                return future;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> accept(String sysAddr, String id, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                CompletableFuture<TransactionReceipt> future = system.accept(new Utf8String(id)).sendAsync();

                LOGGER.info("Order succeeded: " + id);
                return future;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public CompletableFuture<TransactionReceipt> refund(String sysAddr, String id, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                CompletableFuture<TransactionReceipt> future = system.refund(new Utf8String(id)).sendAsync();

                LOGGER.info("Order refunded: " + id);
                return future;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }
}
