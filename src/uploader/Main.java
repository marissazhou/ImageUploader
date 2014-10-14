// Author: Tenger
package uploader;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Main {
	
	public static void main(String[] args) {
		
        SwingUtilities.invokeLater(new Runnable () {
        	public void run () {
        		try {
							UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
						} catch (ClassNotFoundException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (InstantiationException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (UnsupportedLookAndFeelException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
        		View view = new View();
        	    Model model = new Model();
        	    @SuppressWarnings("unused")
							Controller controller = new Controller(model, view);
        	    view.setVisible(true);
        	}
        });
	    
    }
}
