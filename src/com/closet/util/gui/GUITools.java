package com.closet.util.gui;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashMap;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.LookAndFeel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;

public class GUITools {

    public static final Dimension LOCAL_SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();

    // System.getProperty("M3DVMName"); ---> M3DOnline32 ... M3DOnline64 
    static void _showCriticalMessage(final String title, final String msg, final boolean shutdown) {
        JOptionPane pane = new JOptionPane(msg, JOptionPane.INFORMATION_MESSAGE, JOptionPane.DEFAULT_OPTION, GUITools.getDefaultIcon());
        JDialog d = pane.createDialog(title);
        GUITools.setIcon(d);
        d.setAlwaysOnTop(true);
        d.setVisible(true);
        if (shutdown) {
            System.exit(0);
        }
    }

    public static void showCriticalMessage(final String title, final String msg, final boolean shutdown) {
        if (SwingUtilities.isEventDispatchThread()) {
            _showCriticalMessage(title, msg, shutdown);
        } else {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    _showCriticalMessage(title, msg, shutdown);
                }
            });
        }
    }

    public static JFrame getTopLevelFrame(Component comp) {
        return (JFrame) SwingUtilities.getRoot(comp);
    }

    public static Window getMyWindow(Component comp) {
        return SwingUtilities.getWindowAncestor(comp);
    }

    static ImageIcon DEFAULT_ICON = null;

    static HashMap<String, ImageIcon> ICONS = new HashMap<String, ImageIcon>();

    public static ImageIcon getIcon(String name) {
        if (ICONS.containsKey(name)) {
            return ICONS.get(name);
        }
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = GUITools.class.getResourceAsStream("/" + name + ".icon");
            int i;
            while ((i = in.read()) >= 0) {
                out.write(i);
            }
            out.flush();
            ImageIcon ico = new ImageIcon(out.toByteArray());
            ICONS.put(name, ico);
            return ico;
        } catch (Exception e2) {
            e2.printStackTrace();
            System.out.println(" !!!!!! can not find icon " + name + ".icon");
            //e2.printStackTrace();
            return getDefaultIcon();
        }
    }

    public static ImageIcon getDefaultIcon() {
        if (DEFAULT_ICON != null) {
            return DEFAULT_ICON;
        }
        try {
            URL url = Integer.class.getResource(System.getProperty("Application") + ".icon");
            return DEFAULT_ICON = new ImageIcon(url);
        } catch (Exception ex) {
            try {
                URL url = Integer.class.getResource("/default.icon");
                return DEFAULT_ICON = new ImageIcon(url);
            } catch (Exception e2) {
                return null;
            }
        }
    }

    public static JPopupMenu createPopupMenu(JComponent c, ActionListener listener, String... items) {
        final JPopupMenu menu = new JPopupMenu(c.getClass().getName());
        c.add(menu);
        c.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                maybeShowPopup(e);
            }

            public void mouseReleased(MouseEvent e) {
                maybeShowPopup(e);
            }

            private void maybeShowPopup(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    menu.show(e.getComponent(), e.getX(), e.getY());
                }
            }
        });
        for (String s : items) {
            JMenuItem item = new JMenuItem(s);
            menu.add(item);
            item.addActionListener(listener);
        }
        return menu;
    }

    public static final FileFilter PNG = new FileNameExtensionFilter("PNG", "png");
    public static final FileFilter JPG = new FileNameExtensionFilter("JPG", "jpg");
    public static final FileFilter GIF = new FileNameExtensionFilter("GIF", "gif");
    public static final FileFilter BMP = new FileNameExtensionFilter("BMP", "bmp");
    public static final FileFilter DCM = new FileNameExtensionFilter("DCM", "dcm");

    public static String showSaveToFile(Component parent, String initialDirectory, FileFilter... filters) {
        JFileChooser chooser = new JFileChooser(initialDirectory);
        chooser.setMultiSelectionEnabled(false);
        for (FileFilter f : filters) {
            chooser.addChoosableFileFilter(f);
        }
        chooser.setFileFilter(filters[0]);
        chooser.setAcceptAllFileFilterUsed(false);
        int returnVal = chooser.showSaveDialog(parent);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File file = chooser.getSelectedFile();
            return file.getAbsolutePath() + "." + chooser.getFileFilter().getDescription();
        }
        return null;
    }

    public static JDialog showAsDialog(Frame parent, JPanel panel, boolean modal) {
        JDialog dialog = new JDialog(parent, panel.getName(), modal);
        setIcon(dialog);
        dialog.setContentPane(panel);
        dialog.pack();
        GUITools.setLocationToWindowCenter(parent, dialog);
        return dialog;
    }
    
    public static JDialog showAsDialog(Frame parent, JPanel panel,String iconName, boolean modal) {
        JDialog dialog = new JDialog(parent, panel.getName(), modal);
        setIcon(dialog, iconName);
        dialog.setContentPane(panel);
        dialog.pack();
        GUITools.setLocationToWindowCenter(parent, dialog);
        return dialog;
    }

    public static JDialog showAsUndecoratedDialog(Frame parent, JPanel panel, boolean modal) {
        JDialog dialog = new JDialog(parent, panel.getName(), modal);
        dialog.setUndecorated(true);
        setIcon(dialog);
        dialog.setContentPane(panel);
        dialog.pack();
        GUITools.setLocationToWindowCenter(parent, dialog);
        return dialog;
    }
    
    public static JDialog showAsUndecoratedDialog(JPanel panel, boolean modal) {
        return showAsUndecoratedDialog(null, panel, modal);
    }
    
    public static JDialog showAsDialog(JPanel panel, boolean modal) {
        return showAsDialog(null, panel, modal);
    }
    
    public static JDialog showAsDialog(JPanel panel, String iconName, boolean modal){
        return showAsDialog(null, panel, iconName, modal);
    }

    public static void showMessageDialogWithTextArea(Component parent, String title, String message) {
        JTextArea textArea = new JTextArea(message);
        textArea.setColumns(50);
        textArea.setRows(40);
        textArea.setLineWrap(true);//超过设置的列数自动换行
        textArea.setEditable(false);
        JOptionPane.showMessageDialog(parent, new JScrollPane(textArea), title, JOptionPane.PLAIN_MESSAGE);
    }

    public static void setLocationToWindowCenter(Frame parent, Component dialog) {
        if (mainFrame != null) {
            setLocationToWindowCenter(mainFrame, dialog);
        } else {
            if (parent != null) {
                setLocationToScreenRight(dialog);
            } else {
                setLocationToScreenCenter(dialog);
            }
        }
    }

    public static void setLocationToWindowCenter(Component parent, Component component) {
        Point p = parent.getLocationOnScreen();
        int w = parent.getWidth();
        int h = parent.getHeight();
        component.setLocation(w / 2 - component.getWidth() / 2, h / 2 - component.getHeight() / 2);
    }

    public static void setLocationToScreenRight(Component component) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        component.setLocation(d.width - component.getWidth(), 0);
    }

    public static void setLocationToScreenCenter(Component component) {
        Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
        component.setLocation(d.width / 2 - component.getWidth() / 2, d.height / 2 - component.getHeight() / 2);
    }

    public static void disposeDialog(JPanel panel) {
        try {
            java.awt.Window w = SwingUtilities.windowForComponent(panel);
            if (w != null && w instanceof JDialog) {
                w.dispose();
            }
        } catch (Exception ex) {
        }
    }

    public static void fitTableColumns(JTable myTable) {
        //myTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        JTableHeader header = myTable.getTableHeader();
        int rowCount = myTable.getRowCount();
        Enumeration columns = myTable.getColumnModel().getColumns();
        while (columns.hasMoreElements()) {
            TableColumn column = (TableColumn) columns.nextElement();
            int col = header.getColumnModel().getColumnIndex(column.getIdentifier());
            int width = (int) header.getDefaultRenderer().getTableCellRendererComponent(myTable, column.getIdentifier(), false, false, -1, col).getPreferredSize().getWidth();
            for (int row = 0; row < rowCount; row++) {
                int preferedWidth = (int) myTable.getCellRenderer(row, col).getTableCellRendererComponent(myTable, myTable.getValueAt(row, col), false, false, row, col).getPreferredSize().getWidth();
                width = Math.max(width, preferedWidth);
            }
            header.setResizingColumn(column); // 此行很重要
            column.setWidth(width + 8 + myTable.getIntercellSpacing().width);
        }
    }

    public static void setColumnWidth(JTable table, int c, int width) {
        table.getColumnModel().getColumn(c).setPreferredWidth(width);
        table.getColumnModel().getColumn(c).setMinWidth(width);
        table.getColumnModel().getColumn(c).setMaxWidth(width);
    }

    public static void setRowSelection(AbstractTableModel model, int i) {
        JTable[] tables = model.getListeners(JTable.class);
        for (JTable t : tables) {
            t.addRowSelectionInterval(i, i);
        }
    }

    public static void clearRowSelection(AbstractTableModel model, int i) {
        JTable[] tables = model.getListeners(JTable.class);
        for (JTable t : tables) {
            t.removeRowSelectionInterval(i, i);
        }
    }

    public static void setIcon(Window diag) {
        try {
            URL url = diag.getClass().getResource(System.getProperty("Application") + ".icon");
            diag.setIconImage(javax.imageio.ImageIO.read(url.openStream()));
        } catch (Exception ex) {
            try {
                URL url = diag.getClass().getResource("/default.icon");
                diag.setIconImage(javax.imageio.ImageIO.read(url.openStream()));
            } catch (Exception e2) {
                //ex.printStackTrace();
            }
        }
    }
    
    public static void setIcon(Window diag, String iconName) {
        try {
            URL url = diag.getClass().getResource("/" + iconName + ".icon");
            diag.setIconImage(javax.imageio.ImageIO.read(url.openStream()));
        } catch (Exception ex) {
            try {
                URL url = diag.getClass().getResource("/default.icon");
                diag.setIconImage(javax.imageio.ImageIO.read(url.openStream()));
            } catch (Exception e2) {
                //ex.printStackTrace();
            }
        }
    }

    public static interface MainFrameCreator {

        JFrame createMainFrame() throws Exception;
    }

    public static JFrame mainFrame;

    public static void mainFrameLauncher(final String appName, final String title, final MainFrameCreator frameCreator, final int w, final int h, LookAndFeel lookAndFeel) {
        System.setProperty("Application", appName);
        try {
            javax.swing.UIManager.setLookAndFeel(lookAndFeel);
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        } catch (Exception ex) {
        }
        //</editor-fold>
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    mainFrame = frameCreator.createMainFrame();
                    setIcon(mainFrame);
                    if (w <= 0 && h <= 0) {
                        mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
                    } else if (h <= 0) {
                        mainFrame.setPreferredSize(new Dimension(w, 800));
                        mainFrame.setExtendedState(JFrame.MAXIMIZED_VERT);
                    } else if (w <= 0) {
                        mainFrame.setPreferredSize(new Dimension(800, h));
                        mainFrame.setExtendedState(JFrame.MAXIMIZED_HORIZ);
                    } else {
                        mainFrame.setPreferredSize(new Dimension(w, h));
                        mainFrame.setSize(w, h);
                    }
                    mainFrame.setTitle(appName + " " + title);
                    mainFrame.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(null, "主窗口无法打开,系统退出...", "服务错误", JOptionPane.ERROR_MESSAGE);
                    System.exit(0);
                }
            }
        });
    }
    
    public static FileFilter[] getFileFilter(){
        FileNameExtensionFilter imageFilter = new FileNameExtensionFilter("图片 (*.jpg, *.jpeg, *.png, *.bmp, *.gif )", "jpg", "jpeg", "png", "bmp", "gif");
        FileNameExtensionFilter videoFilter = new FileNameExtensionFilter("视频 (*.avi, *.wmv, *.3gp, *.mpeg, *.mkv, *.flv, *.rmvb, *.mp4 )", "avi", "wmv", "3gp", "mpeg", "mkv", "flv", "rmvb", "mp4");
        FileNameExtensionFilter docFilter = new FileNameExtensionFilter("word文档 (*.doc, *.docx )", "doc", "docx");
        FileNameExtensionFilter pptFilter = new FileNameExtensionFilter("ppt文档 (*.ppt, *.pptx )", "ppt", "pptx");
        FileNameExtensionFilter pdfFilter = new FileNameExtensionFilter("pdf文档 (*.pdf )", "pdf");
        
        return new FileFilter[]{imageFilter, videoFilter, docFilter, pptFilter, pdfFilter };
    }
    
    public static File showOpenDialog(Component c){
        return showOpenDialog(c, null, true,"选择文件", JFileChooser.FILES_ONLY);
    }
    
    public static File showOpenDialog(Component c, String dialogTitle, int FileSelectionMode){
        return showOpenDialog(c, null, true, dialogTitle, FileSelectionMode);
    }
    
    /**
     * 
     * @param c Dialog父类面板
     * @param fileFilters 文件过滤器
     * @param isAcceptAllFile 是否过滤所有文件
     * @param dialogTitle Dialog标题
     * @param FileSelectionMode 选择文件的方式（仅文件，仅目录，文件和目录）
     * @return 返回所选择文件
     */
    public static File showOpenDialog(Component c, FileFilter fileFilters[] ,boolean isAcceptAllFile, String dialogTitle, int FileSelectionMode){
        String openPath = System.getProperty("dir.path", "D:/");
        JFileChooser fileChooser = new JFileChooser(openPath);
        
        fileChooser.setAcceptAllFileFilterUsed(isAcceptAllFile);
        if(fileFilters != null){
            for(FileFilter fileFilter : fileFilters){
                fileChooser.addChoosableFileFilter(fileFilter);
            }
        fileChooser.setFileFilter(fileFilters[0]);
        }
        fileChooser.setFileSelectionMode(FileSelectionMode);
        fileChooser.setDialogTitle(dialogTitle);
        fileChooser.setApproveButtonText("选择");
        
        int returnValue = fileChooser.showOpenDialog(c);
        if(returnValue == JFileChooser.APPROVE_OPTION){
            File returnFile = fileChooser.getSelectedFile();
            System.getProperty("dir.path", returnFile.getPath());
            return returnFile;
        }        
        return null;
    }
}
