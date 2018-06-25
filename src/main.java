import java.awt.Font;
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

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;

import org.json.JSONObject;

import Models.Location;
import Models.Weather;

public class main 
{

	public static void main(String[] args) 
	{
		WeatherGetter wg = new WeatherGetter();
		// TODO Auto-generated method stub
		wg.CreateDatabase();
		wg.CreateGUI();

	}
}
