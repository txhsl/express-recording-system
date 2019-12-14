package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.crypto.CipherException;
import pl.piomin.service.blockchain.model.*;
import pl.piomin.service.blockchain.service.*;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;
    private final MessageService messageService;

    public UserController(SystemService systemService, UserService userService,
                          DataService dataService, MessageService messageService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
        this.messageService = messageService;
    }

    //Normal
    @PostMapping("/signIn")
    public Result signIn(@RequestBody UserSwapper user) throws IOException, CipherException {
        return new Result(userService.signIn(user.getAddress(), user.getPassword()));
    }

    @PostMapping("/requestReader")
    public Result requestReader(@RequestBody PermissionSwapper permission) throws Exception {
        String scAddr = systemService.getDC(permission.getPropertyName(), userService.getCurrent());

        permission.setIsRead(true);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message(permission, Message.Type.Permission, dataService.getAdmin(scAddr, userService.getCurrent()), df.format(new Date()));
        messageService.add(msg);
        return new Result(true);
    }

    @PostMapping("/requestWriter")
    public Result requestWriter(@RequestBody PermissionSwapper permission) throws Exception {
        String scAddr = systemService.getDC(permission.getPropertyName(), userService.getCurrent());

        permission.setIsRead(false);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message msg = new Message(permission, Message.Type.Permission, dataService.getAdmin(scAddr, userService.getCurrent()), df.format(new Date()));
        messageService.add(msg);
        return new Result(true);
    }

    //DC owner
    @PostMapping("/permitReader")
    public Result permitReader(@RequestBody PermissionSwapper permission) throws Exception {
        TaskSwapper task = new TaskSwapper(permission.getPropertyName(), Message.Type.Permission.toString(), userService.getCurrent().getAddress());
        task.setFuture(userService.assignReaderAsync(systemService.getSysAddress(), permission.getTarget(), permission.getPropertyName()));
        BlockchainService.addPending(task);
        return new Result(true);
    }
    //DC owner
    @PostMapping("/permitWriter")
    public Result permitWriter(@RequestBody PermissionSwapper permission) throws Exception {
        TaskSwapper task = new TaskSwapper(permission.getPropertyName(), Message.Type.Permission.toString(), userService.getCurrent().getAddress());
        task.setFuture(userService.assignWriterAsync(systemService.getSysAddress(), permission.getTarget(), permission.getPropertyName()));
        BlockchainService.addPending(task);
        return new Result(true);
    }

    //For query
    @GetMapping("/getManaged")
    public Map<String, String> getManaged() throws Exception {
        String roleName = systemService.getRole(userService.getCurrent());
        String rcAddr = systemService.getRC(roleName, userService.getCurrent());
        return userService.getManagedAll(rcAddr);
    }

    @GetMapping("/getOwned")
    public Map<String, String> getOwned() throws Exception {
        String roleName = systemService.getRole(userService.getCurrent());
        String rcAddr = systemService.getRC(roleName, userService.getCurrent());
        return userService.getOwnedAll(rcAddr);
    }

    @GetMapping("/getAdministrated")
    public Map<String, String> getAdministrated() throws Exception {
        return userService.getAdministratedAll(systemService.getSysAddress(), userService.getCurrent().getAddress());
    }

    @PostMapping("/checkAdmin")
    public boolean checkAdmin(@RequestBody NewContractSwapper rc) throws Exception {
        String rcAddr = systemService.getRC(rc.getName(), userService.getCurrent());
        return !userService.getAdmin(rcAddr).equals(rc.getAdmin());
    }
}
