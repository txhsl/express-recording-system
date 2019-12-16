package pl.piomin.service.blockchain.controller.app;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import pl.piomin.service.blockchain.PropertyType;
import pl.piomin.service.blockchain.exception.NoPermissionException;
import pl.piomin.service.blockchain.model.FileSwapper;
import pl.piomin.service.blockchain.model.Message;
import pl.piomin.service.blockchain.model.TaskSwapper;
import pl.piomin.service.blockchain.model.app.FullForm;
import pl.piomin.service.blockchain.model.app.OrderForm;
import pl.piomin.service.blockchain.model.app.PickForm;
import pl.piomin.service.blockchain.service.*;
import pl.piomin.service.blockchain.service.app.ExpressService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
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
    private final ExpressService expressService;

    public ExpressController(SystemService systemService, UserService userService, DataService dataService,
                             FileService fileService, MessageService messageService, BlockchainService blockchainService,
                             ExpressService expressService) {
        this.systemService = systemService;
        this.userService = userService;
        this.dataService = dataService;
        this.fileService = fileService;
        this.messageService = messageService;
        this.blockchainService = blockchainService;
        this.expressService = expressService;
    }

    @PostMapping("/upload")
    public String upload(@RequestParam("img") MultipartFile img) throws IOException {
        return expressService.upload(img);
    }

    @GetMapping("/download/{hash}")
    public void download(@PathVariable String hash, HttpServletRequest request, HttpServletResponse response) throws IOException {
        System.out.println(hash);
        expressService.download(hash, response);
    }

    @GetMapping("/all")
    public ArrayList<FullForm> getAll() throws Exception {
        ArrayList<FullForm> all = new ArrayList<>();
        for (String id : expressService.getOrderIds()) {
            FullForm info = new FullForm();

            info.setId(id);
            info.setImgQR(read("imgQR", id));
            info.setSender(read("sender", id));
            info.setSenderPhone(read("senderPhone", id));
            info.setSenderAddress(read("senderAddress", id));
            info.setReceiver(read("receiver", id));
            info.setReceiverPhone(read("receiverPhone", id));
            info.setReceiverAddress(read("receiverAddress", id));

            info.setValue(read("value", id));
            info.setWeight(read("weight", id));
            info.setDate(read("date", id));
            info.setDeliverer(read("deliverer", id));
            info.setFee(read("fee", id));

            info.setOrdered(read("ordered", id));
            info.setPickedUp(read("pickedUp", id));
            info.setDelivering(read("delivering", id));
            info.setReceived(read("received", id));

            all.add(info);
        }
        return all;
    }

    public String read(String property, String id) throws Exception {
        //Check permission
        String scAddr = systemService.getDC(property, userService.getCurrent());
        if (!userService.checkReader(systemService.getSysAddress(), property)) {
            return null;
        }

        //Try the cache
        String result = fileService.query(property, id);

        //Try IPFS
        if (result == null) {
            //Try cache
            String hash = null;
            ArrayList<TaskSwapper> pending = BlockchainService.getPending();
            for (TaskSwapper tx : pending) {
                if (tx.getTaskName().equals(dataService.getFileNum(scAddr, id, userService.getCurrent()))) {
                    hash = tx.getTaskContent();
                }
            }

            //Try Eth
            if (hash == null) {
                hash = dataService.read(systemService.getSysAddress(), property, userService.getCurrent(), id);
                if (hash.equals("")) {
                    return null;
                }
            }

            FileSwapper file = FileService.input(fileService.download(hash));
            if (FileService.checkFile(file.getFileName(), property + '_' + dataService.getFileNum(scAddr, id, userService.getCurrent()))) {
                result = file.getContent(id);
            } else {
                throw new IOException();
            }
        }
        return result;
    }

    @PostMapping("/order")
    public String order(@RequestBody OrderForm form) throws Exception {
        Map<String, String> data = new HashMap<>();
        String id = String.valueOf(System.currentTimeMillis());

        data.putIfAbsent("imgQR", form.getImgQR());
        data.putIfAbsent("sender", form.getSender());
        data.putIfAbsent("senderPhone", form.getSenderPhone());
        data.putIfAbsent("senderAddress", form.getSenderAddress());
        data.putIfAbsent("receiver", form.getReceiver());
        data.putIfAbsent("receiverPhone", form.getReceiverPhone());
        data.putIfAbsent("receiverAddress", form.getReceiverAddress());
        data.putIfAbsent("value", form.getValue());
        data.putIfAbsent("date", form.getDate());
        data.putIfAbsent("ordered", "Yes");

        for (String property : data.keySet()) {
            String scAddr = systemService.getDC(property, userService.getCurrent());

            if (userService.checkWriter(systemService.getSysAddress(), property)) {
                String fileNo = dataService.getFileNum(scAddr, id, userService.getCurrent());
                fileService.record(property, fileNo, id, data.get(property));
            } else {
                throw new NoPermissionException();
            }
        }

        TaskSwapper task = new TaskSwapper(id, Message.Type.Order.toString(), userService.getCurrent().getAddress());
        task.setFuture(expressService.addOrder(systemService.getSysAddress(), id, form.getSender(), form.getReceiver(), Double.parseDouble(form.getValue()), userService.getCurrent()));
        BlockchainService.addPending(task);

        return id;
    }

    @PostMapping("/pickUp")
    public String pickUp(@RequestBody PickForm form) throws Exception {
        Map<String, String> data = new HashMap<>();

        data.putIfAbsent("deliverer", userService.getCurrent().getAddress());
        data.putIfAbsent("pickedUp", "Yes");

        for (String property : data.keySet()) {
            String scAddr = systemService.getDC(property, userService.getCurrent());

            if (userService.checkWriter(systemService.getSysAddress(), property)) {
                String fileNo = dataService.getFileNum(scAddr, form.getId(), userService.getCurrent());
                fileService.record(property, fileNo, form.getId(), data.get(property));
            } else {
                throw new NoPermissionException();
            }
        }

        TaskSwapper task = new TaskSwapper(form.getId(), Message.Type.Pick.toString(), userService.getCurrent().getAddress());
        task.setFuture(expressService.pickOrder(systemService.getSysAddress(), form.getId(), userService.getCurrent()));
        BlockchainService.addPending(task);

        return form.getId();
    }

    @PostMapping("/deliver")
    public String deliver(@RequestBody PickForm form) throws Exception {

        Map<String, String> data = new HashMap<>();
        String id = form.getId();

        data.putIfAbsent("delivering", "Yes");
        data.putIfAbsent("weight", form.getWeight());
        data.putIfAbsent("fee", form.getFee());

        for (String property : data.keySet()) {
            String scAddr = systemService.getDC(property, userService.getCurrent());

            if (userService.checkWriter(systemService.getSysAddress(), property)) {
                String fileNo = dataService.getFileNum(scAddr, id, userService.getCurrent());
                fileService.record(property, fileNo, id, data.get(property));
            } else {
                throw new NoPermissionException();
            }
        }

        TaskSwapper task = new TaskSwapper(id, Message.Type.Deliver.toString(), userService.getCurrent().getAddress());
        task.setFuture(expressService.deliver(systemService.getSysAddress(), id, Double.valueOf(form.getFee()), userService.getCurrent()));
        BlockchainService.addPending(task);

        return form.getFee();
    }

    @PostMapping("/receive")
    public String receive(@RequestBody PickForm form) throws Exception {

        String scAddr = systemService.getDC("received", userService.getCurrent());

        if (userService.checkWriter(systemService.getSysAddress(), "received")) {
            String fileNo = dataService.getFileNum(scAddr, form.getId(), userService.getCurrent());
            fileService.record("received", fileNo, form.getId(), "Yes");
        } else {
            throw new NoPermissionException();
        }

        TaskSwapper task = new TaskSwapper(form.getId(), Message.Type.Receive.toString(), userService.getCurrent().getAddress());
        task.setFuture(expressService.accept(systemService.getSysAddress(), form.getId(), userService.getCurrent()));
        BlockchainService.addPending(task);

        return form.getId();
    }
}
