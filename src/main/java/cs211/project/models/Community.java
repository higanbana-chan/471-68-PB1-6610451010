package cs211.project.models;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Community extends Postable {
    private int id;
    private String postDetail;

    private static final String post_csv_path = "src/main/resources/cs211/project/database/postDetails.csv";

    // Constructor
    public Community(int id, String postDetail) {
        this.id = id;
        this.postDetail = postDetail;
    }
    public Community(String postDetails) {
        super(postDetails);
    }
    public int getId() {
        return id;
    }
    public String getPostDetail() {
        return postDetail;
    }


    public static List<Community> getAllPosts() {
        List<Community> posts = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(post_csv_path))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int id = Integer.parseInt(values[0]);
                String postDetail = values[1];
                posts.add(new Community(id, postDetail));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return posts;
    }
    public void addPostToCSV() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(post_csv_path, true))) {
            bw.write(this.id + "," + this.postDetail + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private int getNextId() {
        int highestId = 0;
        try (BufferedReader br = new BufferedReader(new FileReader(post_csv_path))) {
            String line;
            // Skip the header line
            br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                int currentId = Integer.parseInt(values[0]);
                if (currentId > highestId) {
                    highestId = currentId;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highestId + 1; // Return the next available id
    }

}
