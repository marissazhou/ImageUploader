package fileprocess;

import java.io.File;
import java.io.FilenameFilter;

public class FileValidation implements FilenameFilter {

	@Override
	public boolean accept(File dir, String name) {
		
		//the dir includes directory
		if(name.indexOf('.') == -1){
			return true;
		}
		
		return imgAndText(dir, name);
	}
	
	
	//all the files are acceptable
	public boolean files(File dir, String name){
		return true;
	}
	
	
	//all the image files and text files
	public boolean imgAndText(File dir, String name){
		
		if( (name.indexOf(".jpg") != -1 ) || (name.indexOf(".JPG") != -1) || 
				(name.indexOf(".jpeg") != -1) || (name.indexOf(".JPEG") != -1) 
				|| (name.indexOf(".txt") != -1) ){
			return true;
		}
		
		return false;
	}
	
	
	//all median photos from sensecam
	public boolean medPhotoFromSensecam(File dir, String name){
		if(dir.equals("640_480")){
			return imgAndText(dir, name);
		}
		return false;
	}

}
