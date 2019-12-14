package pl.piomin.service.blockchain.contract;

import io.reactivex.Flowable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.web3j.abi.EventEncoder;
import org.web3j.abi.FunctionEncoder;
import org.web3j.abi.TypeReference;
import org.web3j.abi.datatypes.Address;
import org.web3j.abi.datatypes.Event;
import org.web3j.abi.datatypes.Function;
import org.web3j.abi.datatypes.Type;
import org.web3j.abi.datatypes.Utf8String;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameter;
import org.web3j.protocol.core.RemoteCall;
import org.web3j.protocol.core.methods.request.EthFilter;
import org.web3j.protocol.core.methods.response.Log;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.tx.Contract;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;

/**
 * <p>Auto generated code.
 * <p><strong>Do not modify!</strong>
 * <p>Please use the <a href="https://docs.web3j.io/command_line.html">web3j command line tools</a>,
 * or the org.web3j.codegen.SolidityFunctionWrapperGenerator in the 
 * <a href="https://github.com/web3j/web3j/tree/master/codegen">codegen module</a> to update.
 *
 * <p>Generated with web3j version 4.1.1.
 */
public class Role_sol_Role extends Contract {
    private static final String BINARY = "608060405234801561001057600080fd5b506040516020806107b38339810180604052602081101561003057600080fd5b50516003805433600160a060020a03199182161790915560028054909116600160a060020a039092169190911790556107458061006e6000396000f3fe608060405234801561001057600080fd5b5060043610610073577c010000000000000000000000000000000000000000000000000000000060003504631f0686c781146100785780634933b2001461012b57806358f9ab54146101ed5780636e9960c314610293578063ff952f6c1461029b575b600080fd5b6101296004803603604081101561008e57600080fd5b8101906020810181356401000000008111156100a957600080fd5b8201836020820111156100bb57600080fd5b803590602001918460018302840111640100000000831117156100dd57600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955050509035600160a060020a0316915061034c9050565b005b6101d16004803603602081101561014157600080fd5b81019060208101813564010000000081111561015c57600080fd5b82018360208201111561016e57600080fd5b8035906020019184600183028401116401000000008311171561019057600080fd5b91908080601f0160208091040260200160405190810160405280939291908181526020018383808284376000920191909152509295506104fa945050505050565b60408051600160a060020a039092168252519081900360200190f35b6101d16004803603602081101561020357600080fd5b81019060208101813564010000000081111561021e57600080fd5b82018360208201111561023057600080fd5b8035906020019184600183028401116401000000008311171561025257600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955061056b945050505050565b6101d161059d565b610129600480360360408110156102b157600080fd5b8101906020810181356401000000008111156102cc57600080fd5b8201836020820111156102de57600080fd5b8035906020019184600183028401116401000000008311171561030057600080fd5b91908080601f01602080910402602001604051908101604052809392919081815260200183838082843760009201919091525092955050509035600160a060020a031691506105ac9050565b600354600160a060020a031633146103c557604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b806000836040518082805190602001908083835b602083106103f85780518252601f1990920191602091820191016103d9565b51815160209384036101000a6000190180199092169116179052920194855250604080519485900382018520805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a039788161790553080865295871690850152606084820181815288519186019190915287517f07d18b60ba8898cd98aa083a78538e587d8caff61c375c2bf0f5c44b2c5cbbc396958995508894509260808401919086019080838360005b838110156104ba5781810151838201526020016104a2565b50505050905090810190601f1680156104e75780820380516001836020036101000a031916815260200191505b5094505050505060405180910390a15050565b60006001826040518082805190602001908083835b6020831061052e5780518252601f19909201916020918201910161050f565b51815160209384036101000a6000190180199092169116179052920194855250604051938490030190922054600160a060020a0316949350505050565b600080826040518082805190602001908083836020831061052e5780518252601f19909201916020918201910161050f565b600254600160a060020a031690565b600354600160a060020a0316331461062557604080517f08c379a000000000000000000000000000000000000000000000000000000000815260206004820152601260248201527f5065726d697373696f6e2064656e6965642e0000000000000000000000000000604482015290519081900360640190fd5b806001836040518082805190602001908083835b602083106106585780518252601f199092019160209182019101610639565b51815160209384036101000a6000190180199092169116179052920194855250604080519485900382018520805473ffffffffffffffffffffffffffffffffffffffff1916600160a060020a039788161790553080865295871690850152606084820181815288519186019190915287517f4c4ba6d0e34e55d28ff2af756b30380918b4e6e02dea8f69226466c6b9c53e289695899550889450926080840191908601908083836000838110156104ba5781810151838201526020016104a256fea165627a7a723058202ebff502cdfc353dc1e960229de42d219172af0612353a3696e0078f554a7a800029";

    public static final String FUNC_ADDOWNED = "addOwned";

    public static final String FUNC_GETMANAGED = "getManaged";

    public static final String FUNC_GETOWNED = "getOwned";

    public static final String FUNC_GETADMIN = "getAdmin";

    public static final String FUNC_ADDMANAGED = "addManaged";

    public static final Event OWN_EVENT = new Event("own", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    public static final Event MANAGE_EVENT = new Event("manage", 
            Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}, new TypeReference<Utf8String>() {}, new TypeReference<Address>() {}));
    ;

    @Deprecated
    protected Role_sol_Role(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    protected Role_sol_Role(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, credentials, contractGasProvider);
    }

    @Deprecated
    protected Role_sol_Role(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        super(BINARY, contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    protected Role_sol_Role(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        super(BINARY, contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public RemoteCall<TransactionReceipt> addOwned(Utf8String _name, Address _scAddr) {
        final Function function = new Function(
                FUNC_ADDOWNED, 
                Arrays.<Type>asList(_name, _scAddr), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public RemoteCall<Address> getManaged(Utf8String _name) {
        final Function function = new Function(FUNC_GETMANAGED, 
                Arrays.<Type>asList(_name), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> getOwned(Utf8String _name) {
        final Function function = new Function(FUNC_GETOWNED, 
                Arrays.<Type>asList(_name), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<Address> getAdmin() {
        final Function function = new Function(FUNC_GETADMIN, 
                Arrays.<Type>asList(), 
                Arrays.<TypeReference<?>>asList(new TypeReference<Address>() {}));
        return executeRemoteCallSingleValueReturn(function);
    }

    public RemoteCall<TransactionReceipt> addManaged(Utf8String _name, Address _scAddr) {
        final Function function = new Function(
                FUNC_ADDMANAGED, 
                Arrays.<Type>asList(_name, _scAddr), 
                Collections.<TypeReference<?>>emptyList());
        return executeRemoteCallTransaction(function);
    }

    public List<OwnEventResponse> getOwnEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(OWN_EVENT, transactionReceipt);
        ArrayList<OwnEventResponse> responses = new ArrayList<OwnEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            OwnEventResponse typedResponse = new OwnEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._self = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse._name = (Utf8String) eventValues.getNonIndexedValues().get(1);
            typedResponse._scAddr = (Address) eventValues.getNonIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<OwnEventResponse> ownEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, OwnEventResponse>() {
            @Override
            public OwnEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(OWN_EVENT, log);
                OwnEventResponse typedResponse = new OwnEventResponse();
                typedResponse.log = log;
                typedResponse._self = (Address) eventValues.getNonIndexedValues().get(0);
                typedResponse._name = (Utf8String) eventValues.getNonIndexedValues().get(1);
                typedResponse._scAddr = (Address) eventValues.getNonIndexedValues().get(2);
                return typedResponse;
            }
        });
    }

    public Flowable<OwnEventResponse> ownEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(OWN_EVENT));
        return ownEventFlowable(filter);
    }

    public List<ManageEventResponse> getManageEvents(TransactionReceipt transactionReceipt) {
        List<Contract.EventValuesWithLog> valueList = extractEventParametersWithLog(MANAGE_EVENT, transactionReceipt);
        ArrayList<ManageEventResponse> responses = new ArrayList<ManageEventResponse>(valueList.size());
        for (Contract.EventValuesWithLog eventValues : valueList) {
            ManageEventResponse typedResponse = new ManageEventResponse();
            typedResponse.log = eventValues.getLog();
            typedResponse._self = (Address) eventValues.getNonIndexedValues().get(0);
            typedResponse._name = (Utf8String) eventValues.getNonIndexedValues().get(1);
            typedResponse._scAddr = (Address) eventValues.getNonIndexedValues().get(2);
            responses.add(typedResponse);
        }
        return responses;
    }

    public Flowable<ManageEventResponse> manageEventFlowable(EthFilter filter) {
        return web3j.ethLogFlowable(filter).map(new io.reactivex.functions.Function<Log, ManageEventResponse>() {
            @Override
            public ManageEventResponse apply(Log log) {
                Contract.EventValuesWithLog eventValues = extractEventParametersWithLog(MANAGE_EVENT, log);
                ManageEventResponse typedResponse = new ManageEventResponse();
                typedResponse.log = log;
                typedResponse._self = (Address) eventValues.getNonIndexedValues().get(0);
                typedResponse._name = (Utf8String) eventValues.getNonIndexedValues().get(1);
                typedResponse._scAddr = (Address) eventValues.getNonIndexedValues().get(2);
                return typedResponse;
            }
        });
    }

    public Flowable<ManageEventResponse> manageEventFlowable(DefaultBlockParameter startBlock, DefaultBlockParameter endBlock) {
        EthFilter filter = new EthFilter(startBlock, endBlock, getContractAddress());
        filter.addSingleTopic(EventEncoder.encode(MANAGE_EVENT));
        return manageEventFlowable(filter);
    }

    @Deprecated
    public static Role_sol_Role load(String contractAddress, Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit) {
        return new Role_sol_Role(contractAddress, web3j, credentials, gasPrice, gasLimit);
    }

    @Deprecated
    public static Role_sol_Role load(String contractAddress, Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit) {
        return new Role_sol_Role(contractAddress, web3j, transactionManager, gasPrice, gasLimit);
    }

    public static Role_sol_Role load(String contractAddress, Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider) {
        return new Role_sol_Role(contractAddress, web3j, credentials, contractGasProvider);
    }

    public static Role_sol_Role load(String contractAddress, Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider) {
        return new Role_sol_Role(contractAddress, web3j, transactionManager, contractGasProvider);
    }

    public static RemoteCall<Role_sol_Role> deploy(Web3j web3j, Credentials credentials, ContractGasProvider contractGasProvider, Address _admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_admin));
        return deployRemoteCall(Role_sol_Role.class, web3j, credentials, contractGasProvider, BINARY, encodedConstructor);
    }

    public static RemoteCall<Role_sol_Role> deploy(Web3j web3j, TransactionManager transactionManager, ContractGasProvider contractGasProvider, Address _admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_admin));
        return deployRemoteCall(Role_sol_Role.class, web3j, transactionManager, contractGasProvider, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Role_sol_Role> deploy(Web3j web3j, Credentials credentials, BigInteger gasPrice, BigInteger gasLimit, Address _admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_admin));
        return deployRemoteCall(Role_sol_Role.class, web3j, credentials, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    @Deprecated
    public static RemoteCall<Role_sol_Role> deploy(Web3j web3j, TransactionManager transactionManager, BigInteger gasPrice, BigInteger gasLimit, Address _admin) {
        String encodedConstructor = FunctionEncoder.encodeConstructor(Arrays.<Type>asList(_admin));
        return deployRemoteCall(Role_sol_Role.class, web3j, transactionManager, gasPrice, gasLimit, BINARY, encodedConstructor);
    }

    public static class OwnEventResponse {
        public Log log;

        public Address _self;

        public Utf8String _name;

        public Address _scAddr;
    }

    public static class ManageEventResponse {
        public Log log;

        public Address _self;

        public Utf8String _name;

        public Address _scAddr;
    }
}
