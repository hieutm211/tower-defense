# tower-defense
An OOP project - https://www.dropbox.com/s/oanjo44fghrol9s/BTL-OOP.pdf?dl=0&fbclid=IwAR0xHVyP9NEiv1c20YDiIa3cXoRsuQXFoQipL5E_rf2xdg2lMWwYF6X50eo

Tower Defense là game phòng thủ chiến thuật thời gian thực. Mục đích của game là ngăn không cho kẻ thù tới đích bằng cách đặt các loại tháp phòng thủ có tác dụng gây sát thương, đóng băng với nhiều mức độ khác nhau. 

Game được làm trên Android.

Các chức năng đã làm được:

**. Phần cơ bản:**

a) Đảm bảo hoàn thành đầy đủ các yêu cầu thiết kế Hướng đối tượng mà đề bài yêu cầu.  
b) Xây dựng đầy đủ các tương tác giữa các đối tượng mà đề bài yêu cầu.

**. Phần nâng cao (phần a, c, d trong yêu cầu nâng cao).**

1. Tự xây dựng project mà không dùng starter project.

2. Xử lí hiệu ứng âm thành và hình ảnh

3. Lưu trạng thái hiện tại của game khi người chơi vừa qua một màn chơi. Trạng thái game sẽ tự khôi phục khi người chơi mở lại map đó.

4. Mở rộng lối chơi:
      - Có máy bay, đi theo đường riêng.
      - Mua, bán, nâng cấp trụ.
      - Map có nhiều đường đi cho Enemy
      - Xây dựng nhiều map khác nhau. (multiple map)

**. Các tính năng khác:**
- Sử dụng thuật toán BFS để tìm đường cho Enemy.
- Thuật toán va chạm giữa các Enemy (những Enemy cùng loại sẽ tránh nhau trên đường đi).
- Enemy đi theo đường cong khi chuyển hướng.
- Có chức năng *Pause, Mute/Unmute, Restart* Game.
- Sử dụng android canvas để draw objects
- Các thao tác với màn hình (User Interface) như kéo, thả, chạm, di chuyển màn hình được tự code hoàn toàn (không sử dụng thư viện ngoài).

