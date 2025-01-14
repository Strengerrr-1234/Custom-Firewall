import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class CustomFirewall {
    // List to hold predefined rules
    private List<Rule> rules;

    public CustomFirewall() {
        rules = new ArrayList<>();
    }

    // Method to add rules
    public void addRule(String ipAddress, int port, boolean allow) {
        rules.add(new Rule(ipAddress, port, allow));
    }

    // Method to monitor and filter traffic
    public void startFirewall(int listeningPort) {
        try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {
            System.out.println("Firewall is active on port " + listeningPort);

            while (true) {
                Socket clientSocket = serverSocket.accept();
                String clientIP = clientSocket.getInetAddress().getHostAddress();
                int clientPort = clientSocket.getPort();

                if (isAllowed(clientIP, clientPort)) {
                    System.out.println("Connection allowed from " + clientIP + ":" + clientPort);
                    // Handle allowed connections here (e.g., pass data to another server)
                } else {
                    System.out.println("Connection denied from " + clientIP + ":" + clientPort);
                    clientSocket.close();
                }
            }
        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    // Method to check if a connection is allowed based on rules
    private boolean isAllowed(String ipAddress, int port) {
        for (Rule rule : rules) {
            if (rule.matches(ipAddress, port)) {
                return rule.isAllow();
            }
        }
        return false; // Default to deny if no rule matches
    }

    // Inner class to represent a rule
    private static class Rule {
        private String ipAddress;
        private int port;
        private boolean allow;

        public Rule(String ipAddress, int port, boolean allow) {
            this.ipAddress = ipAddress;
            this.port = port;
            this.allow = allow;
        }

        public boolean matches(String ipAddress, int port) {
            return this.ipAddress.equals(ipAddress) && this.port == port;
        }

        public boolean isAllow() {
            return allow;
        }
    }

    public static void main(String[] args) {
        CustomFirewall firewall = new CustomFirewall();
        
        // Adding rules: allow or deny specific IP and port
        firewall.addRule("192.168.1.10", 8080, true); // Allow specific IP and port
        firewall.addRule("192.168.1.11", 8081, false); // Deny specific IP and port

        // Start firewall on a specific port
        firewall.startFirewall(9999); // Listening on port 9999 for incoming connections
    }
}
