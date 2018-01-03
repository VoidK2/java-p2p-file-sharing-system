package hfuu.zzx;

import java.awt.*;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import javax.swing.*;
/**
 * @author k2
 * 多线程
 */

public class receiveFile extends JFrame implements Runnable {
    public static String savePath = null;
    JTextArea transmsg;
    public static String ip = null;
    public static int port = 0;
    public receiveFile(String a,String b,int c){
        savePath = a;
        ip = b;
        port = c;
        initgui2();

    }
    public void initgui2(){
        transmsg = new JTextArea(10,100);
//        transmsg.setTabSize(4);
        transmsg.setFont(new Font("标楷体",Font.BOLD,12));
        transmsg.setLineWrap(true);                                    //自动换行
        transmsg.setWrapStyleWord(true);                               //断行不断字
        transmsg.setBackground(Color.LIGHT_GRAY);
        transmsg.setEditable(false);
        add(transmsg);
        JScrollPane scrollPane_1 = new JScrollPane();
        scrollPane_1.setBounds(28, 209, 752, 265);
        scrollPane_1.setViewportView(transmsg);
        add(scrollPane_1);
        this.setLocation(900,100);
        this.setTitle("进程显示");
        this.setSize(200,428);
        Image i1 = new ImageIcon("image/ico.jpg").getImage();
        this.setIconImage(i1);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    public void run(){
        Socket socket=null;
        try {
            socket = new Socket(ip,port);
        } catch (UnknownHostException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        DataInputStream dis=null;
        try {
            dis = new DataInputStream(new BufferedInputStream(socket.getInputStream()));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        int bufferSize = 2048;
        // 缓冲区
        byte[] buf = new byte[bufferSize];
        int passedlen = 0;
        long len = 0;
        // 获取文件名称
        try{
            savePath += dis.readUTF();
            DataOutputStream fileOut = new DataOutputStream(new BufferedOutputStream(new BufferedOutputStream(
                    new FileOutputStream(savePath))));
            len = dis.readLong();
            transmsg.append("文件的长度为:" + len + "    KB\n");
            System.out.println("文件的长度为:" + len + "    KB");

            while (true) {
                int read = 0;
                if (dis!= null) {
                    read = dis.read(buf);
                }
                passedlen += read;
                if (read == -1) {
                    break;
                }
                transmsg.append("文件接收了" + (passedlen * 100 / len) + "%\n");
                System.out.println("文件接收了" + (passedlen * 100 / len) + "%");
                fileOut.write(buf, 0, read);
            }
            transmsg.append("接收完成，文件存为" + savePath+"\n");
            System.out.println("接收完成，文件存为" + savePath);
            fileOut.close();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }
}
