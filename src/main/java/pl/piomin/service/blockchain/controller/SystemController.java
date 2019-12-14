package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.model.*;
import pl.piomin.service.blockchain.service.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/system")
public class SystemController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;
    private final BlockchainService blockchainService;
    private final MessageService messageService;

    public SystemController(SystemService systemService, UserService userService,
                            DataService dataService, BlockchainService blockchainService, MessageService messageService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
        this.blockchainService = blockchainService;
        this.messageService = messageService;
    }

    //System owner
    @PostMapping("/reset")
    public Result reset() throws Exception {
        boolean rs = systemService.reset(userService.getCurrent());
        boolean ru = userService.reset(systemService.getSysAddress());
        return new Result(rs && ru);
    }
    @PostMapping("/register")
    public TransactionReceipt register(@RequestBody UserSwapper user) throws Exception {
        return systemService.register(new Address(user.getAddress()), user.getRole(), userService.getCurrent());
    }
    @PostMapping("/signUp")
    public Result signUp(@RequestBody UserSwapper user) throws Exception {
        userService.signIn(user.getAddress(), user.getPassword());

        PermissionSwapper permission = new PermissionSwapper(user.getRole(), user.getAddress());
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message(permission, Message.Type.Register, UserService.accounts[0], df.format(new Date()));
        messageService.add(msg);

        return new Result(true);
    }

    //RC owner
    @PostMapping("/requestRole")
    public Result requestRole(@RequestBody NewContractSwapper role) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message(new PermissionSwapper(role.getName(), role.getAdmin()), Message.Type.Role, UserService.accounts[0], df.format(new Date()));
        messageService.add(msg);
        return new Result(true);
    }

    @PostMapping("/requestProperty")
    public Result requestProperty(@RequestBody NewContractSwapper property) throws Exception {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message(new PermissionSwapper(property.getName(), property.getAdmin()), Message.Type.Property, UserService.accounts[0], df.format(new Date()));
        messageService.add(msg);
        return new Result(true);
    }

    //System owner
    @PostMapping("/permitRole")
    public TransactionReceipt permitRole(@RequestBody NewContractSwapper role) throws Exception {
        RoleType.Types.add(role.getName());
        return systemService.addRC(role.getName(), new Address(role.getAdmin()), userService.getCurrent());
    }

    @PostMapping("/permitProperty")
    public TransactionReceipt permitProperty(@RequestBody NewContractSwapper property) throws Exception {
        RoleType.Types.add(property.getName());
        return systemService.addDC(property.getName(), new Address(property.getAdmin()), userService.getCurrent());
    }

    @GetMapping("/getRole/{address}")
    public String getRole(@PathVariable String address) throws Exception {
        String roleName = systemService.getRole(address, userService.getCurrent());
        return systemService.getRC(roleName, userService.getCurrent());
    }
    //For query
    @GetMapping("/getRoles")
    public Map<String, String> getRoles() throws Exception {
        return systemService.getRoleAll(userService.getCurrent());
    }

    @GetMapping("/getProperties")
    public Map<String, String> getProperties() throws Exception {
        return systemService.getPropertyAll(userService.getCurrent());
    }

    @GetMapping("/getRoleNames")
    public String[] getRoleNames() {
        return RoleType.Types.toArray(new String[0]);
    }

    @GetMapping("/getPropertyNames")
    public String[] getPropertyNames() {
        return PropertyType.Types.toArray(new String[0]);
    }
}
