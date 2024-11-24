package net.shoreline.client.api.account.msa.model;

public final class OAuthResult {
   private String sfttTag;
   private String postUrl;
   private String cookie;

   public String getSfttTag() {
      return this.sfttTag;
   }

   public void setSfttTag(String sfttTag) {
      this.sfttTag = sfttTag;
   }

   public String getPostUrl() {
      return this.postUrl;
   }

   public void setPostUrl(String postUrl) {
      this.postUrl = postUrl;
   }

   public String getCookie() {
      return this.cookie;
   }

   public void setCookie(String cookie) {
      this.cookie = cookie;
   }
}
