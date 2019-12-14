package pl.piomin.service.blockchain.service.app;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.abi.datatypes.generated.Uint256;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.contract.System_sol_System;

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
    private String sysAddress;

    private final int REQUEST_LIMIT = 10;
    private final Web3j web3j;

    public ExpressService(Web3j web3j) {
        this.web3j = web3j;
    }

    public TransactionReceipt addOrder(String sysAddr, String id, String sender, String receiver, int value, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.addOrder(new Utf8String(id), new Address(sender), new Address(receiver), new Uint256(value)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt pickOrder(String sysAddr, String id, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.pickOrder(new Utf8String(id)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt accept(String sysAddr, String id, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.accept(new Utf8String(id)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }

    public TransactionReceipt refund(String sysAddr, String id, Credentials credentials) throws Exception {
        int count = 0;

        while(count < REQUEST_LIMIT) {
            try {
                System_sol_System system = System_sol_System.load(sysAddr, web3j, credentials, GAS_PRICE, GAS_LIMIT);
                TransactionReceipt transactionReceipt = system.refund(new Utf8String(id)).send();

                LOGGER.info("Transaction succeed: " + transactionReceipt.toString());
                return transactionReceipt;
            } catch (NullPointerException e) {
                LOGGER.error(e.toString());
                count++;
            }
        }
        throw new NullPointerException();
    }
}
