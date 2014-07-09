import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import java.awt.List;
import javax.swing.AbstractListModel;


public class MainWindow {

	private JFrame frame;
	
	private final DefaultListModel<String> listModel = new DefaultListModel<String>();
	
	private JList<String> listOfFiles;
	
	private JFileChooser fileChooser;
	
	private File folder;
	
	private ArrayList<File> currentFiles;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindow window = new MainWindow();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainWindow() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 300, 400);
		frame.setResizable(false);
		frame.setTitle("GrabAllFilesToOne");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnSelectFiles = new JButton("Select folder");
		btnSelectFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				fileChooser = new JFileChooser(); 
			    fileChooser.setCurrentDirectory(new java.io.File("."));
			    fileChooser.setDialogTitle("Select folder");
			    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    //
			    // disable the "All files" option.
			    //
			    fileChooser.setAcceptAllFileFilterUsed(false);
			    //    
			    if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) { 
			      System.out.println("getCurrentDirectory(): " 
			         +  fileChooser.getCurrentDirectory());
			      System.out.println("getSelectedFile() : " 
			         +  fileChooser.getSelectedFile());
			      folder = fileChooser.getSelectedFile();
			      }
			    else {
			    	folder = null;
			    	System.out.println("No Selection ");
			      }
				
				
			    listModel.removeAllElements();
			    currentFiles = MainWindow.this.getAllFiles(folder);			    
			    for(File buffer : currentFiles){
			    	listModel.addElement(buffer.getName());
			    }
			    
			    listOfFiles.setModel(listModel);
				
			}
		});
		btnSelectFiles.setBounds(10, 11, 264, 23);
		frame.getContentPane().add(btnSelectFiles);
		
		JTextPane txtpnSelectedFiles = new JTextPane();
		txtpnSelectedFiles.setText("Files in folder:");
		txtpnSelectedFiles.setEditable(false);
		txtpnSelectedFiles.setBounds(10, 45, 264, 20);
		frame.getContentPane().add(txtpnSelectedFiles);
		
		JButton btnSaveIntoOne = new JButton("Save into one file");
		btnSaveIntoOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
								
				String path = null;
				path = currentFiles.get(0).getParentFile()+"\\generalFile.txt";				
				File generalFile = new File(path);
				
				
				FileOutputStream outputStream = null;
				try {
					outputStream = new FileOutputStream(generalFile);
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				for(File file1 : currentFiles){
									
					 MainWindow.this.writeToFile(outputStream, MainWindow.this.readFile(file1));
					 String bytes = "\n\n\n\n";
					 byte[] buffer1 = bytes.getBytes();
					 MainWindow.this.writeToFile(outputStream, buffer1);
					
				}
				
				try {
					outputStream.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		btnSaveIntoOne.setBounds(10, 328, 264, 23);
		frame.getContentPane().add(btnSaveIntoOne);
		
		listOfFiles = new JList<String>();
		listOfFiles.setBounds(10, 76, 264, 241);
		frame.getContentPane().add(listOfFiles);
	}
	
	
	
	
	private ArrayList<File> getAllFiles(File folder){
		ArrayList<File> buf = new ArrayList<File>();
		for(File buffer: folder.listFiles()){
			buf.add(buffer);			
		}
		
		return buf;
	}
	
	
	private byte[] readFile(File file){

		byte [] returnValue = new byte[(int) file.length()];
		
		FileInputStream inputStream;
		int total = 0;
		try {
			inputStream = new FileInputStream(file);
            int nRead = 0;
            while((nRead = inputStream.read(returnValue)) != -1) {
                total += nRead;
            }	

            inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return returnValue;
		
	}
	
	
	
	
	private void writeToFile(FileOutputStream outputStream, byte[] array){
		try{
            outputStream.write(array);
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
	}
}
