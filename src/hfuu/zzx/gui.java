package hfuu.zzx;

import javax.swing.*;
import java.awt.*;
import java.net.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import javax.swing.JFileChooser;

/**
 * @author k2
 */

public class gui extends JFrame implements ActionListener{

    /**
     * 声明组件
     */
    public String receiveIP = null;
    public int receivePORT = 0;
    public String filePath = "C:\\downloads\\p2pfile";
    public String folderPath = null;
    JTextArea transmsg;
    JPanel jpz,jp1,jp11,jp2;
    JLabel jl1,jl11,jl2,jl3;
    JButton jb1,jb3,jb4,jbinput,jbtrans,jbrec;
    JTextField jtf1,jtf2,tatip,tatport;


    /**
     * 获取局域网ip和可用端口
     */
    String localport ="8821";
    public String getLocalHostIP(){
        String ip;
        try {
            InetAddress addr = InetAddress.getLocalHost();
            ip = addr.getHostAddress();
        } catch(Exception ex){
            transmsg.append("未获取到本机地址");
            ip = "127.0.0.1";
        }
        return ip;
    }
    String localip =getLocalHostIP();

    /**
     * GUI界面
     */
    public gui() {
//        String lookAndFeel = "com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
//        UIManager.setLookAndFeel(lookAndFeel);
        setLayout(new BorderLayout());   //UI和布局管理

        jp1 = new JPanel();                                     //第一行
        jpz = new JPanel();
        jp11 = new JPanel();
        jbtrans = new JButton("开始传送");
        jbrec = new JButton("开始接收");
        jb1 = new JButton("选择文件");
        jbinput = new JButton("确定");
        jl3= new JLabel("本地网络路径");
        jl1 = new JLabel("输入发送者网络IP");
        jl11 = new JLabel("输入发送者网络端口");
        jp1.add(jl3);
        jtf1 = new JTextField(localip+":"+localport);
        jtf1.setEditable(false);
        jp1.add(jtf1);
        jp1.add(jb1);
        jp1.add(jbtrans);
        jp1.add(jbrec);
        tatip = new JTextField(10);
        tatport = new JTextField(5);
        jp11.add(jl1);
        jp11.add(tatip);
        jp11.add(jl11);
        jp11.add(tatport);
        jp11.add(jbinput);

        jb1.addActionListener(this);                         //注册panel1的监听器
        jb1.setActionCommand("选择文件");
        jbinput.addActionListener(this);
        jbinput.setActionCommand("确认输入");
        jbtrans.addActionListener(this);
        jbtrans.setActionCommand("开始传送");
        jbrec.addActionListener(this);
        jbrec.setActionCommand("开始接收");

        jpz.setLayout(new BorderLayout());                     //一二行共享容器的NORTH
        jpz.add(jp1,BorderLayout.NORTH);
        jpz.add(jp11,BorderLayout.CENTER);
        jpz.add(jp11);

        jp2 = new JPanel();                                    //第三行
        jl2 = new JLabel("保存目录");
        jb3  =new JButton("浏览");
        jb4 = new JButton("打开目录");
        jtf2 = new JTextField(folderPath,15);
        jp2.add(jl2);
        jp2.add(jtf2);
        jp2.add(jb3);
        jp2.add(jb4);
        jpz.add(jp2,BorderLayout.SOUTH);
        add(jpz,BorderLayout.NORTH);

        jb3.addActionListener(this);                         //注册panel2的监听器
        jb3.setActionCommand("选择文件夹");
        jb4.addActionListener(this);
        jb4.setActionCommand("打开目录");

        transmsg = new JTextArea(14,100);                            //传输信息
//        transmsg.setTabSize(4);
        transmsg.setFont(new Font("标楷体",Font.BOLD,16));
        transmsg.setLineWrap(true);                                    //自动换行
        transmsg.setWrapStyleWord(true);                               //断行不断字
        transmsg.setBackground(Color.LIGHT_GRAY);
        transmsg.setEditable(false);
        add(transmsg,BorderLayout.SOUTH);

        /**
         * *窗口总布局
         * */
        this.setLocation(200,100);
        this.setTitle("p2p file sharing system");
        this.setSize(700,428);
        Image i1 = new ImageIcon("image/ico.jpg").getImage();
        this.setIconImage(i1);
        this.setResizable(false);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    }

    /**
     * 监听器事件
     */
    @Override
    public void actionPerformed(ActionEvent e){
        if(e.getActionCommand().equals("选择文件")){
            /**
             * 用getSelectedFile获取文件的绝对路径
             */
            JFileChooser chooser = new JFileChooser();
            if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
//                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                File fo = chooser.getSelectedFile();
                filePath = fo.getAbsolutePath();
                transmsg.append("已选择文件"+filePath+"\n");
            }
        }
        if(e.getActionCommand().equals("选择文件夹")){
            /**
             * 获取接收文件夹的路径
             */
            JFileChooser chooser1 = new JFileChooser();
            chooser1.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            if (chooser1.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
            {
                System.out.println("getCurrentDirectory(): " + chooser1.getCurrentDirectory());
                System.out.println("getSelectedFile() : " + chooser1.getSelectedFile());

                File fo1 = chooser1.getSelectedFile();
                folderPath = fo1.getAbsolutePath();
                transmsg.append("已获取接收文件夹"+folderPath+"\n");
                jtf2.setText(folderPath);                                            //打印保存文件夹路径在文本框中
            }
        }
        if(e.getActionCommand().equals("打开目录")){
            /**
             * 用命令行打开传输完毕的文件
             */
            Runtime runtime = null;
            try {
                runtime = Runtime.getRuntime();
                runtime.exec("cmd /c start explorer " + folderPath);
            } catch (IOException ex) {
                ex.printStackTrace();
            } finally {
                if (null != runtime) {
                    runtime.runFinalization();
                }
            }
        }
        if(e.getActionCommand().equals("确认输入")){
            receiveIP = tatip.getText();
            String temp = tatport.getText();
            receivePORT =Integer.parseInt(temp);
            if(receivePORT !=0 && receiveIP !=null && !receiveIP.isEmpty())
                transmsg.append("已获取网络地址\n");
            else
                transmsg.append("无法获取网络地址\n");
        }
        if(e.getActionCommand().equals("开始传送")){
            // start hfuu.zzx.sendFile.java
            try {
                sendFile s1 = new sendFile(filePath,Integer.parseInt(localport));
                Thread t1  = new Thread(s1);
                t1.start();
                String temp = null;
                transmsg.append("开始发送文件!\n等待接受\n");
                transmsg.append(temp);
            }catch (Exception err){
                err.printStackTrace();
                JOptionPane.showMessageDialog(this,"发送错误");
            }
        }
        if(e.getActionCommand().equals("开始接收")){
            //start hfuu.zzx.receiveFile.java
            try {
                receiveFile r1 = new receiveFile(folderPath+"\\",receiveIP, receivePORT);
                Thread t2 = new Thread(r1);
                t2.start();
            }catch (Exception err1){
                err1.printStackTrace();
                JOptionPane.showMessageDialog(this,"接受错误");
            }
        }
    }

}