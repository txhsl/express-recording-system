package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.piomin.service.blockchain.exception.NoPermissionException;
import pl.piomin.service.blockchain.model.app.OrderForm;
import pl.piomin.service.blockchain.model.app.PickForm;
import pl.piomin.service.blockchain.service.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/app")
public class ExpressController {

    private final SystemService systemService;
    private final UserService userService;
    private final DataService dataService;
    private final FileService fileService;
    private final MessageService messageService;
    private final BlockchainService blockchainService;

    public ExpressController(SystemService systemService, UserService userService, DataService dataService,
                         FileService fileService, MessageService messageService, BlockchainService blockchainService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
        this.fileService = fileService;
        this.messageService = messageService;
        this.blockchainService = blockchainService;
    }

    @PostMapping("/order")
    public String order(@RequestBody OrderForm form) throws Exception {
        Map<String, String> data = new HashMap<>();
        String id = String.valueOf(System.currentTimeMillis());
        data.putIfAbsent("imgQR", form.getimgQR());
        data.putIfAbsent("sender", form.getSender());
        data.putIfAbsent("senderPhone", form.getSenderPhone());
        data.putIfAbsent("receiver", form.getReceiver());
        data.putIfAbsent("receiverPhone", form.getReceiverPhone());
        data.putIfAbsent("date", form.getDate());
        data.putIfAbsent("ordered", "Yes");

        try {
            for (String property : data.keySet()) {
                String scAddr = systemService.getSC(property, userService.getCurrent());

                if (userService.checkWriter(systemService.getSysAddress(), property)) {
                    String fileNo = dataService.getFileNum(scAddr, id, userService.getCurrent());
                    fileService.record(property, fileNo, id, data.get(property));
                } else {
                    throw new NoPermissionException();
                }
            }
        }
        catch (NoPermissionException e){
            return e.getMsg();
        }
        return id;
    }

    @PostMapping("/pickUp")
    public String pickUp(@RequestBody PickForm form) throws Exception {
        Map<String, String> data = new HashMap<>();
        String id = form.getId();
        data.putIfAbsent("pickedUp", "Yes");
        data.putIfAbsent("deliverer", userService.getCurrent().getAddress());
        data.putIfAbsent("weight", form.getWeight());
        data.putIfAbsent("fee", form.getFee());

        try {
            for (String property : data.keySet()) {
                String scAddr = systemService.getSC(property, userService.getCurrent());

                if (userService.checkWriter(systemService.getSysAddress(), property)) {
                    String fileNo = dataService.getFileNum(scAddr, id, userService.getCurrent());
                    fileService.record(property, fileNo, id, data.get(property));
                } else {
                    throw new NoPermissionException();
                }
            }
        }
        catch (NoPermissionException e){
            return e.getMsg();
        }
        return form.getFee();
    }

    @PostMapping("/deliver")
    public String deliver(@RequestBody String id) throws Exception {
        try {
            String scAddr = systemService.getSC("delivered", userService.getCurrent());

            if (userService.checkWriter(systemService.getSysAddress(), "delivered")) {
                String fileNo = dataService.getFileNum(scAddr, id, userService.getCurrent());
                fileService.record("delivered", fileNo, id, "Yes");
            } else {
                throw new NoPermissionException();
            }
        }
        catch (NoPermissionException e){
            return e.getMsg();
        }
        return id;
    }

    @PostMapping("/receive")
    public String receive(@RequestBody String id) throws Exception {
        try {
            String scAddr = systemService.getSC("received", userService.getCurrent());

            if (userService.checkWriter(systemService.getSysAddress(), "received")) {
                String fileNo = dataService.getFileNum(scAddr, id, userService.getCurrent());
                fileService.record("received", fileNo, id, "Yes");
            } else {
                throw new NoPermissionException();
            }
        }
        catch (NoPermissionException e){
            return e.getMsg();
        }
        return id;
    }
}
