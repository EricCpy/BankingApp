# Banking App
**Online Banking app with different account types**

## Table of Contents
- [Banking App](#banking-app)
  - [Table of Contents](#table-of-contents)
  - [Intro - Task A)](#intro---task-a)
    - [Requirements](#requirements)
    - [Features](#features)
    - [Preview](#preview)
  - [Documentation - Task B)](#documentation---task-b)
    - [1. Git](#1-git)
    - [2. UML](#2-uml)
    - [3. DDD](#3-ddd)
    - [4. Metrics](#4-metrics)
    - [5. Clean Code Development](#5-clean-code-development)
    - [6. Build Management](#6-build-management)
    - [7. CI/CD](#7-cicd)
    - [8. Unit Tests](#8-unit-tests)
    - [9. IDE](#9-ide)
    - [10. DSL](#10-dsl)
    - [11. Functional Programming](#11-functional-programming)
  - [Task Completion](#task-completion)

## Intro - Task A)

### Requirements
  - Backend
    - Java 21
    - Maven     
  - Frontend
    - Node.js

### Features

- **User Authentication/Authorization**
  - Email registration
  - JWT tokens for authentication
  - Different user groups with different privileges
- **Account**
  - CRUD operations
- **Checking/Savings Accounts**
  - CRUD operations
  - Transfers
    - Scheduled transfers
    - Recurring transfers
  - Annual interest calculation
  - List and filter account transactions
    - Export transactions of a specific period to PDF
- **Saving Bonds**
  - Create for specific period and delete after bond period 

This repository is a monorepository containing frontend and backend of the banking app. The [backend](bankingapp) is implemented with Spring Boot 3 and the [frontend](bankingapp-ui) with Angular 17.

### Preview
TODO add Frontend pictures

## Documentation - Task B)

### 1. Git
In this project, I used various Git features. I choose to use a monorepository for both applications. The project has a production branch (main) and feature branches, which can only be merged into the main branch through a pull request with atleast one review. Therefore, it is not possible to push directly into the main branch (branch protection).

<div style="text-align:center">
  <img src="docs\pictures\branch-protection.png" alt="Main protection">
</div>

I utilized different (uncommon) git features, such as squashing commits, particularly when testing pipeline features with multiple small test commits. Additionally, I rebased most branches after changes on the main branch instead of merging to get a cleaner commit history.

### 2. UML

### 3. DDD
I used Miro for event storming. At first, I brainstormed domain events and potential conflicts that could arise in the app.

<div style="text-align:center">
  <img src="docs\EventStormingStep1.PNG" alt="DDD Step1">
</div>

Then, I categorized these terms into different processes and further grouped them within the processes into smaller subsets.

<div style="text-align:center">
  <img src="docs\EventStormingStep2.PNG" alt="DDD Step1">
</div>

I assigned individual names to these groups and organized them in a Core Domain Chart. As shown in the image, my core domains primarily revolve around financial aspects such as trading and transferring money. These are intended to be supported by analysis sub-domains, which enhance the customer experience but are not essential. Tasks like withdrawing money from ATMs can be outsourced to third parties (generic domain).

<div style="text-align:center">
  <img src="docs\Core_Domain_Chart.PNG" alt="DDD Step1">
</div>

Im finalen Schritt habe ich Beziehungen hinzugefügt. Die Analyse Domainen sind meistens Downstream von der richtigen Domain, welche Informationen für diese bereit stellt, dies kann man beispielsweise beim trading sehen. der geld transfer und die financial services teilen sich einen gemeinsamen kern, da diese auf geld transfer daten zugreifen und analyse daten zugreifen müssen um richtig zu funktionieren. so kann man kunden ermöglichen, dass ihre transfers durch bestehende transfertools analysiert werden können. der kunde könnte beispielsweise so empfehlungen für ein optimiertes konsum verhalten erhalten. 

In the final step, I added relationships. The analysis domains are downstream of the core domains, which provides information for them, for example in trading. The money transfer and financial services have a shared core, they both require access to money transfer data and analytical data for proper functionality. This shared core enables both teams to easily communicate and access shared data. Example use case: Customers might receive recommendations for optimizing their spending behavior.

<div style="text-align:center">
  <img src="docs\Core_Domain_Chart_Relations.PNG" alt="DDD Step1">
</div>

### 4. Metrics

### 5. Clean Code Development

### 6. Build Management

### 7. CI/CD

### 8. Unit Tests

### 9. IDE
I used IntelliJ and VsCode. Ich habe IntelliJ für die Java Applikation verwendet, da IntelliJ ein sehr hübsches UI hat um verschiedene Maven commands auszuführen und einiges auch automatisch für einen erledigt.
VsCode habe ich auch teilweise benutzt, um die REST requests zu testen, da es eine VsCode Extension dafür gibt und diese in IntelliJ Geld kosten würde oder ich sonst eine andere Software wie Postman benutzen müsste. Zudem habe ich VsCode für das Angular Frontend verwendet. 

I used IntelliJ and VsCode. I used IntelliJ for the Java application because IntelliJ has a very nice UI to execute various Maven and Java commands and automatically handles some tasks. I also used VsCode for the Angular frontend and to test the HTTP requests because there is a VsCode extension for it and using such an extension in IntelliJ requires premium :( (without this extension I would need Postman).

**Favorite Shortcuts Overall**:
- Ctrl + Shift + F, to search for something in all project files

**VsCode**:
- Ctrl + Shift + P, to execute VsCode actions (for example to reload VsCode)
- Ctrl + ., for autocomplete and code action
- Ctrl + Shift + Ö, to open the terminal
- Shift + Alt + F, autoformat
  
**IntelliJ**:
- Ctrl + Alt + L, autoformat
- Ctrl + Shift + A, to open IntelliJ Actions
- Ctrl + Alt + O, to optimize imports / remove unused ones
- Alt + Enter, to perform code actions

I use these shortcuts the most while working. However, there are also other useful shortcuts that I sometimes use, but not nearly as often as these.

### 10. DSL

### 11. Functional Programming
For this task, I programmed a [TicTacToe](functional-programming\tictactoe.clj) console game in Clojure.
Aspects of functional programming:
- only final data structures
  - in clojure all data structures are immutable
- (mostly) side-effect-free functions
  - does not have side effects except print statements (if they count)
  - an example for a side-effect-free function is the 'execute-move' function, it takes the board player and field and returns a new board with the new executed move
    ```clojure
    (defn execute-move [player curr-player board field]
      (let [row (quot field (count board)) col (mod field (count board))]
        (println (format "%s placing %s in col %d and row %d" (if (= player curr-player) "You are""Enemy is") curr-player col row))
        (update-in board [row col] (constantly curr-player))
      )
    )
    ```
- the use of higher-order functions
  - I used higher order functions like [map](https://github.com/EricCpy/BankingApp/blob/main/functional-programming/tictactoe.clj#L85) and [every?](https://github.com/EricCpy/BankingApp/blob/main/functional-programming/tictactoe.clj#L81), these functions take other functions as parameters
  - my [ask-for-field](https://github.com/EricCpy/BankingApp/blob/main/functional-programming/tictactoe.clj#L52) also takes an function as parameters for the player input 
- functions as parameters and return values
  - the [create-tic-tac-toe](https://github.com/EricCpy/BankingApp/blob/main/functional-programming/tictactoe.clj#L105) returns a function with a user/programmer defined TicTacToe board size, which can be used to create multiple games of TicTacToe
- use closures / anonymous functions
  - the [create-tic-tac-toe](https://github.com/EricCpy/BankingApp/blob/main/functional-programming/tictactoe.clj#L105) is also an example for a closure, the return function remembers the create-tic-tac-toe input parameter
  - I use anonymous functions in many higher-order functions, like every?. For example, in the game over function to check if every field has been used
  ```clojure
  (defn game-over? [board]
    (every? (fn [row] (every? #{"X" "O"} row)) board)
  )
  ```

## Task Completion

- [x] DSL Create a small DSL Demo example snippet in your code even if it does not contribute to your project (hence it can also be in another language).
- [ ] Integrate some nice unit tests in your Code to be integrated into the Build
- [ ] Clean Code Development: A) At least 5 points you can show me with an explanation of why this is clean code in your code and/or what has improved & B) >>10 points on your personal CCD cheat sheet. E.g. a PDF.
- [x] Functional Programming: prove that you have covered all functional aspects in your code as:
    - only final data structures
    - (mostly) side-effect-free functions 
    - the use of higher-order functions
    - functions as parameters and return values
    - use closures / anonymous functions
    - You can also do it outside of your project. Even in other languages such as F#, Clojure, Julia, etc. 
- [x] Use and understand Git!
- [x] UML at least 3 good different diagrams. "good" means you can pump it up artificially as written in DDD. You have 10 million $ from me! Please export the pics. I can not install all the tools to view them!
- [x] DDD If your domain is too small, invent other domains around and document these domains (as if you have 100 Mio € from Edlich-Investment!) Develop a clear strategic design with mappings/relationships with >4 Domains coming from an Event Storming. Drop your Domains into a Core Domain Chart and indicate the Relations between the Domains!
- [x] Metrics at least two. Sonarcube would be great. Other non-trivial metrics are also fine.
- [x] Build Management with any Build System as Ant, Maven, Gradle, etc. (only Travis is perhaps not enough) Do e.g. generate Docs, call tests, etc. (it could be also disconnected from the project just to learn a build tool!)
- [x] Continuous Delivery: show me your pipeline using e.g. Jenkins, Travis-CI, Circle-CI, GitHub Action, GitLab CI, etc. E.g. you can also use Jenkins Pipelining or BlueOcean, etc. But at least insert more than 2 script calls as done in the lecture! (e.g. also call Ant or Gradle or something else).
- [x] Use a good IDE and get fluent with it: e.g. IntelliJ. What are your favourite key shortcuts?!



