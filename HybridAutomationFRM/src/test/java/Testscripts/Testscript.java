package Testscripts;

import java.io.FileInputStream;
import java.util.Properties;

import org.testng.annotations.Test;

public class Testscript {
	
	@Test
	public void run_testscript() {
		

			String value = null;
			try {
				FileInputStream file = new FileInputStream("./src/main/resources/configurations/config.properties");
				Properties properties = new Properties();
				try {
					properties.load(file);
					value = properties.getProperty("url");

				} catch (Exception e) {
					System.out.println(e);

				}

			} catch (Exception e) {
				System.out.println(e);
			}
			System.out.println(value);
		}


	}


