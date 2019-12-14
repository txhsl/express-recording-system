package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.*;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import pl.piomin.service.blockchain.model.*;
import pl.piomin.service.blockchain.service.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/arbitration")
public class ArbitrateController {

    private final SystemService systemService;
    private final MessageService messageService;
    private final UserService userService;
    private final ArbitrateService arbitrateService;

    public ArbitrateController(SystemService systemService, MessageService messageService,
                               UserService userService, ArbitrateService arbitrateService) {
        this.systemService = systemService;
        this.messageService = messageService;
        this.userService = userService;
        this.arbitrateService = arbitrateService;
    }

    @PostMapping("/report")
    public Result report(@RequestBody Report report) {
        try{
            String roleName = systemService.getRole(report.getTarget(), userService.getCurrent());
            String rcAddr = systemService.getRC(roleName, userService.getCurrent());
            String admin = userService.getAdmin(rcAddr);
            report.setTo(admin);
        }
        catch (Exception e) {
            return new Result(false);
        }
        return new Result(arbitrateService.report(report));
    }

    @GetMapping("/receive")
    public Report[] receive() {
        ArrayList<Report> result = arbitrateService.get(userService.getCurrent().getAddress());
        return result == null ? new Report[0] : result.toArray(new Report[0]);
    }

    @PutMapping("/agree/{index}")
    public Result agree(@PathVariable int index) throws Exception {
        Report report = arbitrateService.get(userService.getCurrent().getAddress(), index);
        CompletableFuture<TransactionReceipt> receipt = arbitrateService.arbitrate(systemService.getSysAddress(), index, true, userService.getCurrent());

        TaskSwapper task = new TaskSwapper("Arbitration", Message.Type.Report.name() ,userService.getCurrent().getAddress());
        task.setFuture(receipt);
        BlockchainService.addPending(task);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message toReporter = new Message(new PermissionSwapper("Arbitration Result", report.getTarget()), Message.Type.Report, report.getFrom(), df.format(new Date()));
        Message toTarget = new Message(new PermissionSwapper("Arbitration Result", report.getTarget()), Message.Type.Report, report.getTarget(), df.format(new Date()));
        toReporter.setReceipt(receipt);
        toTarget.setReceipt(receipt);
        messageService.addReceipt(toReporter);
        messageService.addReceipt(toTarget);
        return new Result(true);
    }

    @PutMapping("/disagree/{index}")
    public Result disagree(@PathVariable int index) throws Exception {
        Report report = arbitrateService.get(userService.getCurrent().getAddress(), index);
        CompletableFuture<TransactionReceipt> receipt = arbitrateService.arbitrate(systemService.getSysAddress(), index, false, userService.getCurrent());

        TaskSwapper task = new TaskSwapper("Arbitration", Message.Type.Report.name() ,userService.getCurrent().getAddress());
        task.setFuture(receipt);
        BlockchainService.addPending(task);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Message toReporter = new Message(new PermissionSwapper("Arbitration Result", report.getTarget()), Message.Type.Report, report.getFrom(), df.format(new Date()));
        Message toTarget = new Message(new PermissionSwapper("Arbitration Result", report.getTarget()), Message.Type.Report, report.getTarget(), df.format(new Date()));
        toReporter.setReceipt(receipt);
        toTarget.setReceipt(receipt);
        messageService.addReceipt(toReporter);
        messageService.addReceipt(toTarget);
        return new Result(true);
    }

    @GetMapping("/balance/{address}")
    public int getBalance(@PathVariable String address) throws Exception {
        return arbitrateService.getBalance(systemService.getSysAddress(), address, userService.getCurrent());
    }
}
