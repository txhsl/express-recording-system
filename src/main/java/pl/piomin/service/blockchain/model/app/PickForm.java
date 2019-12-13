package pl.piomin.service.blockchain.model.app;

public class PickForm {

    private String id;
    private String weight;
    private String fee;

    public PickForm(String id, String weight){
        this.id = id;
        this.weight = weight;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getFee() {
        return String.valueOf(Long.parseLong(weight)/100);
    }
}