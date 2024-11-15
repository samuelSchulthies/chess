package server;

import com.google.gson.Gson;
import exception.DataAccessException;
import requestresult.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {
    private final String serverUrl;

    public ServerFacade(String serverUrl) {
        this.serverUrl = serverUrl;
    }

    public void clear() throws DataAccessException {
        makeRequest("DELETE", "/db", null, null, null);
    }

    public CreateResult create(CreateRequest createRequest, String authToken) throws DataAccessException {
        return makeRequest("POST", "/game", createRequest, authToken, CreateResult.class);
    }

    public JoinResult join(JoinRequest joinRequest, String authToken) throws DataAccessException {
        return makeRequest("PUT", "/game", joinRequest, authToken, JoinResult.class);
    }

    public ListResult list(String authToken) throws DataAccessException {
        return makeRequest("GET", "/game", null, authToken, ListResult.class);
    }

    public LoginResult login(LoginRequest loginRequest) throws DataAccessException {
        return makeRequest("POST", "/session", loginRequest, null, LoginResult.class);
    }

    public void logout(String authToken) throws DataAccessException {
        makeRequest("DELETE", "/session", null, authToken, null);
    }

    public RegisterResult register(RegisterRequest registerRequest) throws DataAccessException {
        return makeRequest("POST", "/user", registerRequest, null, RegisterResult.class);
    }


    private <T> T makeRequest(String method, String path, Object request, String authToken, Class<T> responseClass) throws DataAccessException{
        try {
            URL url = (new URI(serverUrl + path).toURL());
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            if (authToken != null){
                writeHeader(authToken, http);
            }
            writeBody(request, http);
            http.connect();
            throwIfError(http);
            return readBody(http, responseClass);
        } catch (Exception e){
            throw new DataAccessException(e.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null){
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()){
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static void writeHeader(String authToken, HttpURLConnection http){
        http.addRequestProperty("Authorization", authToken);
    }

    private static <T> T readBody (HttpURLConnection http, Class<T> responseClass) throws IOException{
        T response = null;
        if (http.getContentLength() < 0){
            try (InputStream resBody= http.getInputStream()){
                InputStreamReader reader = new InputStreamReader(resBody);
                if(responseClass != null){
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private void throwIfError(HttpURLConnection http) throws IOException, DataAccessException {
        if (http.getResponseCode() != 200){
            throw new DataAccessException(http.getResponseMessage());
        }
    }

}
