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
  - [Rapport](#rapport)

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

## Rapport

Le rapport du projet est réalisé en Latex.

Pour le compiler :

```bash
cd report
pdflatex -output-directory=build report.tex
```
