package pl.piomin.service.blockchain.model.app;

/**
 * @author: HuShili
 * @date: 2019/12/13
 * @description: none
 */
public class OrderForm {

    private String id;
    private String imgQR;
    private String value;
    private String sender;
    private String senderPhone;
    private String senderAddress;
    private String receiver;
    private String receiverPhone;
    private String receiverAddress;
    private String date;

    public OrderForm(String imgQR, String value, String sender, String senderPhone, String senderAddress,
                     String receiver, String receiverPhone, String receiverAddress, String date) {
        this.imgQR = imgQR;
        this.value = value;
        this.sender = sender;
        this.senderPhone = senderPhone;
        this.senderAddress = senderAddress;
        this.receiver = receiver;
        this.receiverPhone = receiverPhone;
        this.receiverAddress = receiverAddress;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgQR() {
        return imgQR;
    }

    public void setImgQR(String imgQR) {
        imgQR = imgQR;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
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

    public String getReceiverAddress() {
        return receiverAddress;
    }

    public String getSender() {
        return sender;
    }

    public String getSenderPhone() {
        return senderPhone;
    }

    public String getSenderAddress() {
        return senderAddress;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
