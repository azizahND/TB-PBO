
import java.util.Date;

interface PeminjamanInterface {
    void pinjamMobil(Mobil mobil, Pelanggan pelanggan, Date tanggalPeminjaman, Date tanggalPengembalian);
    void kembalikanMobil();
    void perpanjangPeminjaman(Date newDueDate);
}