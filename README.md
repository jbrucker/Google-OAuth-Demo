Google-OAuth-Demo
=================

A console-based application using Google's OAuth for Installed Applications.
The class SimpleOAuth performs the authorization requests using HTTP, so you can see what the messages are, and required parameters.  The methods in this class show you how the requests are formed.  The Google class contains only constants that are specific to OAuth provider, like the URLs.

The class SimpleOAuthDemo invokes methods of SimpleOAuth.  To run this, you first need to get your own Client ID from <a href="https://code.google.com/apis/console">Google API Console</a> (its free) and insert the CLIENT ID and CLIENT SECRET values into SimpleOAuthDemo.java.

The code uses Apache HTTP Client to send requests, and Jackson to parse the JSON message containing access token.
For ease of use, the required JARs are included in the project's lib/ directory.
