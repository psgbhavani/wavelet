import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

class Handler implements URLHandler {
    // The one bit of state on the server: a list of strings that will be manipulated by
    // various requests.
    List<String> my_strings = new ArrayList<String>();

    public String handleRequest(URI url) {
        System.out.println(url);
        if (url.getPath().equals("/")) {
            return String.format("List of Strings: %s", my_strings.toString());
        } else {
            if (url.getPath().contains("/add")){
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    my_strings.add(parameters[1]);
                    return String.format("The string %s was added to the list! The list is now %s", parameters[1], my_strings.toString());
                }
            }

            if (url.getPath().contains("/search")){
                String[] parameters = url.getQuery().split("=");
                if (parameters[0].equals("s")) {
                    List<String> substring_list = find_substrings(parameters[1], my_strings);
                    return String.format("The following strings in the list %s have a substring of '%s': %s", my_strings.toString(), parameters[1], substring_list.toString());
                }
            }

            return "404 Not Found!";
        }
    }

    private List<String> find_substrings(String query, List<String> my_strings) {
        List<String> substring_list = new ArrayList<String>();

        for (int i = 0; i < my_strings.size(); i++) {
            String current = my_strings.get(i);
            if (current.contains(query)){
                //Add to subtring list
                substring_list.add(current);
            }
        } 

        return substring_list;
    }
}

class NumberServer {
    public static void main(String[] args) throws IOException {
        if(args.length == 0){
            System.out.println("Missing port number! Try any number between 1024 to 49151");
            return;
        }

        if(args[0].equals("22")){
            System.out.println("Did you know that port 22 is the one used by ssh? We can't use it for a web server.");
            return;
        }

        int port = Integer.parseInt(args[0]);

        Server.start(port, new Handler());
    }
}
