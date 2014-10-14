package communication;

import java.io.File;
import java.util.ArrayList;

public class AnnotationConnection {

	//singleton pattern
	private static AnnotationConnection ac = null;
	private AnnotationConnection(){
		this.connection = new HttpConnection(HOST);
		connection_status = CONNECT_NORMAL;
	}
	
	public static AnnotationConnection getConnection(){
		
		if(ac == null){
			ac = new AnnotationConnection();
		}
		
		return ac;
	}
	
	
	
	//instance from actual connection
	private HttpConnection connection;
	
	//connection status
	private String connection_status;
	
	public static String CONNECT_FAIL = "connection_failed";
	public static String CONNECT_NORMAL = "connection_normal";
	public static String LOGIN_SUCCESS = "login_success";
	public static String LOGIN_FAIL = "login_failed";
	
	
	//ip address and port of host
	public static String HOST = "localhost:9969/";;
	
	
	
	//relative url paths
	public static String LOGIN_URL = "accounts/login/";
	public static String FOLDER_UPLOAD_URL = "fileuploader";
//	public static String FOLDER_UPLOAD_URL_POST = "fileuploader";
	
	
	
	//the number of files uploaded per request
	public static int UPLOAD_LIMIT = 1;
	
	//files uploaded
	private int uploadedFilesCount = 0;
	
	public int getUploadedCount(){
		return uploadedFilesCount;
	}
	
	//get the status for the connection to the server
	public String getStatus(){
		return connection_status;
	}
	
	
	//get login page
	//and return validation code
	public String getLogin(){
		
		//page returned from host
		String response = connection.getHttpRequest(LOGIN_URL);
//		System.out.println("AnnotationConnection-->getLogin" + response);
		
		if(response == null){
			connection_status = CONNECT_FAIL;
			return null;
		}
		
		//parse response and get validation
		//get target line
		int start = response.indexOf("<form");
		int end = response.indexOf('\n', start + 1);
		
		//get target validation
		response = response.substring(start+1, end);
		start = response.indexOf('\'', response.indexOf('v') + 1);
		end = response.indexOf('\'', start + 1);
		
		
		return response.substring(start + 1, end);
	}
	
	
	
	//login and return if successful
	public boolean login(String username, String password){
		
		ArrayList<String> formpara = new ArrayList<String>();
		
		//get validation from login page
		String validation = getLogin();
		
		if(validation == null){
			connection_status = CONNECT_FAIL;
			return false;
		}
		
		//add parameters for form
		formpara.add("csrfmiddlewaretoken");formpara.add(validation);
		formpara.add("username"); formpara.add(username);
		formpara.add("password"); formpara.add(password);
		formpara.add("group"); formpara.add("3");
		
		//post the form to login
		
		boolean login_result = connection.httpPost(LOGIN_URL, formpara);
		
		
		if(login_result){//successfully login
			connection_status = LOGIN_SUCCESS;
		}else{
			connection_status = LOGIN_FAIL;
		}
		
		return login_result;
	}
	
	
	
	//get fileuploader page
	public String getFileuploader(){
		
		//page returned from host
		String response = connection.getHttpRequest(FOLDER_UPLOAD_URL);
//		System.out.println(response);
		
		//extract validation
		int start = response.indexOf("csrfmiddlewaretoken");
		int end = response.indexOf("/>", start + 1);
		
		//get target validation
		response = response.substring(start+1, end);
//		System.out.println("AnnotationConnection-->getFileuploader" + response);
//		System.out.println("AnnotationConnection-->getFileuploader, start:" + start + "  end:" + end + "  response:" + response);
		start = response.indexOf('\'', response.indexOf('v') + 1);
		end = response.indexOf('\'', start + 1);
//		System.out.println("AnnotationConnection-->getFileuploader, response:" + response.substring(start + 1, end));
		
//		System.out.println("AnnotationConnection-->getFileuploader:" + response.substring(start + 1, end));
		return response.substring(start + 1, end);
		
	}
	
	
	
	//upload all image files to the host and return if successful
	public boolean fileUpload(ArrayList<File> files, String deviceType){
		
		ArrayList<String> text = new ArrayList<String>();
		text.add("csrfmiddlewaretoken"); text.add(getFileuploader());
		text.add("Sign_In_As");text.add("Subject");
		
		text.add("uploader_device_type");
		if( deviceType.equals("autographer")){
			text.add("0");
		}else
		{
			text.add("1");
		}
		
		
		//considering the problem of large number of files
		//post multiple times to upload files
		
		int total_times = files.size()/UPLOAD_LIMIT;
		for( int times = 0; times < total_times; times ++ ){
			if( !connection.httpPost(FOLDER_UPLOAD_URL, text, new ArrayList<File>(files.subList(0, UPLOAD_LIMIT)) ) ){
				return false;
			}
			files.subList(0, UPLOAD_LIMIT).clear();
			uploadedFilesCount += UPLOAD_LIMIT;
			
		}	
		
				
		//last post
		if(!connection.httpPost(FOLDER_UPLOAD_URL, text, files)){
			return false;
		}
		
		uploadedFilesCount += files.size();
		
		
		return true;
	}
	
	
	
	public static void main(String[] args){
		AnnotationConnection ac = getConnection();
//		System.out.println(ac.getLogin());
		ac.login("tenger", "tenger");
//		
//		ac.getFileuploader();

		
		/////////////////////////////////////////
		//upload files
		
	}
}
