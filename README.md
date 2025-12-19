# Projet de développement Agile de Machines Virtuelles – Groupe 2

## Table des matières

- [Projet de développement Agile de Machines Virtuelles – Groupe 2](#projet-de-développement-agile-de-machines-virtuelles--groupe-2)
  - [Table des matières](#table-des-matières)
  - [Présentation du projet](#présentation-du-projet)
  - [Équipe – Groupe 2](#équipe--groupe-2)
  - [Installation et Compilation](#installation-et-compilation)
    - [Architecture du projet](#architecture-du-projet)
    - [Prérequis](#prérequis)
    - [Installation](#installation)
    - [Git](#git)
  - [Rapport](#rapport)
    - [Compilation du PDF](#compilation-du-pdf)
    - [Suppression des fichiers générés](#suppression-des-fichiers-générés)

---

## Présentation du projet

Ce projet s’inscrit dans le cadre du **Master 1 d'Informatique au sein de l'Université Marie et Louis Paster**.

L’objectif est de développer une **machine virtuelle** permettant de :

- Compiler des programmes écrits en **MiniJaja** vers **JajaCode**,
- Interpréter et exécuter ces programmes,
- Comparer les résultats entre l’interprétation MiniJaja et celle de son JajaCode compilé,
- Fournir une interface graphique en **JavaFX** pour éditer, exécuter et observer les résultats.

Le tout, en utilisant des méthodes Agiles de travaille au sein d'un groupe.

---

## Équipe – Groupe 2

Ce projet est réalisé par le **Groupe 2**, constitué de **6 membres** :

- BARADI Khaled
- CUINET Antoine
- HANZAL Ines
- MONTENERI Dylan
- QUENTIN Gaspard
- TATIN Raphaël

---

## Installation et Compilation

### Architecture du projet

Le projet est découpé en plusieurs modules :

- **Analyseur lexical et syntaxique** (via **ANTLR**)
- **Arbre de Syntaxe Abstraite (AST)**
- **Contrôle de type**
- **Compilateur MiniJaja → JajaCode**
- **Interpréteurs MiniJaja et JajaCode**
- **Gestion mémoire** (Pile / Tas, Garbage Collector)
- **Interface graphique (JavaFX)**

### Prérequis

Avant d’exécuter le projet, assurez-vous d’avoir installé :

- **Java 17+** (JDK)
- **Maven 3.8+**
- **JavaFX SDK** (si non inclus dans votre JDK)

### Installation

1. Télécharger le jar **miniJajaIDE-<version>.jar** sur Nexus : Browse > maven-releases > fr > umlp > m1info > projectvms > group2 > gui

2. Exécuter le jar : 

```
java -jar miniJajaIDE-<version>.jar
```


### Installation (developpeur)

1. Cloner le dépôt :

    ```bash
    git clone https://disc.univ-fcomte.fr/cr700-gitlab/projet-amv-2025/groupe-2
    cd groupe-2
    ```

2. Compiler et construire :

    ```bash
    mvn clean install
    ```

3. Lancer les tests :

    ```bash
    mvn test
    ```

4. Exécuter le module JavaFX :

    ```bash
    cd gui
    mvn clean javafx:run
    ```

5. Exécuter la CLI : 

    ````bash
    mvn clean install
    mvn exec:java -pl cli -Dexec.mainClass="fr.umlp.m1info.projectvms.group2.cli.CLI" -Dexec.args="--help"
    ```

### Git

Afin de réaliser un travail **propre**, voici quelque remarque sur le fonctionnement du dépot git :

- Le dépôt est coupé en branches, avec une branche par partie à développer. Il est important de vérifier d'être sur la bonne branche avant de coder :
  
  ```bash
  git branch -a
  git checkout <my_branch>
  ```

- On créer des commits **clair et précis** en suivant les Conventions Git (feature, refactor...), voir [conventional commit specification](https://www.conventionalcommits.org/) :

  ```bash
  git status
  git add <mon_fichier>
  git commit -m "feat(my_branch): add information on my commit"
  git push
  ```

- Pour éviter de commiter les fichiers temporaires (.aux, .log, etc),on fait un `$ git status` et on jette un œil au `.gitignore`.

## Rapport

Le rapport du projet est réalisé en **Latex**.

### Compilation du PDF

Pour le compiler :

```bash
cd report
make
```

Le PDF se trouvera ensuite dans le dossier `/build` qui aura été créé.

⚠️ **Avant** de faire un commit, vérifier que tous les **fichiers générés sont bien ignorés**, dans le cas contraire, c'est surement que vous avez compiler via une extension VsCode ou via la commande `pdflatex`. ⚠️

### Suppression des fichiers générés

Pour supprimer localement les fichiers générés :

```bash
make clean
```
