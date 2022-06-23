# BancoDeiCreditiDelSOS

![bcds_logo](app/src/main/resources/static/images/logo.png)

Questo è il progetto sviluppato da Isaia del Rosso e Tommaso Rocca per la seconda traccia del progetto di Sistemi Distribuiti per l'anno accademico 2021/2022.

Il progetto mira a creare un servizio web per la gestione di operazioni bancari elementari, esponendole attraverso endpoint REST e pagine HTML.

## Documentazione per i metodi aggiuntivi

Abbiamo aggiunto i seguenti endpoint REST:

- `/api/transaction`
    - metodo `GET`: restituisce tutte le transazioni effettuate. Restituisce una lista vuota nel caso non siano presenti transazioni.
- `/api/transfer/{accountId}`
    - metodo `GET`: restituisce tutte le transazioni in cui compare `accountId` come mittente e destinatario. Restituisce una lista vuota nel caso non siano presenti transazioni.

## 0. Prerequisiti

Per poter avviare il sistema da noi progettato è necessario avere:
- **Eclipse**, la versione per Web Developers che è possibile trovare [qui](https://www.eclipse.org/downloads/packages/release/2022-06/r/eclipse-ide-enterprise-java-and-web-developers)

## 1. Download the files

Estrarre l'archivio `.zip` presente all'interno della mail in una directory a piacere

## 2. Installazione

Una volta estratti i file, è necessario **importarli all'interno di Eclipse**.
Per farlo bisogna andare in *File* > *Import*.

Nella finestra che compare, scegliere l'opzione *Existing Maven Project*.

Selezionare *Browse*  o inserire manualmente il percorso dove avete precedentemente scaricato i file. 

Premere *Finish*.

## 3. Run

Per poter lanciare in server è necessario aspettare che **Maven** installi tutte le dipendenze necessarie.

![maven carica dipendenze]()

Una volta finito potete cliccare sulla voce *Run* > *AppApplication.java*

Nota: Il server viene avviato sulla **porta 8080**, quindi assicuratevi di lasciarla libera.

Se è andato tutto bene, dovreste poter accedere alla pagina principale tramite http://localhost:8080/

## 4. Enjoy BancodeiCreditiDelSos
