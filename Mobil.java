class Mobil {
    private String merek;
    private String model;
    private String nomorPolisi;

    public Mobil(String merek, String model, String nomorPolisi) {
        this.merek = merek;
        this.model = model;
        this.nomorPolisi = nomorPolisi;
    }

    public String getNomorPolisi() {
        return nomorPolisi;
    }

    public String getMerek() {
        return merek;
    }

    public String getModel() {
        return model;
    }
}