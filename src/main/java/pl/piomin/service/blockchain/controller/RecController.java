package pl.piomin.service.blockchain.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import pl.piomin.service.blockchain.model.User;
import pl.piomin.service.blockchain.model.data.*;
import pl.piomin.service.blockchain.service.*;

import java.io.IOException;

@RestController
public class RecController {

    private final UserService userService;
    private final BuyerService buyerService;
    private final ProductionService productionService;
    private final RetailService retailService;
    private final StoragerService storagerService;
    private final TransportService transportService;

    public RecController(UserService userService, BuyerService buyerService, ProductionService productionService
        , RetailService retailService, StoragerService storagerService, TransportService transportService) {
        this.userService = userService;
        this.buyerService = buyerService;
        this.productionService = productionService;
        this.retailService = retailService;
        this.storagerService = storagerService;
        this.transportService = transportService;
    }

    @PostMapping("/register")
    public boolean register(@RequestBody User user) throws IOException {
        return userService.register(user);
    }

    @PostMapping("/buyer/add")
    public boolean addBuyer(@RequestHeader String addr, @RequestBody BuyerData data) throws IOException {
        return buyerService.add(addr, data);
    }

    @PostMapping("/buyer/update")
    public boolean updateBuyer(@RequestHeader String addr, @RequestBody BuyerData data) throws IOException {
        return buyerService.update(addr, data);
    }

    @PostMapping("/production/add")
    public boolean addProduction(@RequestHeader String addr, @RequestBody ProductionData data) throws IOException {
        return productionService.add(addr, data);
    }

    @PostMapping("/production/update")
    public boolean updateProduction(@RequestHeader String addr, @RequestBody ProductionData data) throws IOException {
        return productionService.update(addr, data);
    }

    @PostMapping("/retail/add")
    public boolean addRetail(@RequestHeader String addr, @RequestBody RetailData data) throws IOException {
        return retailService.add(addr, data);
    }

    @PostMapping("/retail/update")
    public boolean updateRetail(@RequestHeader String addr, @RequestBody RetailData data) throws IOException {
        return retailService.update(addr, data);
    }

    @PostMapping("/storager/add")
    public boolean addStorager(@RequestHeader String addr, @RequestBody StoragerData data) throws IOException {
        return storagerService.add(addr, data);
    }

    @PostMapping("/storager/update")
    public boolean updateStorager(@RequestHeader String addr, @RequestBody StoragerData data) throws IOException {
        return storagerService.update(addr, data);
    }

    @PostMapping("/transport/add")
    public boolean addTransport(@RequestHeader String addr, @RequestBody TransportData data) throws IOException {
        return transportService.add(addr, data);
    }

    @PostMapping("/transport/update")
    public boolean updateTransport(@RequestHeader String addr, @RequestBody TransportData data) throws IOException {
        return transportService.update(addr, data);
    }
}