Google-OAuth-Demo
=================

A console-based application using Google's OAuth for Installed Applications.
The class SimpleOAuth performs the authorization requests using HTTP, so you can see what the messages are, and required parameters.

The class SimpleOAuthDemo invokes methods of SimpleOAuth.  To run this, you first need to get your own Client ID from Google's API Console (its free) and insert the CLIENT ID and CLIENT SECRET values into SimpleOAuthDemo.java.

The code uses Apache HTTP Client to send requests, and Jackson to parse the JSON message containing access token.
For ease of use, the required JARs are included in the lib/ directory.
