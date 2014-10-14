package uploader;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.UIManager;

import communication.AnnotationConnection;

import fileprocess.Folder;

import java.util.ArrayList;
import java.io.File;

public class Controller {

	private Model model;
	private View view;
	
	//constructor
	public Controller (Model model, View view) {
		
			this.model = model;
			this.view = view;
			
			this.view.addLogInListener(new LogInListener());
			this.view.addCancelListener(new CancelListener());
			this.view.addLogoutListener(new LogoutListener());
			this.view.addBrowseFoldersListener(new BrowseFoldersListener());
			this.view.addUpLoadListener(new UpLoadListener());
			this.view.addSettingListener(new SettingListener());
	}
	
//============ Action Listeners =================================================================================//	
	
	//Login Listener
	class LogInListener implements ActionListener {
	
			@Override
			public void actionPerformed(ActionEvent e) {
				
				logIn();
				
			}
	
	} //end ActionListener
	
	//Cancel Login Listener
	class CancelListener implements ActionListener {
		
		@Override
		public void actionPerformed(ActionEvent e) {
			
			cancelLogin();
			
		}
		
	}

	//Browse Folders Listener
	class BrowseFoldersListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			selectImagesFolder();
		}
		
	}
	
	//Upload Listener
	class UpLoadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			uploadFolders();
		}
		
	}
	
	//Setting Listener
	class SettingListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			startSetting();
		}
		
	}
	
	
	//Logout listener
	class LogoutListener implements ActionListener {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				logout();
				
			}//end method
	}

//============== Helper Methods =================================================================================//	

	//called when user clicks "Setting" button
	private void startSetting(){
		//Switch the view to the setting screen
		
		CardLayout cl = (CardLayout)(view.mainPanel.getLayout());
		view.setTitle(view.appName + " - Setting");
		cl.show(view.mainPanel, "setting");
	}
	
	
	//called when user clicks "Connect" button
	private void logIn() {
		
		//get input username
		String username = view.getUserName();
		
		//get input password
		char [] passChars = view.getPassword();
		String password = "";
		for(int i=0; i<passChars.length; i ++) {
			password += passChars[i];
		}
		
		
		//submit as the format of form
		AnnotationConnection connection = AnnotationConnection.getConnection();
		boolean connection_statuc = connection.login(username, password);
		
		
		
		if (!connection_statuc) {
			
			//Can display a message to the user on the login screen while connecting to the database
			view.loginMessages.setForeground(Color.red); 	//set the color of the message on the login screen
			
			//check failure reason
			if(connection.getStatus() == connection.CONNECT_FAIL){
				view.loginMessages.setText("check internet access please");
			}else{
				view.loginMessages.setText("incorrect username/password entered");
			}
			
		}else {//If the login details are correct, switch the view to the upload screen
			
			CardLayout cl = (CardLayout)(view.mainPanel.getLayout());
			view.setTitle(view.appName + " - Upload Album(s)");
			cl.show(view.mainPanel, "upload");
			
		}
		
		
		
//		if (username.equals("")) {
//			//Can display a message to the user on the login screen while connecting to the database
//			view.loginMessages.setForeground(Color.red); 	//set the color of the message on the login screen
//			view.loginMessages.setText("incorrect username/password entered"); 
//		}
//		
//		//If the login details are correct, switch the view to the upload screen
//		if (!username.equals("")) {
//			CardLayout cl = (CardLayout)(view.mainPanel.getLayout());
//			view.setTitle(view.appName + " - Upload Album(s)");
//			cl.show(view.mainPanel, "upload");
//			
//		}
		
		
		
	} // end method 
	
	//called when user clicks "Cancel" button
	private void cancelLogin() {
		
		//Close any connections etc.....
		//close the window
		view.dispose();
		
	}
	
	//called when user clicks "Logout" button
	private void logout () {
		
		//Close any connections etc.....
		//close the window
		view.dispose();
		
	}

	//called when user clicks "folder" button
	private void selectImagesFolder() {
		
		//remove option to create a new folder from the file chooser
		UIManager.put("FileChooser.readOnly", Boolean.TRUE); 

		JFileChooser openFile = new JFileChooser();
		
		openFile.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
		openFile.setApproveButtonText("Select");
		openFile.setAcceptAllFileFilterUsed(false);
	
		hideFileChooserComponents(openFile.getComponents());
		
		if (openFile.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
			view.foldersTextField.setText(openFile.getSelectedFile().toString());
		} 
		
    UIManager.put("FileChooser.readOnly", Boolean.FALSE);
	}
		
	private void uploadFolders() {
		
		String folderPath = view.foldersTextField.getText();
		
		if(!folderPath.equals("")) {
		
			//you need to validate this path before doing anything else!!!!!!!
			System.out.println("Selected Folder -> " + folderPath + "!!!");
			
			
			//get all image files form that folder
			Folder folder = new Folder(folderPath);
			ArrayList<File> images = folder.getTargetFiles();
			System.out.println("Controller-->uploadFolders:images size" + images.size());
			
			//start showing uploading progress
			new Thread(new UploadProgressThread(images.size())).start();
			
			
			//start uploading files
			new Thread(new UploadFilesThread(images, folder.getDeviceType())).start();

			
//			for(File img : images){
//				System.out.println(img.getName());
//			}
			
			
			
		} else {
			
			//give user feedback during upload process eg % complete
			view.uploadMessages.setForeground(Color.red);
			view.uploadMessages.setText("You have not selected a folder to upload");
			
		}
		
	}
	
	//showing upload progress in an independent thread
	class UploadProgressThread implements Runnable{

		
		private int filesCount;//the number of total files to be uploaded
		private int filesUploadedCount;//the number of files already uploaded
		
		private AnnotationConnection connection;
		
		
		public UploadProgressThread(int filesCount){
			this.filesCount = filesCount;
			filesUploadedCount = 0;
			
			connection = AnnotationConnection.getConnection();
		}
		
		
		@Override
		public void run() {
			System.out.println("Controller:UploadProgressThread-->filescount:" + filesCount + "   ");
			//show the progress of uploading
			while(filesUploadedCount < filesCount){
				filesUploadedCount = connection.getUploadedCount();
//				System.out.println("Controller:UploadProgressThread-->run: filesCount=" + filesCount + "     uploaded=" + filesUploadedCount);
				view.uploadProgress.setValue(filesUploadedCount * 100 / filesCount);//value is percentage
				view.uploadMessages.setText(filesUploadedCount * 100 / filesCount + "%  total=" + filesCount + "  uploaded=" + filesUploadedCount);
				view.uploadProgress.repaint();
				

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			//give user feedback during upload process eg % complete
			view.uploadMessages.setForeground(Color.BLUE);
			view.uploadMessages.setText("Upload sucessfully completed!!");
		}
		
		
		
//		@Override
//		public void run() {
//			System.out.println("Controller:UploadProgressThread-->filescount:" + filesCount + "   ");
//			//show the progress of uploading
//			for(int i = 0; i < 100; i ++){
//				filesUploadedCount = connection.getUploadedCount();
////				System.out.println("Controller:UploadProgressThread-->run: filesCount=" + filesCount + "     uploaded=" + filesUploadedCount);
//				view.uploadProgress.setValue(i);//value is percentage
//				view.uploadMessages.setText(i + "%");
//				view.uploadProgress.repaint();
//				
//
//				try {
//					Thread.sleep(50);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//			}
//		}
		
		
		
	}

	
	//uploading files in an independent thread
	class UploadFilesThread implements Runnable{

		private AnnotationConnection connection;
		private ArrayList<File> images;//files to be uploaded
		private String deviceType;
		
		public UploadFilesThread(ArrayList<File> images, String deviceType){
			this.images = images;
			this.deviceType = deviceType;
			connection = AnnotationConnection.getConnection();
		}
		
		@Override
		public void run() {
			
			connection.fileUpload(images, deviceType);
		}
		
	}
	
	
	//hides components on the file chooser
	private void hideFileChooserComponents(Component[] panels) {
	
    // traverse through the components
   for (int p=0; p< panels.length; p++) {
  	
      if (panels[p] instanceof JPanel) {
  
      	Component[] components = ((JPanel) panels[p]).getComponents();
      	
      	for (int c=0; c < components.length; c++) {
      		
      		 if(p==3 && c == 2) {
      			 //hides the "Files of Type" label and combo-box
      			 components[c].setVisible(false);
      		 }//end if
      		 
      	}//end c for loop
      	
      }//end if
      
   }//end p for loop
   
	}//end method
	
//===============================================================================================================//

}