# Custom-Firewall
A custom firewall in Java to monitor and filter traffic based on predefined rules involves working with low-level networking libraries, such as java.net. How to create such a firewall using the Java library to filter traffic based on rules like IP address and port number.


## 1. Class Overview
* `CustomFirewall`: The main class that implements the firewall logic.
    * Holds a list of traffic filtering rules.
    * Provides methods to add rules, monitor traffic, and filter connections based on the rules.


## 2. Rules Management
#### Rule Class (Inner Class)
```
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
```
* **Purpose**: Represents a rule that defines whether traffic from a specific IP and port should be allowed or denied.
    * `ipAddress`: Specifies the IP address to match.
    * `port`: Specifies the port number to match.
    * `allow`: Boolean flag indicating whether the traffic is allowed (`true`) or denied (`false`).
* **Methods**:
    * `matches(String ipAddress, int port)`: Checks if the given IP and port match this rule.
    * `isAllow()`: Returns whether this rule allows traffic.

### List of Rules
```
private List<Rule> rules;

public CustomFirewall() {
    rules = new ArrayList<>();
}
```
* The firewall uses a `List<Rule>` to store all predefined rules.
* `CustomFirewall()` constructor initializes this list.
```
Adding Rules

public void addRule(String ipAddress, int port, boolean allow) {
    rules.add(new Rule(ipAddress, port, allow));
}
```
* Adds a new rule to the list by creating a Rule object with the specified IP, port, and allow/deny decision.


## 3. Traffic Monitoring
`startFirewall` Method
```
public void startFirewall(int listeningPort) {
    try (ServerSocket serverSocket = new ServerSocket(listeningPort)) {
        System.out.println("Firewall is active on port " + listeningPort);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            String clientIP = clientSocket.getInetAddress().getHostAddress();
            int clientPort = clientSocket.getPort();

            if (isAllowed(clientIP, clientPort)) {
                System.out.println("Connection allowed from " + clientIP + ":" + clientPort);
            } else {
                System.out.println("Connection denied from " + clientIP + ":" + clientPort);
                clientSocket.close();
            }
        }
    } catch (IOException e) {
        System.err.println("Error: " + e.getMessage());
    }
}
```

* **Overview**: The core of the firewall, responsible for monitoring and filtering traffic.
* **Steps**:
  1. **Start Listening**: A `ServerSocket` listens for incoming connections on the specified `listeningPort`.
  2. **Accept Connections**: The `accept()` method waits for an incoming connection and returns a `Socket` object representing the client connection.
  3. **Get Client Details**:
      * `clientSocket.getInetAddress().getHostAddress()`: Retrieves the IP address of the client.
      * `clientSocket.getPort()`: Retrieves the port number used by the client.
  5. **Filter Traffic**:
      *  Calls `isAllowed()` to check if the connection is allowed based on predefined rules.
      *  Logs whether the connection is allowed or denied.
      *  Closes the connection (`clientSocket.close()`) if denied.


## 4. Traffic Filtering
`isAllowed` Method
```
private boolean isAllowed(String ipAddress, int port) {
    for (Rule rule : rules) {
        if (rule.matches(ipAddress, port)) {
            return rule.isAllow();
        }
    }
    return false; // Default to deny if no rule matches
}
```
* **Purpose**: Determines whether a connection is allowed.
* **Logic**:
    1. Loops through all `Rule` objects in the `rules` list.
    2. Checks if the client's IP and port match the `rule using rule.matches(ipAddress, port)`.
    3. If a match is found, returns the rule's `allow` value.
    4. If no rule matches, denies the connection by returning `false`.


## 5. Main Method
```
public static void main(String[] args) {
    CustomFirewall firewall = new CustomFirewall();
    
    // Adding rules: allow or deny specific IP and port
    firewall.addRule("192.168.1.10", 8080, true); // Allow specific IP and port
    firewall.addRule("192.168.1.11", 8081, false); // Deny specific IP and port

    // Start firewall on a specific port
    firewall.startFirewall(9999); // Listening on port 9999 for incoming connections
}
```
* **Steps**:
  1. Creates an instance of CustomFirewall.
  2. Adds two rules:
      * Allows traffic from IP `192.168.1.10` on port `8080`.
      * Denies traffic from IP `192.168.1.11` on port `8081`.
  3. Starts the firewall to listen for connections on port `9999`.


## Summary
* The program listens for incoming network traffic on a specific port.
* Filters traffic based on predefined rules (IP and port).
* Logs whether connections are allowed or denied.
* Can be extended to handle more complex scenarios, such as ranges of IPs/ports or specific protocols.
  
