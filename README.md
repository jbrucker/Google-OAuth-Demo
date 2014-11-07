Google-OAuth-Demo
=================

A console-based application using Google's OAuth for Installed Applications.
The class SimpleOAuth performs the authorization requests using HTTP, so you can see the messages and required information.

The class SimpleOAuthDemo invokes methods of SimpleOAuth.  To run this, you first need to get your own Client ID from Google's API Console (its free).

The code using Apache HTTP Client to send requests, and Jackson to parse the JSON message containing access token.
For ease of use, the required JARs are included in the lib/ directory.
