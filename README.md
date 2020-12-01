# Heal-O-Chat
Heal-O-Chat is a Social Media Application for people who have been feeling less motivated in life or are losing hope. This platform allows users to chat with people and share their thoughts and feelings with each other and thereby let go of stress, anxiety, and depression that they've been feeling for long.

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/plazzy99/Heal-O-Chat/blob/master/LICENSE)
![first-timers-only](https://img.shields.io/badge/first--timers--only-friendly-yellow.svg?style=flat)
![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat)
![Minimum API Level](https://img.shields.io/badge/Min%20API%20Level-21-green)
![Maximum API Level](https://img.shields.io/badge/Max%20API%20Level-29-orange)
![Open Source Love](https://badges.frapsoft.com/os/v2/open-source.svg?v=103)

![](https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/doodle.png)

Table of Contents
=================

   * [Features](#top-features-of-the-app)
   * [Tech Used](#tech-used)
   * [Requirements](#requirements)
   * [Discord Channel](#discord-channel)
   * [Getting Started](#getting-started)
      * [1. Star and Fork this Repository](#1-star-and-fork-this-repository)
      * [2. Clone the Repository](#2-clone-the-repository)
      * [3. Create New Branch](#3-create-new-branch)
      * [4. Commit and Push](#4-commit-and-push)
      * [5. Update Local Repository](#5-update-local-repository)
      * [6. Configure a Remote for the Fork](#6-configure-a-remote-for-the-fork)
      * [7. Sync the Fork](#7-sync-the-fork)
      * [8. Create Pull Request](#8-create-pull-request)
   * [Screenshorts of the app](#screenshots)
   * [Download app from the link](#download-app-from-the-link)
   * [Project Maintainer](#project-maintainer)
   * [Check your Njack Leaderboard](#check-the-leaderboard-at)

## Top Features of the App
- You can create your account in app using your google account.
- You can see post made by users.
- You can like, report posts in the app.
- You can add your own post.
- You can see public cure profiles.
- You can text and call users.
- You can see posts made by the user in user profile.
- You can send text messages and images to other users.
- You can block user from chat.
- You can Track Your activity of the timeline.
- You can deactivate you account.
- You can make profile private.
- You can revoke call from other users.
- You can edit your profile.
- You can add profile picture to the app.

## Tech Used
1. Android
2. Java
3. Firebase RealTime Database
4. Firebase FireStore
5. Firebase Authentication
6. Google Analytics
7. RecyclerView
8. Material I/O

## Requirements
1. Android Version 5.0 and above
2. compileSdkVersion 29
3. minSdkVersion 21

## Discord channel
[![chat on discord](https://img.shields.io/badge/chat-on%20discord-brightgreen)](https://discord.gg/bcqbktHd)

## Getting started

### 1. Star and Fork this Repository
###### You can star ⭐ and fork 🍽️ this repository on GitHub by navigating at the top of this repository.
![](./AppImages/star.png)


![](./AppImages/fork.png)
###### GitHub repository URLs will reference both the username associated with the owner of the repository, as well as the repository name. For example, plazzy99 is the owner of this repository, so the GitHub URL for this project is:

https://github.com/plazzy99/heal-o-chat

###### When you’re on the main page for the repository, you’ll see a button to "Star" and “Fork” the repository on your upper right-hand side of the page, underneath your user icon.

### 2. Clone the Repository
###### To make your own local copy of the repository you would like to contribute to, let’s first open up a terminal window.
###### We’ll use the git clone command along with the URL that points to your fork of the repository.
###### This URL will be similar to the URL above, except now it will end with .git. In the example above, the URL will look like this:

https://github.com/plazzy99/Heal-O-Chat.git

###### You can alternatively copy the URL by using the green “Clone or download” button from your repository page that you just forked from the original repository page. Once you click the button, you’ll be able to copy the URL by clicking the binder button next to the URL:
![](./AppImages/clone.png)

###### Once we have the URL, we’re ready to clone the repository. To do this, we’ll combine the git clone command with the repository URL from the command line in a terminal window:
```
git clone https://github.com/plazzy99/Heal-O-Chat.git
```

### 3. Create New Branch
###### Once the project is opened create a new branch and checkout in it where you can make the changes in the code.
###### You can do this either from terminal or Directly in Android Studio.
###### To do from Terminal:
```
git branch new-branch
git checkout new-branch
```
###### To do directly from Android Studio
###### Click on Git branch in the bottom-right corner in Android Studio and create a new branch from there and checkout to it.
![](./AppImages/Change_branch.png)

### 4. Commit and Push
###### After making the required changes commit and push your code
###### Terminal:
###### To add the changes after you have made the modifications
``` git add . ``` or ``` git add -A ```
###### To commit and push the changes
```
git commit -m <Your-commit-message>
```
```
git push --set-upstream origin new-branch
```

### 5. Update Local Repository
###### While working on a project alongside other contributors, it is important for you to keep your local repository up-to-date with the project as you don’t want to make a pull request for code that will cause conflicts. To keep your local copy of the code base updated, you’ll need to sync changes.
###### We’ll first go over configuring a remote for the fork, then syncing the fork.

### 6. Configure a Remote for the Fork
###### You’ll have to specify a new remote upstream repository for us to sync with the fork. This will be the original repository that you forked from. you’ll have to do this with the git remote add command.
```
git remote add upstream https://github.com/plazzy99/Heal-O-Chat.git
```
###### In this example, // upstream // is the shortname we have supplied for the remote repository since in terms of Git, “upstream” refers to the repository that you cloned from. If you want to add a remote pointer to the repository of a collaborator, you may want to provide that collaborator’s username or a shortened nickname for the shortname.

### 7. Sync the Fork
###### Once you have configured a remote that references the upstream and original repository on GitHub, you are ready to sync your fork of the repository to keep it up-to-date.
To sync your fork, from the directory of your local repository in a terminal window, you’ll have to use the // git fetch // command to fetch the branches along with their respective commits from the upstream repository. Since you used the shortname “upstream” to refer to the upstream repository, you’ll have to pass that to the command:
``` git fetch upstream ```
###### Switch to the local master branch of our repository:
``` git checkout master ```
###### Now merge any changes that were made in the original repository’s master branch, that you will access through your local upstream/master branch, with your local master branch:
``` git merge upstream/master ```

### 8. Create Pull Request
###### At this point, you are ready to make a pull request to the original repository.
###### Navigate to your forked repository, and press the “New pull request” button on your left-hand side of the page.

# ScreenShots
<img src="https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/splash.png" alt="drawing" width="250"/>  <img src="https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/welcome_screen.png" alt="drawing" width="250"/>
<img src="https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/post.png" alt="drawing" width="250"/>
<img src="https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/your_profile.png" alt="drawing" width="250"/>
<img src="https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/home_screen.png" alt="drawing" width="250"/>
<img src="https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/chat_list.png" alt="drawing" width="250"/>
<img src="https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/cure_profile.png" alt="drawing" width="250"/>
<img src="https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/chat.png" alt="drawing" width="250"/>
<img src="https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/about_us.png" alt="drawing" width="250"/>
<img src="https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/setting.png" alt="drawing" width="250"/>
<img src="https://github.com/plazzy99/Heal-O-Chat/blob/master/app/src/main/res/drawable/post_bottom_sheet.png" alt="drawing" width="250"/>

## Download app from the link
[Download both the files and then click .apk file](https://drive.google.com/drive/folders/1b5-8xw9_6Mp4vyKElj-K4mPzqCfPeGgL?usp=sharing)

## Project Maintainer
[![Issues](https://img.shields.io/github/issues/plazzy99/Heal-O-Chat)](https://github.com/plazzy99) [![Maintenance](https://img.shields.io/maintenance/yes/2020?color=green&logo=github)](https://github.com/plazzy99)

| <a href="https://github.com/plazzy99"><img src="https://avatars2.githubusercontent.com/u/48295138?s=400&u=8e588b8e5d6e8eb2f40886398c26e37b83b33e50&v=4" width=150px height=150px /></a>                                                                                         |
| :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------: |
| **[Vatsal Kesarwani](https://www.linkedin.com/in/vatsal-kesarwani/)**                                                                                                                                        |
| <a href="https://twitter.com/KesarwaniVatsal"><img src="https://openvisualfx.com/wp-content/uploads/2019/10/pnglot.com-twitter-bird-logo-png-139932.png" width="32px" height="32px"></a>  <a href="https://www.linkedin.com/in/vatsal-kesarwani/"><img src="https://mpng.subpng.com/20180324/vhe/kisspng-linkedin-computer-icons-logo-social-networking-ser-facebook-5ab6ebfe5f5397.2333748215219374063905.jpg" width="32px" height="32px"></a> |

> **_Need help?_** 
> **_Feel free to contact me @ [vatsalkesarwani12@gmail.com](mailto:vatsalkesarwani12@gmail@gmail.com?Subject=Heal-O-Chat (NJACK Contributor))_**

## You just made your Firt pull request to Heal-O-Chat
## Do Star ⭐ this Repo.
### Check the Leaderboard at:
https://njackwinterofcode.github.io/leaderboard.html

![Java](https://img.shields.io/badge/java-%230095D5.svg?&style=for-the-badge&logo=java&logoColor=white)
![Git](https://img.shields.io/badge/git%20-%23F05033.svg?&style=for-the-badge&logo=git&logoColor=white)
![Github](https://img.shields.io/badge/github%20-%23121011.svg?&style=for-the-badge&logo=github&logoColor=white)

##### Made with ❤️ By Vatsal Kesarwani
![forthebadge](https://forthebadge.com/images/badges/built-with-love.svg)
![ForTheBadge ANDROID](https://forthebadge.com/images/badges/built-for-android.svg)
