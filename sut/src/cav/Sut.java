/*
 * sut is generated from model  : /home/harcok/workspace3.8.1/tomte/input/Fresh_CAV_LOGIN/model.uppaal.xml
 */

package cav;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

//0.3 import org.apache.log4j.Logger;

public class Sut implements sut.interfaces.SutInterface {
    //0.3 private static final Logger logger = Logger.getLogger(Sut.class);

    // create a client
    HttpClient client = HttpClient.newHttpClient();

    private Random random = new Random(1234567890);

    // Get fresh value
    private int getRandomInt() {
        return random.nextInt(10000000)+10000000;
    }

    //handling each Input

    /* register an uid
     * 
     * notes:
     *   - you can only register once for a specific uid
     *   - at max only MAX_REGISTERED_USERS may be registered 
     */
    public OutputAction IRegister(int uid) throws IOException, InterruptedException {
        String methodName = "ONOK";
        List < Parameter > params = new ArrayList < Parameter > ();

        String reqBody = "{\"uid\":" + uid + "}";

        // create a request
        var request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5000/register"))
            .header("Content-type", "application/json")
            .POST(BodyPublishers.ofString(reqBody))
            .build();

        // use the client to send the request
        var response = client.send(request, BodyHandlers.ofString());
        
        if (!response.body().trim().contains("failed")) {
            var pwd = Integer.parseInt(response.body().trim());
        	methodName = "OOK";
        	
            params.add(new Parameter(pwd, 0));        	
        }

        return new OutputAction(methodName, params);
    }    
    
    /* login an user with uid
     * 
     * notes:
     *   - An user can only login, if the uid  
     *       + is registered 
     *       + and is not logged in
     *   - at max only MAX_LOGGEDIN_USERS users may be logged in 
     */   
    public OutputAction ILogin(int uid,int pwd) throws IOException, InterruptedException {
        String methodName = "ONOK";
        List < Parameter > params = new ArrayList < Parameter > ();

        String reqBody = "{\"uid\":" + uid + ", \"pwd\":" + pwd + "}";

        // create a request
        var request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5000/login"))
            .header("Content-type", "application/json")
            .POST(BodyPublishers.ofString(reqBody))
            .build();

        // use the client to send the request
        var response = client.send(request, BodyHandlers.ofString());
        var result = Integer.parseInt(response.body().trim());

        if (result == 1) {
        	methodName = "OOK";
            params.add(new Parameter(getRandomInt(), 0)); // none important fresh output value
        }

        return new OutputAction(methodName, params);
    }
    
    /* ILogout
     * 
     * A user can only logout when logged in.
     */
    public OutputAction ILogout(int uid) throws IOException, InterruptedException {
        String methodName = "ONOK";
        List < Parameter > params = new ArrayList < Parameter > ();

        String reqBody = "{\"uid\":" + uid + "}";

        // create a request
        var request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5000/logout"))
            .header("Content-type", "application/json")
            .POST(BodyPublishers.ofString(reqBody))
            .build();

        // use the client to send the request
        var response = client.send(request, BodyHandlers.ofString());
        var result = Integer.parseInt(response.body().trim());

        if (result == 1) {
        	methodName = "OOK";
            params.add(new Parameter(getRandomInt(), 0)); // none important fresh output value      	
        }

        return new OutputAction(methodName, params);
    }
    
 
    /* IChangePassword
     * 
     * a  user can only change password when logged in
     */
    public OutputAction IChangePassword(int uid) throws IOException, InterruptedException {
        String methodName = "ONOK";
        List < Parameter > params = new ArrayList < Parameter > ();

        String reqBody = "{\"uid\":" + uid + "}";

        // create a request
        var request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5000/change-password"))
            .header("Content-type", "application/json")
            .POST(BodyPublishers.ofString(reqBody))
            .build();

        // use the client to send the request
        var response = client.send(request, BodyHandlers.ofString());

        if (!response.body().trim().contains("failed")) {
            var pwd = Integer.parseInt(response.body().trim());
        	methodName = "OOK";
            params.add(new Parameter(pwd, 0));        	
        }

        return new OutputAction(methodName, params);
    }      


    // handling all inputs
    public OutputAction handle(InputAction inputAction) throws IOException, InterruptedException {
        String methodName=inputAction.getMethodName();
        if (methodName.equals("ILogout")) {
            List < Parameter > params = inputAction.getParameters();
            int uid=params.get(0).getValue();
            return ILogout(uid);
        } else if (methodName.equals("IRegister")) {
            List < Parameter > params = inputAction.getParameters();
            int uid=params.get(0).getValue();
            return IRegister(uid);
        } else if (methodName.equals("IChangePassword")) {
            List < Parameter > params = inputAction.getParameters();
            int uid=params.get(0).getValue();
            return IChangePassword(uid);
        } else if (methodName.equals("ILogin")) {
            List < Parameter > params = inputAction.getParameters();
            int uid=params.get(0).getValue();
            int pwd=params.get(1).getValue();
            return ILogin(uid,pwd);
        }
        
        throw new RuntimeException("SUT does not support the input:" + inputAction.getMethodName());
    }

  	@Override
	public sut.interfaces.OutputAction sendInput(sut.interfaces.InputAction origInputAction) {
		InputAction inputAction = new InputAction(origInputAction); // make copy to be safe!!
		OutputAction outputAction;
        try {
            outputAction = handle(inputAction);
            return outputAction; // outputAction implements sut.interfaces.OutputAction interface
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        throw new RuntimeException("Something went wrong");
	}

	@Override
	public void sendReset() {
        // create a request
        var request = HttpRequest.newBuilder()
            .uri(URI.create("http://127.0.0.1:5000/reset"))
            .header("Content-type", "application/json")
            .POST(BodyPublishers.ofString(""))
            .build();

        // use the client to send the request
        try {
            client.send(request, BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}

}
