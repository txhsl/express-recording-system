package pl.piomin.service.blockchain.model;

/**
 * @author: HuShili
 * @date: 2019/5/27
 * @description: none
 */

public class Report {
    private String from;
    private String order;
    private String target;
    private String reason;
    private String to;

    public Report() {}

    public String getTarget() {
        return target;
    }

    public String getOrder() {
        return order;
    }

    public String getFrom() {
        return from;
    }

    public String getReason() {
        return reason;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }
}
