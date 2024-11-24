package net.shoreline.client.api.account.msa;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.util.UndashedUuid;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import java.awt.Desktop;
import java.awt.Toolkit;
import java.awt.Desktop.Action;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import net.minecraft.class_320;
import net.minecraft.class_320.class_321;
import net.shoreline.client.Shoreline;
import net.shoreline.client.api.account.msa.callback.BrowserLoginCallback;
import net.shoreline.client.api.account.msa.exception.MSAAuthException;
import net.shoreline.client.api.account.msa.model.MinecraftProfile;
import net.shoreline.client.api.account.msa.model.OAuthResult;
import net.shoreline.client.api.account.msa.model.XboxLiveData;
import net.shoreline.client.api.account.msa.security.PKCEData;
import org.apache.http.Header;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class MSAAuthenticator {
   private static final Logger LOGGER = LogManager.getLogger("MSA-Authenticator");
   private static final CloseableHttpClient HTTP_CLIENT = HttpClientBuilder.create().setRedirectStrategy(new LaxRedirectStrategy()).disableAuthCaching().disableCookieManagement().disableDefaultUserAgent().build();
   private static final String CLIENT_ID = "d1bbd256-3323-4ab7-940e-e8a952ebdb83";
   private static final int PORT = 6969;
   private static final String REAL_USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:107.0) Gecko/20100101 Firefox/107.0";
   private static final String OAUTH_AUTH_DESKTOP_URL = "https://login.live.com/oauth20_authorize.srf?client_id=000000004C12AE6F&redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=service::user.auth.xboxlive.com::MBI_SSL&display=touch&response_type=token&locale=en";
   private static final String OAUTH_AUTHORIZE_URL = "https://login.live.com/oauth20_authorize.srf?response_type=code&client_id=%s&redirect_uri=http://localhost:%s/login&code_challenge=%s&code_challenge_method=S256&scope=XboxLive.signin+offline_access&state=NOT_NEEDED&prompt=select_account";
   private static final String OAUTH_TOKEN_URL = "https://login.live.com/oauth20_token.srf";
   private static final String XBOX_LIVE_AUTH_URL = "https://user.auth.xboxlive.com/user/authenticate";
   private static final String XBOX_XSTS_AUTH_URL = "https://xsts.auth.xboxlive.com/xsts/authorize";
   private static final String LOGIN_WITH_XBOX_URL = "https://api.minecraftservices.com/authentication/login_with_xbox";
   private static final String MINECRAFT_PROFILE_URL = "https://api.minecraftservices.com/minecraft/profile";
   private static final Pattern SFTT_TAG_PATTERN = Pattern.compile("value=\"(.+?)\"");
   private static final Pattern POST_URL_PATTERN = Pattern.compile("urlPost:'(.+?)'");
   private HttpServer localServer;
   private String loginStage = "";
   private boolean serverOpen;
   private PKCEData pkceData;

   public class_320 loginWithCredentials(String email, String password) throws MSAAuthException {
      OAuthResult result = this.getOAuth();
      if (result.getPostUrl() != null && result.getSfttTag() != null) {
         String token = this.getOAuthLoginData(result, email, password);
         return this.loginWithToken(token, false);
      } else {
         throw new MSAAuthException("Failed to retrieve SFTT tag & Post URL");
      }
   }

   public void loginWithBrowser(BrowserLoginCallback callback) throws IOException, URISyntaxException, MSAAuthException {
      if (!this.serverOpen || this.localServer == null) {
         this.localServer = HttpServer.create();
         this.localServer.createContext("/login", (ctx) -> {
            this.setLoginStage("Parsing access token from response");
            Map<String, String> query = this.parseQueryString(ctx.getRequestURI().getQuery());
            String errorDescription;
            if (query.containsKey("error")) {
               errorDescription = (String)query.get("error_description");
               if (errorDescription != null && !errorDescription.isEmpty()) {
                  LOGGER.error("Failed to get token from browser login: {}", errorDescription);
                  this.writeToWebpage("Failed to get token: " + errorDescription, ctx);
                  this.setLoginStage(errorDescription);
               }
            } else {
               errorDescription = (String)query.get("code");
               if (errorDescription != null) {
                  callback.callback(errorDescription);
                  this.writeToWebpage("Successfully got code. You may now close this window", ctx);
               } else {
                  this.writeToWebpage("Failed to get code. Please try again.", ctx);
               }
            }

            this.serverOpen = false;
            this.localServer.stop(0);
         });
      }

      this.pkceData = this.generateKeys();
      if (this.pkceData == null) {
         throw new MSAAuthException("Failed to generate PKCE keys");
      } else {
         String url = String.format("https://login.live.com/oauth20_authorize.srf?response_type=code&client_id=%s&redirect_uri=http://localhost:%s/login&code_challenge=%s&code_challenge_method=S256&scope=XboxLive.signin+offline_access&state=NOT_NEEDED&prompt=select_account", "d1bbd256-3323-4ab7-940e-e8a952ebdb83", 6969, this.pkceData.challenge());
         if (Desktop.getDesktop().isSupported(Action.BROWSE)) {
            Desktop.getDesktop().browse(new URI(url));
            this.setLoginStage("Waiting user response...");
         } else {
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(new StringSelection(url), (ClipboardOwner)null);
            LOGGER.warn("BROWSE action not supported on Desktop Environment, copied to clipboard instead.");
            this.setLoginStage("Link copied to clipboard!");
         }

         if (!this.serverOpen) {
            this.localServer.bind(new InetSocketAddress(6969), 1);
            this.localServer.start();
            this.serverOpen = true;
         }

      }
   }

   public class_320 loginWithToken(String token, boolean browser) throws MSAAuthException {
      this.setLoginStage("Logging in with Xbox Live...");
      XboxLiveData data = this.authWithXboxLive(token, browser);
      this.requestTokenFromXboxLive(data);
      String accessToken = this.loginWithXboxLive(data);
      this.setLoginStage("Fetching MC profile...");
      MinecraftProfile profile = this.fetchMinecraftProfile(accessToken);
      this.pkceData = null;
      return new class_320(profile.username(), UndashedUuid.fromStringLenient(profile.id()), accessToken, Optional.empty(), Optional.empty(), class_321.field_34962);
   }

   public String getLoginToken(String oauthToken) throws MSAAuthException {
      HttpPost httpPost = new HttpPost("https://login.live.com/oauth20_token.srf");
      httpPost.setHeader("Content-Type", ContentType.APPLICATION_FORM_URLENCODED.getMimeType());
      httpPost.setHeader("Accept", "application/json");
      httpPost.setHeader("Origin", "http://localhost:6969/");
      httpPost.setEntity(new StringEntity(this.makeQueryString(new String[][]{{"client_id", "d1bbd256-3323-4ab7-940e-e8a952ebdb83"}, {"code_verifier", this.pkceData.verifier()}, {"code", oauthToken}, {"grant_type", "authorization_code"}, {"redirect_uri", "http://localhost:6969/login"}}), ContentType.create(ContentType.APPLICATION_FORM_URLENCODED.getMimeType(), Charset.defaultCharset())));

      try {
         CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost);

         String var6;
         try {
            String content = EntityUtils.toString(response.getEntity());
            if (content == null || content.isEmpty()) {
               throw new MSAAuthException("Failed to get login token from MSA OAuth");
            }

            JsonObject obj = JsonParser.parseString(content).getAsJsonObject();
            if (obj.has("error")) {
               String var10002 = obj.get("error").getAsString();
               throw new MSAAuthException(var10002 + ": " + obj.get("error_description").getAsString());
            }

            var6 = obj.get("access_token").getAsString();
         } catch (Throwable var8) {
            if (response != null) {
               try {
                  response.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (response != null) {
            response.close();
         }

         return var6;
      } catch (IOException var9) {
         var9.printStackTrace();
         throw new MSAAuthException("Failed to get login token");
      }
   }

   private OAuthResult getOAuth() throws MSAAuthException {
      HttpGet httpGet = new HttpGet("https://login.live.com/oauth20_authorize.srf?client_id=000000004C12AE6F&redirect_uri=https://login.live.com/oauth20_desktop.srf&scope=service::user.auth.xboxlive.com::MBI_SSL&display=touch&response_type=token&locale=en");
      httpGet.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:107.0) Gecko/20100101 Firefox/107.0");
      httpGet.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,*/*;q=0.8");

      try {
         CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);

         OAuthResult var7;
         try {
            String content = EntityUtils.toString(response.getEntity());
            OAuthResult result = new OAuthResult();
            Matcher matcher = SFTT_TAG_PATTERN.matcher(content);
            if (matcher.find()) {
               result.setSfttTag(matcher.group(1));
            }

            if ((matcher = POST_URL_PATTERN.matcher(content)).find()) {
               result.setPostUrl(matcher.group(1));
            }

            List<Header> cookies = Arrays.asList(response.getHeaders("Set-Cookie"));
            result.setCookie((String)cookies.stream().map(NameValuePair::getValue).collect(Collectors.joining(";")));
            var7 = result;
         } catch (Throwable var9) {
            if (response != null) {
               try {
                  response.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (response != null) {
            response.close();
         }

         return var7;
      } catch (IOException var10) {
         var10.printStackTrace();
         throw new MSAAuthException("Failed to login with email & password.");
      }
   }

   private String getOAuthLoginData(OAuthResult result, String email, String password) throws MSAAuthException {
      String contentTypeRaw = ContentType.APPLICATION_FORM_URLENCODED.getMimeType();
      HttpPost httpPost = new HttpPost(result.getPostUrl());
      httpPost.setHeader("Cookie", result.getCookie());
      httpPost.setHeader("Content-Type", contentTypeRaw);
      String encodedEmail = URLEncoder.encode(email);
      String encodedPassword = URLEncoder.encode(password);
      httpPost.setEntity(new StringEntity(this.makeQueryString(new String[][]{{"login", encodedEmail}, {"loginfmt", encodedEmail}, {"passwd", encodedPassword}, {"PPFT", result.getSfttTag()}}), ContentType.create(contentTypeRaw)));
      HttpClientContext ctx = HttpClientContext.create();

      try {
         CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost, ctx);

         label79: {
            String var17;
            try {
               List<URI> redirectLocations = ctx.getRedirectLocations();
               if (redirectLocations == null || redirectLocations.isEmpty()) {
                  throw new MSAAuthException("Failed to get valid response from Microsoft");
               }

               String content = ((URI)redirectLocations.get(redirectLocations.size() - 1)).toString().split("#")[1];
               String[] var12 = content.split("&");
               int var13 = var12.length;
               int var14 = 0;

               while(true) {
                  if (var14 >= var13) {
                     content = EntityUtils.toString(response.getEntity());
                     if (content != null && !content.isEmpty()) {
                        if (content.contains("Sign in to")) {
                           throw new MSAAuthException("The provided credentials were incorrect");
                        }

                        if (content.contains("Help us protect your account")) {
                           throw new MSAAuthException("2FA has been enabled on this account");
                        }
                     }
                     break label79;
                  }

                  String param = var12[var14];
                  String[] parameter = param.split("=");
                  if (parameter[0].equals("access_token")) {
                     var17 = parameter[1];
                     break;
                  }

                  ++var14;
               }
            } catch (Throwable var19) {
               if (response != null) {
                  try {
                     response.close();
                  } catch (Throwable var18) {
                     var19.addSuppressed(var18);
                  }
               }

               throw var19;
            }

            if (response != null) {
               response.close();
            }

            return var17;
         }

         if (response != null) {
            response.close();
         }
      } catch (IOException var20) {
         var20.printStackTrace();
      }

      throw new MSAAuthException("Failed to get access token");
   }

   private XboxLiveData authWithXboxLive(String accessToken, boolean browser) throws MSAAuthException {
      String body = "{\"Properties\":{\"AuthMethod\":\"RPS\",\"SiteName\":\"user.auth.xboxlive.com\",\"RpsTicket\":\"" + (browser ? "d=" : "") + accessToken + "\"},\"RelyingParty\":\"http://auth.xboxlive.com\",\"TokenType\":\"JWT\"}";
      String content = this.makePostRequest("https://user.auth.xboxlive.com/user/authenticate", body, ContentType.APPLICATION_JSON);
      if (content != null && !content.isEmpty()) {
         JsonObject object = JsonParser.parseString(content).getAsJsonObject();
         XboxLiveData data = new XboxLiveData();
         data.setToken(object.get("Token").getAsString());
         data.setUserHash(object.get("DisplayClaims").getAsJsonObject().get("xui").getAsJsonArray().get(0).getAsJsonObject().get("uhs").getAsString());
         return data;
      } else {
         throw new MSAAuthException("Failed to authenticate with Xbox Live account");
      }
   }

   private void requestTokenFromXboxLive(XboxLiveData xboxLiveData) throws MSAAuthException {
      String body = "{\"Properties\":{\"SandboxId\":\"RETAIL\",\"UserTokens\":[\"" + xboxLiveData.getToken() + "\"]},\"RelyingParty\":\"rp://api.minecraftservices.com/\",\"TokenType\":\"JWT\"}";
      String content = this.makePostRequest("https://xsts.auth.xboxlive.com/xsts/authorize", body, ContentType.APPLICATION_JSON);
      if (content != null && !content.isEmpty()) {
         JsonObject object = JsonParser.parseString(content).getAsJsonObject();
         if (object.has("XErr")) {
            throw new MSAAuthException("Xbox Live Error: " + object.get("XErr").getAsString());
         }

         xboxLiveData.setToken(object.get("Token").getAsString());
      }

   }

   private String loginWithXboxLive(XboxLiveData data) throws MSAAuthException {
      String var10000 = data.getUserHash();
      String body = "{\"ensureLegacyEnabled\":true,\"identityToken\":\"XBL3.0 x=" + var10000 + ";" + data.getToken() + "\"}";
      String content = this.makePostRequest("https://api.minecraftservices.com/authentication/login_with_xbox", body, ContentType.APPLICATION_JSON);
      if (content != null && !content.isEmpty()) {
         JsonObject object = JsonParser.parseString(content).getAsJsonObject();
         if (object.has("errorMessage")) {
            throw new MSAAuthException(object.get("errorMessage").getAsString());
         }

         if (object.has("access_token")) {
            return object.get("access_token").getAsString();
         }
      }

      return null;
   }

   private MinecraftProfile fetchMinecraftProfile(String accessToken) throws MSAAuthException {
      HttpGet httpGet = new HttpGet("https://api.minecraftservices.com/minecraft/profile");
      httpGet.setHeader("Accept", ContentType.APPLICATION_JSON.getMimeType());
      httpGet.setHeader("Authorization", "Bearer " + accessToken);

      try {
         CloseableHttpResponse response = HTTP_CLIENT.execute(httpGet);

         MinecraftProfile var6;
         try {
            if (response.getStatusLine().getStatusCode() != 200) {
               throw new MSAAuthException("Failed to fetch MC profile: Status code != 200, sc=" + response.getStatusLine().getStatusCode());
            }

            String rawJSON = EntityUtils.toString(response.getEntity());
            JsonObject object = JsonParser.parseString(rawJSON).getAsJsonObject();
            if (object.has("error")) {
               String var10002 = object.get("error").getAsString();
               throw new MSAAuthException("Failed to fetch MC profile: " + var10002 + " -> " + object.get("errorMessage").getAsString());
            }

            var6 = new MinecraftProfile(object.get("name").getAsString(), object.get("id").getAsString());
         } catch (Throwable var8) {
            if (response != null) {
               try {
                  response.close();
               } catch (Throwable var7) {
                  var8.addSuppressed(var7);
               }
            }

            throw var8;
         }

         if (response != null) {
            response.close();
         }

         return var6;
      } catch (IOException var9) {
         throw new MSAAuthException(var9.getMessage());
      }
   }

   private String makePostRequest(String url, String body, ContentType contentType) {
      HttpPost httpPost = new HttpPost(url);
      httpPost.setHeader("Content-Type", contentType.getMimeType());
      httpPost.setHeader("Accept", "application/json");
      httpPost.setEntity(new StringEntity(body, ContentType.create(contentType.getMimeType(), Charset.defaultCharset())));

      try {
         CloseableHttpResponse response = HTTP_CLIENT.execute(httpPost);

         String var6;
         try {
            var6 = EntityUtils.toString(response.getEntity());
         } catch (Throwable var9) {
            if (response != null) {
               try {
                  response.close();
               } catch (Throwable var8) {
                  var9.addSuppressed(var8);
               }
            }

            throw var9;
         }

         if (response != null) {
            response.close();
         }

         return var6;
      } catch (IOException var10) {
         Shoreline.error("Failed to make POST request to {}", url);
         var10.printStackTrace();
         return null;
      }
   }

   private void writeToWebpage(String message, HttpExchange ext) throws IOException {
      byte[] bytes = message.getBytes(StandardCharsets.UTF_8);
      ext.sendResponseHeaders(200, (long)message.length());
      OutputStream outputStream = ext.getResponseBody();
      outputStream.write(bytes, 0, bytes.length);
      outputStream.close();
   }

   private String makeQueryString(String[][] parameters) {
      StringJoiner joiner = new StringJoiner("&");
      String[][] var3 = parameters;
      int var4 = parameters.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String[] parameter = var3[var5];
         joiner.add(parameter[0] + "=" + parameter[1]);
      }

      return joiner.toString();
   }

   private Map<String, String> parseQueryString(String query) {
      Map<String, String> parameterMap = new LinkedHashMap();
      String[] var3 = query.split("&");
      int var4 = var3.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         String part = var3[var5];
         String[] kv = part.split("=");
         parameterMap.put(kv[0], kv.length == 1 ? null : kv[1]);
      }

      return parameterMap;
   }

   private PKCEData generateKeys() {
      try {
         byte[] randomBytes = new byte[32];
         (new SecureRandom()).nextBytes(randomBytes);
         String verifier = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
         byte[] verifierBytes = verifier.getBytes(StandardCharsets.US_ASCII);
         MessageDigest digest = MessageDigest.getInstance("SHA-256");
         digest.update(verifierBytes, 0, verifierBytes.length);
         byte[] d = digest.digest();
         String challenge = Base64.getUrlEncoder().withoutPadding().encodeToString(d);
         return new PKCEData(challenge, verifier);
      } catch (Exception var7) {
         return null;
      }
   }

   public void setLoginStage(String loginStage) {
      this.loginStage = loginStage;
   }

   public String getLoginStage() {
      return this.loginStage;
   }
}
