package pl.piomin.service.blockchain.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.web3j.protocol.core.methods.response.TransactionReceipt;

import java.util.concurrent.CompletableFuture;

/**
 * @author: HuShili
 * @date: 2019/3/29
 * @description: none
 */

public class Message {
    public enum Type {
        Property, Role, Permission, Register, Report, Order, Pick, Deliver, Receive
    }

    private PermissionSwapper permission;
    private String to;
    private String time;
    private Type type;
    @JsonIgnore
    private CompletableFuture<TransactionReceipt> receipt;
    private boolean isAccepted;
    private boolean isRead;

    public Message(PermissionSwapper permissionSwapper, Type type,
                   String to, String time) {
        this.permission = permissionSwapper;
        this.to = to;
        this.time = time;
        this.type = type;
    }

    public void setRead(boolean read) {
        isRead = read;
    }

    public boolean isRead() {
        return isRead;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public PermissionSwapper getPermission() {
        return permission;
    }

    public void setPermission(PermissionSwapper permission) {
        this.permission = permission;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setReceipt(CompletableFuture<TransactionReceipt> receipt) {
        this.receipt = receipt;
    }

    public CompletableFuture<TransactionReceipt> getReceipt() {
        return receipt;
    }

}
