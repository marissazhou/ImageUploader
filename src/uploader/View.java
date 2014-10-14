package uploader;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

public class View extends JFrame{
	
	private static final int MAIN_PANEL_WIDTH = 400;
	private static final int MAIN_PANEL_HEIGHT = 240;
	
	private JTextField usernameField;
	private JPasswordField passwordField;
	
	
	JFrame frame;
	JPanel mainPanel, loginPanel, upLoadPanel, settingPanel;
	JTextField foldersTextField;
	JButton logInButton, cancelButton, logoutButton, browseFoldersButton, uploadButton, settingButton;
	JLabel usernameLabel, passwordLabel, loginMessages, uploadMessages;
	JProgressBar uploadProgress;
	
	String appName = "MemLog Lifelog Uploader";
	
	public View () {
		createView();
	}
	
	private void createView () {
		createSettingPanel();
		createLoginPanel();
		createUploadPanel();
		createMainPanel();
	}
	
	private void createLoginPanel () {
		
		loginPanel = new JPanel(new FlowLayout (FlowLayout.CENTER, 20, 20));
		
		JPanel loginPanelForm = new JPanel(new GridBagLayout());	
		loginPanelForm.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH-40, MAIN_PANEL_HEIGHT-40));
		loginPanelForm.setBorder(BorderFactory.createTitledBorder(""));
		
		JLabel iconLabel = new JLabel ();
		
		//might be a problem for windows
		ImageIcon icon = new ImageIcon("images/login.png");
		
		GridBagConstraints gbc = new GridBagConstraints ();
		
		loginPanel.add(loginPanelForm);
		
		iconLabel.setIcon(icon); 
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridheight = 4;
		gbc.insets = new Insets(0,0,0,20); 
		gbc.fill = GridBagConstraints.VERTICAL;
		loginPanelForm.add(iconLabel, gbc);
		
		usernameLabel = new JLabel("Username:");
		usernameLabel.setPreferredSize(new Dimension(80, 25));
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.gridheight = 1;
		gbc.insets = new Insets(0,0,0,10); 
		usernameLabel.setFont (usernameLabel.getFont ().deriveFont (12.0f));
		loginPanelForm.add(usernameLabel,gbc);
		
		usernameField = new JTextField(10);
		gbc.gridx = 2;
		gbc.gridy = 0;
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.insets = new Insets(0,0,0,0);
		usernameField .setFont (usernameField .getFont ().deriveFont (12.0f));
		loginPanelForm.add(usernameField, gbc);
		
		passwordLabel = new JLabel("Password: ");
		passwordLabel.setPreferredSize(new Dimension(80, 25));
		gbc.gridx = 1;
		gbc.gridy = 1;
		gbc.insets = new Insets(10,0,0,10);
		loginPanelForm.add(passwordLabel,gbc);
		
		passwordField = new JPasswordField(10);
		passwordField .setFont (passwordField .getFont ().deriveFont (12.0f));
		gbc.fill = GridBagConstraints.VERTICAL;
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.insets = new Insets(10,0,0,0);
		loginPanelForm.add(passwordField, gbc);
		
		logInButton = new JButton ("Connect");
		logInButton.setPreferredSize(new Dimension (100, 25));
		gbc.gridx = 1;
		gbc.gridy = 2;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(20,0,0,0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		loginPanelForm.add(logInButton, gbc);
		
		cancelButton = new JButton ("Cancel");
		cancelButton.setPreferredSize(new Dimension (100, 25));
		gbc.gridx = 1;
		gbc.gridy = 3;
		gbc.insets = new Insets(10,0,0,0);
		loginPanelForm.add(cancelButton, gbc);
		
		settingButton = new JButton ("Setting");
		settingButton.setPreferredSize(new Dimension (100, 25));
		gbc.gridx = 1;
		gbc.gridy = 4;
		gbc.gridwidth = 2;
		gbc.insets = new Insets(10,0,0,0);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		loginPanelForm.add(settingButton, gbc);
		
		loginMessages = new JLabel (" ");
		loginMessages.setFont(loginMessages .getFont ().deriveFont (14.0f));
		loginMessages.setHorizontalAlignment( SwingConstants.CENTER );
		loginMessages.setForeground(Color.blue);
		gbc.fill = GridBagConstraints.HORIZONTAL;
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.insets = new Insets(10,0,0,0);
		gbc.gridwidth = 3;
		loginPanelForm.add(loginMessages, gbc);
		
	}
	
	private void createUploadPanel () {
		
		upLoadPanel = new JPanel(new FlowLayout (FlowLayout.CENTER, 10, 10));
		
		JPanel uploadPanelForm = new JPanel(new GridBagLayout());
		uploadPanelForm.setBorder(BorderFactory.createTitledBorder(""));
		uploadPanelForm.setPreferredSize(new Dimension(MAIN_PANEL_WIDTH-20, MAIN_PANEL_HEIGHT-20));
		
		GridBagConstraints gbc = new GridBagConstraints();
		
		JLabel instructions = new JLabel("Select / Enter location of folder to be uploaded:");
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 3;
		gbc.fill = GridBagConstraints.BOTH;
		uploadPanelForm.add(instructions, gbc);
		
		foldersTextField = new JTextField(20);
		gbc.gridx = 0;
		gbc.gridy = 1;
		gbc.gridwidth = 2;
		gbc.fill = GridBagConstraints.BOTH;
		uploadPanelForm.add(foldersTextField, gbc);
		
		Icon icon = UIManager.getIcon("FileView.directoryIcon");
		browseFoldersButton = new JButton(icon);
		gbc.gridx = 2;
		gbc.gridy = 1;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(0,5,0,0);
	    uploadPanelForm.add(browseFoldersButton, gbc);
		
		uploadButton = new JButton("Upload Images");
		gbc.gridx = 0;
		gbc.gridy = 2;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(15,0,0,0); 
		gbc.fill = GridBagConstraints.HORIZONTAL;
		uploadPanelForm.add(uploadButton, gbc);
		
		uploadMessages = new JLabel (" ");
		uploadMessages.setFont(loginMessages .getFont ().deriveFont (14.0f));
		uploadMessages.setHorizontalAlignment( SwingConstants.CENTER );
		gbc.gridx = 0;
		gbc.gridy = 3;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(5,0,0,0);
		uploadPanelForm.add(uploadMessages, gbc);
		
		uploadProgress = new JProgressBar(0, 100);
		gbc.gridx = 0;
		gbc.gridy = 4;
		gbc.gridwidth = 3;
		gbc.insets = new Insets(5,0,0,0);
		uploadPanelForm.add(uploadProgress, gbc);
		
		logoutButton = new JButton ("Logout");
		gbc.gridx = 2;
		gbc.gridy = 5;
		gbc.gridwidth = 1;
		gbc.insets = new Insets(50,0,0,0);
		uploadPanelForm.add(logoutButton, gbc);
		 
		upLoadPanel.add(uploadPanelForm);
		

		this.pack();
	}
	
	
	
	private void createSettingPanel(){
		
		settingPanel = new JPanel(new FlowLayout (FlowLayout.CENTER, 10, 10));

		

		this.pack();
	}
	
	
	
	
	private void createMainPanel() {		
		
		mainPanel = new JPanel(new CardLayout());
		getContentPane().add(mainPanel);
		setTitle(appName + " - Login");
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		
		mainPanel.add(loginPanel,"login");
		mainPanel.add(upLoadPanel, "upload");
		mainPanel.add(settingPanel, "setting");
		
		pack();
		setLocationRelativeTo(null);
		
}
	
//====================================== Action Listeners =======================================================//	
	
	void addLogInListener(ActionListener listenForLogIn) {
		logInButton.addActionListener(listenForLogIn);	
	}
	
	void addCancelListener(ActionListener listenForCancel) {
		cancelButton.addActionListener(listenForCancel);	
	}
	
	void addLogoutListener(ActionListener listenForLogout) {
		logoutButton.addActionListener(listenForLogout);	
	}
	
	void addBrowseFoldersListener(ActionListener listenForBrowseFoldersButton) {
			browseFoldersButton.addActionListener(listenForBrowseFoldersButton);
	}
	
	void addUpLoadListener(ActionListener listenForUpLoadButton) {
			uploadButton.addActionListener(listenForUpLoadButton);
	}
	
	void addSettingListener(ActionListener listenForSettingButton){
		settingButton.addActionListener(listenForSettingButton);
	}
	
	void addUploadProgressListener(ActionListener listenForUploadProgress){
//		uploadProgress.addActionListener(listenForUploadProgress);
	}
//====================================== Helper Methods =======================================================//	

	protected String getUserName() {
		return usernameField.getText();
	}
	
	protected char [] getPassword() {
		return passwordField.getPassword();
	}
	
}