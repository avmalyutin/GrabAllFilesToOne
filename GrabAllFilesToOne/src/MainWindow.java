import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.prefs.Preferences;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JTextPane;
import javax.swing.JCheckBox;


public class MainWindow {

	private JFrame frame;
	
	private final DefaultListModel<String> listModel = new DefaultListModel<String>();
	
	private JList<String> listOfFiles;
	
	private JFileChooser fileChooser;
	
	private JCheckBox chckbxGrabFilesFrom;
	
	private File folder;
	
	private ArrayList<File> currentFiles;
	
	private String LAST_USED_FOLDER = "LAST_USED_FOLDER";

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
				
				Preferences prefs = Preferences.userRoot().node(getClass().getName());
				
			    fileChooser.setCurrentDirectory(new java.io.File(prefs.get(LAST_USED_FOLDER, new File(".").getAbsolutePath())));
			    fileChooser.setDialogTitle("Select folder");
			    fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			
			    fileChooser.setAcceptAllFileFilterUsed(false);
			    //    
			    if (fileChooser.showOpenDialog(fileChooser) == JFileChooser.APPROVE_OPTION) { 
			      System.out.println("getCurrentDirectory(): " 
			         +  fileChooser.getCurrentDirectory());
			      System.out.println("getSelectedFile() : " 
			         +  fileChooser.getSelectedFile());
			      folder = fileChooser.getSelectedFile();
			      
			      prefs.put(LAST_USED_FOLDER, folder.getAbsolutePath());
			      
			      }
			    else {
			    	folder = null;
			    	System.out.println("No Selection ");
			      }
				
				
			    listModel.removeAllElements();
			    currentFiles = new ArrayList<File>();
			    
			    if(MainWindow.this.chckbxGrabFilesFrom.isSelected()){
				    MainWindow.this.getAllFilesWithSubfolders(folder.getAbsolutePath(), currentFiles);
			    }
			    else{
			    	MainWindow.this.getAllFiles(currentFiles);
			    }
			    
			    
			    System.out.println("You shall not pass: " + folder.getAbsolutePath());
			    for(File buffer : currentFiles){
			    	listModel.addElement(buffer.getName() + " in " + buffer.getAbsolutePath());
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
		listOfFiles.setBounds(10, 98, 264, 219);
		frame.getContentPane().add(listOfFiles);
		
		chckbxGrabFilesFrom = new JCheckBox("Grab files from subfolders?");
		chckbxGrabFilesFrom.setBounds(10, 74, 264, 23);
		frame.getContentPane().add(chckbxGrabFilesFrom);
	}
	
	
	
	
	private void getAllFiles(ArrayList<File> files){
		for(File buffer: folder.listFiles()){
			if(buffer.isFile())
				files.add(buffer);			
		}
	}
	
	
	private void getAllFilesWithSubfolders(String directoryName, ArrayList<File> files) {
	    File directory = new File(directoryName);

	    // get all the files from a directory
	    File[] fList = directory.listFiles();
	    if(fList != null)
		    for (File file : fList) {
		        if (file.isFile()) {
		            files.add(file);
		        } else if (file.isDirectory()) {
		        	getAllFilesWithSubfolders(file.getAbsolutePath(), files);
		        }
		    }
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
