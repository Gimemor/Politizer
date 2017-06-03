package Politizer;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

import com.github.scribejava.apis.VkontakteApi;
import com.github.scribejava.core.builder.ServiceBuilder;
import com.github.scribejava.core.model.OAuth2AccessToken;
import com.github.scribejava.core.model.OAuthRequest;
import com.github.scribejava.core.model.Response;
import com.github.scribejava.core.model.Verb;
import com.github.scribejava.core.oauth.OAuth20Service;

//
// Модуль, отвечающий за прохождение авторизации на VK.com
//
public class AuthModule {
	// Что нам нужно? 
	// Нужно получить AccessToken
    private static final String PROTECTED_RESOURCE_URL = "https://api.vk.com/method/users.get";

	public static void ObtainAccessToken(String clientId, String secret, String scope) throws IOException, InterruptedException, ExecutionException
	{
		OAuth20Service service = new ServiceBuilder()
				.apiKey(clientId)
                .apiSecret(secret)
                .scope(scope)
                .callback("https://oauth.vk.com/blank.html")
                .build(VkontakteApi.instance());
        Scanner in = new Scanner(System.in);
        // Obtain the Authorization URL
        System.out.println("Fetching the Authorization URL...");
        final String authorizationUrl = service.getAuthorizationUrl();
        System.out.println("Got the Authorization URL!");
        System.out.println("Now go and authorize ScribeJava here:");
        System.out.println(authorizationUrl);
        System.out.println("And paste the authorization code here");
        System.out.print(">>");
        final String code = in.nextLine();
        System.out.println();

        // Trade the Request Token and Verfier for the Access Token
        System.out.println("Trading the Request Token for an Access Token...");
        OAuth2AccessToken accessToken = service.getAccessToken(code);
        System.out.println("Got the Access Token!");
        System.out.println("(if your curious it looks like this: " + accessToken
                + ", 'rawResponse'='" + accessToken.getRawResponse() + "')");
        System.out.println();

        // Now let's go and ask for a protected resource!
        System.out.println("Now we're going to access a protected resource...");
        final OAuthRequest request = new OAuthRequest(Verb.GET, PROTECTED_RESOURCE_URL);
        service.signRequest(accessToken, request);
        final Response response = service.execute(request);
        System.out.println("Got it! Lets see what we found...");
        System.out.println();
        System.out.println(response.getCode());
        System.out.println(response.getBody());
        in.close();
        System.out.println();
        System.out.println("Thats it man! Go and build something awesome with ScribeJava! :)");
	}
	
}
