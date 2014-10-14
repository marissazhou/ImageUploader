package fileprocess;

import java.io.File;
import java.util.ArrayList;
import java.util.Stack;


public class Folder {

	private String folderPath;
	
	private ArrayList<File> targetFiles = new ArrayList<File>();
	private Stack<File> workFiles = new Stack<File>();
	
	public Folder(String folderPath){
		this.folderPath = folderPath;
	}
	
	
	//device type
	public static String AUTOGRAPHER = "autographer";
	public static String OTHER_DEVICE = "other_device";
	
	private String deviceType;
	
	public String getDeviceType(){
		//after getStructure
		return deviceType;
	}
	
	public ArrayList<File> getConcreteFiles() {

		// get base path
		File basepath = new File(this.folderPath);

		// get filter
		FileValidation validation = new FileValidation();

		// it must be a directory
		if (!basepath.isDirectory()) {
			System.out.println("basepath is not a directory!");
		}

		workFiles.push(basepath);

		// continue until all file is desired
		while (!workFiles.empty()) {

			File workfile = workFiles.peek();

			// erase it
			workFiles.pop();
			
			
			// file or directory
			if (workfile.isDirectory()) {
				
				for (File subFile : workfile.listFiles(validation)) {
					workFiles.push(subFile);
				}
				

			} else {
				// write to combined file
				//directory is not acceptable
				targetFiles.add(workfile);
			}
		}

		// clear
		workFiles = null;
		
		return targetFiles;
	}
	
	
	public void delete(File file) {

		//recursion
		if(file.isDirectory()){
			
			for(File subFile : file.listFiles()){
				delete(subFile);
			}
			
			file.delete();
		}else
		{
			file.delete();
		}
		
	}
	
	
	public void delete() {
		// get base path
		File basepath = new File(this.folderPath);

		// it must be a directory
		if (!basepath.isDirectory()) {
			System.out.println("basepath is not a directory!");
		}

		delete(basepath);
	}
	
	public void getStructure(){
		//after getTargetFiles
		for(File file : targetFiles){
			
			if( file.getName().indexOf(".txt") == - 1 ){
				//found img files
				if(file.getParent().indexOf("256_192") != - 1 || file.getParent().indexOf("640_480") != -1){
					deviceType =  AUTOGRAPHER;
					return;
				}else
				{
					
					for( File subFile : file.getParentFile().listFiles() ){
						if(subFile.getName().indexOf("256_192") != -1 || subFile.getName().indexOf("640_480") != -1){
							deviceType =  AUTOGRAPHER;
							return;
						}
					}
				}
				
				
			}
		}
		
		deviceType =  OTHER_DEVICE;
	}
	
	public ArrayList<File> getTargetFiles(){
		getConcreteFiles();
		getStructure();
		
		if( AUTOGRAPHER == deviceType ){
			for( int i = 0; i < targetFiles.size(); i ++ ){
				File concreteFile = targetFiles.get(i);
				System.out.println("Folder-->getTargetFiles:" + concreteFile.getParent());
				if( concreteFile.getName().indexOf(".txt") == -1 && !concreteFile.getParent().contains("640_480")){
					targetFiles.remove(i);
					i --;
				}
			}
		}
		
		return targetFiles;
	}

}
