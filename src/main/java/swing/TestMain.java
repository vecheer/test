package swing;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/**
 * @author yq
 * @version v1.0 2023-02-16 11:25 AM
 */
public class TestMain extends JFrame {
    public static void main(String[] args) {
        TestMain testMain = new TestMain();

    }
    private JTextArea recordOfMsgText;
    private JComboBox<String> jcb;
    private JTextArea sendMsgText;
    private JButton send;

    public TestMain() {
        this.setTitle("YQ chat");//最重要的！名字
        this.setLayout(new FlowLayout());
        this.setSize(600, 550);
        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);//设置点击×退出程序I

        recordOfMsgText = new JTextArea(20, 50);//文本域 存放历史消息
        recordOfMsgText.setLineWrap(true);
        recordOfMsgText.setEditable(false);//改为不可编辑的

        jcb = new JComboBox<String>();//下拉框
        jcb.addItem("ALL");
        jcb.setPreferredSize(new Dimension(100, 50));//设置大小

        sendMsgText = new JTextArea(3, 30);//消息发送框
        sendMsgText.setLineWrap(true);

        send = new JButton("send");//发送按钮
        send.setPreferredSize(new Dimension(100, 50));

        this.add(recordOfMsgText);
        this.add(jcb);
        this.add(sendMsgText);
        this.add(send);
        this.setVisible(true);
    }

    public JTextArea getSendMsgText() {
        return sendMsgText;
    }

    public JButton getSend() {
        return send;
    }

    public JComboBox<String> getJcb() {
        return jcb;
    }

    public JTextArea getRecordOfMsgText() {
        return recordOfMsgText;
    }

}

