package ku.ske.googleauth;

/**
 * Demo using RESTful interface to Google tasks.
 * Google also has classes for accessing Tasks using Google's API
 * instead of REST.
 * 
 * @author jim
 */
public class TasksDemo {

	//1. get task lists: https://www.googleapis.com/tasks/v1/users/@me/lists
	// Response:
	/*
	 {
  "items": [
    {
      "kind": "tasks#taskList", 
      "updated": "2014-11-02T04:55:38.000Z", 
      "id": "MDYyNzQ4MzA0NjY0NTY2NDgwODk6MDow", 
      "selfLink": "https://www.googleapis.com/tasks/v1/users/@me/lists/MDYyNzQ4MzA0NjY0NTY2NDgwODk6MDow", 
      "title": "MyTasks"
    }
  ], 
  "kind": "tasks#taskLists", 
  "etag": "\"NcRr-h9-i3HwoSX-LVon2euS0GY/g6mwpq4R_QmU9WSAfoonZuYqurs\""
  }
	 */
	
	//2. Get a list of tasks. https://www.googleapis.com/tasks/v1/lists/{tasklist}/tasks
	//Response:
	/*
	 {
  "items": [
    {
      "status": "needsAction", 
      "kind": "tasks#task", 
      "title": "OAuth Sample Project", 
      "notes": "Sample project using OAuth.", 
      "updated": "2014-11-02T04:55:24.000Z", 
      "due": "2014-11-04T00:00:00.000Z", 
      "etag": "\"NcRr-h9-i3HwoSX-LVon2euS0GY/LTE2MzM3MDQwMjk\"", 
      "position": "00000000002639615317", 
      "id": "MDYyNzQ4MzA0NjY0NTY2NDgwODk6MDo4NDE3NzkzNzM", 
      "selfLink": "https://www.googleapis.com/tasks/v1/lists/MDYyNzQ4MzA0NjY0NTY2NDgwODk6MDow/tasks/MDYyNzQ4MzA0NjY0NTY2NDgwODk6MDo4NDE3NzkzNzM"
    },  
    ... more items (tasks) ...
  ], 
  "kind": "tasks#tasks", 
  "etag": "\"NcRr-h9-i3HwoSX-LVon2euS0GY/MTg1OTg5NzQ2NA\""
}
	 */
	
	
}
