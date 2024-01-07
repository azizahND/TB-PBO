import java.util.Date;
import java.text.SimpleDateFormat;
class PeminjamanMobil implements PeminjamanInterface {
    private Mobil mobil;
    private Pelanggan pelanggan;
    private Date tanggalPeminjaman;
    private Date tanggalPengembalian;
    private double biayaSewa;
    private boolean mobilDisewa;

     public double getBiaya() {
        return biayaSewa;
    }

    public Date gettanggalPeminjaman() {
        return this.tanggalPeminjaman;
    }
    
    public Date gettanggalPengembalian() {
        return this.tanggalPengembalian;
    }
    
    public Pelanggan getPelanggan() {
        return pelanggan;
    }

    public Mobil getMobil() {
        return mobil;
    }

    public void pinjamMobil(Mobil mobil, Pelanggan pelanggan, Date tanggalPeminjaman, Date tanggalPengembalian) {
        if (!mobilDisewa) {
            this.mobil = mobil;
            this.pelanggan = pelanggan;
            this.tanggalPeminjaman = tanggalPeminjaman;
            this.tanggalPengembalian = tanggalPengembalian;
            this.biayaSewa = hitungBiayaSewa();
            this.mobilDisewa = true;
            System.out.println("Peminjaman berhasil! Mobil " + mobil.getMerek() + " " + mobil.getModel() +
                    " telah disewakan kepada " + convertToUpperCase(pelanggan.getNama()) + ". Biaya sewa: " + biayaSewa);
        } else {
            System.out.println("Maaf, mobil sedang tidak tersedia untuk disewakan.");
        }
    }
    

    public void kembalikanMobil() {
        if (mobilDisewa) {
            this.mobilDisewa = false;
            System.out.println("Mobil " + mobil.getMerek() + " " + mobil.getModel() + " telah dikembalikan.");
            System.out.println("Biaya sewa: " + biayaSewa);
        } else {
            System.out.println("Mobil sudah tersedia, tidak dapat dikembalikan kembali.");
        }
    }

    public void perpanjangPeminjaman(Date newDueDate) {
        if (mobilDisewa) {
            this.tanggalPengembalian = newDueDate;
            System.out.println("Peminjaman telah diperpanjang hingga " +
                    new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(newDueDate) +
                    ". Biaya sewa tambahan: " + hitungBiayaSewa());
        } else {
            System.out.println("Maaf, mobil belum disewa, tidak dapat diperpanjang.");
        }
    }

    private double hitungBiayaSewa() {
        long selisihJam = (tanggalPengembalian.getTime() - tanggalPeminjaman.getTime()) / (60 * 60 * 1000);
        double biayaPerJam = 0;

        // Hitung biaya sewa berdasarkan jenis mobil
        if (mobil instanceof Avanza) {
            biayaPerJam = 300000;
            if (selisihJam >= 24 * 2) {
                biayaPerJam *= 0.95; // Diskon 5% jika lebih dari 2 hari
            }
        } else if (mobil instanceof Ayla) {
            biayaPerJam = 400000;
            if (selisihJam >= 24 * 3) {
                biayaPerJam *= 0.9; // Diskon 10% jika lebih dari 3 hari
            }
        } else if (mobil instanceof Xenia) {
            biayaPerJam = 600000;
            if (selisihJam >= 24 * 3) {
                biayaPerJam *= 0.95; // Diskon 5% jika lebih dari 3 hari
            }
        }

        return selisihJam * biayaPerJam;
    }
    private static String convertToUpperCase(String input) {
        return input.toUpperCase();
    }
    
}