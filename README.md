# User Directory

Aplikasi Android untuk mengelola daftar user, dibuat sebagai bagian dari technical test.
 
---

## Cara Penggunaan

### Melihat Daftar User
Buka aplikasi, daftar user langsung ditampilkan. Kalau tidak ada koneksi, aplikasi akan menampilkan data terakhir yang tersimpan di lokal dengan banner kecil di bagian atas.

### Mencari User
Ketik nama di kolom pencarian yang ada di bawah toolbar. Hasil akan langsung difilter tanpa perlu menekan tombol apapun.

### Mengurutkan User
Tekan ikon sort di kanan atas untuk toggle urutan A–Z atau Z–A.

### Filter berdasarkan Kota
Tekan ikon filter di kanan atas, pilih kota dari bottom sheet yang muncul. Untuk mereset filter, pilih "Semua Kota". Filter ini juga tetap bisa digunakan saat offline karena data kota ikut di-cache.

### Menambah User Baru
Tekan tombol + di kanan atas, isi form, lalu tekan Simpan.
- Kalau online: data langsung dikirim ke server.
- Kalau offline: data disimpan sementara dan akan dikirim otomatis saat koneksi kembali tersedia.
---

## Teknologi yang Digunakan

- **Kotlin** — bahasa utama
- **Jetpack Compose** — UI
- **Hilt** — dependency injection
- **Retrofit + Moshi** — HTTP client dan JSON parser
- **Room** — local database untuk caching dan offline queue
- **WorkManager** — sinkronisasi data pending saat kembali online
- **Coroutine + Flow** — async dan reactive programming
- **Navigation Compose** — navigasi antar screen
- **Timber** — logging
---

## Kenapa Tampilan/Interaksi Seperti Ini

**Search langsung di toolbar area** — supaya user tidak perlu buka screen baru hanya untuk cari nama. Lebih cepat dan langsung kelihatan hasilnya.

**Filter pakai bottom sheet** — pilihan kota tidak sedikit, bottom sheet lebih nyaman di mobile dibanding dropdown. User juga bisa scroll kalau kotanya banyak.

**Sort toggle di toolbar** — fitur yang mungkin jarang dipakai tapi tetap mudah dijangkau. Pakai satu tombol toggle daripada menu tambahan supaya tidak crowded.

**Form tambah user di halaman terpisah** — form punya beberapa field, kalau pakai dialog atau bottom sheet akan terasa sempit. Halaman sendiri lebih leluasa dan bisa di-scroll kalau keyboard muncul.

**Banner offline** — user perlu tahu kalau data yang ditampilkan bukan data terbaru. Daripada langsung error, lebih baik kasih tahu dengan cara yang tidak mengganggu.

**Offline queue untuk tambah user** — kalau user sudah isi form lengkap lalu ternyata tidak ada koneksi, sayang kalau harus diulang dari awal. Data disimpan dulu dan dikirim otomatis lewat WorkManager saat internet tersedia lagi.