# GHRepoSearcher
GHRepoSearcher is a tool, written in Java, that allows searching for repositories on GitHub. It is built upon the GitHub Search API (v3). The purpose of GHRepoSearcher is to easy the research of repositories on GitHub by overcoming three limitations imposed by the GitHub Search API, namely: 

1. GitHub Search API provides up to 1,000 repositories for each search (i.e., if a search returned more than 1,000 repositories, only the first 1,000 repositories would be provided);

2. For each search, GitHub Search API pages repositories (i.e., up to 100 repositories per page);

3. Any (authenticated) client can make at most 30 requests per minute.
