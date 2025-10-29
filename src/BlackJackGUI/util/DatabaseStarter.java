/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package BlackJackGUI.util;
import java.io.BufferedReader;
import java.io.InputStreamReader;
/**
 *
 * @author nicho
 */



public class DatabaseStarter {
    
    public static boolean ensureDerbyServerRunning() {
        if (isServerRunning()) {
            System.out.println("Derby network server is already running");
            return true;
        }
        
        return startDerbyServer();
    }
    
    private static boolean isServerRunning() {
        try {
            // Try to connect to the server
            Class.forName("org.apache.derby.jdbc.ClientDriver");
            java.sql.Connection testConn = java.sql.DriverManager.getConnection(
                "jdbc:derby://localhost:1527/BlackJack;create=true", "pdc", "pdc");
            testConn.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
    
    private static boolean startDerbyServer() {
        try {
            System.out.println("Starting Derby network server...");
            
            //find derby home
            String derbyHome = System.getProperty("derby.home");
            if (derbyHome == null) {
                //try common derby locations
                derbyHome = findDerbyHome();
            }
            
            if (derbyHome == null) {
                System.err.println("Could not find Derby installation");
                return false;
            }
            
            String startScript = derbyHome + "/bin/startNetworkServer";
            if (System.getProperty("os.name").toLowerCase().contains("win")) {
                startScript = derbyHome + "/bin/startNetworkServer.bat";
            }
            
            ProcessBuilder pb = new ProcessBuilder(startScript);
            pb.redirectErrorStream(true);
            Process process = pb.start();
            
            //wait a bit for server to start
            Thread.sleep(5000);
            
            if (isServerRunning()) {
                System.out.println("Derby network server started successfully");
                return true;
            } else {
                System.err.println("Failed to start Derby server");
                return false;
            }
            
        } catch (Exception e) {
            System.err.println("Error starting Derby server: " + e.getMessage());
            return false;
        }
    }
    
    private static String findDerbyHome() {
        // Check common Derby installation locations
        String[] possiblePaths = {
            System.getenv("DERBY_HOME"),
            "C:\\Program Files\\db-derby\\bin",
            "C:\\db-derby\\bin",
            "/usr/share/db-derby/bin",
            "/opt/db-derby/bin"
        };
        
        for (String path : possiblePaths) {
            if (path != null) {
                java.io.File dir = new java.io.File(path);
                if (dir.exists()) {
                    return dir.getParent();
                }
            }
        }
        return null;
    }
}
