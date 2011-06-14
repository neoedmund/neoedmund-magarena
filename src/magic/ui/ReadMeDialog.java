package magic.ui;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import magic.data.ReadMeFile;

import java.awt.BorderLayout;

public class ReadMeDialog extends JDialog {
	private static final long serialVersionUID = 1L;
	private static final String README_FILENAME="README.txt";
	private ReadMeFile file;
	private JTextArea readMeTextArea;
	private JScrollPane readMeScrollPane;
	
	public ReadMeDialog(final MagicFrame frame){
		super(frame,true);
		this.setLayout(new BorderLayout());
		this.setTitle("ReadMe");
		this.setSize(600,505);
		this.setLocationRelativeTo(frame);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		final JPanel readMePanel=new JPanel();
		readMePanel.setLayout(null);
		
		readMeTextArea = new JTextArea();
		readMeTextArea.setEditable(false);
		displayReadMe();
		
		readMeScrollPane = new JScrollPane(readMeTextArea);
		readMeScrollPane.setSize(this.getSize().width-5,this.getSize().height);
		readMePanel.add(readMeScrollPane,BorderLayout.CENTER);	
		
		this.add(readMePanel);
		this.setVisible(true);	
	}
	
	public void displayReadMe(){
		file = new ReadMeFile(README_FILENAME);
		try{
			readMeTextArea.setText(file.getDataFromFile());
		}catch(Exception ex){
			System.err.print(ex.getMessage());
		}
	}
}
