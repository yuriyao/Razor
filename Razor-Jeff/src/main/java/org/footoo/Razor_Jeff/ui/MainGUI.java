/**
 * Alipay.com Inc.
 * Copyright (c) 2004-2014 All Rights Reserved.
 */
package org.footoo.Razor_Jeff.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.border.LineBorder;
import javax.swing.filechooser.FileFilter;

import org.footoo.Razor_Jeff.exception.RazorException;
import org.footoo.Razor_Jeff.kernel.RazorCompress;
import org.footoo.Razor_Jeff.kernel.callbacks.CompressStateListener;

import sun.awt.VerticalBagLayout;

/**
 * 主UI界面
 * 
 * @author jeff
 * @version $Id: MainGUI.java, v 0.1 2014年6月11日 上午10:49:44 jeff Exp $
 */
public class MainGUI extends JFrame {

    /**  */
    private static final long serialVersionUID = -7166704461536277559L;
    /** 程序的宽度 */
    private static final int  WIDTH            = 400;
    /** 初始窗口的高度 */
    private static final int  INIT_HEIGHT      = 80;
    /** 压缩面板的高度 */
    private static final int  COMPRESS_HEIGHT  = 350;
    /** 用于现实选择的待压缩的文件 */
    private JList<String>     fileList         = new JList<>();
    /** 压缩面板 */
    private JPanel            compressPanel    = createCompressPanel();
    /** 压缩文件列表 */
    private List<File>        compressFiles    = new ArrayList<>();

    public MainGUI() {
        drawUI();

        setVisible(true);
    }

    /**
     * 界面处理
     */
    private void drawUI() {
        //设置title
        setTitle("Razor 压缩软件");
        //设置尺寸
        setSize(WIDTH, INIT_HEIGHT);
        addButtons();
        //
        //addCompressPanel();
        this.addWindowListener(new InternelWindowListener());
    }

    /**
     * 添加按钮
     */
    private void addButtons() {
        JButton compressButton = new JButton("压缩");
        JButton uncompressButton = new JButton("解压");

        //添加布局
        JPanel buttonsPanel = new JPanel();
        buttonsPanel.setSize(WIDTH, INIT_HEIGHT);
        buttonsPanel.add(compressButton);
        buttonsPanel.add(uncompressButton);
        //添加监听器
        compressButton.addActionListener(new CompressActionListener());
        uncompressButton.addActionListener(new UnCompressActionListener());

        this.getContentPane().setLayout(new VerticalBagLayout());

        this.getContentPane().add(buttonsPanel);
    }

    /**
     * 添加压缩面板
     */
    private void addCompressPanel() {
        this.setSize(WIDTH, INIT_HEIGHT + COMPRESS_HEIGHT);
        this.getContentPane().add(compressPanel);
    }

    /**
     * 创建压缩的panel
     * 
     * @return
     */
    private JPanel createCompressPanel() {
        JPanel compressPanel = new JPanel(new GridLayout(1, 2));

        //压缩面板的尺寸
        compressPanel.setSize(WIDTH, COMPRESS_HEIGHT);

        //文件列表的panel
        JPanel fileListPanel = new JPanel(new GridLayout(1, 1));
        //fileList = 
        fileList.setSize(WIDTH / 2, COMPRESS_HEIGHT);
        fileListPanel.setSize(WIDTH / 2, COMPRESS_HEIGHT);
        fileListPanel.add(fileList);

        fileListPanel.add(fileList);
        fileListPanel.setBorder(new LineBorder(new Color(200, 200, 200)));
        fileList.setListData(new String[] { "hello", "world" });

        //选项
        JPanel optionsPanel = new JPanel();
        optionsPanel.setLayout(new GridLayout(3, 1));

        JButton addFile = new JButton("添加文件");
        addFile.setSize(50, 50);
        JButton removeFile = new JButton("移除文件");
        JButton compressFile = new JButton("压缩文件");

        //添加监听器
        addFile.addActionListener(new AddFilesActionListener());
        removeFile.addActionListener(new RemoveFileActionListener());
        compressFile.addActionListener(new CompressFilesActionListener());

        optionsPanel.add(addFile);
        optionsPanel.add(removeFile);
        optionsPanel.add(compressFile);
        optionsPanel.setSize(WIDTH / 2, COMPRESS_HEIGHT);

        compressPanel.add(fileListPanel);
        compressPanel.add(optionsPanel);
        compressPanel.setBorder(new LineBorder(new Color(200, 200, 200)));
        return compressPanel;

    }

    /**
     * 压缩监听器
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月11日 下午2:44:32 jeff Exp $
     */
    private class CompressActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            addCompressPanel();
        }

    }

    /**
     * 解压按钮点击后的处理函数
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月11日 下午3:42:34 jeff Exp $
     */
    private class UnCompressActionListener implements ActionListener {
        /** 文件选择器 */
        private JFileChooser fileChooser = new CompressedFileChoser();

        @Override
        public void actionPerformed(ActionEvent e) {
            int result = fileChooser.showOpenDialog(MainGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();
                //System.out.println("unCompress:" + file.getName());
                UncompressThread uncompressThread = new UncompressThread(file);
                Thread thread = new Thread(uncompressThread);
                CompressProcessDialog dialog = new CompressProcessDialog("解压文件:" + file.getPath(),
                    "", thread);

                //展示
                dialog.setVisible(true);
                uncompressThread.setDialog(dialog);
                thread.start();

            }
        }
    }

    /**
     * 进行文件压缩
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月16日 上午12:16:57 jeff Exp $
     */
    private class CompressFilesActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {

            CompressThread compressThread = new CompressThread();
            Thread thread = new Thread(compressThread);
            CompressProcessDialog dialog = new CompressProcessDialog("压缩文件", "", thread);

            //展示
            dialog.setVisible(true);
            compressThread.setDialog(dialog);
            thread.start();
        }

    }

    /**
     * 解压和压缩过程的监控
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月11日 下午3:46:29 jeff Exp $
     */
    private class CompressProcessDialog extends JDialog {
        /**  */
        private static final long serialVersionUID = -2271931932026727294L;
        /** 提示信息 */
        private JLabel            tipLabel         = new JLabel();
        /** 进度条 */
        private JProgressBar      progressBar      = new JProgressBar(0, 100);
        /** 取消按钮 */
        private JButton           cancel           = new JButton("取消");

        /**
         * 
         * @param title
         * @param tips
         * @param thread
         */
        public CompressProcessDialog(String title, String tips, final Thread thread) {
            drawDialog();
            setTip(tips);
            setTitle(title);
            setSize(400, 100);
            //
            cancel.addActionListener(new ActionListener() {

                @SuppressWarnings("deprecation")
                @Override
                public void actionPerformed(ActionEvent e) {
                    thread.stop();
                }
            });
        }

        /**
         * 画dialog的界面
         */
        private void drawDialog() {
            this.setLayout(new GridLayout(2, 1));
            this.getContentPane().add(tipLabel);
            //
            JPanel panel = new JPanel();
            progressBar.setSize(350, 50);
            cancel.setSize(50, 50);
            panel.add(progressBar);
            panel.add(cancel);
            this.getContentPane().add(panel);
        }

        /**
         * 设置tip信息
         * 
         * @param tip
         */
        public void setTip(String tip) {
            tipLabel.setText(tip);
        }

        /**
         * 设置处理的进度
         * 
         * @param n
         */
        public void setProcess(int n) {
            progressBar.setValue(n);
        }

    }

    /**
     * 压缩工作线程
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月16日 上午12:12:17 jeff Exp $
     */
    private class CompressThread extends RazorCompress implements Runnable {
        /** 关联的dialog */
        private CompressProcessDialog dialog;

        @Override
        public void processChanged(int process) {
            dialog.setProcess(process);
        }

        @Override
        public void stateChanged(int state) {
            switch (state) {
                case CompressStateListener.COMPRESS_START:
                    dialog.setTip("开始压缩");
                    break;
                case CompressStateListener.SEARCHING_FILES:
                    dialog.setTip("正在遍历文件");
                    break;
                case CompressStateListener.COMPRESS_FAIL:
                    dialog.setTip("压缩失败");
                    break;
                case CompressStateListener.COMPRESS_OK:
                    dialog.setTip("压缩成功");
                    break;
                case CompressStateListener.COMPRESSING:
                    dialog.setTip("正在压缩");
                    break;
                default:
                    break;
            }
        }

        @Override
        public void run() {
            try {
                this.compress(compressFiles, "test/test.je");
            } catch (RazorException e) {
                dialog.setTip("压缩失败[" + e.getMessage() + "]");
            }
        }

        /**
         * Getter method for property <tt>dialog</tt>.
         * 
         * @return property value of dialog
         */
        public final CompressProcessDialog getDialog() {
            return dialog;
        }

        /**
         * Setter method for property <tt>dialog</tt>.
         * 
         * @param dialog value to be assigned to property dialog
         */
        public final void setDialog(CompressProcessDialog dialog) {
            this.dialog = dialog;
        }

    }

    /**
     * 解压的工作线程
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月11日 下午4:09:48 jeff Exp $
     */
    private class UncompressThread extends RazorCompress implements Runnable {
        /** 文件 */
        private File                  file;
        /** 关联的dialog */
        private CompressProcessDialog dialog;

        public UncompressThread(File file) {
            this.file = file;
        }

        @Override
        public void processChanged(int process) {
            dialog.setProcess(process);
            System.out.println("解压进度：" + process);
        }

        @Override
        public void stateChanged(int state) {
            if (state == CompressStateListener.COMPRESS_OK) {
                dialog.setTip("解压完成");
            }
        }

        @Override
        public void run() {
            try {
                uncompress(file.getAbsolutePath(), "test/tt");
            } catch (RazorException e) {
                dialog.setTip("解压发生错误:" + e.getMessage());
            }
        }

        /**
         * Setter method for property <tt>dialog</tt>.
         * 
         * @param dialog value to be assigned to property dialog
         */
        public final void setDialog(CompressProcessDialog dialog) {
            this.dialog = dialog;
        }

    }

    /**
     * 刷新用于显示的文件
     */
    private void flushWantCompressFiles() {
        //
        //Collections.sort(compressFiles);
        String[] fileNames = new String[compressFiles.size()];
        for (int i = 0; i < compressFiles.size(); i++) {
            fileNames[i] = compressFiles.get(i).getName();
        }
        fileList.setListData(fileNames);
    }

    /**
     * 添加待压缩的文件
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月15日 下午11:28:02 jeff Exp $
     */
    private class AddFilesActionListener implements ActionListener {
        /** 添加文件的对话框 */
        private JFileChooser addFilesDialog = new WantCompressFileChoser();

        public AddFilesActionListener() {

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int result = addFilesDialog.showOpenDialog(MainGUI.this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File[] files = addFilesDialog.getSelectedFiles();
                //看看是否有相同的文件名
                for (File src : compressFiles) {
                    for (File comp : files) {
                        if (src.getName().equals(comp.getName())) {
                            JOptionPane.showMessageDialog(MainGUI.this, "已经包含了名字为[" + src.getName()
                                                                        + "]的文件", "错误",
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                }
                //添加文件
                for (File file : files) {
                    compressFiles.add(file);
                }
                flushWantCompressFiles();
            }
        }
    }

    /**
     * 移除文件
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月16日 上午12:09:14 jeff Exp $
     */
    private class RemoveFileActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            int[] indexs = fileList.getSelectedIndices();
            Arrays.sort(indexs);
            for (int i = indexs.length - 1; i >= 0; i--) {
                compressFiles.remove(i);
            }
            flushWantCompressFiles();
        }

    }

    /**
     * 选择待压缩的文件
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月15日 下午11:31:23 jeff Exp $
     */
    private class WantCompressFileChoser extends JFileChooser {

        /**  */
        private static final long serialVersionUID = -6268117118589709602L;

        public WantCompressFileChoser() {
            setDialogTitle("添加待压缩的文件");
            setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
            setMultiSelectionEnabled(true);
        }

    }

    /**
     * 压缩文件选择器
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月11日 下午3:36:10 jeff Exp $
     */
    private class CompressedFileChoser extends JFileChooser {
        /**  */
        private static final long serialVersionUID = 7166175674464377160L;

        /**
         * 
         */
        public CompressedFileChoser() {
            //标题
            setDialogTitle("选择Razor压缩文件");
            //过滤器
            setFileFilter(new CompressFileFilter("Razor压缩文件"));
            //只能是文件
            setFileSelectionMode(JFileChooser.FILES_ONLY);
            //不能多选
            setMultiSelectionEnabled(false);
        }
    }

    /**
     * 压缩文件过滤器
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月11日 下午3:32:40 jeff Exp $
     */
    private class CompressFileFilter extends FileFilter {
        /** 文件描述 */
        private String des;

        public CompressFileFilter(String des) {
            this.des = des;

        }

        @Override
        public boolean accept(File f) {
            if (f.getName().toLowerCase().endsWith("je")) {
                return true;
            } else if (f.isDirectory()) {
                return true;
            }
            return false;
        }

        @Override
        public String getDescription() {
            return des;
        }

    }

    /**
     * 监听窗口事件
     * 
     * @author jeff
     * @version $Id: MainGUI.java, v 0.1 2014年6月11日 上午10:58:42 jeff Exp $
     */
    private class InternelWindowListener extends WindowAdapter {

        /**
         * 窗口关闭
         * 
         * @see java.awt.event.WindowAdapter#windowClosing(java.awt.event.WindowEvent)
         */
        public void windowClosing(WindowEvent e) {
            System.out.println("Exiting");
            System.exit(0);
        }
    }

    public static void main(String args[]) {
        new MainGUI();
    }
}
