class Pelanggan {
    private String nama;
    private String alamat;
    private String nomorPelanggan;

    public Pelanggan(String nama, String alamat, String nomorPelanggan) {
        this.nama = nama;
        this.alamat = alamat;
        this.nomorPelanggan = nomorPelanggan;
    }

    public String getNomorPelanggan() {
        return nomorPelanggan;
    }

    public String getNama() {
        return nama;
    }

    public String getAlamat() {
        return alamat;
    }
}
