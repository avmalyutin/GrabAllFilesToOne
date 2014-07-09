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


public class MainWindow {

	private JFrame frame;
	
	private JFileChooser chooser;
	
	private JList<String> list;
	
	private File folder;
	
	ArrayList<File> currentFiles;
	List list_1;

	private DefaultListModel<String> model;

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
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnSelectFiles = new JButton("Select files");
		btnSelectFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				
				chooser = new JFileChooser(); 
			    chooser.setCurrentDirectory(new java.io.File("."));
			    chooser.setDialogTitle("Select folder");
			    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			    //
			    // disable the "All files" option.
			    //
			    chooser.setAcceptAllFileFilterUsed(false);
			    //    
			    if (chooser.showOpenDialog(chooser) == JFileChooser.APPROVE_OPTION) { 
			      System.out.println("getCurrentDirectory(): " 
			         +  chooser.getCurrentDirectory());
			      System.out.println("getSelectedFile() : " 
			         +  chooser.getSelectedFile());
			      folder = chooser.getSelectedFile();
			      }
			    else {
			    	folder = null;
			      System.out.println("No Selection ");
			      }
				
				
			    
			    list_1.removeAll();
			    
			    currentFiles = MainWindow.this.getAllFiles(folder);
			    
			    System.out.print(currentFiles.get(1).getName());
			    
			    for(File buffer : currentFiles){
			    	
			    	list_1.add(buffer.getName());	
			    }
			    
			    
				
			}
		});
		btnSelectFiles.setBounds(10, 11, 178, 23);
		frame.getContentPane().add(btnSelectFiles);
		
		JTextPane txtpnSelectedFiles = new JTextPane();
		txtpnSelectedFiles.setText("Selected files:");
		txtpnSelectedFiles.setBounds(10, 45, 89, 20);
		frame.getContentPane().add(txtpnSelectedFiles);
		
		list = new JList<String>();
		list.setBounds(10, 236, 116, -140);
		model = new DefaultListModel<String>();
		list.setModel(model);
		frame.getContentPane().add(list);
		
		JButton btnSaveIntoOne = new JButton("Save into one folder");
		btnSaveIntoOne.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				byte[] fileArray;
				
				String path = null;
				
					path = currentFiles.get(0).getParentFile()+"\\general.txt";
				
				
				System.out.println("Path:" + path);
				
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
		btnSaveIntoOne.setBounds(268, 228, 156, 23);
		frame.getContentPane().add(btnSaveIntoOne);
		
		list_1 = new List();
		list_1.setBounds(10, 71, 178, 159);
		frame.getContentPane().add(list_1);
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
		

            // read fills buffer with data and returns
            // the number of bytes read (which of course
            // may be less than the buffer size, but
            // it will never be more).
            
            int nRead = 0;
            while((nRead = inputStream.read(returnValue)) != -1) {
                // Convert to String so we can display it.
                // Of course you wouldn't want to do this with
                // a 'real' binary file.
               // System.out.println(new String(returnValue));
                total += nRead;
            }	

            // Always close files.
            inputStream.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

            System.out.println("Read " + total + " bytes");
		
		return returnValue;
		
	}
	
	
	
	
	private void writeToFile(FileOutputStream outputStream, byte[] array){
		try{
                        // write() writes as many bytes from the buffer
            // as the length of the buffer. You can also
            // use
            // write(buffer, offset, length)
            // if you want to write a specific number of
            // bytes, or only part of the buffer.
            outputStream.write(array);
		

            System.out.println("Wrote " + array.length + 
                " bytes");
        }
        catch(IOException ex) {
            System.out.println(
                "Error writing file '"
                +  "'");
            // Or we could just do this:
             ex.printStackTrace();
        }
		
		
		
		
	}
}
