package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.abi.datatypes.Address;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.RoleType;
import pl.piomin.service.blockchain.model.Message;
import pl.piomin.service.blockchain.model.Result;
import pl.piomin.service.blockchain.model.TaskSwapper;
import pl.piomin.service.blockchain.service.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;
    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;

    public MessageController(MessageService messageService, SystemService systemService,
                             UserService userService, DataService dataService) {
        this.messageService = messageService;
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
    }

    @Deprecated
    @PostMapping("/send")
    public Result send(@RequestBody Message msg) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        msg.setTime(df.format(new Date()));
        return new Result(messageService.add(msg));
    }

    @GetMapping("/receive")
    public Message[] receive() {
        ArrayList<Message> result = messageService.get(userService.getCurrent().getAddress());
        return result == null ? new Message[0] : result.toArray(new Message[0]);
    }

    @GetMapping("/receipt")
    public Message[] getReceipt() {
        ArrayList<Message> result = messageService.getReceipt(userService.getCurrent().getAddress());
        return result == null ? new Message[0] : result.toArray(new Message[0]);
    }

    @PutMapping("/read/{index}")
    public Result read(@PathVariable int index) {
        return new Result(messageService.markRead(userService.getCurrent().getAddress(), index));
    }

    @PutMapping("/accept/{index}")
    public boolean accept(@PathVariable int index) throws Exception {
        Message msg = messageService.get(userService.getCurrent().getAddress(), index);

        //Handle permit
        TaskSwapper permissionTask = new TaskSwapper(msg.getPermission().getPropertyName(), msg.getType().name(), userService.getCurrent().getAddress());
        switch (msg.getType()) {
            case Role:
                permissionTask.setFuture(systemService.addRCAsync(msg.getPermission().getPropertyName(), new Address(msg.getPermission().getTarget()), userService.getCurrent()));
                RoleType.Types.add(msg.getPermission().getPropertyName());
                break;
            case Property:
                permissionTask.setFuture(systemService.addDCAsync(msg.getPermission().getPropertyName(), new Address(msg.getPermission().getTarget()), userService.getCurrent()));
                PropertyType.Types.add(msg.getPermission().getPropertyName());
                break;
            case Permission:
                String toRole = systemService.getRole(msg.getPermission().getTarget(), userService.getCurrent());
                if (msg.getPermission().getIsRead()) {
                    permissionTask.setFuture(userService.assignReaderAsync(systemService.getSysAddress(), toRole, msg.getPermission().getPropertyName()));
                }
                else {
                    permissionTask.setFuture(userService.assignWriterAsync(systemService.getSysAddress(), toRole, msg.getPermission().getPropertyName()));
                }
                break;
            case Register:
                permissionTask.setFuture(systemService.registerAsync(new Address(msg.getPermission().getTarget()), msg.getPermission().getPropertyName(), userService.getCurrent()));
                break;
            default:
                return false;
        }
        BlockchainService.addPending(permissionTask);
        msg.setReceipt(permissionTask.getFuture());

        if(messageService.markAccepted(userService.getCurrent().getAddress(), index)) {
            //Send receipt
            msg.setRead(false);
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            msg.setTime(df.format(new Date()));
            msg.setTo(msg.getPermission().getTarget());
            return messageService.addReceipt(msg);
        }
        return false;
    }
}
