package twitterbot;

import javafx.scene.control.ListView;
import javafx.stage.FileChooser;

import java.io.*;
import java.util.LinkedList;
import java.util.List;

public class TextFileImportExport {

    private static File loadFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Text files");
        // fileChooser.setInitialDirectory(new File("C:"));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("Text Files", "*.txt"));

        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            return selectedFile;
        }
        else {
           // AlertBox.display("Error","Importing Text File Failed!");
            return null;
        }

    }

    public static void saveFile(List list){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save file");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text doc(*.txt)", "*.txt"));
        fileChooser.setInitialFileName("FileName");
        File savedFile = fileChooser.showSaveDialog(null);

        if (savedFile != null) {

            try {
                PrintWriter pw = new PrintWriter(savedFile);
                list.forEach(p->pw.println(p.toString()));
                pw.close();
            }
            catch(IOException e) {

               // e.printStackTrace();
                AlertBox.display("Error","An ERROR occurred while saving the file!");
                return;
            }
        }
    }

    public static void readLines(ListView<Quote> listLines){

        File file = loadFile();
        if(file!=null) {
            List<Quote> list = new LinkedList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                br.lines().filter(p->p.length()>0).forEach(p -> list.add(new Quote(p)));

            } catch (FileNotFoundException e) {
                //  e.printStackTrace();
                AlertBox.display("File Not Found", file.getName() + " was not found!");
            }
            listLines.getItems().addAll(list);
        }
    }

    public static List<String> loadConfig(){
        File file = loadFile();

        if(file!=null) {
            List<String> list = new LinkedList<>();
            try {
                BufferedReader br = new BufferedReader(new FileReader(file));

                br.lines().filter(p->p.length()>0).forEach(p -> {
                    list.add(p);
                });

                if(list.size()!=4) {
                    AlertBox.display("Something went wrong!","Please check if the selected file contains only the required strings in the right order");
                    return null;
                }

                return list;

            } catch (FileNotFoundException e) {
                //  e.printStackTrace();
                AlertBox.display("File Not Found", file.getName() + " was not found!");
            }
        }
        return null;
    }
}
