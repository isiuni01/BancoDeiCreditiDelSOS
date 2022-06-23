# BancoDeiCreditiDelSOS

![bcds_logo](app/src/main/resources/static/images/logo.png)

Questo è il progetto sviluppato da Isaia del Rosso e Tommaso Rocca per la seconda traccia del progetto di Sistemi Distribuiti per l'anno accademico 2021/2022.

Il progetto mira a creare un servizio web per la gestione di operazioni bancari elementari, esponendole attraverso endpoint REST e pagine HTML.

## Documentazione per i metodi aggiuntivi

Abbiamo aggiunto i seguenti endpoint REST:

- `/api/transaction`
    - metodo `GET`: restituisce tutte le transazioni effettuate. Restituisce una lista vuota nel caso non siano presenti transazioni.
- `/api/transfer/{accountId}`
    - metodo `GET`: restituisce tutte le transazioni in cui compare `accountId` come mittente o destinatario. Restituisce una lista vuota nel caso non siano presenti transazioni.

## Scelte programmative

### Salvataggio su memoria secondaria

Abbiamo scelto di utilizzare la *Serializzazione* per salvare in memoria secondaria l'insieme delle transazioni e degli account