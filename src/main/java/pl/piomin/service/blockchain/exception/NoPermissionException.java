package pl.piomin.service.blockchain.exception;

public class NoPermissionException extends Exception {

    private static String MESSAGE = "Permission Denied";

    public String getMsg(){
        return MESSAGE;
    }
}
