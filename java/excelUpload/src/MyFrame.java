import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URLDecoder;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import Log.Log;

import sun.awt.Symbol;
import sun.rmi.runtime.NewThreadAction;

public class MyFrame extends JFrame implements ActionListener {
	/**
* 
*/
	private static final long serialVersionUID = 1L;
	private JPanel panel = new JPanel();
	private JFileChooser fileChooser = new JFileChooser(".");
	private JButton openButton = new JButton("open");
	private JButton saveButton = new JButton("save");
	private JButton startButton = new JButton("start");
	private JButton clearButton = new JButton("clear");
	public static JTextArea textArea = new JTextArea();
	private JTextField saveField = new JTextField();
	private JTextField openField = new JTextField();

	private JScrollPane scrollPane = new JScrollPane(textArea);
	public static String projectPath;
	public static String instructionStmts="First: click the open button" +
			" to open an dataset excel file"+"\n"+
			"Second: if you want save the insert statements, click the save button "+
			"and specify a path for it"+"\n"+
			"Third: click the start button, you can see the insert statements in the text"+
			"area after a while. And if you specify a path in the second step, a file containing the " +
			"sql insert statements will be created."+"\n"+
			"Fourth: click the clear button and the contents in text area" +
			" will be cleared."+"\n"+"Thank you.";
	public MyFrame() {
		// //
		String path = "";
		try {
			path = this.getClass().getProtectionDomain().getCodeSource()
					.getLocation().getPath();
			path = URLDecoder.decode(path, "utf-8");
			int beginIndex = 1;
			int endIndex = path.indexOf("bin");
			projectPath = path.substring(beginIndex, endIndex);
			// System.out.println(path);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			textArea.setText(e.toString());
		}
		// projectPath="";
		System.out.println(projectPath);
//		textArea.setText(textArea.getText()+projectPath);
		this.setTitle("Excel2Sql");
		this.setSize(500, 500);
		// this.setLocale(null);
		panel.setSize(500, 500);
		panel.setBounds(0, 0, 500, 500);
		this.getContentPane().add(panel);
		addComponent();
		openButton.addActionListener(this);
		saveButton.addActionListener(this);
		startButton.addActionListener(this);
		clearButton.addActionListener(this);
		this.setVisible(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		textArea.setText(instructionStmts);
	}

	private void addComponent() {

		panel.setLayout(null);
		panel.add(openButton);

		panel.add(openField);
		panel.add(saveButton);
		panel.add(saveField);
		panel.add(startButton);
		panel.add(scrollPane);
		panel.add(clearButton);
		textArea.setBounds(0, 0, 470, 390);
		// textArea.setEditable(false);
		// textArea.setLineWrap(true);
		// textArea.setAutoscrolls(true);
		openField.setBounds(10, 10, 300, 30);
		openButton.setBounds(320, 10, 80, 30);
		saveField.setBounds(10, 50, 300, 30);
		saveButton.setBounds(320, 50, 80, 30);
		startButton.setBounds(410, 10, 70, 30);
		clearButton.setBounds(410, 50, 70, 30);
		
		scrollPane.setBounds(10, 90, 470, 360);
		scrollPane.setPreferredSize(new Dimension(textArea.getWidth(), textArea
				.getHeight()));
		// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		// ScrollPane.setVerticalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scrollPane
				.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		// scrollPane.setEnabled(enabled)
	}

	public void actionPerformed(ActionEvent e) {
		JButton source = (JButton) e.getSource();
		// 触发JButton(此例仅设置有一个按钮，多按钮请自行更改)
		if (source == openButton) {
			openFile(openField);
		} else if (source == saveButton) {
			openFile(saveField);
		} else if (source==startButton){
			// 调用转换程序

			try {
				// openFile(saveField);
				textArea.setText(textArea.getText() + "in process....");
				// this.repaint();
				String sql = "";
				try {
					Excel2Database excel2Database = new Excel2Database(
							openField.getText());
//					textArea.setText(textArea.getText() + " here 1....");
					// System.out.println(openField.getText());
//					sql = excel2Database.createInsertStmt();
//					textArea.setText(textArea.getText() + " here 2....");
				} catch (Exception exception) {
//					textArea.a
					textArea.setText(textArea.getText()+exception.toString());
				}
//				textArea.setText(textArea.getText() + "done");
				textArea.setText(sql);
				if (saveField.getText().equals(""))
					return;
				Log log = new Log(saveField.getText(), false);
				log.writeLine(sql);

			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		else if(source==clearButton){
			textArea.setText("");
		}
	}

	public void openFile(JTextField field) {
		// fileChooser.setFileSelectionMode();
		fileChooser.setDialogTitle("open File");
		int ret = fileChooser.showOpenDialog(null);
		if (ret == JFileChooser.APPROVE_OPTION) {
			// 文件夹路径
			String path = fileChooser.getSelectedFile().getAbsolutePath();
			field.setText(path);
			System.out.println(fileChooser.getSelectedFile().getAbsolutePath());
		}

	}

	public static void main(String[] args) {
		Frame frame = new MyFrame();
		frame.show();
	}

}
