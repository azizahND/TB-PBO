import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Mainn {
    static final String DB_URL = "jdbc:mysql://localhost:3306/rental_mobil";
    static final String USER = "root";
    static final String PASS = "Jijah4321.*";

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            int choice;
            do {
            // Menampilkan menu CRUD
            System.out.println("\n===== Program Zizah's Rental Mobil  =====");
            System.out.println("\n");
            System.out.println("Menu :");
            System.out.println("1. Tambah Data");
            System.out.println("2. Tampilkan Data");
            System.out.println("3. Update Data");
            System.out.println("4. Hapus Data");
            System.out.print("Pilih menu (1-4): ");
            choice = scanner.nextInt();

            switch (choice) {
                case 1:
                    tambahData();
                    break;
                case 2:
                    tampilkanData();
                    break;
                case 3:
                    updateData();
                    break;
                case 4:
                    hapusData();
                    break;
                default:
                    System.out.println("Pilihan tidak valid");
            }
        } while (choice != 5);  // Loop terus sampai pengguna memilih keluar (menu 5)
        } catch (Exception e) {
            System.out.println("Terjadi kesalahan: " + e.getMessage());
        }
    }

    private static Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private static void tambahData() {
        try (Connection connection = connect()) {
            // Input data pelanggan
        Scanner scanner = new Scanner(System.in);
        System.out.print("\nNama pelanggan  : ");
        String namaPelanggan = scanner.nextLine();
        System.out.print("Alamat pelanggan: ");
        String alamatPelanggan = scanner.nextLine();
        System.out.print("Nomor pelanggan : ");
        String nomorPelanggan = scanner.nextLine();

        // Input data mobil
        System.out.print("\nJenis mobil : \n1. Avanza \n2. Ayla \n3. Xenia");
        System.out.print("\nPilih : ");
        int jenisMobil = scanner.nextInt();
        scanner.nextLine(); // Consuming the newline character

        Mobil mobil;
        switch (jenisMobil) {
            case 1:
                mobil = new Avanza("Toyota", "Avanza", "ABC123");
                break;
            case 2:
                mobil = new Ayla("Daihatsu", "Ayla", "XYZ456");
                break;
            case 3:
                mobil = new Xenia("Daihatsu", "Xenia", "DEF789");
                break;
            default:
                throw new IllegalArgumentException("Jenis mobil tidak valid");
        }

        // Input lama penyewaan
        System.out.print("Lama penyewaan (dalam hari): ");
        int lamaPenyewaan = scanner.nextInt();

        // Hitung tanggal pengembalian
        Date tanggalPeminjaman = new Date();
        Date tanggalPengembalian = new Date(tanggalPeminjaman.getTime() + lamaPenyewaan * 24 * 60 * 60 * 1000);
        // Membuat objek PeminjamanMobil
        PeminjamanMobil peminjaman = new PeminjamanMobil();
        peminjaman.pinjamMobil(mobil, new Pelanggan(namaPelanggan, alamatPelanggan, nomorPelanggan),
                tanggalPeminjaman, tanggalPengembalian);

        // Menampilkan struk
        System.out.println("\n===== Struk Peminjaman Mobil =====");
        System.out.println("Nama Pelanggan      : " + peminjaman.getPelanggan().getNama().toUpperCase());
        System.out.println("Alamat Pelanggan    : " + peminjaman.getPelanggan().getAlamat().toUpperCase());
        System.out.println("Nomor Pelanggan     : " + peminjaman.getPelanggan().getNomorPelanggan());
        System.out.println("Jenis Mobil         : " + peminjaman.getMobil().getMerek() + " " + peminjaman.getMobil().getModel());
        System.out.println("Lama Penyewaan      : " + lamaPenyewaan + " hari");
        System.out.println("Tanggal Peminjaman  : " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(peminjaman.gettanggalPeminjaman()));
        System.out.println("Tanggal Pengembalian: " + new SimpleDateFormat("dd-MM-yyyy HH:mm:ss").format(tanggalPengembalian));
        System.out.println("Biaya Total         : " + peminjaman.getBiaya() + " IDR");

        // Tambah data pelanggan
        String tambahPelangganQuery = "INSERT INTO pelanggan (nama, alamat, nomor_pelanggan) VALUES (?, ?, ?)";
        try (PreparedStatement pelangganStatement = connection.prepareStatement(tambahPelangganQuery)) {
            pelangganStatement.setString(1, namaPelanggan);
            pelangganStatement.setString(2, alamatPelanggan);
            pelangganStatement.setString(3, nomorPelanggan);
            pelangganStatement.executeUpdate();
        }

        // Tambah data mobil
        /// Tambah data mobil
String tambahMobilQuery = "INSERT INTO mobil (id, merek, model, nomor_polisi) VALUES (?, ?, ?, ?)";
try (PreparedStatement mobilStatement = connection.prepareStatement(tambahMobilQuery)) {
    mobilStatement.setString(1, nomorPelanggan);
    mobilStatement.setString(2, mobil.getMerek());
    mobilStatement.setString(3, mobil.getModel());
    mobilStatement.setString(4, mobil.getNomorPolisi());

    mobilStatement.executeUpdate();
}


        // Tambah data peminjaman
        String tambahPeminjamanQuery = "INSERT INTO peminjaman (id_pelanggan, id_mobil, tanggal_peminjaman, tanggal_pengembalian) VALUES ((SELECT id FROM pelanggan WHERE nomor_pelanggan = ?), (SELECT id FROM mobil WHERE nomor_polisi = ?), ?, ?)";
        try (PreparedStatement peminjamanStatement = connection.prepareStatement(tambahPeminjamanQuery)) {
            peminjamanStatement.setString(1, nomorPelanggan);
            peminjamanStatement.setString(2, mobil.getNomorPolisi());
            peminjamanStatement.setTimestamp(3, new java.sql.Timestamp(tanggalPeminjaman.getTime()));
            peminjamanStatement.setTimestamp(4, new java.sql.Timestamp(tanggalPengembalian.getTime()));
            peminjamanStatement.executeUpdate();
        }

        System.out.println("Data berhasil ditambahkan ke dalam database.");
    } catch (SQLException e) {
    
            System.out.println("Terjadi kesalahan saat menambah data: " + e.getMessage());
        }
    }

    private static void tampilkanData() {
        try (Connection connection = connect()) {
            // Tampilkan data pelanggan
        String tampilkanPelangganQuery = "SELECT * FROM pelanggan";
        try (PreparedStatement pelangganStatement = connection.prepareStatement(tampilkanPelangganQuery)) {
            ResultSet pelangganResultSet = pelangganStatement.executeQuery();
            
            System.out.println("\n===== Data Pelanggan =====");
            System.out.println("ID\tNama\tAlamat\tNomor Pelanggan");

            while (pelangganResultSet.next()) {
                int idPelanggan = pelangganResultSet.getInt("id");
                String namaPelanggan = pelangganResultSet.getString("nama");
                String alamatPelanggan = pelangganResultSet.getString("alamat");
                String nomorPelanggan = pelangganResultSet.getString("nomor_pelanggan");

                System.out.println(idPelanggan + "\t" + namaPelanggan + "\t" + alamatPelanggan + "\t" + nomorPelanggan);
            }
        }

        // Tampilkan data mobil
String tampilkanMobilQuery = "SELECT * FROM mobil";
try (PreparedStatement mobilStatement = connection.prepareStatement(tampilkanMobilQuery)) {
    ResultSet mobilResultSet = mobilStatement.executeQuery();

    System.out.println("\n===== Data Mobil =====");
    System.out.println("ID\tMerek\tModel\tNomor Polisi");

    while (mobilResultSet.next()) {
        int idMobil = mobilResultSet.getInt("id");
        String merekMobil = mobilResultSet.getString("merek");
        String modelMobil = mobilResultSet.getString("model");
        String nomorPolisi = mobilResultSet.getString("nomor_polisi");

        System.out.println(idMobil + "\t" + merekMobil + "\t" + modelMobil + "\t" + nomorPolisi);
    }
}


        // Tampilkan data peminjaman
        String tampilkanPeminjamanQuery = "SELECT * FROM peminjaman";
        try (PreparedStatement peminjamanStatement = connection.prepareStatement(tampilkanPeminjamanQuery)) {
            ResultSet peminjamanResultSet = peminjamanStatement.executeQuery();

            System.out.println("\n===== Data Peminjaman =====");
            System.out.println("ID\tID Pelanggan\tID Mobil\tTanggal Peminjaman\tTanggal Pengembalian");

            while (peminjamanResultSet.next()) {
                int idPeminjaman = peminjamanResultSet.getInt("id");
                int idPelanggan = peminjamanResultSet.getInt("id_pelanggan");
                int idMobil = peminjamanResultSet.getInt("id_mobil");
                Date tanggalPeminjaman = peminjamanResultSet.getTimestamp("tanggal_peminjaman");
                Date tanggalPengembalian = peminjamanResultSet.getTimestamp("tanggal_pengembalian");

                System.out.println(idPeminjaman + "\t" + idPelanggan + "\t" + idMobil + "\t" + tanggalPeminjaman + "\t" + tanggalPengembalian);
            }
        }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat menampilkan data: " + e.getMessage());
        }
    }

    private static void updateData() {
        try (Connection connection = connect()) {

            Scanner scanner = new Scanner(System.in);
            // Input nomor pelanggan untuk mencari data yang akan diperbarui
        System.out.print("Masukkan nomor pelanggan yang akan diperbarui: ");
        String nomorPelangganUpdate = scanner.nextLine();

        // Cek apakah nomor pelanggan ada di database
        String cekPelangganQuery = "SELECT * FROM pelanggan WHERE nomor_pelanggan = ?";
        try (PreparedStatement cekPelangganStatement = connection.prepareStatement(cekPelangganQuery)) {
            cekPelangganStatement.setString(1, nomorPelangganUpdate);
            ResultSet cekPelangganResultSet = cekPelangganStatement.executeQuery();

            if (!cekPelangganResultSet.next()) {
                System.out.println("Nomor pelanggan tidak ditemukan.");
                return;
            }
        }

        // Input data pelanggan yang baru
        System.out.print("Masukkan nama pelanggan baru: ");
        String namaPelangganBaru = scanner.nextLine();
        System.out.print("Masukkan alamat pelanggan baru: ");
        String alamatPelangganBaru = scanner.nextLine();

        // Update data pelanggan
        String updatePelangganQuery = "UPDATE pelanggan SET nama = ?, alamat = ? WHERE nomor_pelanggan = ?";
        try (PreparedStatement updatePelangganStatement = connection.prepareStatement(updatePelangganQuery)) {
            updatePelangganStatement.setString(1, namaPelangganBaru);
            updatePelangganStatement.setString(2, alamatPelangganBaru);
            updatePelangganStatement.setString(3, nomorPelangganUpdate);
            int rowsUpdated = updatePelangganStatement.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Data pelanggan berhasil diperbarui.");
            } else {
                System.out.println("Gagal memperbarui data pelanggan.");
            }
        }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat memperbarui data: " + e.getMessage());
        }
    }

    private static void hapusData() {
        try (Connection connection = connect()) {
            Scanner scanner = new Scanner(System.in);

        // Input nomor pelanggan untuk mencari data yang akan dihapus
        System.out.print("Masukkan nomor pelanggan yang akan dihapus: ");
        String nomorPelangganHapus = scanner.nextLine();

        // Cek apakah nomor pelanggan ada di database
        String cekPelangganQuery = "SELECT * FROM pelanggan WHERE nomor_pelanggan = ?";
        try (PreparedStatement cekPelangganStatement = connection.prepareStatement(cekPelangganQuery)) {
            cekPelangganStatement.setString(1, nomorPelangganHapus);
            ResultSet cekPelangganResultSet = cekPelangganStatement.executeQuery();

            if (!cekPelangganResultSet.next()) {
                System.out.println("Nomor pelanggan tidak ditemukan.");
                return;
            }
        }

        // Hapus data pelanggan
        String hapusPelangganQuery = "DELETE FROM pelanggan WHERE nomor_pelanggan = ?";
        try (PreparedStatement hapusPelangganStatement = connection.prepareStatement(hapusPelangganQuery)) {
            hapusPelangganStatement.setString(1, nomorPelangganHapus);
            int rowsDeleted = hapusPelangganStatement.executeUpdate();

            if (rowsDeleted > 0) {
                System.out.println("Data pelanggan berhasil dihapus.");
            } else {
                System.out.println("Gagal menghapus data pelanggan.");
            }
        }
        } catch (SQLException e) {
            System.out.println("Terjadi kesalahan saat menghapus data: " + e.getMessage());
        }
    }
}
