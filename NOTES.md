# NOTES.md

Bella sos, ho creato questo file così da poterti tenere aggiornato su quello che ho modificato e perchè, fallo anche tu pliz <3

## Modifiche

### 17/06
- Ho creato una nuova eccezione *AccountNotFoundException*, così da avere un modo per comunicare l'assenza di un account,  ti piaze?
- Ho modificato il metodo *Bank.transfer(String a1, String a2, double amount)* facendo in modo che controlli che **a1 e a2 siano not null** e che siano due **account registrati**;
- Ho popolato il metodo *Controller.operazione*, che sarebbe il metodo per fare versamenti e prelievi, utilizzando il metodo *transfer* con due accounId identici, quindi un trasferimento a se stessi.
- [ ] guarda *Controller.operazione*
- [x] casella spuntata

baci, @tommi
