package com.qmunity.lib.http.github;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qmunity.lib.http.ApiHTTP;

public class GitHubIssue implements IGitHubObject {

    private GitHubRepo repo;
    private int id;
    private boolean closed;
    private String user;
    private String[] labels;
    private String assignee;
    private String body;

    private JsonObject issue;

    public GitHubIssue(GitHubRepo repo, int id) {

        this.repo = repo;
        this.id = id;
    }

    protected GitHubIssue(GitHubRepo repo, int id, JsonObject issue) {

        this(repo, id);
        this.issue = issue;
    }

    @Override
    public void load() {

        try {
            JsonObject issue;
            if (this.issue != null) {
                issue = this.issue;
                this.issue = null;
            } else {
                issue = ApiHTTP.readJSONObjectFromURL(ApiGitHub.get(repo + ".issues." + id));
            }

            closed = issue.getAsJsonObject("state").getAsString().equals("closed");
            user = issue.getAsJsonObject("user").getAsJsonObject("login").getAsString();

            List<String> labels = new ArrayList<String>();
            JsonArray labelArray = issue.getAsJsonArray("labels");
            for (int i = 0; i < labelArray.size(); i++)
                labels.add(labelArray.get(i).getAsString());
            this.labels = labels.toArray(new String[labels.size()]);
            labels.clear();

            assignee = issue.get("assignee").getAsString();
            body = issue.get("body").getAsString();

        } catch (Exception ex) {
            throw new RuntimeException("Could not load repository!");
        }
    }

    @Override
    public void dispose() {

    }

    public GitHubRepo getRepo() {

        return repo;
    }

    public int getId() {

        return id;
    }

    public String getUser() {

        return user;
    }

    public String[] getLabels() {

        return labels;
    }

    public String getAssignee() {

        return assignee;
    }

    public String getBody() {

        return body;
    }

    public boolean isClosed() {

        return closed;
    }

}
