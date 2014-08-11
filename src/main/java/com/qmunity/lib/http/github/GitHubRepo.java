package com.qmunity.lib.http.github;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.qmunity.lib.http.ApiHTTP;

public class GitHubRepo implements IGitHubObject {

    private String owner;
    private String repo;

    private List<GitHubIssue> issues = new ArrayList<GitHubIssue>();

    public GitHubRepo(String owner, String name) {

        this.owner = owner;
        repo = name;
    }

    @Override
    public void load() {

        dispose();

        try {
            // JsonObject repo = ApiHTTP.readJSONObjectFromURL(ApiGitHub.get(toString()));

            // Issues
            {
                JsonArray issues = ApiHTTP.readJSONArrayFromURL(ApiGitHub.get(this + ".issues"));
                for (int i = 0; i < issues.size(); i++) {
                    JsonObject issueContent = issues.get(i).getAsJsonObject();
                    GitHubIssue issue = new GitHubIssue(this, issueContent.get("number").getAsInt(), issueContent);
                    issue.load();
                    this.issues.add(issue);
                }
            }

        } catch (Exception ex) {
            throw new RuntimeException("Could not load repository!");
        }
    }

    @Override
    public void dispose() {

        issues.clear();
    }

    public String getOwner() {

        return owner;
    }

    public String getRepoName() {

        return repo;
    }

    @Override
    public String toString() {

        return owner + "." + repo;
    }

    public List<GitHubIssue> getIssues() {

        return issues;
    }
}
