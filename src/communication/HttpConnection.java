package communication;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.io.IOUtils;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.HttpHostConnectException;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;


public class HttpConnection {

	//host url
	private String host;
	
	private CloseableHttpClient httpclient;
	
	public HttpConnection(String host){
		
		this.host = host;
		
		httpclient = HttpClients.createDefault();
	}
	
	
	//GET request connection
	//path is relative url path
	public String getHttpRequest(String path){
		
		String result = null;
		
		try {
			
			URI uri = new URIBuilder().setScheme("http").setHost(host).setPath(path).build();
			
			
			//post request and obtain response from host
			CloseableHttpResponse response = httpclient.execute(new HttpGet(uri));
			
			
			HttpEntity entity = response.getEntity();
			result = IOUtils.toString(entity.getContent(), "UTF-8");
			
			//close socket
			response.close();
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch(HttpHostConnectException e){
			System.out.println("HttpConnection:getHttpRequest-->Connect problem");
//			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		//if result is null, then there is connection problem
		return result;
	}
	
	
	
	
	
	//POST form through http request
	//the form has only text
	public boolean httpPost(String path, ArrayList<String> pairs){
	
		boolean result = false;
		
		try {
			
			URI uri = new URIBuilder().setScheme("http").setHost(host).setPath(path).build();
			
			
			//construct form
			List<NameValuePair> formparams = new ArrayList<NameValuePair>();
			
			Iterator<String> iter = pairs.iterator();
			while(iter.hasNext()){
				formparams.add( new BasicNameValuePair(iter.next(), iter.next()) );
			}
			
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(formparams, Consts.UTF_8);
			
			//construct request
			HttpPost httppost = new HttpPost(uri);
			httppost.setEntity(formEntity);
			
			//post and obtain response
			CloseableHttpResponse response = httpclient.execute(httppost);
			
			
			//read from response
			InputStream in = response.getEntity().getContent();
			
//			System.out.println(IOUtils.toString(in, "UTF-8"));
			if( IOUtils.toString(in, "UTF-8").length() == 0 ){
				result = true;
			}
			
			response.close();
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}
	
	
	//POST form through http request
	//the form contain text and files
	public boolean httpPost(String path, ArrayList<String> pairs, ArrayList<File> files){
	
		boolean result = false;
		
		try {
			
			URI uri = new URIBuilder().setScheme("http").setHost(host).setPath(path).build();
			
			
			//construct form
			MultipartEntityBuilder builder = MultipartEntityBuilder.create();
			builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
			
			
			//add text to form
			Iterator<String> iter_text = pairs.iterator();
			while(iter_text.hasNext()){
				builder.addTextBody(iter_text.next(), iter_text.next());
			}
			
			
			//add files to form
			Iterator<File> iter_file = files.iterator();
			while(iter_file.hasNext()){
				builder.addPart("file", new FileBody(iter_file.next()));
//				File aFile = iter_file.next();
//				System.out.println(aFile.getPath());
//				
//				
//				//put only the date as filename
//				String filePath = aFile.getPath();
//				int start = filePath.lastIndexOf('/');
//				int end = filePath.indexOf('.');
//				
//				filePath = filePath.substring(start + 1, end);
//				
//				start = filePath.indexOf('_');
//				filePath = filePath.substring(filePath.indexOf('_', start + 1) + 1);
//				
//				System.out.println(filePath.substring(0, 4) + "/" + filePath.substring(4, 6) + "/" + filePath.substring(6, 8) + "/" + filePath.substring(8));
//				builder.addPart("file", new FileBody(aFile, ContentType.DEFAULT_BINARY, filePath.substring(0, 3) + "/" + filePath.substring(4, 5) + "/" + filePath.substring(6) ));
			}
			
			
			//construct request
			HttpPost httppost = new HttpPost(uri);
			httppost.setEntity(builder.build());
			
			
			//post and obtain response
			CloseableHttpResponse response = httpclient.execute(httppost);
			
			
			//read from response
			InputStream in = response.getEntity().getContent();
			
//			System.out.println(IOUtils.toString(in, "UTF-8"));
			
			if( IOUtils.toString(in, "UTF-8").length() == 0 ){
				result = true;
			}
			
			response.close();
			
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return result;
	}

	
}
