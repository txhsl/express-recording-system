package pl.piomin.service.blockchain.model.app;

/**
 * @author: HuShili
 * @date: 2019/12/13
 * @description: none
 */
public class OrderForm {

    private String id;
    private String imgQR;
    private String sender;
    private String senderPhone;
    private String receiver;
    private String receiverPhone;
    private String date;

    public OrderForm(String imgQR, String sender, String senderPhone, String receiver,
                String receiverPhone, String date) {
        this.imgQR = imgQR;
        this.sender = sender;
        this.senderPhone = senderPhone;
        this.receiver = receiver;
        this.receiverPhone = receiverPhone;
        this.receiverPhone = receiverPhone;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getimgQR() {
        return imgQR;
    }

    public void setImgQR(String imgQR) {
        imgQR = imgQR;
    }

    public String getDate() {
        return date;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getReceiverPhone() {
        return receiverPhone;
    }

    public String getSender() {
        return sender;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public void setReceiverPhone(String receiverPhone) {
        this.receiverPhone = receiverPhone;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setSenderPhone(String senderPhone) {
        this.senderPhone = senderPhone;
    }
}
