import javax.swing.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.MalformedURLException;
import java.io.*;
import java.util.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;

public class MainWindow extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField URLField;

	public MainWindow() {
		this.setSize(400, 98);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		getContentPane().setLayout(null);
		
		URLField = new JTextField();
		URLField.setBounds(0, 23, 400, 28);
		getContentPane().add(URLField);
		URLField.setColumns(10);

		
		JLabel lblPutUrlHere = new JLabel("Put URL here");
		lblPutUrlHere.setBounds(6, 6, 388, 16);
		getContentPane().add(lblPutUrlHere);
		
		JButton btnDownload = new JButton("Download");
		btnDownload.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				List<String> list = new ArrayList();
				String title = "";
				String urlStr = URLField.getText();
				String userAgent = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:43.0) Gecko/20100101 Firefox/43.0";
				if(urlStr.isEmpty()) return;
				if(!(urlStr.substring(0,5).equals("https"))){
					return;
				}
				try {
					Document doc = Jsoup.connect(urlStr).userAgent(userAgent).get();
					Elements ele = doc.body().getAllElements();
					StringBuilder builder = new StringBuilder();
					for(Element element : ele){
						String eleStr = element.toString();
						if(eleStr.length() > 26){
							if(eleStr.substring(0,26).equals("<span class=\"mdCMN09Image\"")){
								list.add(eleStr.substring(eleStr.indexOf("https"), eleStr.lastIndexOf("png")+3));
								System.out.println(eleStr.substring(eleStr.indexOf("https"), eleStr.lastIndexOf("png")+3));
							}
						}
						if(element.ownText()==null)
							continue;
						builder.append(element.ownText()).append("\n");
					}
					title = doc.title();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				download(list, title);
			}
		});
		btnDownload.setBounds(283, 47, 117, 29);
		getContentPane().add(btnDownload);
	}
	
	public void download(List<String> l, String title){
		FileOutputStream out = null;
		InputStream in = null;
		for(String urlStr : l){
			URL url;
			try {
				url = new URL(urlStr);
				try {
					URLConnection conn = url.openConnection();
					in = conn.getInputStream();
					//urlStr.substring(78,89)
					File file = new File(title);
					file.mkdir();
					file = new File(title + "/" + urlStr.substring(urlStr.indexOf("stickers/") + 9,urlStr.lastIndexOf("png")+3));
					out = new FileOutputStream(file, false);
					int b;
					while((b = in.read()) != -1){
						out.write(b);
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			out.close();
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		MainWindow mWindow = new MainWindow();
		mWindow.setVisible(true);
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
