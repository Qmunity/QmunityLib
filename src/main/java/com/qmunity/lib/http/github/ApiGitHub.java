package com.qmunity.lib.http.github;

import java.net.URL;

public class ApiGitHub {

    /**
     * Gets a repository from GitHub and loads its data into cache
     * 
     * @param owner
     *            Owner of the repository
     * @param repository
     *            Repository name
     * @return The repository
     */
    public static GitHubRepo getRepository(String owner, String repository) {

        GitHubRepo repo = new GitHubRepo(owner, repository);
        repo.load();
        return repo;
    }

    protected static URL get(String category) {

        try {
            category = "https://api.github.com/" + category.replace(".", "/");
            return new URL(category);
        } catch (Exception ex) {
        }
        return null;
    }

}
