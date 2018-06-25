import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.json.JSONObject;

import Models.Location;
import Models.Weather;


public class WeatherGetter {

	private JLabel image;


void CreateGUI()
{
	JFrame mainWindow = new JFrame("Weather task");
	mainWindow.setSize(500, 500);
	
	JLabel label = new JLabel("Please enter postcode");
	label.setSize(200, 50);
	label.setBounds(100, 10, 200, 20);
	
	mainWindow.add(label);
	JTextArea postCode = new JTextArea();
	postCode.setBounds(100, 30, 200, 20);
	mainWindow.add(postCode);
	
	JButton submitButton = new JButton("Get Weather");
	submitButton.setBounds(100, 50, 200, 20);
	mainWindow.add(submitButton);
	
	image = new JLabel("");
	mainWindow.add(image);
	
	submitButton.addActionListener(new ActionListener()
	{
	    @Override
	    public void actionPerformed(ActionEvent e)
	    {
	      GetWeather(postCode.getText());
	    }
	});
	
	mainWindow.setVisible(true);
}

private void GetWeather(String postcode)
{
	
	Location enteredLocation = GetLocationFromPostCode(postcode);
	try
	{
		String json = SendGET("https://api.darksky.net/forecast/d67f13b1588f29805f6a7ccedabe098c/" + enteredLocation.Latitude + "," + enteredLocation.Longitude);
		insert(json);
		Weather weather = new Weather(GetLatest());
	image.setIcon((new ImageIcon("C:/Users/Andrew/Desktop/DarkSky-icons/DarkSky-icons/PNG/" + weather.icon + ".png")));
		
	System.out.println("output: " + weather.icon + " " + weather.summary);
	}
	catch(Exception e)
	{
		System.out.println("Main: " + e.getMessage());
	}
}

private static String SendGET(String url) throws IOException {
	URL obj = new URL(url);
	HttpURLConnection con = (HttpURLConnection) obj.openConnection();
	con.setRequestMethod("GET");
	con.setRequestProperty("User-Agent", "Mozilla/5.0");
	int responseCode = con.getResponseCode();
	//System.out.println("GET Response Code :: " + responseCode);
	if (responseCode == HttpURLConnection.HTTP_OK) { // success
		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		//System.out.println(response.toString());
		return response.toString();
	} else {
		//System.out.println("GET request not worked");
		return "Unexpected error";
	}
}

private static Location GetLocationFromPostCode(String postcode)
{	
	try
	{	
	String jsonstring = SendGET("http://api.postcodes.io/postcodes/" + postcode);
	JSONObject json = new JSONObject(jsonstring);
	json = json.getJSONObject("result");
	Location l = new Location();
	l.Latitude = json.getDouble("latitude");
	l.Longitude = json.getDouble("longitude");
	return l;
	}
	catch(Exception e)
	{
		System.out.println("GetLocatioFromPostCode: " + e.getMessage());
	}
	return null;
}

private static String GetLatest(){
    String sql = "SELECT * FROM weather ORDER BY id DESC LIMIT 1;";
    
    try (Connection conn = connect();
         Statement stmt  = conn.createStatement();
         ResultSet rs    = stmt.executeQuery(sql)){
        
    	 return rs.getString("json");
    } 
    catch (SQLException e) 
    {
        System.out.println(e.getMessage());
    }
    return null;
}

static void CreateDatabase()
{
	String url = "jdbc:sqlite:C:/Users/Andrew/Desktop/JamesTask/weather.db";
    
    // SQL statement for creating a new table
    String sql = "CREATE TABLE IF NOT EXISTS weather (\n"
            + "	id integer PRIMARY KEY,\n"
            + "	json text NOT NULL\n"
            + ");";
    
    try (Connection conn = DriverManager.getConnection(url);
            Statement stmt = conn.createStatement()) 
    {
        stmt.execute(sql);
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }

}

private static Connection connect() 
{
    // SQLite connection string
    String url = "jdbc:sqlite:C:/Users/Andrew/Desktop/JamesTask/weather.db";
    Connection conn = null;
    try {
        conn = DriverManager.getConnection(url);
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
    return conn;
}

private static void insert(String json) 
{
    String sql = "INSERT INTO weather(json) VALUES(?)";

    try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setString(1, json);
        pstmt.executeUpdate();
    } catch (SQLException e) {
        System.out.println(e.getMessage());
    }
}
}
